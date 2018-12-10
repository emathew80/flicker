package com.example.elsonmathew.flickrclone.Adapter;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import com.example.elsonmathew.flickrclone.Fragments.FieldFragment;
import com.example.elsonmathew.flickrclone.Model.Items;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerFeedAdapter extends FragmentStatePagerAdapter {

    private List<Items> itemsList;

    public ViewPagerFeedAdapter(FragmentManager fm){
        super(fm);
        itemsList = new ArrayList<>();
    }



    public int getCount(){
        return itemsList.size();
    }



    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.getRootView().setTag(itemsList);
        return super.instantiateItem(container, position);
    }



    @Override
    public FieldFragment getItem(final int position){
        FieldFragment fragment = new FieldFragment();
        final Bundle args = new Bundle();
        args.putSerializable("flickerfeed", itemsList.get(position));
        fragment.setArguments(args);
        return fragment;
    }



    public void updateData(List<Items> items){
        itemsList = items;
        Log.d("ITEMS", String.valueOf(itemsList.size()));
        for (int i=0; i<itemsList.size(); i++)
        {
            Log.d("ITEMS", itemsList.get(i).getTitle());

        }
        notifyDataSetChanged();
    }


}
