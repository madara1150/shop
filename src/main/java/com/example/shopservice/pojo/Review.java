package com.example.shopservice.pojo;

import java.io.Serializable;

public class Review implements Serializable {
    private String content;
    private double rate;
    private String owner_name;
    private boolean isAnonymous;

    public Review(String content, double rate, String owner_name, boolean isAnonymous) {
        this.content = content;
        this.rate = rate;
        this.owner_name = owner_name;
        this.isAnonymous = isAnonymous;
    }
}
