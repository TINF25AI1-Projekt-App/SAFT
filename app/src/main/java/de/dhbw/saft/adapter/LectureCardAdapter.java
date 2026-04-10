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
package de.dhbw.saft.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.model.Lecture;

/**
 * Adapter for displaying lecture entries inside the planner list.
 * It binds {@link Lecture} items to their card layout.
 */
public class LectureCardAdapter extends CardAdapter<LectureCardAdapter.CardViewHolder, Lecture> {

	public LectureCardAdapter(List<Entry> items) {
		super(items);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.planner_item_card;
	}

	@Override
	public CardViewHolder createCardHolder(View view) {
		return new CardViewHolder(view);
	}

	@Override
	public void bindCardHolder(CardViewHolder holder, Lecture item) {
		if (hasNullField(item)) return;
		holder.textLectureTitle.setText(item.name());
		holder.textPlannerTime.setText(Formatter.formatTime(item.start()) + " - " + Formatter.formatTime(item.end()));
		holder.textPlannerRooms.setText(String.join(", ", item.rooms()));
		holder.imageViewType.setImageResource(
				item.type() == Lecture.Type.PRESENCE ? R.drawable.planner_room : R.drawable.planner_online);
	}

	private boolean hasNullField(Lecture item){
		if (item.name() == null ||
				item.type() == null ||
				item.rooms() == null ||
				item.start() == null ||
				item.end() == null) return true;
		return false;
	}

	public static class CardViewHolder extends RecyclerView.ViewHolder {
		final ImageView imageViewType;
		final TextView textLectureTitle, textPlannerTime, textPlannerRooms;

		public CardViewHolder(View itemView) {
			super(itemView);
			textLectureTitle = itemView.findViewById(R.id.text_lecture_title);
			textPlannerTime = itemView.findViewById(R.id.text_planner_time);
			textPlannerRooms = itemView.findViewById(R.id.text_lecture_type);
			imageViewType = itemView.findViewById(R.id.imageView_lecture_type);
		}
	}
}
