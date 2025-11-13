package de.dhbw.saft.common;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Consumer;

import de.dhbw.saft.HomeActivity;
import de.dhbw.saft.R;
import de.dhbw.saft.databinding.FragmentHomeBinding;
import de.dhbw.saft.fragment.HomeFragment;

/**
 * Utility class for dynamically creating clickable tiles in the
 * {@link HomeActivity}.
 */
public class TileBuilder {

	private static final int TILES_CARD_GRID_COLUMNS = 2;

	private final HomeFragment fragment;

	private final FragmentActivity activity;

	private final String[] TILE_TITLES;

	/**
	 * Creates a new tile builder for {@link HomeFragment}.
	 *
	 * @param fragment The activity to create the tiles for
	 */
	public TileBuilder(@NonNull HomeFragment fragment) {
		this.fragment = fragment;
		activity = fragment.getActivity();
		TILE_TITLES = fragment.getResources().getStringArray(R.array.all_tiles_title);
	}

	/**
	 * Creates a new clickable tile opening a certain link.
	 *
	 * @param titleIndex 		The tiles index used for their title
	 * @param iconResourceId 	The icon id of the tile
	 * @param link				The link to open
	 * @return 					The used builder
	 */
	public TileBuilder addTile(int titleIndex, int iconResourceId, @NonNull String link) {
		Consumer<View> onClickAction = view -> {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			Intent chooser = Intent.createChooser(intent, fragment.getString(R.string.title_browser_chooser));

			if (chooser.resolveActivity(activity.getPackageManager()) != null) {
				activity.startActivity(chooser);
				return;
			}

			Toast.makeText(activity, R.string.error_text_no_app_found_chooser, Toast.LENGTH_SHORT).show();
		};
		return addTile(titleIndex, iconResourceId, onClickAction);
	}

	/**
	 * Creates a new clickable tile.
	 *
	 * @param titleIndex		The tiles index used for their title
	 * @param iconResourceId	The icon id of the tile
	 * @param onClickAction		The action to take
	 * @return 					The used builder
	 */
	private TileBuilder addTile(int titleIndex, int iconResourceId, @NonNull Consumer<View> onClickAction) {
		final String packageName = activity.getPackageName();
		final Resources resources = fragment.getResources();
		final FragmentHomeBinding binding = fragment.getBinding();

		String cardViewID = "card_view_f" + titleIndex;
		int cardResID = resources.getIdentifier(cardViewID, "id", packageName);
		CardView cardView = binding.getRoot().findViewById(cardResID);

		String constraintID = "constraintLayout" + titleIndex;
		int constraintResID = resources.getIdentifier(constraintID, "id", packageName);
		ConstraintLayout constraintLayout = binding.getRoot().findViewById(constraintResID);
		ConstraintSet constraintSet = new ConstraintSet();
		constraintSet.clone(constraintLayout);
		constraintSet.setDimensionRatio(cardView.getId(), "1:1");

		constraintSet.applyTo(constraintLayout);

		cardView.setUseCompatPadding(false);

		cardView.setVisibility(VISIBLE);

		LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);

		TextView textView = new TextView(activity);
		textView.setText(TILE_TITLES[titleIndex]);
		textView.setTextSize(24);
		textView.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
		textView.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(activity);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		imageView.setImageResource(iconResourceId);

		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		cardView.addView(linearLayout);

		cardView.setOnClickListener(onClickAction::accept);

		ViewGroup parent1 = (ViewGroup) cardView.getParent();
		if (parent1 != null)
			parent1.removeView(cardView);
		constraintLayout.addView(cardView);

		LinearLayout test = titleIndex > 1 ? binding.linearlayoutBottom : binding.linearlayoutTop;

		ViewGroup parent = (ViewGroup) constraintLayout.getParent();
		if (parent != null)
			parent.removeView(constraintLayout);
		test.addView(constraintLayout);

		return this;
	}
}
