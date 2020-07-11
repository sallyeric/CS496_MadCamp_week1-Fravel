package com.example.firstproject;

import java.util.ArrayList;

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

    public static ArrayList<Item> createContactsList(int numContacts) {
        ArrayList<Item> contacts = new ArrayList<Item>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Item("Person ", "000-0000-0000")); //PERSON / NUMBER
        }

        return contacts;
    }
}
