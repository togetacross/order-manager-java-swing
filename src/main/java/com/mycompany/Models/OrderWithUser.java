package com.mycompany.Models;

public class OrderWithUser {
    public Order order;
    public Costumer costumer;

    public OrderWithUser(Order order, Costumer costumer) {
        this.order = order;
        this.costumer = costumer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public void setCostumer(Costumer costumer) {
        this.costumer = costumer;
    }
}
