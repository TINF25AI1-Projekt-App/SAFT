package de.dhbw.saft.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import de.dhbw.saft.R;
import de.dhbw.saft.common.DishCard;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Header;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MensaCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
				final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensa_item_card, parent,
						false);
				yield new CardViewHolder(view);
			}
		};
	}

	@Override
	@SuppressLint("SetTextI18n")
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof HeaderViewHolder holder) {
			Header header = (Header) items.get(position);
			holder.dateText.setText(header.date());
			return;
		}

		if (!(viewHolder instanceof CardViewHolder holder)) {
			return;
		}

		DishCard item = (DishCard) items.get(position);
		holder.textDishTitle.setText(item.name());
		holder.textIngredients.setText(item.description());
		holder.textPrice.setText(String.format(Locale.getDefault(), "%,.2f €", item.price()));
		Drawable drawable = Drawable.createFromPath(item.image());
		holder.imageViewDish.setForeground(drawable);

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
		// final CardView cardView;
		final CardView imageViewDish;
		final TextView textDishTitle, textIngredients, textPrice;

		public CardViewHolder(View itemView) {
			super(itemView);
			// cardView = itemView.findViewById(R.id.card_view);
			textDishTitle = itemView.findViewById(R.id.text_mensa_dish_title);
			textIngredients = itemView.findViewById(R.id.text_mensa_ingredients);
			textPrice = itemView.findViewById(R.id.text_mensa_price);
			imageViewDish = itemView.findViewById(R.id.card_view_dish_image);
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
