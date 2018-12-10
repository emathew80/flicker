package com.example.elsonmathew.flickrclone.Model;

import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("m")
    private String m;

    public Media(String m){
        this.m = m;
    }

    public String getM ()
    {
        return m;
    }

}
