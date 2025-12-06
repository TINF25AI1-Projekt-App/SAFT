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

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Map;

import de.dhbw.saft.R;

public class AddPreferenceDialog {
	private final Context context;
	private final SharedPreferencesManager prefs;
	private final String[] suggestions;
	private final Map<String, String> keyValueMap;
	private final TextView textView;
	private final String preference;
	private OnPreferenceSavedListener listener;

	public AddPreferenceDialog(Context context, SharedPreferencesManager prefs, Map<String, String> keyValueMap,
			String[] suggestions, @Nullable TextView textView, String preference) {
		this.context = context;
		this.prefs = prefs;
		this.keyValueMap = keyValueMap;
		this.suggestions = suggestions;
		this.textView = textView;
		this.preference = capitalizeFirst(preference);
	}

	private static String capitalizeFirst(String str) {
		if (str == null || str.isEmpty())
			return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public void setOnPreferenceSavedListener(OnPreferenceSavedListener listener) {
		this.listener = listener;
	}

	public void show() {
		final AutoCompleteTextView input = new AutoCompleteTextView(context);
		String existingValue = null;

		for (String key : keyValueMap.keySet()) {
			String value = prefs.getString(key, null);
			if (value != null && !value.isEmpty()) {
				existingValue = value;
				break;
			}
		}

		if (existingValue != null)
			input.setText(existingValue);
		else
			input.setHint((context.getString(R.string.preference_hint_course)));

		ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,
				suggestions);
		input.setAdapter(adapter);
		input.setThreshold(1);

		AlertDialog.Builder builder = getBuilder(input);

		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private AlertDialog.Builder getBuilder(AutoCompleteTextView input) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(String.format(context.getString(R.string.add_preference_title), preference));
		builder.setMessage(R.string.add_preference_message);
		builder.setView(input);

		builder.setPositiveButton(R.string.ok, (dialog, which) -> {
			String text = input.getText().toString().trim();
			if (!text.isEmpty()) {
				for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().replace("${INPUT}", text);
					prefs.putString(key, value);
				}
				assert textView != null;
				textView.setText(text);
				if (listener != null) {
					listener.onPreferenceSaved(text);
				}
			}
		});

		builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
		return builder;
	}

	public interface OnPreferenceSavedListener {
		void onPreferenceSaved(String newValue);
	}
}
