package com.mycompany.Models;

import java.time.LocalDate;

public class Order {

    private Integer id;
    private LocalDate date;
    private int costumerId;

    public Order() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%-40s%-20s%-20s", getId(), getCostumerId(), getDate());
    }

}
