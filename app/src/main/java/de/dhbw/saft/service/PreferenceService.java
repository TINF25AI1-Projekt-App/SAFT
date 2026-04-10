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
package de.dhbw.saft.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Service class responsible for storing and reading preferences.
 */
public class PreferenceService {

	public static final String PREFERENCES_NAME = "settings_preferences";

	private final SharedPreferences preferences;

	public PreferenceService(Context context) {
		preferences = context.getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * Gets the preference value as String.
	 *
	 * @param key			Key to the preference value
	 * @param defaultValue	Value to return if the preference is not found
	 * @return				Preference as String or defaultValue
	 */
	public String getString(String key, String defaultValue) {
		return preferences.getString(key, defaultValue);
	}

	/**
	 * Gets the preference value as int.
	 *
	 * @param key			Key to the preference value
	 * @return				Preference as String or 0
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/**
	 * Gets the preference value as int.
	 *
	 * @param key			Key to the preference value
	 * @param defaultValue	Value to return if the preference is not found
	 * @return				Preference as int or defaultValue
	 */
	public int getInt(String key, int defaultValue) {
		return preferences.getInt(key, defaultValue);
	}

	/**
	 * Stores a String preference value.
	 *
	 * @param key		Key to the preference value
	 * @param value		Value to store
	 */
	public void putString(String key, String value) {
		preferences.edit().putString(key, value).apply();
	}

	/**
	 * Stores a int preference value.
	 *
	 * @param key		Key to the preference value
	 * @param value		Value to store
	 */
	public void putInt(String key, int value) {
		preferences.edit().putInt(key, value).apply();
	}
}
