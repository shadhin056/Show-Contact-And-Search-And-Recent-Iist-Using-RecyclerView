package com.example.contactlistshowandsearchinlistview;

public class Contact {
    String name;
    String image="iconfinder_search";
    String phone;

    public Contact() {
    }

    public Contact(String name, String image, String phone) {
        this.name = name;
        this.image = image;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}