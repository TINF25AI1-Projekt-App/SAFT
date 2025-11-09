package de.dhbw.saft.model;

import org.jetbrains.annotations.NotNull;

public record Lecture(@NotNull String name, @NotNull Type type, @NotNull String[] rooms, @NotNull String startTime,
		@NotNull String endTime) {

	public @NotNull String getDeclarativeName() {
		return name.trim();
	}

	public enum Type {
		PRESENCE, ONLINE
	}

}
