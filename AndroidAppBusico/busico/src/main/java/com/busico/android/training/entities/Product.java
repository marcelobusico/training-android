package com.busico.android.training.entities;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private String productUrl;
    private String description;
    private double price;
    private String imageUrl;

    public Product(String id, String productUrl, String description, double price, String imageUrl) {
        this.id = id;
        this.productUrl = productUrl;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return description;
    }
}
