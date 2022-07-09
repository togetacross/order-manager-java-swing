package com.mycompany.Models;

public class Service {

    private int id;
    private String name;
    private int price;

    public Service() {
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return String.format("%-5s%-40s", getId(), getName());
    }

}
