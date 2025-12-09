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
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import de.dhbw.saft.HomeActivity;
import de.dhbw.saft.R;
import de.dhbw.saft.common.DialogBuilder;
import de.dhbw.saft.service.PreferenceService;
import de.dhbw.saft.service.DataService;

/**
 * Splash screen activity responsible for preloading all required data
 * before the main application starts.
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	public static final String KEY_NAME = "selected_course";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);

		final DataService dataService = DataService.getInstance();

		final PreferenceService preferenceService = new PreferenceService(this);
		final String value = preferenceService.getString(KEY_NAME, null);
		dataService.fetchCourses().thenRun(() -> {
			if (value == null || value.trim().isEmpty()) {
				runOnUiThread(() ->
					new DialogBuilder(this, preferenceService, Map.of(KEY_NAME, "${INPUT}"))
							.addSuggestions(DataService.getCourses()).onOkay(course -> fetch(dataService, course)).show()
				);
			} else {
				fetch(dataService, value);
			}
		});
	}

	private void fetch(DataService dataService, String course) {
		CompletableFuture.allOf(dataService.fetchLectures(course), dataService.fetchMenus())
				.thenRun(() -> runOnUiThread(() -> {
					startActivity(new Intent(this, HomeActivity.class));
					finish();
				}));
	}
}
