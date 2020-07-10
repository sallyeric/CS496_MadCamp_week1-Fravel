package com.example.firstproject;

public class Item {
    private String name;
    private String number;

    public Item () { }

    public Item(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
