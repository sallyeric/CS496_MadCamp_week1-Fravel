package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseScore {
    public String name; //수정
    public String score;

    public FirebaseScore(){

    }

    public FirebaseScore(String name, String score){ //수정
        this.name=name;
        this.score=score;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name",name);
        result.put("score",score);
        return result;
    }
}
