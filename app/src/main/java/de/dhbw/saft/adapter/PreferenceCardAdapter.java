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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dhbw.saft.R;
import de.dhbw.saft.common.AddPreferenceDialog;
import de.dhbw.saft.common.PreferenceItem;
import de.dhbw.saft.common.SharedPreferencesManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PreferenceCardAdapter extends RecyclerView.Adapter<PreferenceCardAdapter.CardViewHolder> {

	private final List<PreferenceItem> items;
	private final SharedPreferencesManager preferencesManager;
	private final Context context;

	@NonNull
	@Override
	public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preference_item_card, parent, false);
		return new CardViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
		PreferenceItem item = items.get(position);
		holder.preferenceTitle.setText(item.title());
		holder.preferenceDescription.setText(item.description());
		holder.preferenceValue.setText(item.value());
		holder.cardView.setOnClickListener(view -> {
			AddPreferenceDialog dialog = getAddPreferenceDialog(preferencesManager,
					holder.preferenceTitle.getText().toString(), holder.preferenceValue);
			dialog.show();
		});
	}

	@NonNull
	private AddPreferenceDialog getAddPreferenceDialog(SharedPreferencesManager prefsManager, String preference,
			TextView preferenceValue) {
		Map<String, String> keyValueMap = new HashMap<>();
		keyValueMap.put(preference.toLowerCase(), "${INPUT}");

		String[] suggestions = {"MA-TINF25AI1", "Suggestion 2", "Suggestion 3"};
		return new AddPreferenceDialog(context, prefsManager, keyValueMap, suggestions, preferenceValue, preference);
	}

	@Override
	public int getItemCount() {
		return items == null ? 0 : items.size();
	}

	public static class CardViewHolder extends RecyclerView.ViewHolder {
		private final CardView cardView;
		private final TextView preferenceTitle, preferenceDescription, preferenceValue;
		public CardViewHolder(@NonNull View itemView) {
			super(itemView);
			preferenceTitle = itemView.findViewById(R.id.textViewPreferenceTitle);
			preferenceDescription = itemView.findViewById(R.id.textViewPreferenceDescription);
			preferenceValue = itemView.findViewById(R.id.textViewPreferenceValue);
			cardView = itemView.findViewById(R.id.cardViewPreference);
		}
	}
}
