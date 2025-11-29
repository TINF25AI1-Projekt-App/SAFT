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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import java.util.concurrent.CompletableFuture;

import de.dhbw.saft.HomeActivity;
import de.dhbw.saft.R;
import de.dhbw.saft.service.DataService;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);

		// TODO: Use selected Course. If none is selected, don't fetch
		CompletableFuture.allOf(DataService.fetchLectures("MA-TINF25AI1"), DataService.fetchMenus()).thenRun(() -> {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
		});
	}
}
