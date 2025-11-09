package de.dhbw.saft.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Menu(String date, Dish[] mainCourses, Dish[] desserts) {

	public record Dish(@NotNull String name, @SerializedName("priceStudent") float price, @NotNull String image) {

		public @NotNull String getDeclarativeName() {
			return name.split(",")[0].trim();
		}

		public @Nullable String getDescription() {
			if (!name.contains(",")) {
				return null;
			}

			return name.split(",", 2)[1].trim();
		}
	}
}
