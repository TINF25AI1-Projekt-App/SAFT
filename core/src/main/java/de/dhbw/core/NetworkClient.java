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
package de.dhbw.core;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import de.dhbw.core.model.Course;
import de.dhbw.core.parser.CourseListParser;
import de.dhbw.core.parser.DefaultParser;
import de.dhbw.core.parser.ResponseParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Provides asynchronous HTTP request execution and response parsing.
 */
public abstract class NetworkClient {

	protected abstract @NotNull ExecutorService getExecutor();
	protected abstract @NotNull String getEndpoint();

	protected final String LECTURE_URL = getEndpoint() + "/rapla/lectures/{0}/events";
	protected final String COURSE_URL = getEndpoint() + "/courses/MA/mapped";
	protected final String MENU_URL = getEndpoint() + "/mensa/MA";

	protected static final DefaultParser DEFAULT_PARSER = new DefaultParser();
	protected static final Gson GSON = new Gson();

	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static List<String> courses = new ArrayList<>();

	/**
	 * Fetches and caches all courses from API.
	 *
	 * @return 		 A {@link CompletableFuture<Void>} which is completed once
	 * 				 the courses have been fetched.
	 */
	public @NotNull CompletableFuture<Void> fetchCourses() {
		return fetch(COURSE_URL, new CourseListParser(GSON)).thenAccept(response -> {
			courses.clear();
			courses.addAll(response.stream().map(Course::name).toList());
		});
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
	protected <T> CompletableFuture<T> fetch(@NotNull String url, @NotNull ResponseParser<T> parser) {
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
		}, getExecutor());
	}

	public static List<String> getCourses() {
		return Collections.unmodifiableList(courses);
	}
}
