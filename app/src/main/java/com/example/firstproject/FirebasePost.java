package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String name; //수정
    public String number;
    public String img;

    public FirebasePost(){

    }

    public FirebasePost(String name, String number){ //수정
        this.name=name;
        this.number=number;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name",name);
        result.put("number",number);
        return result;
    }
}
