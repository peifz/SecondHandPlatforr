package com.example.secondhandplatforms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String appKey;
    private String tUserId;
    private String imageCode;
    private String content;
    private double price;
    private String addr;
    private int typeId;
    private String typeName;
    private int status;
    private String createTime;
    private String username;
    private String avatar;
    private List<String> imageUrlList;
    private int appIsShare;

    // 添加构造函数和 getter/setter 方法

    public Product(){

    }
    public Product(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.appKey = jsonObject.getString("appKey");
            this.tUserId = jsonObject.getString("tUserId");
            this.imageCode = jsonObject.getString("imageCode");
            this.content = jsonObject.getString("content");
            this.price = jsonObject.getDouble("price");
            this.addr = jsonObject.getString("addr");
            this.typeId = jsonObject.getInt("typeId");
            this.typeName = jsonObject.getString("typeName");
            this.status = jsonObject.getInt("status");
            this.createTime = jsonObject.getString("createTime");
            this.username = jsonObject.getString("username");
            this.avatar = jsonObject.isNull("avatar") ? null : jsonObject.getString("avatar");

            JSONArray imageUrlArray = jsonObject.getJSONArray("imageUrlList");
            this.imageUrlList = new ArrayList<>();
            for (int i = 0; i < imageUrlArray.length(); i++) {
                this.imageUrlList.add(imageUrlArray.getString(i));
            }

            this.appIsShare = jsonObject.getInt("appIsShare");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTUserId() {
        return tUserId;
    }

    public void setTUserId(String tUserId) {
        this.tUserId = tUserId;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public int getAppIsShare() {


        return appIsShare;
    }

    public void setAppIsShare(int appIsShare) {
        this.appIsShare = appIsShare;
    }
}

