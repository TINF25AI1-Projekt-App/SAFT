package de.dhbw.saft.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for parsing, formatting and converting dates and durations.
 */
public class Formatter {

	private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			Locale.US);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.GERMANY);

	static {
		INPUT_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
	}

	private static final long SECOND = 1_000L;
	private static final long MINUTE = 60 * SECOND;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;

	/**
	 * Converts an ISO date into millis.
	 *
	 * @param value	ISO date as String
	 * @return 		Date as millis
	 */
	public static long getDateMillis(@NonNull String value) {
		Date date = getDate(value);
		return date != null ? date.getTime() : 0;
	}

	/**
	 * Formats an ISO date into a readable german date.
	 *
	 * @param value	ISO date as String
	 * @return 		Formatted date
	 */
	public static @Nullable String formatDate(@NonNull String value) {
		return format(value, DATE_FORMAT);
	}

	/**
	 * Formats an ISO date into a day time.
	 *
	 * @param value	ISO date as String
	 * @return 		Formatted day time
	 */
	public static @Nullable String formatTime(@NonNull String value) {
		return format(value, TIME_FORMAT);
	}

	/**
	 * Formats millis into a readable duration.
	 *
	 * @param millis	Duration in millis
	 * @return 			Formatted duration
	 */
	public static String formatDuration(long millis) {
		if (millis < SECOND) {
			return "Jeden Moment";
		}

		long seconds = millis / SECOND;
		long minutes = millis / MINUTE;
		long hours = millis / HOUR;
		long days = millis / DAY;

		long remainingHours = hours % 24;
		long remainingMinutes = minutes % 60;
		long remainingSeconds = seconds % 60;

		Stream<String> base = Stream.of(validate(days, "Tag", "Tage"), validate(remainingHours, "Stunde", "Stunden"));

		Stream<String> extra = Stream.empty();
		if (millis < 2 * HOUR) {
			extra = Stream.of(validate(remainingMinutes, "Minute", "Minuten"),
					millis < 5 * MINUTE ? validate(remainingSeconds, "Sekunde", "Sekunden") : null);
		}

		return Stream.concat(base, extra).filter(Objects::nonNull).collect(Collectors.joining(" "));
	}

	/**
	 * Builds a time unit with proper singular or plural.
	 *
	 * @param unit		The amount
	 * @param singular	Singular time form
	 * @param plural	Plural time form
	 * @return 			Formatted time unit or null if unit is 0
	 */
	private static @Nullable String validate(long unit, @NonNull String singular, @NonNull String plural) {
		if (unit < 1) {
			return null;
		}
		return unit + " " + (unit == 1 ? singular : plural);
	}

	/**
	 * Formats an ISO date using a date format.
	 *
	 * @param value		ISO date as String
	 * @param format	The date format to be used
	 * @return 			Formatted ISO date or null
	 */
	private static @Nullable String format(@NonNull String value, @NonNull SimpleDateFormat format) {
		Date date = getDate(value);
		return date != null ? format.format(date) : null;
	}

	/**
	 * Parses an ISO date into a usable Date
	 *
	 * @param value	ISO date as String
	 * @return 		Parsed date or null
	 */
	private static @Nullable Date getDate(@NonNull String value) {
		try {
			return INPUT_FORMAT.parse(value);
		} catch (ParseException exception) {
			return null;
		}
	}
}
