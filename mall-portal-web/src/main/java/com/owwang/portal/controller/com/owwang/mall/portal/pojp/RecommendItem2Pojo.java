package com.owwang.portal.controller.com.owwang.mall.portal.pojp;

/**
 * 首页分类最新商品推荐
 * @Classname RecommendItem2Pojo
 * @Description TODO
 * @Date 2020-01-03
 * @Created by WANG
 */
/*
{
//图片
"d": "g15\/M00\/13\/1E\/rBEhWFJ4sNUIAAAAAAHJY7c4pHkAAFBugBwkz0AAcl7615.jpg",
//全部取值为字符串“0”
"e": "0",
//价格
"c": "3309.00",
//商品编号
"a": "1068768",
//标题
"b": "ThinkPad\u54c1\u724c\u60e0,\u6781\u81f4\u6027\u80fd\u5546\u52a1\u672c\uff01",
//全部取值为1
"f": 1
}
 */
public class RecommendItem2Pojo {
    //图片
    private String d;
    //取值为0
    private String e = "0";
    //价格
    private Long c;
    //商品编号
    private Long a;
    //标题
    private String b;
    //全部取值为1
    private Integer f = 1;

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public Long getC() {
        return c;
    }

    public void setC(Long c) {
        this.c = c;
    }

    public Long getA() {
        return a;
    }

    public void setA(Long a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }
}
