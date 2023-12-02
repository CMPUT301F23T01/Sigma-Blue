package com.example.sigma_blue.entity.image;

import android.graphics.Bitmap;

public class image {
    private String imagePath;
    private Bitmap image;

    public image(String imagePath) {
        this.imagePath = imagePath;
    }

    public image(String imagePath, Bitmap image) {
        this.imagePath = imagePath;
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
