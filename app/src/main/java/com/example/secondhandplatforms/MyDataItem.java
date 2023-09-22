package com.example.secondhandplatforms;


public class MyDataItem {
    private String id;
    private String content;
    private String price;
    private String addr;
    private String imageCode;
    private String typeid;
    private String typeName;
    private String userid;
    private String buyerName;
    private String createTime;

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    private String goodsDescription;
    public MyDataItem(String id, String content, String price,
                      String addr, String imageCode, String typeid, String typeName,
                      String userid) {
        this.id = id;
        this.content = content;
        this.price = price;
        this.addr = addr;
        this.imageCode = imageCode;
        this.typeid = typeid;
        this.typeName = typeName;
        this.userid = userid;
    }

    // 添加额外的构造器
    public MyDataItem(String id, String goodsDescription,String addr, String price,
                      String buyerName, String createTime) {// 调用原有构造器初始化基本属性
        this.id = id;
        this.addr = addr;
        this.goodsDescription = goodsDescription;
        this.price = price;
        this.buyerName = buyerName;
        this.createTime = createTime;
    }


    public MyDataItem(String id, String content, String price, String addr) {
        this.id = id;
        this.content = content;
        this.price = price;
        this.addr = addr;
    }

    // 添加其他属性的getter和setter方法
    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    // 添加其他属性的getter和setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    // 添加新属性的getter和setter方法
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}










