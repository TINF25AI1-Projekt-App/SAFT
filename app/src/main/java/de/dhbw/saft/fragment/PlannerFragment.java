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
		final List<Entry> entries = new ArrayList<>();
		final Set<String> dates = new HashSet<>();

		for (Lecture lecture : lectures) {
			String date = Formatter.formatDate(lecture.start());
			if (date != null && dates.add(date)) {
				entries.add(new Header(date));
			}

			entries.add(lecture);
		}

		LectureCardAdapter adapter = new LectureCardAdapter(entries);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
