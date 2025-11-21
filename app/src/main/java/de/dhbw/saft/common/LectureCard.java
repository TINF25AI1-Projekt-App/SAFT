package de.dhbw.saft.common;

import de.dhbw.saft.model.Lecture;

public record LectureCard(String name, Lecture.Type type, String[] rooms, String start, String end) implements Entry {

	@Override
	public Entry.Type getEntryType() {
		return Type.ITEM;
	}
}
