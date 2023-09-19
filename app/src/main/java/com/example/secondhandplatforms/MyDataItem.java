package com.example.secondhandplatforms;

public class MyDataItem {
    private String id;
    private String content;

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

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

    public MyDataItem(String id, String content, String price,
                      String addr, String imageCode, String typeid,
                      String typeName, String userid) {
        this.id = id;
        this.content = content;
        this.price = price;
        this.addr = addr;
        this.imageCode = imageCode;
        this.typeid = typeid;
        this.typeName = typeName;
        this.userid = userid;
    }

    private String price;
    private String addr;
    private String imageCode;
    private String typeid;
    private String typeName;
    private String userid;



    public MyDataItem(String id, String content, String price, String addr) {
        this.id = id;
        this.content = content;
        this.price = price;
        this.addr = addr;
    }



    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }

    public String getAddr() {
        return addr;
    }
}

