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
import de.dhbw.saft.fragment.LectureFragment;

/**
 * Main activity of the SAFT app that hosts the primary navigation structure,
 * initializes the toolbar, sets up the bottom navigation, and manages
 * fragment switching for the home screen, mensa view, and lecture planner.
 */
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
				new LectureFragment()));

		final BottomNavigationView bottomNavigation = binding.bottomNavigation;
		bottomNavigation.setOnItemSelectedListener(this::onClickBottomNavigation);
		bottomNavigation.setSelectedItemId(R.id.nav_home);
	}

	/**
	 * Handles bottom navigation item selections by loading the
	 * corresponding fragment from the internal fragment map.
	 *
	 * @param item	The selected menu item
	 * @return 		Whether a matching fragment exists
	 */
	private boolean onClickBottomNavigation(MenuItem item) {
		final Fragment fragment = fragments.get(item.getItemId());
		if (fragment == null) {
			return false;
		}

		loadFragment(fragment);
		return true;
	}

	/**
	 * Replaces the current fragment inside the activity's container.
	 *
	 * @param fragment the fragment to display
	 */
	private void loadFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}
}
