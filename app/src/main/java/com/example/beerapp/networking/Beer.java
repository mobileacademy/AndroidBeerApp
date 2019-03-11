package com.example.beerapp.networking;

import com.google.gson.annotations.SerializedName;

public class Beer {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String desc;
    @SerializedName("image_url")
    String imageUrl;

    public Beer(int id, String name, String desc, String imageUrl) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imageUrl = imageUrl;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
