package com.mycompany.Models;

public class Company {

    private String name;
    private String registeredSeat;
    private String registeredNumber;
    private String phoneNumber;
    private String email;
    private String vatNumber;

    public Company() {
    }

    public Company(String name, String registeredSeat, String registeredNumber, String phoneNumber, String email, String vatNumber) {
        this.name = name;
        this.registeredSeat = registeredSeat;
        this.registeredNumber = registeredNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.vatNumber = vatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisteredSeat() {
        return registeredSeat;
    }

    public void setRegisteredSeat(String registeredSeat) {
        this.registeredSeat = registeredSeat;
    }

    public String getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(String registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    @Override
    public String toString() {
        return String.format("%s\n\n%s\n%s\n%s\n%s\n",
                getName(), "Address: " + getRegisteredSeat(), "Phone number: " + getPhoneNumber(),
                "Email: " + getEmail(), "Vat number.: " + getVatNumber());
    }

}
