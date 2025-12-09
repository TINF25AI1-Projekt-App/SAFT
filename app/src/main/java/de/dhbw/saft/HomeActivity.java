/*
* Copyright 2025 SAFT Authors and Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package de.dhbw.saft;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import de.dhbw.saft.fragment.NamedFragment;
import de.dhbw.saft.service.PreferenceService;
import de.dhbw.saft.databinding.ActivityHomeBinding;
import de.dhbw.saft.databinding.ToolbarBinding;
import de.dhbw.saft.fragment.HomeFragment;
import de.dhbw.saft.fragment.MensaFragment;
import de.dhbw.saft.fragment.LectureFragment;
import de.dhbw.saft.fragment.SettingsFragment;

/**
 * Main activity of the SAFT app that hosts the primary navigation structure,
 * initializes the toolbar, sets up the bottom navigation, and manages
 * fragment switching for the home screen, mensa view, and lecture planner.
 */
public class HomeActivity extends AppCompatActivity {

	private final Map<Integer, NamedFragment> fragments = new HashMap<>();
	private final FragmentManager fragmentManager = getSupportFragmentManager();

	private BottomNavigationView bottomNavigation;
	private TextView toolbarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		final PreferenceService preferenceService = new PreferenceService(this);

		ToolbarBinding toolbar = binding.toolbar;
		toolbarTitle = toolbar.toolbarTitle;
		toolbar.toolbarButton.setOnClickListener(view -> {
			final SettingsFragment settingsFragment = new SettingsFragment(toolbar, bottomNavigation,
					preferenceService);
			toolbar.toolbarButton.setVisibility(View.GONE);
			loadFragment(settingsFragment);
		});

		final HomeFragment homeFragment = new HomeFragment();
		fragments.putAll(Map.of(R.id.nav_home, homeFragment, R.id.nav_mensa, new MensaFragment(), R.id.nav_planner,
				new LectureFragment()));

		bottomNavigation = binding.bottomNavigation;
		bottomNavigation.setOnItemSelectedListener(this::onBottomNavItemSelected);
		bottomNavigation.setSelectedItemId(R.id.nav_home);

		binding.bottomNavigation.setPadding(binding.bottomNavigation.getPaddingLeft(),
				binding.bottomNavigation.getPaddingTop(), binding.bottomNavigation.getPaddingRight(), 0);

		binding.getRoot().setOnApplyWindowInsetsListener((view, insets) -> insets);

		loadFragment(homeFragment);
	}

	private boolean onBottomNavItemSelected(MenuItem item) {
		NamedFragment target = fragments.get(item.getItemId());
		if (target == null) {
			return false;
		}

		loadFragment(target);
		return true;
	}

	/**
	 * Replaces the current fragment inside the activity's container.
	 *
	 * @param fragment the fragment to display
	 */
	private void loadFragment(NamedFragment fragment) {
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (currentFragment != null && currentFragment.getClass() == fragment.getClass()) {
			return;
		}

		String tag = fragment.getClass().getSimpleName();
		fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();
		toolbarTitle.setText(fragment.getName());
	}
}
