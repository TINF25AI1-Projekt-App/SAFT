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

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dhbw.saft.R;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Header;
import lombok.AllArgsConstructor;

/**
 * Abstract recycler adapter that handles a list of {@link Entry} items.
 * Subclasses define the layout and binding logic for card entries.
 */
@AllArgsConstructor
public abstract class CardAdapter<T extends RecyclerView.ViewHolder, E extends Entry>
		extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final List<Entry> items;

	public abstract int getLayoutResource();
	public abstract T createCardHolder(View view);
	public abstract void bindCardHolder(T holder, E item);

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		final Entry.Type type = Entry.Type.fromIdentifier(viewType);
		return switch (type) {
			case HEADER -> {
				final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_item_header, parent,
						false);
				yield new HeaderViewHolder(view);
			}
			case ITEM -> {
				final int resource = getLayoutResource();
				final View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
				yield createCardHolder(view);
			}
		};
	}

	@Override
	@SuppressLint("SetTextI18n")
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
		final Entry entry = items.get(position);
		if (viewHolder instanceof HeaderViewHolder holder) {
			Header header = (Header) entry;
			holder.date.setText(header.date());
			return;
		}

		bindCardHolder((T) viewHolder, (E) entry);
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getEntryType().identifier;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	/**
	 * Responsible for holding the headline of Entries.
	 */
	private static class HeaderViewHolder extends RecyclerView.ViewHolder {
		final TextView date;

		public HeaderViewHolder(View itemView) {
			super(itemView);
			date = itemView.findViewById(R.id.text_day);
		}
	}
}
