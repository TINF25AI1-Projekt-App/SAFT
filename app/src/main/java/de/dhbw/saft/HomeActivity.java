package de.dhbw.saft;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import de.dhbw.saft.databinding.ActivityHomeBinding;
import de.dhbw.saft.databinding.ToolbarBinding;
import de.dhbw.saft.fragment.HomeFragment;
import de.dhbw.saft.fragment.MensaFragment;
import de.dhbw.saft.fragment.PlannerFragment;

public class HomeActivity extends AppCompatActivity {

	private final Map<Integer, Fragment> fragments = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		ToolbarBinding toolbar = binding.toolbar;
		toolbar.toolbarTitle.setText(R.string.home_fragment_title);

		final HomeFragment homeFragment = new HomeFragment();
		loadFragment(homeFragment);
		fragments.putAll(Map.of(R.id.nav_home, homeFragment, R.id.nav_mensa, new MensaFragment(), R.id.nav_planner,
				new PlannerFragment()));

		final BottomNavigationView bottomNavigation = binding.bottomNavigation;
		bottomNavigation.setOnItemSelectedListener(this::onClickBottomNavigation);
		bottomNavigation.setSelectedItemId(R.id.nav_home);
	}

	private boolean onClickBottomNavigation(MenuItem item) {
		final Fragment fragment = fragments.get(item.getItemId());
		if (fragment == null) {
			return false;
		}

		loadFragment(fragment);
		return true;
	}

	private void loadFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}
}
