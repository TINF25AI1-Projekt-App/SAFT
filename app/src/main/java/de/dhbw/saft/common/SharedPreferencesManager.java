/*
* Copyright 2025 SAFT Authors and Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package de.dhbw.saft.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	private static SharedPreferencesManager instance;
	private final SharedPreferences prefs;
	// private static final String PREF_NAME = "MyAppPrefs";

	private SharedPreferencesManager(Context context, String prefNane) {
		prefs = context.getApplicationContext().getSharedPreferences(prefNane, Context.MODE_PRIVATE);
	}

	public static SharedPreferencesManager getInstance(Context context, String prefName) {
		if (instance == null) {
			instance = new SharedPreferencesManager(context, prefName);
		}
		return instance;
	}

	public String getString(String key, String defaultValue) {
		return prefs.getString(key, defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		return prefs.getInt(key, defaultValue);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return prefs.getBoolean(key, defaultValue);
	}

	public long getLong(String key, long defaultValue) {
		return prefs.getLong(key, defaultValue);
	}

	public float getFloat(String key, float defaultValue) {
		return prefs.getFloat(key, defaultValue);
	}

	public void putString(String key, String value) {
		prefs.edit().putString(key, value).apply();
	}

	public void putInt(String key, int value) {
		prefs.edit().putInt(key, value).apply();
	}

	public void putBoolean(String key, boolean value) {
		prefs.edit().putBoolean(key, value).apply();
	}

	public void putLong(String key, long value) {
		prefs.edit().putLong(key, value).apply();
	}

	public void putFloat(String key, float value) {
		prefs.edit().putFloat(key, value).apply();
	}

}
