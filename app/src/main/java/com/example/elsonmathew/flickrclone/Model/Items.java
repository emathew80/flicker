package com.example.elsonmathew.flickrclone.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.elsonmathew.flickrclone.Model.Media;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Items implements Serializable, Parcelable {

    @SerializedName("tags")
    private String tags;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("date_taken")
    private String date_taken;

    @SerializedName("link")
    private String link;

    @SerializedName("author_id")
    private String author_id;

    @SerializedName("published")
    private String published;

    @SerializedName("media")
    public Media media;

    public String getTags ()
    {
        return tags;
    }

    public void setTags (String tags)
    {
        this.tags = tags;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getDate_taken ()
    {
        return date_taken;
    }

    public void setDate_taken (String date_taken)
    {
        this.date_taken = date_taken;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getAuthor_id ()
    {
        return author_id;
    }

    public void setAuthor_id (String author_id)
    {
        this.author_id = author_id;
    }

    public String getPublished ()
    {
        return published;
    }

    public void setPublished (String published)
    {
        this.published = published;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}
}
