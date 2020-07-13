package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String name; //수정
    public String number;
    public String address;

    public FirebasePost(){

    }

    public FirebasePost(String name, String number, String address){ //수정
        this.name=name;
        this.number=number;
        this.address=address;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name",name);
        result.put("number",number);
        result.put("address",address);
        return result;
    }
}
