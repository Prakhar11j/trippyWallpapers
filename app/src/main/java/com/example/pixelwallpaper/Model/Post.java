package com.example.pixelwallpaper.Model;

public class Post {
    private String UserId, imageUrl, postId;

    public Post(){
    }

    public Post(String userId, String imageUrl, String postId) {
        UserId = userId;
        this.imageUrl = imageUrl;
        this.postId = postId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
