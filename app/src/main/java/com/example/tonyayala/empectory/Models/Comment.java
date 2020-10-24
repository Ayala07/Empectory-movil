package com.example.tonyayala.empectory.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content;
    private String uid;
    private String uimg;
    private String uname;
    private String commentKey;
    private Object timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uimg, String uname, String commentKey) {
        this.commentKey = commentKey;
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
