package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebasePlace {
    public String name; //수정
    public double lat;
    public double lng;

    public FirebasePlace(){

    }

    public FirebasePlace(String name, double lat, double lng){ //수정
        this.name=name;
        this.lat=lat;
        this.lng=lng;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name",name);
        result.put("lat",lat);
        result.put("lng",lng);
        return result;
    }
}
