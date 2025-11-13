package de.dhbw.saft;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.dhbw.saft.databinding.ActivityHomeBinding;
import de.dhbw.saft.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		Toolbar toolbar = binding.toolbar.toolbar;
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}

		actionBar.setTitle(R.string.main_activity_toolbar_title);
		toolbar.setNavigationIcon(R.drawable.baseline_home_24);
		toolbar.setNavigationOnClickListener(view -> {

		});

		loadFragment(new HomeFragment());

		final BottomNavigationView bottomNavigation = binding.bottomNavigation;
		bottomNavigation.setSelectedItemId(R.id.nav_home);
		bottomNavigation.setOnItemSelectedListener(this::onClickBottomNavigation);
	}

	private boolean onClickBottomNavigation(MenuItem item) {
		return switch (item.getItemId()) {
			case 1000017 -> {
				loadFragment(new HomeFragment());
				yield true;
			}
			case 1000008 -> false;
			case 1000010 -> false;
			default -> false;
		};
	}

	private void loadFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}
}
