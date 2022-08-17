package com.project.util.data;

public class AdminInfo {
    String id;
    String email;
    String verified_email;
    String name;
    String given_name;
    String family_name;
    String picture;
    String local;

    public AdminInfo(String id, String email, String verified_email, String name, String given_name, String family_name, String picture, String local) {
        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.local = local;
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", verified_email='" + verified_email + '\'' +
                ", name='" + name + '\'' +
                ", given_name='" + given_name + '\'' +
                ", family_name='" + family_name + '\'' +
                ", picture='" + picture + '\'' +
                ", local='" + local + '\'' +
                '}';
    }
}

