package com.example.bjornsod.interview;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private int rating;
    private int countPosts;
    private int countAnswers;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCountPosts(int countPosts) {
        this.countPosts = countPosts;
    }

    public void setCountAnswers(int countAnswers) {
        this.countAnswers = countAnswers;
    }

    public int getRating() {
        return rating;
    }

    public int getCountPosts() {
        return countPosts;
    }

    public int getCountAnswers() {
        return countAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
