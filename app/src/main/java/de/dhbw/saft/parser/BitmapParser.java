package de.dhbw.saft.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;

public class BitmapParser implements ResponseParser<Bitmap> {

	@Override
	public Bitmap parse(@NonNull ResponseBody body) throws IOException {
		byte[] bytes = body.bytes();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
