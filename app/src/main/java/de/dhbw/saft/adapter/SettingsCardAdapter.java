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

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import de.dhbw.saft.R;
import de.dhbw.saft.activity.SplashScreenActivity;
import de.dhbw.saft.common.DialogBuilder;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Preference;
import de.dhbw.saft.service.DataService;
import de.dhbw.saft.service.PreferenceService;

/**
 *
 */
public class SettingsCardAdapter extends CardAdapter<SettingsCardAdapter.CardViewHolder, Preference> {

	private final PreferenceService preferenceService;
	private final Context context;

	public SettingsCardAdapter(@NonNull List<Entry> items, @NonNull PreferenceService preferenceService, @NonNull Context context) {
		super(items);
		this.preferenceService = preferenceService;
		this.context = context;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.preference_item_card;
	}

	@Override
	public CardViewHolder createCardHolder(View view) {
		return new CardViewHolder(view);
	}

	@Override
	public void bindCardHolder(CardViewHolder holder, Preference item) {
		holder.title.setText(item.title());
		holder.description.setText(item.description());
		holder.value.setText(item.value());
		holder.cardView.setOnClickListener(view -> {
			new DialogBuilder(context, preferenceService, Map.of(SplashScreenActivity.KEY_NAME, "${INPUT}"))
					.addSuggestions(DataService.getCourses()).addTextView(holder.value)
					.onOkay(course -> DataService.getInstance().fetchLectures(course)).show();
		});
	}

	public static class CardViewHolder extends RecyclerView.ViewHolder {
		private final CardView cardView;
		private final TextView title, description, value;

		public CardViewHolder(@NonNull View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.textViewPreferenceTitle);
			description = itemView.findViewById(R.id.textViewPreferenceDescription);
			value = itemView.findViewById(R.id.textViewPreferenceValue);
			cardView = itemView.findViewById(R.id.cardViewPreference);
		}
	}
}
