package de.dhbw.saft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import java.util.Objects;
import de.dhbw.saft.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		CardView cardViewF1 = binding.cardViewF1;
		Toolbar toolbar = binding.toolbar.findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_toolbar_title);
		toolbar.setNavigationIcon(R.drawable.baseline_home_24);
		toolbar.setNavigationOnClickListener(v -> {

		});

		cardViewF1.setOnClickListener(view -> {
			openUrlInBrowser("https://dhbw.app/c/MA-TINF25AI1");
		});
	}

	private void openUrlInBrowser(String url) {
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		Intent chooser = Intent.createChooser(intent, getString(R.string.title_browser_chooser));

		if (chooser.resolveActivity(getPackageManager()) != null) {
			startActivity(chooser);
		} else {
			Toast.makeText(this, R.string.error_text_no_app_found_chooser, Toast.LENGTH_SHORT).show();
		}
	}

}
