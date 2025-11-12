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
