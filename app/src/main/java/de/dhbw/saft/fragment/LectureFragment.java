package de.dhbw.saft.fragment;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dhbw.saft.R;
import de.dhbw.saft.adapter.LectureCardAdapter;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.service.DataService;

/**
 * Fragment that displays all lectures in a grouped, scrollable list.
 * Data is sourced from {@link DataService}, and the resulting entries are passed
 * to a {@link LectureCardAdapter} for rendering.
 */
public class LectureFragment extends RecyclerFragment<LectureCardAdapter> {

	@NonNull
	@Override
	public List<Entry> getEntries() {
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

		return entries;
	}

	@NonNull
	@Override
	public LectureCardAdapter getCardAdapter(@NonNull List<Entry> entries) {
		return new LectureCardAdapter(entries);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.fragment_planner;
	}
}
