package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 2/13/2018.
 */

public class User {

    private String username;
    private String phone_number;
    private String image;
    private String uid;
    private String email;
    private String career;
    private String about;

    public User() {
    }

    public User(String username, String phone_number, String image, String uid, String email, String career, String about) {
        this.username = username;
        this.phone_number = phone_number;
        this.image = image;
        this.uid = uid;
        this.email = email;
        this.career = career;
        this.about = about;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
