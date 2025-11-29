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
package de.dhbw.saft.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Represents a single day menu used for mapping JSON response from API.
 */
public record Menu(String date, Dish[] mainCourses, Dish[] desserts) {

	/**
	 * Represents a single dish in the menu.
	 */
	public record Dish(@NonNull String name, @SerializedName("priceStudent") float price, @Nullable String image) {

		/**
		 * Extracts the dish name from additional description.
		 *
		 * @return The actual name
		 */
		public @NonNull String getDeclarativeName() {
			return name.split(",")[0].trim();
		}

		/**
		 * Extracts the additional description from the name.
		 *
		 * @return The ingredients
		 */
		public @Nullable String getDescription() {
			if (!name.contains(",")) {
				return null;
			}

			return name.split(",", 2)[1].trim();
		}

		@Override
		public @NonNull String toString() {
			return "name:" + name + ", price: " + price + ", image: " + image;
		}
	}

	@Override
	public @NonNull String toString() {
		return "date: " + date + ", mainCourses: [" + Arrays.toString(mainCourses) + "]" + ", desserts: ["
				+ Arrays.toString(desserts) + "]";
	}
}
