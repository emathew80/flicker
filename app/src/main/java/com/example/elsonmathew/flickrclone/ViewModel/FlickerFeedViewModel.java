package com.example.elsonmathew.flickrclone.ViewModel;
import android.util.Log;
import com.example.elsonmathew.flickrclone.Model.FlickrResponse;
import com.example.elsonmathew.flickrclone.Model.Items;
import com.example.elsonmathew.flickrclone.Network.GetDataService;
import com.example.elsonmathew.flickrclone.Network.RetrofitClientInstance;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickerFeedViewModel extends ViewModel {
	private MutableLiveData<List<Items>> flickerFeedsList;
	public LiveData<List<Items>> getFeeds() {
		if (flickerFeedsList == null) {
			flickerFeedsList = new MutableLiveData<List<Items>>();
			loadFeed();
		}
		return flickerFeedsList;
	}



	public void loadFeed() {
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
