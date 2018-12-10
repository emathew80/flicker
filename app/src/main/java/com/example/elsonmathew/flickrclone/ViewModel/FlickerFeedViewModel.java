package com.example.elsonmathew.flickrclone.ViewModel;

import android.util.Log;
import android.widget.Toast;

import com.example.elsonmathew.flickrclone.Activities.MainActivity;
import com.example.elsonmathew.flickrclone.Model.FlickrFeed;
import com.example.elsonmathew.flickrclone.Model.FlickrResponse;
import com.example.elsonmathew.flickrclone.Model.Items;
import com.example.elsonmathew.flickrclone.Network.GetDataService;
import com.example.elsonmathew.flickrclone.Network.RetrofitClientInstance;
import com.example.elsonmathew.flickrclone.R;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickerFeedViewModel extends ViewModel {
	private MutableLiveData<List<Items>> flickerFeedsList;
	public LiveData<List<Items>> getFeeds() {
		flickerFeedsList = null;
		if (flickerFeedsList == null) {
			Log.d("78741", "feedList null");

			flickerFeedsList = new MutableLiveData<List<Items>>();
			loadFeed();
		}
		Log.d("78741", "returning feed");
		return flickerFeedsList;
	}

	private void loadFeed() {

		Log.d("78741", "loadFeed");
		GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
		Call<FlickrResponse> call = service.getFlickrResponse();
		call.enqueue(new Callback<FlickrResponse>() {
			@Override
			public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
				Log.d("78741", "onRESPONSE Received");
				flickerFeedsList.setValue(response.body().items);

			}

			@Override
			public void onFailure(Call<FlickrResponse> call, Throwable t) {
//				swipeRefreshLayout.setRefreshing(false);
				Log.d("78741", t.toString());
			}
		});
	}

}
