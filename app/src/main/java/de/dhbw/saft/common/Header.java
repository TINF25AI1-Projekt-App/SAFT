package de.dhbw.saft.common;

/**
 * Entry representing a date header.
 */
public record Header(String date) implements Entry {

	@Override
	public Type getEntryType() {
		return Type.HEADER;
	}

}
