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

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.service.DataService;

/**
 * Adapter for displaying dish entries inside the planner list.
 * It binds {@link Menu.Dish} items to their card layout.
 */
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

		final String image = item.image();
		final ImageView imageView = holder.imageViewDish;
		if (image == null) {
			imageView.setImageResource(R.drawable.food_default);
			return;
		}

		DataService.getInstance().fetchImage(image).thenAccept(bitmap -> {
			if (bitmap == null) {
				imageView.post(() -> imageView.setImageResource(R.drawable.food_default));
				return;
			}

			Drawable drawable = new BitmapDrawable(holder.imageViewDish.getResources(), bitmap);
			holder.imageViewDish.post(() -> imageView.setImageDrawable(drawable));
		});
	}

	public static class CardViewHolder extends RecyclerView.ViewHolder {
		final ImageView imageViewDish;
		final TextView textDishTitle, textIngredients, textPrice;

		public CardViewHolder(View itemView) {
			super(itemView);
			textDishTitle = itemView.findViewById(R.id.text_mensa_dish_title);
			textIngredients = itemView.findViewById(R.id.text_mensa_ingredients);
			textPrice = itemView.findViewById(R.id.text_mensa_price);
			imageViewDish = itemView.findViewById(R.id.image_dish);
		}
	}
}
