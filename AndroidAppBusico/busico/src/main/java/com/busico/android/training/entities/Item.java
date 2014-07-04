package com.busico.android.training.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String productUrl;
    private String description;
    private double price;
    private String subtitle;
    private String imageUrl;
    private int availableQuantity;
    private transient Bitmap image;
    private transient boolean downloadingImage;

    public Item(String id, String productUrl, String description, double price, String imageUrl) {
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

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isDownloadingImage() {
        return downloadingImage;
    }

    public void setDownloadingImage(boolean downloadingImage) {
        this.downloadingImage = downloadingImage;
    }

    @Override
    public String toString() {
        return description;
    }
}
