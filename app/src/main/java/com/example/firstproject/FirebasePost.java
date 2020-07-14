package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String name; //수정
    public String number;
    public String review;
    public String img;

    public FirebasePost(){

    }

    public FirebasePost(String name, String number, String review, String img){ //수정
        this.name=name;
        this.number=number;
        this.review=review;
        this.img=img;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name", name);
        result.put("number", number);
        result.put("review", review);
        result.put("img", img);
        return result;
    }
}
