package de.dhbw.saft.parser;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;

public interface ResponseParser<T> {

	T parse(@NonNull ResponseBody body) throws IOException;
}
