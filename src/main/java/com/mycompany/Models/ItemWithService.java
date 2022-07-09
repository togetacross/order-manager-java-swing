package com.mycompany.Models;

public class ItemWithService {

    private Item item;
    private Service service;

    public ItemWithService(Item item, Service service) {
        this.item = item;
        this.service = service;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
