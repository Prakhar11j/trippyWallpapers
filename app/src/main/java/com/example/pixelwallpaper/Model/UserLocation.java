package com.example.pixelwallpaper.Model;

public class UserLocation {
    private String userid,username,fullname;
    private Double latitude,longitude;

    public UserLocation(){}

    public UserLocation(String userid, Double latitude, Double longitude, String username, String fullname) {
        this.userid = userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.fullname = fullname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
