package de.dhbw.saft.service;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.dhbw.saft.BuildConfig;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.parser.BitmapParser;
import de.dhbw.saft.parser.ResponseParser;
import de.dhbw.saft.parser.StringParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Service class responsible for loading and caching data from API.
 */
public class DataService {

	private static final List<Lecture> LECTURES = new ArrayList<>();
	private static final List<Menu> MENUS = new ArrayList<>();

	private static final String LECTURE_URL = BuildConfig.API_ENDPOINT + "/rapla/lectures/{0}/events";
	private static final String MENU_URL = BuildConfig.API_ENDPOINT + "/mensa/MA";

	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(4);
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
			return get(url, new StringParser()).thenAccept(json -> {
				if (json == null || json.isEmpty()) {
					return;
				}

				LECTURES.clear();
				Lecture[] plan = GSON.fromJson(json, Lecture[].class);
				LECTURES.addAll(Arrays.asList(plan));
			});
		} catch (IllegalArgumentException exception) {
			return CompletableFuture.completedFuture(null);
		}
	}

	/**
	 * Fetches and caches all menus from API.
	 *
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 all menus have been fetched.
	 */
	public static CompletableFuture<Void> fetchMenus() {
		try {
			return get(MENU_URL, new StringParser()).thenAccept(json -> {
				if (json == null || json.isEmpty()) {
					return;
				}

				JsonArray root = GSON.fromJson(json, JsonArray.class);
				JsonObject object = root.get(0).getAsJsonObject();
				JsonArray menusArray = object.getAsJsonArray("menus");
				if (menusArray == null) {
					return;
				}

				MENUS.clear();
				Menu[] menuArray = GSON.fromJson(menusArray, Menu[].class);
				MENUS.addAll(Arrays.asList(menuArray));
			});
		} catch (IllegalArgumentException exception) {
			return CompletableFuture.completedFuture(null);
		}
	}

	/**
	 * Fetches an Image and parses it to a {@link Bitmap}.
	 *
	 * @param url	The url to request
	 * @return 		A {@link CompletableFuture<Bitmap>}
	 */
	public static CompletableFuture<Bitmap> fetchImage(@NonNull String url) {
		return get(url, new BitmapParser());
	}

	/**
	 * Returns an unmodifiable list of all cached lectures.
	 *
	 * @return Unmodifiable list of lectures
	 */
	public static List<Lecture> getLectures() {
		return Collections.unmodifiableList(LECTURES);
	}

	/**
	 * Returns an unmodifiable list of all cached menus.
	 *
	 * @return Unmodifiable list of menus
	 */
	public static List<Menu> getMenus() {
		return Collections.unmodifiableList(MENUS);
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
	private static <T> CompletableFuture<T> get(@NonNull String url, @NonNull ResponseParser<T> parser) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Request request = new Request.Builder().url(url).addHeader("Accept", "application/json").build();

				try (Response response = CLIENT.newCall(request).execute()) {
					if (!response.isSuccessful()) {
						return null;
					}

					final ResponseBody body = response.body();
					return parser.parse(body);
				}
			} catch (Exception exception) {
				return null;
			}
		}, IO_EXECUTOR);
	}

}
