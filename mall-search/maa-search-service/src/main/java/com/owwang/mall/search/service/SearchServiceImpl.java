package com.owwang.mall.search.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.SearchItem;
import com.owwang.mall.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname SearchServiceImpl
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public MallResult importAllSearchItems() throws Exception {
        //注入mapper
        //调用mapper方法，查询所有商品数据
        List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();
        //通过solrj将数据写入到索引库中
        for(SearchItem searchItem : searchItemList){
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",searchItem.getId().toString());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            document.addField("item_desc",searchItem.getItem_desc());
            //添加索引库
            solrServer.add(document);
        }
        //提交给索引库
        solrServer.commit();
        return MallResult.ok();
    }
}
