package com.example;
import org.json.JSONObject;

public class jsonProcessing {
    public static String challenge(JSONObject object){
        //get the id
        return object.getString("fullId");
    }
}
