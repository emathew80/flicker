package com.example.elsonmathew.flickrclone.Model;

import com.example.elsonmathew.flickrclone.FetchJSONData;

public class FlickrFeed {
    public String mediaURL;
    public String imgTitle;

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }
}
