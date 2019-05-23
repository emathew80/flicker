package com.example.elsonmathew.flickrclone.Fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.elsonmathew.flickrclone.MLKitResources.GraphicOverlay;
import com.example.elsonmathew.flickrclone.MLKitResources.LabelGraphic;
import com.example.elsonmathew.flickrclone.Model.Items;
import com.example.elsonmathew.flickrclone.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class FieldFragment extends Fragment {

	private Items items;
	private ImageView image;
	private TextView text4;
	private TextView text5;
	private Resources res;
	private FirebaseModelInterpreter mInterpreter;
	private FirebaseModelInputOutputOptions mDataOptions;
	private List<String> mLabelList;
	private Bitmap mSelectedImage;


	private static final int DIM_BATCH_SIZE = 1;
	private static final int DIM_PIXEL_SIZE = 3;
	private static final int DIM_IMG_SIZE_X = 224;
	private static final int DIM_IMG_SIZE_Y = 224;
	private static final int RESULTS_TO_SHOW = 3;

	private static final String HOSTED_MODEL_NAME = "detection-model";
	private static final String LOCAL_MODEL_ASSET = "mobilenet_v1_1.0_224_quant.tflite";
	private static final String LABEL_PATH = "labels.txt";
	private static final String TAG = "FieldFragment";

	private final int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];


	private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
			new PriorityQueue<>(
					RESULTS_TO_SHOW,
					(o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));



	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		items = (Items) getArguments().getSerializable("flickerfeed");
		res = getResources();
		initCustomModel();
	}



	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.custom_layout, container, false);
		String title = items.getTitle();
		Log.d("TITLE", title);
		String author = parseAuthorText(items.getAuthor());
		String dateTaken = items.getDate_taken();
		TextView text = v.findViewById(R.id.textView);        //grab the right element to set title
		TextView text2 = v.findViewById(R.id.textView2);        //grab the right element to set Author
		TextView text3 = v.findViewById(R.id.textView3);   //grab the right element to set title
		text4 = v.findViewById(R.id.textView4);
		text5 = v.findViewById(R.id.textView5);
		text.setText(String.format(res.getString(R.string.title), title));    //Set the title
		text2.setText(String.format(res.getString(R.string.author), author));//Set the Author
		text3.setText(String.format(res.getString(R.string.date_taken), dateTaken));    //Set the Date Taken
		image = v.findViewById(R.id.imgView);     //grab right element to set image
		String url = items.media.getM();
		String fullSizeImageURL = url.substring(0, url.lastIndexOf("_")) + url.substring(url.lastIndexOf("."));
		Picasso.with(getContext()).load(fullSizeImageURL).into(target);    //Load the image

		return v;
	}



	private void clickableTags(String tags, TextView textView, View v) {

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



	private String parseAuthorText(String text) {
		String s = text;
		s = s.substring(0, 17);
		text = text.replace(s, "");
		s = text.substring(0, 3);
		text = text.replace(s, "");
		s = text.substring(text.length() - 2, text.length());
		text = text.replace(s, "");
		return text;
	}



	private Target target = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			mSelectedImage = bitmap;
			runTextRecognition(mSelectedImage);
			image.setImageBitmap(mSelectedImage);
			runModelInference();

		}



		@Override
		public void onBitmapFailed(Drawable errorDrawable) {
		}



		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
		}
	};



	/*************************************************************************************************************************
	 *************************************************** TEXT RECOGNITION ***********************************************
	 ***************************************************************************************************************************/


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
			text5.setVisibility(View.GONE);
			return;
		}
		text5.setVisibility(View.VISIBLE);
		text5.setText(String.format(res.getString(R.string.MLDrivenText), texts.getText()));
	}



	/*************************************************************************************************************************
	 ************************************************** END TEXT RECOGNITION ***********************************************
	 ***************************************************************************************************************************/


	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}



	/*************************************************************************************************************************
	 ************************************************** IMAGE RECOGNITION ***********************************************
	 ***************************************************************************************************************************/


	private void initCustomModel() {
		mLabelList = loadLabelList(this.getActivity());

		int[] inputDims = {DIM_BATCH_SIZE, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, DIM_PIXEL_SIZE};
		int[] outputDims = {DIM_BATCH_SIZE, mLabelList.size()};
		try {
			mDataOptions =
					new FirebaseModelInputOutputOptions.Builder()
							.setInputFormat(0, FirebaseModelDataType.BYTE, inputDims)
							.setOutputFormat(0, FirebaseModelDataType.BYTE, outputDims)
							.build();
			FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions
					.Builder()
					.requireWifi()
					.build();
			FirebaseRemoteModel remoteModel = new FirebaseRemoteModel.Builder
					(HOSTED_MODEL_NAME)
					.enableModelUpdates(true)
					.setInitialDownloadConditions(conditions)
					.setUpdatesDownloadConditions(conditions)  // You could also specify
					// different conditions
					// for updates
					.build();
			FirebaseLocalModel localModel =
					new FirebaseLocalModel.Builder("asset")
							.setAssetFilePath(LOCAL_MODEL_ASSET).build();
			FirebaseModelManager manager = FirebaseModelManager.getInstance();
			manager.registerRemoteModel(remoteModel);
			manager.registerLocalModel(localModel);
			FirebaseModelOptions modelOptions =
					new FirebaseModelOptions.Builder()
							.setRemoteModelName(HOSTED_MODEL_NAME)
							.setLocalModelName("asset")
							.build();
			mInterpreter = FirebaseModelInterpreter.getInstance(modelOptions);
		} catch (FirebaseMLException e) {
			showToast("Error while setting up the model");
			e.printStackTrace();
		}
	}



	/**
	 * Reads label list from Assets.
	 */
	private List<String> loadLabelList(Activity activity) {
		List<String> labelList = new ArrayList<>();
		try (BufferedReader reader =
				     new BufferedReader(new InputStreamReader(activity.getAssets().open
						     (LABEL_PATH)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				labelList.add(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "Failed to read label list.", e);
		}
		return labelList;
	}



	private void runModelInference() {
		if (mInterpreter == null) {
			Log.e(TAG, "Image classifier has not been initialized; Skipped.");
			return;
		}
		// Create input data.
		ByteBuffer imgData = convertBitmapToByteBuffer(mSelectedImage, mSelectedImage.getWidth(),
				mSelectedImage.getHeight());

		try {
			FirebaseModelInputs inputs = new FirebaseModelInputs.Builder().add(imgData).build();
			// Here's where the magic happens!!
			mInterpreter
					.run(inputs, mDataOptions)
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							e.printStackTrace();
							showToast("Error running model inference");
						}
					})
					.continueWith(
							new Continuation<FirebaseModelOutputs, List<String>>() {
								@Override
								public List<String> then(Task<FirebaseModelOutputs> task) {
									byte[][] labelProbArray = task.getResult()
											.<byte[][]>getOutput(0);
									List<String> topLabels = getTopLabels(labelProbArray);
									for (int i = 0; i < topLabels.size(); i++) {
										text4.setText(String.format(res.getString(R.string.MLDrivenImageRec), topLabels.get(i)));
									}

									return topLabels;
								}
							});
		} catch (FirebaseMLException e) {
			e.printStackTrace();
			showToast("Error running model inference");
		}

	}



	/**
	 * Gets the top labels in the results.
	 */
	private synchronized List<String> getTopLabels(byte[][] labelProbArray) {
		for (int i = 0; i < mLabelList.size(); ++i) {
			sortedLabels.add(
					new AbstractMap.SimpleEntry<>(mLabelList.get(i), (labelProbArray[0][i] &
							0xff) / 255.0f));
			if (sortedLabels.size() > RESULTS_TO_SHOW) {
				sortedLabels.poll();
			}
		}
		List<String> result = new ArrayList<>();
		final int size = sortedLabels.size();
		for (int i = 0; i < size; ++i) {
			Map.Entry<String, Float> label = sortedLabels.poll();
			Float value = label.getValue() * 100;
			if (value > 0.0f) {
				if (value == 100) {
					result.add("I am confident this is a " + label.getKey());
				} else {
					result.add("With " + value.intValue() + "% confidence I suspect there is a " + label.getKey() + " in this image");
				}
			}
		}
		Log.d(TAG, "labels: " + result.toString());
		return result;
	}



	/**
	 * Writes Image data into a {@code ByteBuffer}.
	 */
	private synchronized ByteBuffer convertBitmapToByteBuffer(
			Bitmap bitmap, int width, int height) {
		ByteBuffer imgData =
				ByteBuffer.allocateDirect(
						DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
		imgData.order(ByteOrder.nativeOrder());
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y,
				true);
		imgData.rewind();
		scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0,
				scaledBitmap.getWidth(), scaledBitmap.getHeight());
		// Convert the image to int points.
		int pixel = 0;
		for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
			for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
				final int val = intValues[pixel++];
				imgData.put((byte) ((val >> 16) & 0xFF));
				imgData.put((byte) ((val >> 8) & 0xFF));
				imgData.put((byte) (val & 0xFF));
			}
		}
		return imgData;
	}



	/*************************************************************************************************************************
	 ************************************************* END IMAGE RECOGNITION **************************************************
	 ***************************************************************************************************************************/

	private void showToast(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}


}
