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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dhbw.saft.R;
import de.dhbw.saft.activity.SplashScreenActivity;
import de.dhbw.saft.adapter.PreferenceCardAdapter;
import de.dhbw.saft.common.LectureFeed;
import de.dhbw.saft.common.PreferenceItem;
import de.dhbw.saft.common.SharedPreferencesManager;
import de.dhbw.saft.databinding.FragmentSettingsBinding;
import de.dhbw.saft.databinding.ToolbarBinding;
import de.dhbw.saft.service.DataService;
import lombok.Getter;

/**
 * Fragment responsible for displaying the home screen of the SAFT app.
 * This fragment reads lecture data from {@link DataService}
 * and derives a {@link LectureFeed} containing all information required for
 * the header card on the home screen.
 */
public class SettingsFragment extends Fragment {

	@Getter
	private FragmentSettingsBinding binding;

	private final ToolbarBinding toolbar;
	private final BottomNavigationView bottomNavigationView;
	private final SharedPreferencesManager preferencesManager;

	public SettingsFragment(ToolbarBinding toolbar, BottomNavigationView bottomNavigationView,
			SharedPreferencesManager preferenceManager) {
		this.toolbar = toolbar;
		this.bottomNavigationView = bottomNavigationView;
		this.preferencesManager = preferenceManager;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentSettingsBinding.inflate(inflater, container, false);

		final RecyclerView recyclerView = binding.recyclerView;

		toolbar.toolbarBackButton.setVisibility(VISIBLE);
		bottomNavigationView.setVisibility(GONE);

		String[] preferencesTitle = this.getResources().getStringArray(R.array.preferences_title);
		String[] preferencesDescription = this.getResources().getStringArray(R.array.preferences_description);
		SharedPreferences prefs = requireActivity().getSharedPreferences(SplashScreenActivity.PREFS_NAME, MODE_PRIVATE);

		Map<String, ?> allPrefs = prefs.getAll();
		List<PreferenceItem> mappedData = new ArrayList<>();

		for (int i = 0; i < preferencesTitle.length; i++) {
			String title = preferencesTitle[i];
			String description = preferencesDescription[i];
			String prefKey = title.toLowerCase().replace(" ", "_");

			String value = "";
			if (allPrefs.containsKey(prefKey)) {
				Object prefValue = allPrefs.get(prefKey);
				if (prefValue instanceof String) {
					value = (String) prefValue;
				}
			}

			mappedData.add(new PreferenceItem(title, description, value));

			recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			final PreferenceCardAdapter adapter = new PreferenceCardAdapter(mappedData, preferencesManager,
					getContext());
			recyclerView.setAdapter(adapter);
		}
		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		toolbar.toolbarButton.setVisibility(VISIBLE);
		bottomNavigationView.setVisibility(VISIBLE);
		toolbar.toolbarBackButton.setVisibility(GONE);
		binding = null;

	}
}
