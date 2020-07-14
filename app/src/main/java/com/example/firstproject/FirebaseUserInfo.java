package com.example.firstproject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserInfo {
    public String signupUsername; //수정
    public String signupPassword;
    public String signupFullname;
    public String signupBirthday;
    public String signupEmail;

    public FirebaseUserInfo(){

    }

    public FirebaseUserInfo(String signupUsername, String signupPassword, String signupFullname,
                        String signupBirthday, String signupEmail){ //수정
        this.signupUsername=signupUsername;
        this.signupPassword=signupPassword;
        this.signupFullname=signupFullname;
        this.signupBirthday=signupBirthday;
        this.signupEmail=signupEmail;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("signupUsername",signupUsername);
        result.put("signupPassword",signupPassword);
        result.put("signupFullname",signupFullname);
        result.put("signupBirthday",signupBirthday);
        result.put("signupEmail",signupEmail);
        return result;
    }
}
