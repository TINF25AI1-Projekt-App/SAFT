package de.dhbw.saft.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.model.Lecture;

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
		holder.textLectureTitle.setText(item.name());
		holder.textPlannerTime.setText(Formatter.formatTime(item.start()) + " - " + Formatter.formatTime(item.end()));
		holder.textPlannerRooms.setText(String.join(", ", item.rooms()));
		holder.imageViewType.setImageResource(
				item.type() == Lecture.Type.PRESENCE ? R.drawable.planner_room : R.drawable.planner_online);
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
