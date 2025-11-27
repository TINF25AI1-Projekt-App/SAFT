package de.dhbw.saft.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import de.dhbw.saft.R;
import de.dhbw.saft.adapter.MensaCardAdapter;
import de.dhbw.saft.common.Entry;
import de.dhbw.saft.common.Formatter;
import de.dhbw.saft.common.Header;
import de.dhbw.saft.model.Menu;
import de.dhbw.saft.service.DataService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MensaFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_mensa, container, false);
		final FragmentActivity mainActivity = requireActivity();
		final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

		final List<Menu> menus = DataService.getMenus();
		final List<Entry> entries = new ArrayList<>();
		final Set<String> dates = new HashSet<>();

		for (Menu menu : menus) {
			final String date = Formatter.formatDate(menu.date());
			final Menu.Dish[] mainCourses = menu.mainCourses();
			final Menu.Dish[] desserts = menu.desserts();
			if (date != null && dates.add(date) && (mainCourses.length > 0 || desserts.length > 0)) {
				entries.add(new Header(date));
			}

			List<Menu.Dish> dishes = Stream.concat(Arrays.stream(mainCourses), Arrays.stream(desserts)).toList();
			entries.addAll(dishes);
		}

		MensaCardAdapter adapter = new MensaCardAdapter(entries);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
