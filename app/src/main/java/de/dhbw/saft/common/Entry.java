package de.dhbw.saft.common;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;

/**
 * Base interface for list entries in recycler fragments.
 * Implementations represent either header or content items.
 */
public interface Entry {

	/**
	 * Returns the type of this entry.
	 *
	 * @return	The entry type
	 */
	Type getEntryType();

	@AllArgsConstructor
	enum Type {
		HEADER(0), ITEM(1);

		public final int identifier;
		public static @NonNull Type fromIdentifier(int identifier) {
			for (Type type : values()) {
				if (type.identifier == identifier) {
					return type;
				}
			}

			throw new IllegalArgumentException("Unknown identifier: " + identifier);
		}
	}
}
