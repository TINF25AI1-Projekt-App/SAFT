package de.dhbw.saft.common;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * OkHttp {@link Callback} implementation that delivers the response body.
 */
@AllArgsConstructor
public class DataCallback implements Callback {

	@NonNull
	private CompletableFuture<String> onComplete;

	@Override
	public void onFailure(@NonNull Call call, @NonNull IOException e) {
		onComplete.completeExceptionally(new Throwable("Request Failed.", e));
	}

	@Override
	public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
		try (response) {
			if (response.isSuccessful()) {
				onComplete.complete(response.body().string());
			} else {
				onComplete.complete(null);
			}
		}
	}
}
