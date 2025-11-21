package de.dhbw.saft.common;

public record Header(String date) implements Entry {

	@Override
	public Type getEntryType() {
		return Type.HEADER;
	}

}
