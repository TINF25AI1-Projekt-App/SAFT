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
package de.dhbw.saft.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import de.dhbw.saft.HomeActivity;
import de.dhbw.saft.R;
import de.dhbw.saft.common.AddPreferenceDialog;
import de.dhbw.saft.common.SharedPreferencesManager;
import de.dhbw.saft.service.DataService;

/**
 * Splash screen activity responsible for preloading all required data
 * before the main application starts.
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	public static final String PREFS_NAME = "settings_prefs";
	public static final String KEY_NAME = "kurs";
	public static final String KEY_NAME2 = "test";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);

		// TODO: Use selected Course. If none is selected, don't fetch
		SharedPreferencesManager prefsManager = SharedPreferencesManager.getInstance(this, PREFS_NAME);
		String value = prefsManager.getString(KEY_NAME, null);
		if (value == null || value.trim().isEmpty()) {
			AddPreferenceDialog dialog = getAddPreferenceDialog(prefsManager);
			dialog.show();
		} else
			launchDataservice(value);
	}

	@NonNull
	private AddPreferenceDialog getAddPreferenceDialog(SharedPreferencesManager prefsManager) {
		Map<String, String> keyValueMap = new HashMap<>();
		keyValueMap.put(KEY_NAME, "${INPUT}");
		// TODO : remove
		keyValueMap.put(KEY_NAME2, "test");

		String[] suggestions = {"MA-TINF25AI1", "Suggestion 2", "Suggestion 3"};
		AddPreferenceDialog dialog = new AddPreferenceDialog(this, prefsManager, keyValueMap, suggestions, null,
				KEY_NAME);
		dialog.setOnPreferenceSavedListener(this::launchDataservice);
		return dialog;
	}

	private void launchDataservice(String value) {
		final DataService service = new DataService();
		CompletableFuture.allOf(service.fetchLectures(value), service.fetchMenus()).thenRun(() -> {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
		});
	}
}
