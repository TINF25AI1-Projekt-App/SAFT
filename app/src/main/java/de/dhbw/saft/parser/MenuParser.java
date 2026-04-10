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
package de.dhbw.saft.parser;

import static de.dhbw.core.NetworkClient.GSON;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.dhbw.core.parser.ResponseParser;
import de.dhbw.saft.model.Menu;
import okhttp3.ResponseBody;

/**
 * Parser that converts an HTTP response body into a List of {@link Menu}.
 */
public class MenuParser implements ResponseParser<List<Menu>> {

	@Override
	public List<Menu> parse(@NonNull ResponseBody body) throws IOException {
		final String json = body.string();
		if (json.isEmpty()) {
			return Collections.emptyList();
		}

		JsonArray root = GSON.fromJson(json, JsonArray.class);
		JsonObject object = root.get(0).getAsJsonObject();
		JsonArray menusArray = object.getAsJsonArray("menus");
		if (menusArray == null) {
			return Collections.emptyList();
		}

		Menu[] menuArray = GSON.fromJson(menusArray, Menu[].class);
		return Arrays.asList(menuArray);
	}
}
