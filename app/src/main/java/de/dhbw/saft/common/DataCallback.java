package de.dhbw.saft.common;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@AllArgsConstructor
public class DataCallback implements Callback {

	private Consumer<String> onComplete;

	@Override
	public void onFailure(@NonNull Call call, @NonNull IOException e) {
		onComplete.accept(null);
	}

	@Override
	public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
		try (response) {
			if (response.isSuccessful()) {
				onComplete.accept(response.body().string());
			} else {
				onComplete.accept(null);
			}
		}
	}
}
