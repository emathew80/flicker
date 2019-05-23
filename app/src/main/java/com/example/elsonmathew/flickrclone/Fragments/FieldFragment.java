package com.example.elsonmathew.flickrclone.Fragments;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.elsonmathew.flickrclone.Model.Items;
import com.example.elsonmathew.flickrclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class FieldFragment extends Fragment {

    private Items items;
    private ImageView image;
    private TextView text4;
    private Resources res;




    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = (Items) getArguments().getSerializable("flickerfeed");
        res = getResources();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_layout, container, false);
        String title = items.getTitle();
        Log.d("TITLE", title);
        String author = parseAuthorText(items.getAuthor());
        String dateTaken = items.getDate_taken();
//        String tags = items.getTags();
        TextView text = v.findViewById(R.id.textView);        //grab the right element to set title
        TextView text2 = v.findViewById(R.id.textView2);        //grab the right element to set Author
        TextView text3 = v.findViewById(R.id.textView3);   //grab the right element to set title
        text4 = v.findViewById(R.id.textView4);
//        clickableTags(tags, text4, v);
        text.setText(String.format(res.getString(R.string.title),title));    //Set the title
        text2.setText(String.format(res.getString(R.string.author),author));//Set the Author
        text3.setText(String.format(res.getString(R.string.date_taken),dateTaken));    //Set the Date Taken
        image =  v.findViewById(R.id.imgView);     //grab right element to set image
        String url = items.media.getM();
        String fullSizeImageURL = url.substring(0, url.lastIndexOf("_")) + url.substring(url.lastIndexOf("."));
        Picasso.with(getContext()).load(fullSizeImageURL).into(target);    //Load the image

        return v;
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            runTextRecognition(bitmap);
            image.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };



    private void runTextRecognition(Bitmap imgBitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imgBitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });

    }


    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            text4.setVisibility(View.GONE);
            return;
        }
        text4.setVisibility(View.VISIBLE);
        text4.setText(String.format(res.getString(R.string.MLDrivenText),texts.getText()));


//        for (int i = 0; i < blocks.size(); i++) {
//            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
//            for (int j = 0; j < lines.size(); j++) {
//                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
//                for (int k = 0; k < elements.size(); k++) {
//                }
//            }
//        }
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void clickableTags(String tags, TextView textView, View v){

        String str = getString(R.string.tags, tags);
        SpannableString ss = new SpannableString(str);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.d("Clickable Tags", "on click");
            }
        };

        ss.setSpan(clickableSpan, 6, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private String parseAuthorText(String text){
        String s = text;
        s = s.substring(0,17);
        text = text.replace(s, "");
        s = text.substring(0,3);
        text = text.replace(s, "");
        s = text.substring(text.length()-2, text.length());
        text = text.replace(s, "");
        return text;
    }



}
