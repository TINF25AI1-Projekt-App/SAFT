/*
* Copyright 2025 SAFT Authors and Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.dhbw.core.NetworkClient;
import de.dhbw.core.parser.DefaultParser;
import de.dhbw.saft.BuildConfig;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.model.Lecture;
import lombok.Getter;

/**
 * Service class responsible for loading and caching data from API.
 */
public class DataService extends NetworkClient {

	@Getter
	private static List<Lecture> lectures = new ArrayList<>();
	@Getter
	private static List<Menu> menus = new ArrayList<>();
	private static final Gson GSON = new Gson();

	@NonNull
	@Override
	protected ExecutorService getExecutor() {
		return Executors.newFixedThreadPool(4);
	}

	@NonNull
	@Override
	protected String getEndpoint() {
		return BuildConfig.API_ENDPOINT;
	}

	/**
	 * Fetches and caches all lectures from API.
	 *
	 * @param course The course to fetch the lectures for
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 the lectures have been fetched.
	 */
	public CompletableFuture<Void> fetchLectures(@NonNull String course) {
		final String url = MessageFormat.format(LECTURE_URL, course);
		try {
			return fetch(url, new DefaultParser()).thenAccept(json -> {
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
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 all menus have been fetched.
	 */
	public CompletableFuture<Void> fetchMenus() {
		try {
			return fetch(MENU_URL, new DefaultParser()).thenAccept(json -> {
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
		} catch (IllegalArgumentException exception) {
			return CompletableFuture.completedFuture(null);
		}
	}
}
