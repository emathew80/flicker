package com.example.elsonmathew.flickrclone.Model;
import java.io.Serializable;

public class FlickrFeed implements Serializable {
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
