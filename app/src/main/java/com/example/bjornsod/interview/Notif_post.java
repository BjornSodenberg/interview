package com.example.bjornsod.interview;

public class Notif_post extends BlogPostId {

    private String description;
    private String user_id;
    private String post_id;
    private String recipientId;

    public Notif_post() {
    }

    public Notif_post(String postId, String userId,String recipientId, String desc) {
        this.description = desc;
        this.user_id = userId;
        this.post_id = postId;
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
