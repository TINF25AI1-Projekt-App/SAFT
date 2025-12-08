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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import de.dhbw.saft.activity.SplashScreenActivity;
import de.dhbw.saft.common.SharedPreferencesManager;
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

	private final Map<Integer, Fragment> fragments = new HashMap<>();
	private BottomNavigationView bottomNavigation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		ToolbarBinding toolbar = binding.toolbar;
		toolbar.toolbarTitle.setText(R.string.home_fragment_title);

		final SharedPreferencesManager prefsManager = SharedPreferencesManager.getInstance(this,
				SplashScreenActivity.PREFS_NAME);
		bottomNavigation = binding.bottomNavigation;

		final HomeFragment homeFragment = new HomeFragment();
		fragments.putAll(Map.of(R.id.nav_home, homeFragment, R.id.nav_mensa, new MensaFragment(), R.id.nav_planner,
				new LectureFragment()));

		bottomNavigation.setOnItemSelectedListener(this::onBottomNavItemSelected);
		loadFragment(homeFragment);

		getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);

		toolbar.toolbarBackButton.setOnClickListener(view -> {
			if (getSupportFragmentManager().getBackStackEntryCount() > 1)
				getSupportFragmentManager().popBackStack();
			else
				getOnBackPressedDispatcher().onBackPressed();
		});

		toolbar.toolbarButton.setOnClickListener(view -> {
			final SettingsFragment settingsFragment = new SettingsFragment(toolbar, bottomNavigation, prefsManager);
			loadFragment(settingsFragment);
			Menu menu = bottomNavigation.getMenu();
			for (int i = 0; i < menu.size(); i++) {
				menu.getItem(i).setChecked(false);
			}
			toolbar.toolbarButton.setVisibility(View.GONE);
		});

		OnBackPressedCallback callback = new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				if (getSupportFragmentManager().getBackStackEntryCount() > 1)
					getSupportFragmentManager().popBackStack();
				else
					finishAffinity();
			}
		};
		getOnBackPressedDispatcher().addCallback(this, callback);

		binding.bottomNavigation.setPadding(binding.bottomNavigation.getPaddingLeft(),
				binding.bottomNavigation.getPaddingTop(), binding.bottomNavigation.getPaddingRight(), 0);

		binding.getRoot().setOnApplyWindowInsetsListener((v, insets) -> insets);

	}

	private boolean onBottomNavItemSelected(MenuItem item) {
		Fragment targetFragment = fragments.get(item.getItemId());
		if (targetFragment == null)
			return false;

		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (currentFragment != null && currentFragment.getClass() == targetFragment.getClass()) {
			if (getSupportFragmentManager().getBackStackEntryCount() > 1)
				getSupportFragmentManager().popBackStackImmediate();
			return true;
		}

		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		loadFragment(targetFragment);
		return true;
	}

	private void onBackStackChanged() {
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (currentFragment != null)
			selectBottomNavForFragment(currentFragment);
	}
	private void selectBottomNavForFragment(Fragment currentFragment) {
		for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
			if (currentFragment.getClass() == entry.getValue().getClass()) {
				Menu menu = bottomNavigation.getMenu();
				for (int i = 0; i < menu.size(); i++) {
					menu.getItem(i).setChecked(menu.getItem(i).getItemId() == entry.getKey());
				}
				return;
			}
		}
		Menu menu = bottomNavigation.getMenu();
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setChecked(false);
		}
	}

	/**
	 * Replaces the current fragment inside the activity's container.
	 *
	 * @param fragment the fragment to display
	 */
	private void loadFragment(Fragment fragment) {
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

		if (currentFragment != null && currentFragment.getClass() == fragment.getClass())
			return;

		String tag = fragment.getClass().getSimpleName();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, tag)
				.addToBackStack(tag).commit();
	}
}
