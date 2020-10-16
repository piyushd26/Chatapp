package com.example.chatapp.model.pojo;

public class ChatList {
    String userid;
    String username;
    String description;
    String date;
    String urlProfile;

    public ChatList() {
        this.userid = userid;
    }

    public ChatList(String userid, String username, String description, String date, String urlProfile) {
        this.userid = userid;
        this.username = username;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
