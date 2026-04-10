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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		final PreferenceService preferenceService = new PreferenceService(this);

		final ToolbarBinding toolbar = binding.toolbar;
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

		// restore the last tab
		SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		int lastTabId = prefs.getInt(KEY_SELECTED_TAB, R.id.nav_home); // default to home

		// Find the MenuItem with that ID in your BottomNavigationView
		Menu menu = binding.bottomNavigation.getMenu();
		MenuItem item = menu.findItem(lastTabId);
		if (item != null) {
			onBottomNavItemSelected(item);   // this will set the correct fragment
			binding.bottomNavigation.setSelectedItemId(lastTabId); // visually highlight the tab
		}
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		final PreferenceService preferenceService = new PreferenceService(this);

		final ToolbarBinding toolbar = binding.toolbar;
		toolbarTitle = toolbar.toolbarTitle;
		toolbar.toolbarButton.setOnClickListener(view -> {
			final SettingsFragment settingsFragment = new SettingsFragment(toolbar, bottomNavigation, preferenceService);
			toolbar.toolbarButton.setVisibility(View.GONE);
			loadFragment(settingsFragment);
		});

		final HomeFragment homeFragment = new HomeFragment();
		fragments.putAll(Map.of(R.id.nav_home, homeFragment, R.id.nav_mensa, new MensaFragment(),
				R.id.nav_planner, new LectureFragment()));

		bottomNavigation = binding.bottomNavigation;
		bottomNavigation.setOnItemSelectedListener(this::onBottomNavItemSelected);

		binding.bottomNavigation.setPadding(
				binding.bottomNavigation.getPaddingLeft(),
				binding.bottomNavigation.getPaddingTop(),
				binding.bottomNavigation.getPaddingRight(),
				0
		);

		binding.getRoot().setOnApplyWindowInsetsListener((view, insets) -> insets);

		SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		int lastTabId = prefs.getInt(KEY_SELECTED_TAB, R.id.nav_home);

		Menu menu = binding.bottomNavigation.getMenu();
		MenuItem item = menu.findItem(lastTabId);
		if (item != null) {
			onBottomNavItemSelected(item);
			binding.bottomNavigation.setSelectedItemId(lastTabId);
		} else {
			binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
			onBottomNavItemSelected(menu.findItem(R.id.nav_home));
		}
	}

	/**
	 * Replaces the current fragment inside the activity's container.
	 *
	 * @param fragment the fragment to display
	 */
	public void loadFragment(NamedFragment fragment) {
		final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (currentFragment != null && currentFragment.getClass() == fragment.getClass()) {
			return;
		}

		final String tag = fragment.getClass().getSimpleName();
		fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();
		toolbarTitle.setText(fragment.getName());
	}

	/**
	 * Event, which is called once an Item in the
	 * bottom navigation is selected.
	 *
	 * @param item	The selected item
	 * @return		Whether the item should be marked as selected
	 */
	/*private boolean onBottomNavItemSelected(MenuItem item) {
		NamedFragment target = fragments.get(item.getItemId());
		if (target == null) {
			return false;
		}

		loadFragment(target);
		return true;
	}*/

	private static final String KEY_SELECTED_TAB = "selected_tab";

	private boolean onBottomNavItemSelected(MenuItem item) {
		NamedFragment target = fragments.get(item.getItemId());
		if (target == null) {
			return false;
		}

		getSharedPreferences(getPackageName(), MODE_PRIVATE)
				.edit()
				.putInt(KEY_SELECTED_TAB, item.getItemId())
				.apply();

		loadFragment(target);
		return true;
	}

}
