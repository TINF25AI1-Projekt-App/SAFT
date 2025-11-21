package de.dhbw.saft.model;

public abstract class ScheduleEntry {
	public static final int TYPE_HEADER = 0;
	public static final int TYPE_ITEM = 1;
	public abstract int getType();
}
