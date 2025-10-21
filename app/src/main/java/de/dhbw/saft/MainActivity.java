package de.dhbw.saft;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import de.dhbw.saft.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		TextView welcomeMessageTV = binding.text;
		Toolbar toolbar = binding.toolbar.findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle("My App");
		toolbar.setNavigationIcon(R.drawable.baseline_home_24);
		toolbar.setNavigationOnClickListener(v -> {

		});
		welcomeMessageTV.setText("Hello World!");
	}
}
