package com.example.secondhandplatforms;

public class MyDataItem {
    private String id;
    private String content;
    private String price;
    private String addr;

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

