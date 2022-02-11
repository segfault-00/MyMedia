package com.example.brijesh.model;

import java.util.ArrayList;

public class Story {
    private String storyBy;
    private long storyAt;
    ArrayList<UsersStories> stories;

    public Story(String storyBy, long storyAt, ArrayList<UsersStories> stories) {
        this.storyBy = storyBy;
        this.storyAt = storyAt;
        this.stories = stories;
    }

    public Story() {

    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UsersStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UsersStories> stories) {
        this.stories = stories;
    }


}
