package de.dhbw.saft.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.TileBuilder;
import de.dhbw.saft.databinding.FragmentHomeBinding;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.service.DataService;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HomeFragment extends Fragment {

	@Getter
	private FragmentHomeBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentHomeBinding.inflate(inflater, container, false);

		final TileBuilder builder = new TileBuilder(this);
		builder.addTile(0, R.drawable.tile_where2go,
				"https://www.google.com/maps/d/embed?mid=1xRb0uZgr4Lsyys_mqYxPgT--4JO4OpA&ehbc=2E312F&ll=49.48905226213409%2C8.480855325064853&z=14")
				.addTile(1, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(2, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(3, R.drawable.tile_zimbra, "https://studgate.dhbw-mannheim.de/");

		displayLecture();

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void displayLecture() {
		final long timestamp = System.currentTimeMillis();
		final Lecture currentLecture = getCurrentLecture(timestamp);
		if (currentLecture != null) {
			final long end = Formatter.getDateMillis(currentLecture.end());
			// TODO: add ending soon
			return;
		}

		final Lecture nextLecture = getNextLecture(timestamp);
		if (nextLecture == null || !isToday(nextLecture)) {
			// TODO: add no course today
			return;
		}

		final long start = Formatter.getDateMillis(nextLecture.start());
		// TODO: add next course
	}

	private @Nullable Lecture getCurrentLecture(long timestamp) {
		for (Lecture lecture : DataService.getLectures()) {
			final long start = Formatter.getDateMillis(lecture.start());
			final long end = Formatter.getDateMillis(lecture.end());
			if (timestamp > start && timestamp < end) {
				return lecture;
			}
		}

		return null;
	}

	private @Nullable Lecture getNextLecture(long timestamp) {
		for (Lecture lecture : DataService.getLectures()) {
			final long start = Formatter.getDateMillis(lecture.start());
			if (timestamp < start) {
				return lecture;
			}
		}

		return null;
	}

	private boolean isToday(@NonNull Lecture lecture) {
		final Date lectureStartDate = Formatter.getDate(lecture.start());
		if (lectureStartDate == null) {
			return false;
		}

		final Date currentDate = new Date(System.currentTimeMillis());
		final Calendar lectureCalendar = Calendar.getInstance();
		lectureCalendar.setTime(lectureStartDate);

		final Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(currentDate);

		return lectureCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
				&& lectureCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR);
	}
}
