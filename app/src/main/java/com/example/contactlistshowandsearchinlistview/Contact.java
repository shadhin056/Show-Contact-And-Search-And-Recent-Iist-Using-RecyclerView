package com.example.contactlistshowandsearchinlistview;

public class Contact {
    String name;
    String image;
    String phone;
    String count;

    public Contact() {
    }

    public Contact(String name, String image, String phone, String count) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}