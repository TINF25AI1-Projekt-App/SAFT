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
import de.dhbw.saft.common.MensaCard;
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
		List<Entry> dates = new ArrayList<>();
		Set<String> addedDates = new HashSet<>();

		for (Menu menu : menus) {
			MensaCard item = new MensaCard(menu.date(), menu.mainCourses(), menu.desserts());
			String date = Formatter.formatDate(item.date());

			/*if (date != null && !addedDates.contains(date)) {
				dates.add(new Header(date));
				addedDates.add(date);
			}*/
			dates.add(item);
		}

		List<Menu.Dish> dishes = menus.stream()
				.flatMap(menu -> Stream.concat(Arrays.stream(menu.mainCourses()), Arrays.stream(menu.desserts())))
				.toList();

		MensaCardAdapter adapter = new MensaCardAdapter(dates, dishes);
		recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
		recyclerView.setAdapter(adapter);
		return root;
	}
}
