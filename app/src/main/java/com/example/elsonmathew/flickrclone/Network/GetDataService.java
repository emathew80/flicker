package com.example.elsonmathew.flickrclone.Network;
import com.example.elsonmathew.flickrclone.Model.FlickrResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/services/feeds/photos_public.gne?lang=en-us&format=json&nojsoncallback=1")
    Call<FlickrResponse> getFlickrResponse();
}
