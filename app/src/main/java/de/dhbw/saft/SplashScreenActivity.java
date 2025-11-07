package de.dhbw.saft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

	private static final long SPLASH_DURATION = 1_500L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
		super.onCreate(savedInstanceState);

		splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::remove);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(() -> {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}, SPLASH_DURATION);
	}
}
