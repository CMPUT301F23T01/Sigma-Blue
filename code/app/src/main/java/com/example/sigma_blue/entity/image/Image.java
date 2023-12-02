package com.example.sigma_blue.entity.image;

import android.graphics.Bitmap;

public class Image {
    private String imagePath;
    private Bitmap image;

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image(String imagePath, Bitmap image) {
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
