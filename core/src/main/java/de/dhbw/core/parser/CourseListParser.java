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
package de.dhbw.core.parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dhbw.core.model.Course;
import lombok.AllArgsConstructor;
import okhttp3.ResponseBody;

/**
 * {@link ResponseParser} implementation used to parse Courses.
 */
@AllArgsConstructor
public class CourseListParser implements ResponseParser<List<Course>> {

	private Gson gson;

	@Override
	public List<Course> parse(@NotNull ResponseBody body) throws IOException {
		final String response = body.string();
		final List<Course> courses = new ArrayList<>();
		JsonObject root = gson.fromJson(response, JsonObject.class);
		for (String section : new String[]{"t", "w", "g"}) {

			JsonObject block = root.getAsJsonObject(section);
			if (block == null)
				continue;

			for (Map.Entry<String, JsonElement> entry : block.entrySet()) {
				JsonElement value = entry.getValue();

				if (!value.isJsonArray()) {
					continue;
				}

				for (JsonElement element : value.getAsJsonArray()) {
					final Course course = new Course(element.getAsString());
					courses.add(course);
				}
			}
		}

		return courses;
	}
}
