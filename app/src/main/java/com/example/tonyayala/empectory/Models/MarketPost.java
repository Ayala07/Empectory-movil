package com.example.tonyayala.empectory.Models;

public class MarketPost {
    private String postKey;
    private String productkey;
    private String img;
    private String tittle;
    private String price;
    private String description;
    private String detail;
    private String exist;
    private String userID;


    public MarketPost(String img, String tittle, String price, String description, String detail, String exist, String userID) {
        this.img = img;
        this.tittle = tittle;
        this.price = price;
        this.description = description;
        this.detail = detail;
        this.exist = exist;
        this.userID = userID;
    }

    public MarketPost(){

    }

    public String getPostKey() {
        return postKey;
    }

    public String getUserID() {
        return userID;
    }

    public String getProductkey() {
        return productkey;
    }

    public String getImg() {
        return img;
    }

    public String getTittle() {
        return tittle;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getDetail() {
        return detail;
    }

    public String getExist() {
        return exist;
    }

    public void setProductkey(String productkey) {
        this.productkey = productkey;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }
}
