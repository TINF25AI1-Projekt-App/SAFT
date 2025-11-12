package de.dhbw.saft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import java.util.Objects;
import java.util.function.Consumer;

import de.dhbw.saft.common.TileBuilder;
import de.dhbw.saft.databinding.ActivityMainBinding;
import lombok.Getter;

public class MainActivity extends AppCompatActivity {

	@Getter
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		Toolbar toolbar = binding.toolbar.findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_toolbar_title);
		toolbar.setNavigationIcon(R.drawable.baseline_home_24);
		toolbar.setNavigationOnClickListener(v -> {
		});

		final TileBuilder builder = new TileBuilder(this);
		builder.addTile(0, R.drawable.tile_where2go,
				"https://www.google.com/maps/d/embed?mid=1xRb0uZgr4Lsyys_mqYxPgT--4JO4OpA&ehbc=2E312F&ll=49.48905226213409%2C8.480855325064853&z=14")
				.addTile(1, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(2, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(3, R.drawable.tile_zimbra, "https://studgate.dhbw-mannheim.de/");

	}


}
