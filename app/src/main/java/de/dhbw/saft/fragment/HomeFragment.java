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
import de.dhbw.saft.common.LectureFeed;
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

		LectureFeed feed = getLectureFeed();
		binding.cardViewHeader.setText(feed.header());
		binding.cardViewDescription.setText(feed.description());
		binding.cardViewHighlighted.setText(feed.highlighted());
		binding.cardViewIcon.setImageResource(feed.iconResource());

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	/**
	 * Creates a {@link LectureFeed} containing all information required
	 * to display the current lecture status.
	 *
	 * @return Information to display
	 */
	private LectureFeed getLectureFeed() {
		final long timestamp = System.currentTimeMillis();
		final Lecture currentLecture = getCurrentLecture(timestamp);
		if (currentLecture != null) {
			final String start = Formatter.formatTime(currentLecture.start());
			final String end = Formatter.formatTime(currentLecture.end());
			final String description = String.join(", ", currentLecture.rooms()) + "\n" + start + " - " + end;
			return new LectureFeed(currentLecture.getDeclarativeName(), description, null,
					R.drawable.navigation_planner);
		}

		final Lecture nextLecture = getNextLecture(timestamp);
		if (nextLecture == null || !isToday(nextLecture)) {
			return new LectureFeed("Keine Vorlesungen", "Heute stehen keine Vorlesungen an", "Bis morgen!",
					R.drawable.outline_bedtime_24);
		}

		final long start = Formatter.getDateMillis(nextLecture.start()) - System.currentTimeMillis();
		return new LectureFeed("Keine Vorlesung", "Deine nächste Vorlesung beginnt in", Formatter.formatDuration(start),
				R.drawable.outline_bedtime_24);
	}

	/**
	 * Gets the current {@link Lecture}
	 *
	 * @param timestamp Timestamp of now
	 * @return			Current lecture or null
	 */
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

	/**
	 * Gets the next {@link Lecture}.
	 *
	 * @param timestamp	Timestamp of now
	 * @return			Next lecture or null
	 */
	private @Nullable Lecture getNextLecture(long timestamp) {
		for (Lecture lecture : DataService.getLectures()) {
			final long start = Formatter.getDateMillis(lecture.start());
			if (timestamp < start) {
				return lecture;
			}
		}

		return null;
	}

	/**
	 * Checks whether the given lecture starts on the current calendar day.
	 *
	 * @param lecture	The lecture
	 * @return			true if the lecture starts today, false if otherwise
	 */
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
