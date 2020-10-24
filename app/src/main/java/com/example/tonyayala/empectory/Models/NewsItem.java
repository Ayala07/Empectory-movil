package com.example.tonyayala.empectory.Models;

public class NewsItem {
    private String Title;
    private String Content;
    private String Date;

    public NewsItem() {
    }

    public NewsItem(String title, String content, String date) {
        Title = title;
        Content = content;
        Date = date;

    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setDate(String date) {
        Date = date;
    }


    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }
}
