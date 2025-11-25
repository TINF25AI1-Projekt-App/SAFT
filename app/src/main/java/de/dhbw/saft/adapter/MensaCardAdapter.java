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

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Menu;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MensaCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final List<Entry> items;
	List<Menu.Dish> dishes;
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

		// MensaCard item = (MensaCard) items.get(position);

		if (dishes != null && !dishes.isEmpty()) {
			holder.textDishTitle.setText(dishes.get(position).getDeclarativeName());
			holder.textIngredients.setText(dishes.get(position).getDescription());
			holder.textPrice.setText("" + dishes.get(position).price());
			Drawable drawable = Drawable.createFromPath(dishes.get(position).image());
			holder.imageViewDish.setForeground(drawable);
		}
		/*if (item.mainCourses() != null && item.mainCourses().length > 0) {
			Menu.Dish mainCourse = item.mainCourses()[0];
			holder.textDishTitle.setText(mainCourse.getDeclarativeName());
			holder.textIngredients.setText(mainCourse.getDescription());
			holder.textPrice.setText("" + mainCourse.price());
			Drawable drawable = Drawable.createFromPath(mainCourse.image());
			if (drawable != null) {
				holder.imageViewDish.setForeground(drawable);
			} else {
				// handle missing drawable, e.g. clear foreground or set a placeholder
				holder.imageViewDish.setForeground(null);
			}
		}*/

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
