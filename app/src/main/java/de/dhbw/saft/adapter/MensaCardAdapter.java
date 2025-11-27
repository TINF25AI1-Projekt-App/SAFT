package de.dhbw.saft.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.model.Menu;

public class MensaCardAdapter extends CardAdapter<MensaCardAdapter.CardViewHolder, Menu.Dish> {

	public MensaCardAdapter(List<Entry> items) {
		super(items);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.mensa_item_card;
	}

	@Override
	public CardViewHolder createCardHolder(View view) {
		return new CardViewHolder(view);
	}

	@Override
	public void bindCardHolder(CardViewHolder holder, Menu.Dish item) {
		holder.textDishTitle.setText(item.getDeclarativeName());
		holder.textIngredients.setText(item.getDescription());
		holder.textPrice.setText(String.format(Locale.getDefault(), "%,.2f €", item.price()));
		Drawable drawable = Drawable.createFromPath(item.image());
		holder.imageViewDish.setForeground(drawable);
	}

	public static class CardViewHolder extends RecyclerView.ViewHolder {
		final CardView imageViewDish;
		final TextView textDishTitle, textIngredients, textPrice;

		public CardViewHolder(View itemView) {
			super(itemView);
			textDishTitle = itemView.findViewById(R.id.text_mensa_dish_title);
			textIngredients = itemView.findViewById(R.id.text_mensa_ingredients);
			textPrice = itemView.findViewById(R.id.text_mensa_price);
			imageViewDish = itemView.findViewById(R.id.card_view_dish_image);
		}
	}
}
