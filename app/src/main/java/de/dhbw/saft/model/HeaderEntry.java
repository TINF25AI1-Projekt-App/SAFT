package de.dhbw.saft.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class HeaderEntry extends ScheduleEntry {

	@Getter
	public String date;

	@Override
	public int getType() {
		return TYPE_HEADER;
	}

}
