package com.alaryani.aamrny.social.googleplus;

import com.alaryani.aamrny.object.User;

/**
 * Created by phamv on 7/25/2016.
 */
public class GUser {

    String id, fullname, email, gender,avatar;

    public GUser() {
    }

    public GUser(String id, String name, String email, String gender, String avatar) {
        this.id = id;
        this.fullname = name;
        this.email = email;
        this.gender = gender;
        this.avatar = avatar;
    }

    public User toUser(){
        User user = new User();
        user.setLinkImage(avatar);
        user.setFullName(fullname);
        user.setEmail(email);
        user.setIdUser(id);
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
