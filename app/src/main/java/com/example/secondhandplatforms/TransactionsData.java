package com.example.secondhandplatforms;

public class TransactionsData {
    private String   id;
    private String  goodsId;
    private String sellerId;
    private String price;
    private String buyerId;
    private String createTime;
    private String sellerName;
    private String buyerName;
    private String sellerAvatar;
    private String buyerAvatar;
    private String goodsDescription;
    private String imageUrlList;

    public TransactionsData(String id, String goodsId,
                            String sellerId, String price,
                            String buyerId, String createTime,
                            String sellerName, String buyerName,
                            String sellerAvatar, String buyerAvatar,
                            String goodsDescription, String imageUrlList) {
        this.id = id;
        this.goodsId = goodsId;
        this.sellerId = sellerId;
        this.price = price;
        this.buyerId = buyerId;
        this.createTime = createTime;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.sellerAvatar = sellerAvatar;
        this.buyerAvatar = buyerAvatar;
        this.goodsDescription = goodsDescription;
        this.imageUrlList = imageUrlList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerAvatar() {
        return sellerAvatar;
    }

    public void setSellerAvatar(String sellerAvatar) {
        this.sellerAvatar = sellerAvatar;
    }

    public String getBuyerAvatar() {
        return buyerAvatar;
    }

    public void setBuyerAvatar(String buyerAvatar) {
        this.buyerAvatar = buyerAvatar;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}
