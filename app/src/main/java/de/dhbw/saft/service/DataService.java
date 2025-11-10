package de.dhbw.saft.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.dhbw.saft.BuildConfig;
import de.dhbw.saft.common.DataCallback;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.model.Lecture;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DataService {

	@Getter
	private static List<Lecture> lectures = new ArrayList<>();
	@Getter
	private static List<Menu> menus = new ArrayList<>();

	private static final String LECTURE_URL = BuildConfig.API_ENDPOINT + "/rapla/lectures/{0}/events";
	private static final String MENU_URL = BuildConfig.API_ENDPOINT + "/mensa/MA";

	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final Gson GSON = new Gson();

	public static void fetchLectures(String course) {
		final String url = MessageFormat.format(LECTURE_URL, course);
		get(url, json -> {
			if (json == null || json.isEmpty()) {
				return;
			}

			lectures.clear();
			Lecture[] plan = GSON.fromJson(json, Lecture[].class);
			lectures.addAll(Arrays.asList(plan));
		});
	}

	public static void fetchMenus() {
		get(MENU_URL, json -> {
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
	}

	private static void get(String url, Consumer<String> onComplete) {
		Request request = new Request.Builder().url(url).addHeader("Accept", "application/json").build();

		final DataCallback callback = new DataCallback(onComplete);
		CLIENT.newCall(request).enqueue(callback);
	}
}
