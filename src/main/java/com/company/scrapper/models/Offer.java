package com.company.scrapper.models;

@SuppressWarnings("unused")
public class Offer {

    public String id;
    public String title;
    public String image;
    public String price;

    public Offer () {

    }

    public Offer(String id, String title, String image, String price) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + ", title: " + this.title + ", price: " + this.price + ", image: " + this.image;
    }
}
