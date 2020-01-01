package com.owwang.mall.search.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * @Classname SolrjTest
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
public class SolrjTest {
    @Test
    public void add() throws IOException, SolrServerException {
        //创建solrServer,建立连接，需要指定地址
        SolrServer solrServer = new HttpSolrServer("http://122.51.105.58:8080/solr/");
        //创建solrimputdocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id", "test002");
        document.addField("item_title", "测试测试002");
        //将文档提交到索引库中
        solrServer.add(document);
        solrServer.commit();
    }

    @Test
    public void testQuery() throws Exception {
        //创建SolrServer对象
        SolrServer solrServer = new HttpSolrServer("http://122.51.105.58:8080/solr/");
        //创建solrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询的条件 设置过滤、主查询、排序等条件
        query.setQuery("手机");
        query.addFilterQuery("item_price[0 TO 3000000000]");
        query.set("df","item_title");
        //执行查询
        QueryResponse response = solrServer.query(query);
        //获取结果集
        SolrDocumentList results = response.getResults();
        //遍历结果集
        System.out.println("查询总记录数："+results.getNumFound());
        for (SolrDocument solrDocument : results){
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
        }
    }
}
