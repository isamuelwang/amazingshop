package com.owwang.mall.search.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.SearchItem;
import com.owwang.mall.pojo.SearchResult;
import com.owwang.mall.search.dao.SearchDao;
import com.owwang.mall.search.mapper.SearchItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
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

    @Autowired
    private SearchDao searchDao;

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

    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
        //1.创建SolrQuery对象
        SolrQuery query = new SolrQuery();
        //2.设置主查询条件
        if(StringUtils.isNotBlank(queryString)){
            query.setQuery(queryString);
        }else {
            query.setQuery(("*:*"));
        }
        //2.1设置过滤条件
        if(page==null)page=1;
        if(rows==null)rows=60;
        query.setStart((page-1)*rows);
        query.setRows(rows);
        //2.2设置默认搜素域
        query.set("df","item_keywords");
        //2.3设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域
        //3.调用DAO方法
        SearchResult search = searchDao.search(query);
        //4.设置result中的总页数
        Long pageCount = 0l;
        pageCount = search.getRecordCount()/rows;
        if(pageCount%rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        return search;
    }
}
