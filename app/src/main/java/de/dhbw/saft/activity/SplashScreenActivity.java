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

/**
 * Splash screen activity responsible for preloading all required data
 * before the main application starts.
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);

		// TODO: Use selected Course. If none is selected, don't fetch
		CompletableFuture.allOf(DataService.fetchLectures("MA-TINF25AI1"), DataService.fetchMenus())
				.thenRun(() -> runOnUiThread(() -> {
					startActivity(new Intent(this, HomeActivity.class));
					finish();
				}));
	}
}
