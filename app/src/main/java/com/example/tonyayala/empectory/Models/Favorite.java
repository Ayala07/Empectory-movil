package com.example.tonyayala.empectory.Models;

public class Favorite {
    String nameF;
    String pictureF;
    String userId;
    String postKey;

    public Favorite(String nameF, String pictureF, String userId, String postKey) {
        this.nameF = nameF;
        this.pictureF = pictureF;
        this.userId = userId;
        this.postKey = postKey;
    }

    public Favorite() {
    }

    public String getNameF() {
        return nameF;
    }

    public void setNameF(String nameF) {
        this.nameF = nameF;
    }

    public String getPictureF() {
        return pictureF;
    }

    public void setPictureF(String pictureF) {
        this.pictureF = pictureF;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
