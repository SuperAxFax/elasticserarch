package com.kuang.pojo;

public class Content {
    private String img;
    private String price;
    private String title;

    public Content(String img, String price, String title) {
        this.img = img;
        this.price = price;
        this.title = title;
    }

    public Content() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Content{" +
                "img='" + img + '\'' +
                ", price='" + price + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
