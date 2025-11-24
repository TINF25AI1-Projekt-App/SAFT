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
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Lecture;
import de.dhbw.saft.common.LectureCard;
import de.dhbw.saft.common.Entry;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LectureCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final List<Entry> items;

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		final Entry.Type type = Entry.Type.fromIdentifier(viewType);
		if (type == null) {
			throw new IllegalArgumentException("Unknown viewType: " + viewType);
		}

		return switch (type) {
			case HEADER -> {
				final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_item_header, parent,
						false);
				yield new HeaderViewHolder(view);
			}
			case ITEM -> {
				final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_item_card, parent,
						false);
				yield new CardViewHolder(view);
			}
		};
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof HeaderViewHolder holder) {
			Header header = (Header) items.get(position);
			holder.dateText.setText(header.date());
			return;
		}

		if (!(viewHolder instanceof CardViewHolder holder)) {
			return;
		}

		LectureCard item = (LectureCard) items.get(position);
		holder.textLectureTitle.setText(item.name());
		holder.textPlannerTime.setText(Formatter.formatTime(item.start()) + " - " + Formatter.formatTime(item.end()));
		holder.textPlannerRooms.setText(String.join(", ", item.rooms()));
		holder.imageViewType.setImageResource(
				item.type() == Lecture.Type.PRESENCE ? R.drawable.planner_room : R.drawable.planner_online);
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getEntryType().identifier;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private static class CardViewHolder extends RecyclerView.ViewHolder {
		final CardView cardView;
		final ImageView imageViewType;
		final TextView textLectureTitle, textPlannerTime, textPlannerRooms;

		public CardViewHolder(View itemView) {
			super(itemView);
			cardView = itemView.findViewById(R.id.card_view);
			textLectureTitle = itemView.findViewById(R.id.text_lecture_title);
			textPlannerTime = itemView.findViewById(R.id.text_planner_time);
			textPlannerRooms = itemView.findViewById(R.id.text_lecture_type);
			imageViewType = itemView.findViewById(R.id.imageView_lecture_type);
		}
	}

	private static class HeaderViewHolder extends RecyclerView.ViewHolder {
		final TextView dateText;

		public HeaderViewHolder(View itemView) {
			super(itemView);
			dateText = itemView.findViewById(R.id.text_day);
		}
	}
}
