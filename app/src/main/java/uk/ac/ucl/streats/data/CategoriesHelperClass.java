package uk.ac.ucl.streats.data;

import android.graphics.drawable.Drawable;

public class CategoriesHelperClass {

    Drawable gradient;
    int image;
    String title;

    public CategoriesHelperClass(Drawable gradient, int image, String titile) {
        this.gradient = gradient;
        this.image = image;
        this.title = titile;
    }

    public Drawable getGradient() {
        return gradient;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
