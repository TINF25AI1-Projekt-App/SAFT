package de.dhbw.saft;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;

public class MainActivity extends ComponentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView welcomeMessageTV = new TextView(this);
		welcomeMessageTV.setText("Hello World!");
		setContentView(welcomeMessageTV);
	}
}
