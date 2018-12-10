package com.example.elsonmathew.flickrclone.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elsonmathew.flickrclone.Model.Items;
import com.example.elsonmathew.flickrclone.R;
import com.squareup.picasso.Picasso;

public class FieldFragment extends Fragment {

    private Items items;


    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = (Items) getArguments().getSerializable("flickerfeed");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Resources res = getResources();
        View v = inflater.inflate(R.layout.custom_layout, container, false);
        String title = items.getTitle();
        Log.d("TITLE", title);
        String author = parseAuthorText(items.getAuthor());
        String dateTaken = items.getDate_taken();
        String tags = items.getTags();
        TextView text = v.findViewById(R.id.textView);        //grab the right element to set title
        TextView text2 = v.findViewById(R.id.textView2);        //grab the right element to set Author
        TextView text3 = v.findViewById(R.id.textView3);   //grab the right element to set title
        TextView text4 = v.findViewById(R.id.textView4);
//        clickableTags(tags, text4, v);
        text.setText(String.format(res.getString(R.string.title),title));    //Set the title
        text2.setText(String.format(res.getString(R.string.author),author));//Set the Author
        text3.setText(String.format(res.getString(R.string.date_taken),dateTaken));    //Set the Date Taken
        ImageView image =  v.findViewById(R.id.imgView);     //grab right element to set image
        Picasso.with(getContext()).load(items.media.getM()).into(image);    //Load the image
        return v;
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
    }

//    private void clickableTags(String tags, TextView textView, View v){
//
//        String str = getString(R.string.tags, tags);
//        SpannableString ss = new SpannableString(str);
//        Toast.makeText(getActivity(), "get state " + bottomSheetBehavior.getState(), Toast.LENGTH_SHORT).show();
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                Toast.makeText(getActivity(), "get state " + bottomSheetBehavior.getState(), Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        ss.setSpan(clickableSpan, 6, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(ss);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//    }


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
