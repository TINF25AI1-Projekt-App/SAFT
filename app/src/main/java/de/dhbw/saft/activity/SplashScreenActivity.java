package de.dhbw.saft.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import de.dhbw.saft.HomeActivity;
import de.dhbw.saft.R;
import de.dhbw.saft.service.DataService;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	private static final long SPLASH_DURATION = 1_500L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		// TODO: Use selected Course. If none is selected, don't fetch
		DataService.fetchLectures("MA-TINF25AI1");
		DataService.fetchMenus();

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(() -> {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}, SPLASH_DURATION);
	}
}
