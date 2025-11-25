package de.dhbw.saft.common;

import androidx.annotation.NonNull;

public record DishCard(@NonNull String name, float price, String image, String description) implements Entry {

	@Override
	public Type getEntryType() {
		return Type.ITEM;
	}
}
