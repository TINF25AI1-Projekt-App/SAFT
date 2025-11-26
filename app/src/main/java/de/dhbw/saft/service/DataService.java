package de.dhbw.saft.service;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.dhbw.saft.BuildConfig;
import de.dhbw.saft.common.DataCallback;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.model.Lecture;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Service class responsible for loading and caching data from API.
 */
public class DataService {

	@Getter
	private static List<Lecture> lectures = new ArrayList<>();
	@Getter
	private static List<Menu> menus = new ArrayList<>();

	private static final String LECTURE_URL = BuildConfig.API_ENDPOINT + "/rapla/lectures/{0}/events";
	private static final String MENU_URL = BuildConfig.API_ENDPOINT + "/mensa/MA";

	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final Gson GSON = new Gson();

	/**
	 * Fetches and caches all lectures from API.
	 *
	 * @param course The course to fetch the lectures for
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 the lectures have been fetched.
	 */
	public static CompletableFuture<Void> fetchLectures(@NonNull String course) {
		final String url = MessageFormat.format(LECTURE_URL, course);
		try {
			return get(url).thenAccept(json -> {
				if (json == null || json.isEmpty()) {
					return;
				}

				lectures.clear();
				Lecture[] plan = GSON.fromJson(json, Lecture[].class);
				lectures.addAll(Arrays.asList(plan));
			});
		} catch (IllegalArgumentException exception) {
			return CompletableFuture.completedFuture(null);
		}
	}

	/**
	 * Fetches and caches all menus from API.
	 */
	public static void fetchMenus() {
		try {
			get(MENU_URL).thenAccept(json -> {
				if (json == null || json.isEmpty()) {
					return;
				}

				JsonArray root = GSON.fromJson(json, JsonArray.class);
				JsonObject object = root.get(0).getAsJsonObject();
				JsonArray menusArray = object.getAsJsonArray("menus");
				if (menusArray == null) {
					return;
				}

				menus.clear();
				Menu[] menuArray = GSON.fromJson(menusArray, Menu[].class);
				menus.addAll(Arrays.asList(menuArray));
			});
		} catch (IllegalArgumentException ignored) {
		}
	}

	/**
	 * Requests a JSON response from a given url.
	 *
	 * @param url	The url to request
	 * @return 		A {@link  CompletableFuture<String>} that completes with the JSON
	 *           	response body, or exceptionally on any network error.
	 *
	 * @throws 		IllegalArgumentException If the provided URL is invalid
	 */
	private static CompletableFuture<String> get(@NonNull String url) throws IllegalArgumentException {
		final CompletableFuture<String> onComplete = new CompletableFuture<>();

		final Request request = new Request.Builder().url(url).addHeader("Accept", "application/json").build();
		final DataCallback callback = new DataCallback(onComplete);
		CLIENT.newCall(request).enqueue(callback);
		return onComplete;
	}
}
