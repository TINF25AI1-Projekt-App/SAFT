package de.dhbw.saft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.gridlayout.widget.GridLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import de.dhbw.saft.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	private GridLayout tileGridLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		Toolbar toolbar = binding.toolbar.findViewById(R.id.toolbar);
		tileGridLayout = binding.tilesGridLayout;

		setSupportActionBar(toolbar);

		Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_toolbar_title);
		toolbar.setNavigationIcon(R.drawable.baseline_home_24);
		toolbar.setNavigationOnClickListener(v -> {});

		buildDynamicallyTiles();
	}

	private void openUrlInBrowser(@NotNull String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		Intent chooser = Intent.createChooser(intent, getString(R.string.title_browser_chooser));

		if (chooser.resolveActivity(getPackageManager()) != null) {
			startActivity(chooser);
			return;
		}

		Toast.makeText(this, R.string.error_text_no_app_found_chooser, Toast.LENGTH_SHORT).show();
	}

	private void buildDynamicallyTiles(){
		final String[] ALL_TILES_TITLE = this.getResources().getStringArray(R.array.all_tiles_title);
		final String[] URLS = {"https://dhbw.app/c/MA-TINF25AI1","","","","",""};
		final int f1Img = R.drawable.baseline_account_balance_24;
		final int[] ALL_ICONS_RESOURCES_IDS = {f1Img,f1Img,f1Img,f1Img,f1Img,f1Img};
		final int TOTAL_CARDS = ALL_TILES_TITLE.length;
		final int TILES_CARD_GRID_COLUMNS = 2;

		for (int i = 0; i < TOTAL_CARDS; i++) {
			CardView cardView = new CardView(this);

			//TODO: DELETE IN PRODUCTION
			if (i > 0) cardView.setVisibility(CardView.INVISIBLE);

			GridLayout.LayoutParams params = new GridLayout.LayoutParams();

			params.width = 0;
			params.height = 0;
			params.columnSpec = GridLayout.spec(i % TILES_CARD_GRID_COLUMNS, 1f);
			params.rowSpec = GridLayout.spec(i / TILES_CARD_GRID_COLUMNS, 1f);
			params.setMargins(8, 8, 8, 8);

			cardView.setLayoutParams(params);
			cardView.setRadius(12);
			cardView.setCardElevation(8);
			cardView.setUseCompatPadding(true);

			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setGravity(Gravity.CENTER);
			linearLayout.setPadding(8, 8, 8, 8);

			TextView textView = new TextView(this);
			textView.setText(ALL_TILES_TITLE[i]);
			textView.setTextSize(24);
			textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
			textView.setGravity(Gravity.CENTER);

			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
			imageView.setImageResource(ALL_ICONS_RESOURCES_IDS[i]);

			linearLayout.addView(textView);
			linearLayout.addView(imageView);
			cardView.addView(linearLayout);

			int finalI = i;
			cardView.setOnClickListener(view -> openUrlInBrowser(URLS[finalI]));

			tileGridLayout.addView(cardView);
		}
	}
}
