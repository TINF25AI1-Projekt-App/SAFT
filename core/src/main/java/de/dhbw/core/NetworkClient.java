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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

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
	protected final String COURSE_URL = getEndpoint() + "courses/MA/mapped";
	protected final String MENU_URL = getEndpoint() + "/mensa/MA";

	private static final OkHttpClient CLIENT = new OkHttpClient();

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
}
