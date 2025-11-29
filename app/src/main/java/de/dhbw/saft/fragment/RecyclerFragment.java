package de.dhbw.saft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dhbw.saft.R;
import de.dhbw.saft.adapter.CardAdapter;
import de.dhbw.saft.common.Entry;

/**
 * Abstract fragment that provides a reusable pattern for displaying
 * lists of {@link Entry} items inside a {@link RecyclerView}.
 */
public abstract class RecyclerFragment<T extends CardAdapter<?, ?>> extends Fragment {

	public abstract @NonNull List<Entry> getEntries();
	public abstract @NonNull T getCardAdapter(@NonNull List<Entry> entries);
	public abstract int getLayoutResource();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(getLayoutResource(), container, false);
		final FragmentActivity mainActivity = requireActivity();
		final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

		final List<Entry> entries = getEntries();
		T adapter = getCardAdapter(entries);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
