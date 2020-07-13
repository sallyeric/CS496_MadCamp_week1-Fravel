package com.example.firstproject;

import java.util.ArrayList;

public class Item2 {
    private String name;
    private String lat;
    private String lng;

    public Item2 () { }

    public Item2(String name, String lat, String lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public static ArrayList<Item2> createContactsList(int numContacts) {
        ArrayList<Item2> contacts = new ArrayList<Item2>();                     // 수정 필요??

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Item2("Place ", "36", "127"));
        }

        return contacts;
    }
}
