package de.dhbw.saft.parser;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;

public class StringParser implements ResponseParser<String> {

	@Override
	public String parse(@NonNull ResponseBody body) throws IOException {
		return body.string();
	}
}
