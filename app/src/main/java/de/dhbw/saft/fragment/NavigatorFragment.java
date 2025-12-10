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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.dhbw.saft.common.LectureFeed;
import de.dhbw.saft.databinding.FragmentNavigatorBinding;
import de.dhbw.saft.service.DataService;
import lombok.Getter;

/**
 * Fragment responsible for displaying the home screen of the SAFT app.
 * This fragment reads lecture data from {@link DataService}
 * and derives a {@link LectureFeed} containing all information required for
 * the header card on the home screen.
 */
public class NavigatorFragment extends Fragment {

	@Getter
	private FragmentNavigatorBinding binding;

	private String link;

	public NavigatorFragment(String link) {
		this.link = link;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentNavigatorBinding.inflate(inflater, container, false);

		WebSettings settings = binding.webviewNavigator.getSettings();
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);

		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setDomStorageEnabled(true);

		binding.webviewNavigator.setWebViewClient(new WebViewClient());
		binding.webviewNavigator.loadUrl(link);

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

}
