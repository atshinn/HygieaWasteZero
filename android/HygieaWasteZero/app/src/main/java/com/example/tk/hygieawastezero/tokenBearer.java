package com.example.tk.hygieawastezero;

import org.json.JSONException;
import org.json.JSONObject;

public class tokenBearer {

    private String unsplitToken = "";
    private String idToken = "";
    private String accessToken = "";
    private int expiration = 0;
    private String tokenType = "";

    tokenBearer(String t){
        unsplitToken = t;
        tokenSplitter();
    }

    public void tokenSplitter(){
        String[] split = unsplitToken.split("#id_token=|&access_token=|&expires_in=|&token_type=");
        idToken = split[1];
        accessToken = split[2];
        expiration = Integer.parseInt(split[3]);
        tokenType = split[4];
    }

    public String getUnsplitToken() {
        return unsplitToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String jsonStringify(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("id", idToken);
            obj.put("access", accessToken);
            obj.put("expiration", expiration);
            obj.put("type", tokenType);
        } catch (JSONException j){
            j.printStackTrace();
        }
        return obj.toString();
    }
}
