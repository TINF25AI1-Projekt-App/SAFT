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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dhbw.saft.R;
import lombok.NoArgsConstructor;
import de.dhbw.saft.adapter.LectureCardAdapter;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.common.LectureCard;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.service.DataService;

@NoArgsConstructor
public class PlannerFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_planner, container, false);
		final FragmentActivity mainActivity = requireActivity();
		final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

		final List<Lecture> lectures = DataService.getLectures();
		List<Entry> dates = new ArrayList<>();
		Set<String> addedDates = new HashSet<>();

		for (Lecture lecture : lectures) {
			LectureCard item = new LectureCard(lecture.name(), lecture.type(), lecture.rooms(), lecture.start(),
					lecture.end());
			String date = Formatter.formatDate(item.start());

			if (date != null && !addedDates.contains(date)) {
				dates.add(new Header(date));
				addedDates.add(date);
			}
			dates.add(item);
		}

		LectureCardAdapter adapter = new LectureCardAdapter(dates);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
