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
package de.dhbw.saft.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import de.dhbw.saft.common.Entry;

/**
 * Represents a single lecture entry in the schedule used for mapping JSON
 * response fom API.
 */
public record Lecture(@NonNull String name, @NonNull Type type, @NonNull String[] rooms,
		@NonNull @SerializedName("startTime") String start,
		@NonNull @SerializedName("endTime") String end) implements Entry {

	/**
	 * Returns a trimmed version name of the lecture.
	 *
	 * @return Trimmed lecture name
	 */
	public @NonNull String getDeclarativeName() {
		return name.trim();
	}

	@Override
	public Entry.Type getEntryType() {
		return Entry.Type.ITEM;
	}

	/**
	 * Defines whether a lecture takes place in presence or online.
	 */
	public enum Type {
		PRESENCE, ONLINE
	}

	@Override
	public @NonNull String toString() {
		return "name: " + name + ", type: " + type + ", rooms: " + Arrays.toString(rooms) + ", start: '" + start
				+ ", end: '" + end;
	}
}
