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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.dhbw.saft.R;
import de.dhbw.saft.service.PreferenceService;

/**
 * Builder class for creating and displaying alerts
 */
public class DialogBuilder {

	private final Context context;
	private final PreferenceService preferenceService;
	private final Map<String, String> inputMap;

	private List<String> suggetions = new ArrayList<>();
	private Consumer<String> onOkay;
	private TextView textView;

	public DialogBuilder(@NonNull Context context, @NonNull PreferenceService preferenceService,
			@NonNull Map<String, String> inputMap) {
		this.context = context;
		this.preferenceService = preferenceService;
		this.inputMap = inputMap;
	}

	/**
	 * Adds suggestions to the text input.
	 *
	 * @param suggestions	Suggestions to add
	 * @return				Current {@link DialogBuilder}
	 */
	public DialogBuilder addSuggestions(@NonNull List<String> suggestions) {
		this.suggetions.addAll(suggestions);
		return this;
	}

	/**
	 * Adds text to the input.
	 *
	 * @param textView		The text
	 * @return				Current {@link DialogBuilder}
	 */
	public DialogBuilder addTextView(@NonNull TextView textView) {
		this.textView = textView;
		return this;
	}

	/**
	 * Adds an action, which is called once 'Okay' is clicked.
	 *
	 * @param onOkay		The action to perform
	 * @return				Current {@link DialogBuilder}
	 */
	public DialogBuilder onOkay(@NonNull Consumer<String> onOkay) {
		this.onOkay = onOkay;
		return this;
	}

	/**
	 * Shows the alert box.
	 */
	public void show() {
		final AutoCompleteTextView input = new AutoCompleteTextView(context);
		String existingValue = null;

		for (String key : inputMap.keySet()) {
			String value = preferenceService.getString(key, null);
			if (value != null && !value.isEmpty()) {
				existingValue = value;
				break;
			}
		}

		if (existingValue != null)
			input.setText(existingValue);
		else {
			input.setHint((context.getString(R.string.preference_hint_course)));
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,
				suggetions.toArray(String[]::new));
		input.setAdapter(adapter);
		input.setThreshold(1);

		final AlertDialog alert = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.add_preference_title)).setMessage(R.string.add_preference_message)
				.setView(input).setPositiveButton(R.string.ok, (dialog, which) -> {
					String text = input.getText().toString().trim();
					if (!text.isEmpty()) {
						for (Map.Entry<String, String> entry : inputMap.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue().replace("${INPUT}", text);
							preferenceService.putString(key, value);
						}
						if (textView != null) {
							textView.setText(text);
						}

						if (onOkay != null) {
							onOkay.accept(text);
						}
					}
				}).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}
}
