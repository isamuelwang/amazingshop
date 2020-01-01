package com.owwang.mall.search.dao;

import com.owwang.mall.pojo.SearchItem;
import com.owwang.mall.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从索引库中搜索商品DAO
 * @Classname SearchDao
 * @Description TODO
 * @Date 2019-12-25
 * @Created by WANG
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    /**
     * 根据查询条件查询商品结果集
     * @Description TODO
     * @param
     * @param solrQuery
     * @return com.owwang.mall.pojo.SearchResult
     * @Date 2019-12-25
     * @auther Samuel
     */
    public SearchResult search(SolrQuery solrQuery) throws Exception{
        //方法返回值
        SearchResult searchResult = new SearchResult();
        //创建solrServer对象，由spring管理
        //执行查询
        QueryResponse response = solrServer.query(solrQuery);
        //获取查询结果集
        SolrDocumentList results = response.getResults();
        //遍历结果集
        searchResult.setRecordCount(results.getNumFound());
        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //返回值中的集合
        List<SearchItem> itemList =new ArrayList<>();
        //遍历查询结果集
        for (SolrDocument solrDocument : results){
            //将solrdocument中的属性设置给searchItem中，再封装给searchResult
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name(solrDocument.get("item_category_name").toString());
            searchItem.setId(Long.parseLong(solrDocument.get("id").toString()));
            searchItem.setImage(solrDocument.get("item_image").toString());
            //searchItem.setItem_desc();
            searchItem.setPrice((Long)(solrDocument.get("item_price")));
            searchItem.setSell_point(solrDocument.get("item_sell_point").toString());
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            //判断是否有高亮
            String HightLightStr = "";
            if(list!=null&&list.size()>0){
                //有高亮
                HightLightStr = list.get(0);
            }else {
                //没高亮
                HightLightStr = solrDocument.get("item_title").toString();
            }
            searchItem.setTitle(HightLightStr);
            itemList.add(searchItem);
        }
        //设置searchResult
        searchResult.setItemList(itemList);
        return searchResult;
    }
}
