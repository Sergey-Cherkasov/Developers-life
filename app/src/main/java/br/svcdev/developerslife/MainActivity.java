package br.svcdev.developerslife;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import br.svcdev.developerslife.databinding.ActivityMainBinding;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

	private NetworkRequest mRetrofit;
	private ActivityMainBinding mBinding;
	private Stack<ResponseJson> backStack;
	private Stack<ResponseJson> forwardStack;
	private ResponseJson responseJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(mBinding.getRoot());

		initRetrofit();

		mBinding.previousBtn.setEnabled(false);

		backStack = new Stack<>();
		forwardStack = new Stack<>();

		mBinding.nextBtn.setOnClickListener(v -> {
			backStack.push(responseJson);
			if (forwardStack.isEmpty()) {
				sendRequest();
			} else {
				responseJson = forwardStack.pop();
				updateUI(responseJson);
			}
			mBinding.previousBtn.setEnabled(true);
		});

		mBinding.previousBtn.setOnClickListener(v -> {
			forwardStack.push(responseJson);
			if (!backStack.isEmpty()) {
				responseJson = backStack.pop();
				mBinding.previousBtn.setEnabled(!backStack.empty());
				updateUI(responseJson);
			}
		});
	}

	private OkHttpClient getOkHttpClient() {
		return new OkHttpClient.Builder()
				.readTimeout(10, TimeUnit.SECONDS)
				.connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.build();
	}

	private void updateUI(ResponseJson responseJson) {
		Glide.with(mBinding.getRoot())
				.load(responseJson.getGifUrl())
				.into(mBinding.imageView);
		mBinding.textView.setText(responseJson.getDescription());
	}

	private void sendRequest() {
		mRetrofit.sendRequest(true).enqueue(new Callback<ResponseJson>() {
			@Override
			public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
				if (response.body() != null) {
					responseJson = response.body();
					updateUI(responseJson);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {

			}
		});
	}

	private void initRetrofit() {
		mRetrofit = new Retrofit.Builder().baseUrl("https://developerslife.ru/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(getOkHttpClient())
				.build()
				.create(NetworkRequest.class);
	}

	@Override
	protected void onStart() {
		super.onStart();
		sendRequest();
	}
}