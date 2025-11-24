package de.dhbw.saft.common;

import androidx.annotation.Nullable;

import lombok.AllArgsConstructor;

public interface Entry {

	Type getEntryType();

	@AllArgsConstructor
	enum Type {
		HEADER(0), ITEM(1);

		public final int identifier;
		public static @Nullable Type fromIdentifier(int identifier) {
			for (Type type : values()) {
				if (type.identifier == identifier) {
					return type;
				}
			}

			return null;
		}
	}
}
