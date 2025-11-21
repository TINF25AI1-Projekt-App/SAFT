package de.dhbw.saft.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dhbw.saft.R;
import de.dhbw.saft.model.HeaderEntry;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.model.PlannerCardItem;
import de.dhbw.saft.model.ScheduleEntry;

public class PlannerCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<ScheduleEntry> items;

	// ViewHolder for Schedule Item
	static class CardViewHolder extends RecyclerView.ViewHolder {
		CardView cardView;
		ImageView imageViewType;
		TextView textLectureTitle, textPlannerTime, textPlannerRooms;
		CardViewHolder(View itemView) {
			super(itemView);
			cardView = itemView.findViewById(R.id.card_view);
			textLectureTitle = itemView.findViewById(R.id.text_lecture_title);
			textPlannerTime = itemView.findViewById(R.id.text_planner_time);
			textPlannerRooms = itemView.findViewById(R.id.text_lecture_type);
			imageViewType = itemView.findViewById(R.id.imageView_lecture_type);
		}
	}

	// ViewHolder for Header
	static class HeaderViewHolder extends RecyclerView.ViewHolder {
		TextView dateText;

		HeaderViewHolder(View itemView) {
			super(itemView);
			dateText = itemView.findViewById(R.id.text_day);
		}
	}

	public PlannerCardAdapter(List<ScheduleEntry> items) {
		this.items = items;
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getType();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == ScheduleEntry.TYPE_HEADER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_item_header, parent, false); // New
																														// layout
																														// for
																														// header
			return new HeaderViewHolder(view);
		} else { // TYPE_ITEM
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_item_card, parent, false); // existing
																														// item
																														// layout
			return new CardViewHolder(view);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof HeaderViewHolder) {
			HeaderEntry header = (HeaderEntry) items.get(position);
			((HeaderViewHolder) holder).dateText.setText(header.getDate());
		} else if (holder instanceof CardViewHolder) {
			PlannerCardItem item = (PlannerCardItem) items.get(position);
			CardViewHolder h = (CardViewHolder) holder;
			h.textLectureTitle.setText(item.getName());
			h.textPlannerTime.setText(item.getStart());

			StringBuilder stringBuilder = new StringBuilder();
			for (String room : item.getRooms()) {
				stringBuilder.append(room).append(" ");
			}
			h.textPlannerRooms.setText(stringBuilder.toString());

			if (item.getLectureType() == Lecture.Type.PRESENCE) {
				h.imageViewType.setImageResource(R.drawable.planner_room);
			} else
				h.imageViewType.setImageResource(R.drawable.planner_online);

		}
	}
}
