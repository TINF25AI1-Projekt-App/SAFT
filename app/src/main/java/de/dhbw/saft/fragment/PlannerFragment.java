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
import de.dhbw.saft.adapter.PlannerCardAdapter;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.model.HeaderEntry;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.model.PlannerCardItem;
import de.dhbw.saft.model.ScheduleEntry;
import de.dhbw.saft.service.DataService;

public class PlannerFragment extends Fragment {

	public PlannerFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_planner, container, false);
		final FragmentActivity mainActivity = requireActivity();
		final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

		final List<Lecture> lectures = DataService.getLectures();
		List<ScheduleEntry> dates = new ArrayList<>();
		Set<String> addedDates = new HashSet<>();

		for (Lecture lecture : lectures) {
			PlannerCardItem item = new PlannerCardItem(lecture.name(), lecture.type(), lecture.rooms(), lecture.start(),
					lecture.end());
			String date = Formatter.formatDate(item.getStart());

			if (date != null && !addedDates.contains(date)) {
				dates.add(new HeaderEntry(date));
				addedDates.add(date);
			}
			dates.add(item);
		}

		PlannerCardAdapter adapter = new PlannerCardAdapter(dates);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
