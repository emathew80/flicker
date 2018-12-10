package com.example.elsonmathew.flickrclone.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;


public class FlickrResponse {
    @SerializedName("items")
    public List<Items> items;
}
