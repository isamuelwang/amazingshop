package com.owwang.mall.search.test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
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
        document.addField("id","test002");
        document.addField("item_title","测试测试002");
        //将文档提交到索引库中
        solrServer.add(document);
        solrServer.commit();
    }
}
