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

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.dhbw.core.NetworkClient;
import de.dhbw.saft.BuildConfig;
import de.dhbw.saft.model.Event;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.parser.BitmapParser;
import de.dhbw.saft.parser.EventParser;
import de.dhbw.saft.parser.LectureParser;
import de.dhbw.saft.parser.MenuParser;

/**
 * Service class responsible for loading and caching data from API.
 */
public class DataService extends NetworkClient {

	private static final DataService INSTANCE = new DataService();

	private static List<Event> events = new ArrayList<>();
	private static List<Lecture> lectures = new ArrayList<>();
	private static List<Menu> menus = new ArrayList<>();

	private DataService() {
	}

	public static DataService getInstance() {
		return INSTANCE;
	}

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
	 * Fetches and caches all events from API.
	 *
	 * @param course The course to fetch the lectures for
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 the events have been fetched.
	 */
	public CompletableFuture<Void> fetchEvents(@NonNull String course) {
		final String url = MessageFormat.format(LECTURE_URL, course);
		try {
			return fetch(url, new EventParser()).thenAccept(events -> DataService.events = events);
		} catch (IllegalArgumentException exception) {
			return CompletableFuture.completedFuture(null);
		}
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
			return fetch(url, new LectureParser()).thenAccept(lectures -> DataService.lectures = lectures);
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
			return fetch(MENU_URL, new MenuParser()).thenAccept(menus -> DataService.menus = menus);
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
	public CompletableFuture<Bitmap> fetchImage(@NonNull String url) {
		return fetch(url, new BitmapParser());
	}

	/**
	 * Returns an unmodifiable list of all cached events.
	 *
	 * @return Unmodifiable list of events
	 */
	public static List<Event> getEvents() {
		return Collections.unmodifiableList(events);
	}

	/**
	 * Returns an unmodifiable list of all cached lectures.
	 *
	 * @return Unmodifiable list of lectures
	 */
	public static List<Lecture> getLectures() {
		return Collections.unmodifiableList(lectures);
	}

	/**
	 * Returns an unmodifiable list of all cached menus.
	 *
	 * @return Unmodifiable list of menus
	 */
	public static List<Menu> getMenus() {
		return Collections.unmodifiableList(menus);
	}
}
