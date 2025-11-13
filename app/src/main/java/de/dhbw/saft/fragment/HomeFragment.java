package de.dhbw.saft.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.dhbw.saft.R;
import de.dhbw.saft.common.TileBuilder;
import de.dhbw.saft.databinding.FragmentHomeBinding;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HomeFragment extends Fragment {

	@Getter
	private FragmentHomeBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		binding = FragmentHomeBinding.inflate(inflater, container, false);

		final TileBuilder builder = new TileBuilder(this);
		builder.addTile(0, R.drawable.tile_where2go,
				"https://www.google.com/maps/d/embed?mid=1xRb0uZgr4Lsyys_mqYxPgT--4JO4OpA&ehbc=2E312F&ll=49.48905226213409%2C8.480855325064853&z=14")
				.addTile(1, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(2, R.drawable.tile_moodle, "https://moodle.dhbw-mannheim.de/login/index.php")
				.addTile(3, R.drawable.tile_zimbra, "https://studgate.dhbw-mannheim.de/");

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
