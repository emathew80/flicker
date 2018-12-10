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
			flickerFeedsList = new MutableLiveData<List<Items>>();
			loadFeed();
		}
		return flickerFeedsList;
	}

	private void loadFeed() {
		GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
		Call<FlickrResponse> call = service.getFlickrResponse();
		call.enqueue(new Callback<FlickrResponse>() {
			@Override
			public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
				flickerFeedsList.setValue(response.body().items);

			}

			@Override
			public void onFailure(Call<FlickrResponse> call, Throwable t) {
				Log.d("78741", t.toString());
			}
		});
	}

}
