package de.dhbw.saft.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dhbw.saft.fragment.HomeFragment;

/**
 * Represents all information required to display the lecture status
 * at {@link HomeFragment}.
 */
public record LectureFeed(@NonNull String header, @NonNull String description, @Nullable String highlighted,
		int iconResource) {
}
