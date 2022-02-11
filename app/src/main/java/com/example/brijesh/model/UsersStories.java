package com.example.brijesh.model;

public class UsersStories {
    private String imageSto;
    private long storyAtSto;

    public UsersStories(String imageSto, long storyAtSto) {
        this.imageSto = imageSto;
        this.storyAtSto = storyAtSto;
    }

    public UsersStories() {
    }

    public String getImageSto() {
        return imageSto;
    }

    public void setImageSto(String imageSto) {
        this.imageSto = imageSto;
    }

    public long getStoryAtSto() {
        return storyAtSto;
    }

    public void setStoryAtSto(long storyAtSto) {
        this.storyAtSto = storyAtSto;
    }
}
