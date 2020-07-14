package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebasePlace {
    public String name; //수정
    public String lat;
    public String lng;

    public FirebasePlace(){

    }

    public FirebasePlace(String name, String lat, String lng){ //수정
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
