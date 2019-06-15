package com.example.bjornsod.interview;

import java.util.Date;

public class Comments extends BlogPostId{

    private String message, user_id;
    private Date timestamp;
    public String image_url;


    public Comments() {
    }

    public Comments(String message, String user_id, Date timestamp, String image_url) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.image_url = image_url;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
