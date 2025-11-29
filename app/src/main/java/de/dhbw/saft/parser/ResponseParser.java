package de.dhbw.saft.parser;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Parser interface for converting an HTTP response body into a target type.
 *
 * @param <T>	The type produced by the parser
 */
public interface ResponseParser<T> {

	/**
	 * Parses the given HTTP response body into the target type.
	 *
	 * @param body			The response body to parse
	 * @return 				The parsed value
	 * @throws IOException	If reading or parsing fails
	 */
	T parse(@NonNull ResponseBody body) throws IOException;
}
