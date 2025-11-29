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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import de.dhbw.saft.R;
import de.dhbw.saft.adapter.LectureCardAdapter;
import de.dhbw.saft.adapter.MensaCardAdapter;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.service.DataService;

/**
 * Fragment that displays all mensa meals grouped by date.
 * Data is sourced from {@link DataService}, and the resulting entries are passed
 * to a {@link MensaCardAdapter} for rendering.
 */
public class MensaFragment extends RecyclerFragment<MensaCardAdapter> {

	@NonNull
	@Override
	public List<Entry> getEntries() {
		final List<Menu> menus = DataService.getMenus();
		final List<Entry> entries = new ArrayList<>();
		final Set<String> dates = new HashSet<>();

		for (Menu menu : menus) {
			final String date = Formatter.formatDate(menu.date());
			final Menu.Dish[] mainCourses = menu.mainCourses();
			final Menu.Dish[] desserts = menu.desserts();
			if (date != null && dates.add(date) && (mainCourses.length > 0 || desserts.length > 0)) {
				entries.add(new Header(date));
			}

			List<Menu.Dish> dishes = Stream.concat(Arrays.stream(mainCourses), Arrays.stream(desserts)).toList();
			entries.addAll(dishes);
		}

		return entries;
	}

	@NonNull
	@Override
	public MensaCardAdapter getCardAdapter(@NonNull List<Entry> entries) {
		return new MensaCardAdapter(entries);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.fragment_mensa;
	}
}
