package com.example.elsonmathew.flickrclone;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.elsonmathew.flickrclone.Model.FlickrFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchJSONData extends AsyncTask<String, String, String> {
    public List<FlickrFeed> flickrFeedList = new ArrayList<>();

    @Override
    protected String doInBackground(String... params) { //Run API request in background
        StringBuffer buffer = new StringBuffer();

        try {
            URL url = new URL("https://api.flickr.com/services/feeds/photos_public.gne?format=json"); // Set URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //Open the connection
            InputStream inputStream = httpURLConnection.getInputStream(); //Grab input stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //Initialize buffered reader

            String line = "";

            //Read line when available and add to buffer string
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            return buffer.toString(); //return the final string

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "0";
        } catch (IOException e) {
            e.printStackTrace();
            return "1";
        }
    }

    @Override
    protected void onPostExecute(String str) {
        if (str == "0")
        {
            Log.d("Malformation Error:", "Malformation Error");
        }
        else if (str =="1")
        {
            Log.d("IO Error:", "IO Error");
        }
        String validJSON = getValidJson(str);
        getMediaUrlAndTitle(validJSON); //set the image on main activity
    }

    private String getValidJson (String json)
    {
        json = json.replace("jsonFlickrFeed(", "");                  //Removes the Jsonflickerfeed([
        json = json.substring(0, json.length() - 2);                //Removes ])
        String s = json;
        String validJson = s.substring(s.indexOf("\"items\":")+8); // Removes everything before and including "items": to get us a valid json we can work with.
        return validJson;
    }

    public void getMediaUrlAndTitle(String str)
    {
        try {
            JSONArray jsonArray = new JSONArray(str);
            for (int j = 0; j <= jsonArray.length(); j++)
            {
                FlickrFeed feed = new FlickrFeed();
                JSONObject jObj = (JSONObject) jsonArray.get(j);
                String unparsedMediaUrl = jObj.optString("media", "");
                String parsedMediaUrl = parseMediaUrl(unparsedMediaUrl); //parse the media/image url string
                String title = jObj.optString("title", "");
                Log.d("MPURL and TITLE", parsedMediaUrl + title);
                feed.mediaURL = parsedMediaUrl;
                feed.imgTitle = title;
                flickrFeedList.add(feed);
            }
//            Log.d("ARRJobjlength", String.valueOf(jObj[0]));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseMediaUrl(String unparsedMediaUrl)
    {
        //{"m":"https:\/\/farm1.staticflickr.com\/919\/28766447197_547393653d_m.jpg"} is returned string format
        String s = unparsedMediaUrl;
        String parsedMediaURL = s.substring(s.indexOf("{\"m\":\"")+6);                  //removes {"m":"
        parsedMediaURL = parsedMediaURL.substring(0, parsedMediaURL.length() - 2);      //Removes "}

        return parsedMediaURL;
    }
}
