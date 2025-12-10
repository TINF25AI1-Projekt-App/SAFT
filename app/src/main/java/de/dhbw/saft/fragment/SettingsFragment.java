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
package de.dhbw.saft.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dhbw.saft.R;
import de.dhbw.saft.activity.SplashScreenActivity;
import de.dhbw.saft.adapter.SettingsCardAdapter;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Preference;
import de.dhbw.saft.service.PreferenceService;
import de.dhbw.saft.databinding.FragmentSettingsBinding;
import de.dhbw.saft.databinding.ToolbarBinding;
import lombok.Getter;

/**
 * Fragment responsible for displaying and editing preferences.
 */
public class SettingsFragment extends RecyclerFragment<SettingsCardAdapter> {

	@Getter
	private FragmentSettingsBinding binding;

	private final ToolbarBinding toolbar;
	private final BottomNavigationView bottomNavigationView;
	private final PreferenceService preferenceService;

	public SettingsFragment(ToolbarBinding toolbar, BottomNavigationView bottomNavigationView,
			PreferenceService preferenceService) {
		this.toolbar = toolbar;
		this.bottomNavigationView = bottomNavigationView;
		this.preferenceService = preferenceService;
	}

	@NonNull
	@Override
	public List<Entry> getEntries() {
		final SharedPreferences sharedPreferences = requireActivity()
				.getSharedPreferences(PreferenceService.PREFERENCES_NAME, MODE_PRIVATE);
		final Map<String, ?> allPreferences = sharedPreferences.getAll();
		final List<Entry> preferences = new ArrayList<>();
		final String[] preferencesTitle = this.getResources().getStringArray(R.array.preferences_title);
		final String[] preferencesDescription = this.getResources().getStringArray(R.array.preferences_description);

		for (int i = 0; i < preferencesTitle.length; i++) {
			final String title = preferencesTitle[i];
			final String description = preferencesDescription[i];

			String value = "";
			Object prefValue = allPreferences.get(SplashScreenActivity.KEY_NAME);
			if (prefValue instanceof String preference) {
				value = preference;
			}

			preferences.add(new Preference(title, description, value));
		}

		return preferences;
	}

	@NonNull
	@Override
	public SettingsCardAdapter getCardAdapter(@NonNull List<Entry> entries) {
		return new SettingsCardAdapter(entries, preferenceService, getContext());
	}

	@Override
	public int getLayoutResource() {
		return R.layout.fragment_settings;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		toolbar.toolbarButton.setVisibility(VISIBLE);
		bottomNavigationView.setVisibility(VISIBLE);
		toolbar.toolbarBackButton.setVisibility(GONE);
		binding = null;
	}

	@NonNull
	@Override
	public String getName() {
		return "Einstellungen";
	}
}
