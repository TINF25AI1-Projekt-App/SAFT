package de.dhbw.saft.common;

import de.dhbw.saft.model.Menu;

public record MensaCard(String date, Menu.Dish[] mainCourses, Menu.Dish[] desserts) implements Entry {

	@Override
	public Type getEntryType() {
		return Type.ITEM;
	}
}
