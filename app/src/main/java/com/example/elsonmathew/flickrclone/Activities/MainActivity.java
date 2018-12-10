package com.example.elsonmathew.flickrclone.Activities;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.elsonmathew.flickrclone.Adapter.ViewPagerFeedAdapter;
import com.example.elsonmathew.flickrclone.R;
import com.example.elsonmathew.flickrclone.ViewModel.FlickerFeedViewModel;
import com.example.elsonmathew.flickrclone.ZoomOutPageTransformer;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPagerFeedAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.ViewPager);
        adapter = new ViewPagerFeedAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPagerIndicator(viewPager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("78741", "REFRESH TRIGGERED");
                getFeeds();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        getFeeds();
        setupFAB();
        setSwipeListener();
    }

    private void getFeeds(){
        FlickerFeedViewModel feedModel = ViewModelProviders.of(this).get(FlickerFeedViewModel.class);
        feedModel.getFeeds().observe(this, feeds -> {
            adapter.updateData(feeds);

        });
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), OssLicensesMenuActivity.class));
            }
        });
    }

    private void viewPagerIndicator(ViewPager vp) {
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private void setSwipeListener() {
        ViewPager viewPager = findViewById(R.id.ViewPager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeRefreshLayout.setEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }
}
