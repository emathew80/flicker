package com.example.elsonmathew.flickrclone;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.elsonmathew.flickrclone.Model.FlickrFeed;

public class MainActivity extends AppCompatActivity {
    GestureDetectorCompat gestureObject;
    Context context = this;
    String Url = "";
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
        FetchJSONData process = new FetchJSONData();
        process.execute();

    }

//    private void displayImageAndTitle(String URL, String title)
//    {
//        TextView text = (TextView) findViewById(R.id.textView);        //grab the right element to set title
//        text.setText(title);                                        //set title
//        ImageView image = (ImageView) findViewById(R.id.image);     //grab right element to set image
//        Picasso.with(context).load(URL).into(image);                //set the image using picasso
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener
    {
        //swipe down command
        @Override
        public boolean onDown(MotionEvent e) {
            //Runs Async process got grab JSON
            FetchJSONData process = new FetchJSONData();
            process.execute();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
        {
            if (event2.getX() > event1.getX())
            {
                //Left to right swipe
                i += 1;
//                getMediaUrlAndDisplayImage(Url);

            }
            else if (event1.getX() > event2.getX())
            {
                //Right to left swipe
                i -= 1;
//                getMediaUrlAndDisplayImage(Url);
            }
            return true;
        }
    }
}
