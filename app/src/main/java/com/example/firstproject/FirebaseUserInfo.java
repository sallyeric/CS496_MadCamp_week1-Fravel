package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserInfo {
    public String userInfo;
    //public Double userLong;
    //public Double userLat;

    public FirebaseUserInfo(){}

    public FirebaseUserInfo(String userInfo){       //, Double userLong, Double userLat){
        this.userInfo=userInfo;
        //this.userLong=userLong;
        //this.userLat=userLat;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("userInfo",userInfo);
        //result.put("userLong",userLong);
        //result.put("userLat",userLat);
        return result;
    }
}
