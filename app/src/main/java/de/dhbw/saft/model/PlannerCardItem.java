package de.dhbw.saft.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PlannerCardItem extends ScheduleEntry {
	@Getter
	private String name;
	@Getter
	private Lecture.Type lectureType;
	@Getter
	private String[] rooms;
	@Getter
	private String start;
	@Getter
	private String end;

	@Override
	public int getType() {
		return TYPE_ITEM;
	}
}
