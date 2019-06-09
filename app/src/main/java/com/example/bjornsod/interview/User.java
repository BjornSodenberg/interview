package com.example.bjornsod.interview;

public class User {

    private String name;
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


}
