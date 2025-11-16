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

import java.text.MessageFormat;
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

	private final HomeFragment fragment;
	private final FragmentActivity activity;
	private final String[] TILE_TITLES;

	private static final String CARD_VIEW_ID_FORMAT = "card_view_f{0}";
	private static final String CONSTRAINT_LAYOUT_ID_FORMAT = "constraintLayout{0}";

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
		final TextView textView = new TextView(activity);
		textView.setText(TILE_TITLES[titleIndex]);
		textView.setTextSize(24);
		textView.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
		textView.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(activity);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		imageView.setImageResource(iconResourceId);

		final LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.addView(imageView);
		linearLayout.addView(textView);

		final FragmentHomeBinding binding = fragment.getBinding();
		final Resources resources = fragment.getResources();
		final String packageName = activity.getPackageName();

		final String cardViewId = MessageFormat.format(CARD_VIEW_ID_FORMAT, titleIndex);
		final int cardResourceId = resources.getIdentifier(cardViewId, "id", packageName);
		final CardView cardView = binding.getRoot().findViewById(cardResourceId);
		cardView.setUseCompatPadding(false);
		cardView.setVisibility(VISIBLE);
		cardView.addView(linearLayout);
		cardView.setOnClickListener(onClickAction::accept);

		final ViewGroup cardViewParent = (ViewGroup) cardView.getParent();
		if (cardViewParent != null) {
			cardViewParent.removeView(cardView);
		}

		final String constraintLayoutId = MessageFormat.format(CONSTRAINT_LAYOUT_ID_FORMAT, titleIndex);
		final int constraintResourceId = resources.getIdentifier(constraintLayoutId, "id", packageName);
		final ConstraintLayout constraintLayout = binding.getRoot().findViewById(constraintResourceId);
		constraintLayout.addView(cardView);

		final ViewGroup constraintLayoutParent = (ViewGroup) constraintLayout.getParent();
		if (constraintLayoutParent != null) {
			constraintLayoutParent.removeView(constraintLayout);
		}

		final ConstraintSet constraintSet = new ConstraintSet();
		constraintSet.clone(constraintLayout);
		constraintSet.setDimensionRatio(cardView.getId(), "1:1");
		constraintSet.applyTo(constraintLayout);

		final LinearLayout cardRow = titleIndex > 1 ? binding.linearlayoutBottom : binding.linearlayoutTop;
		cardRow.addView(constraintLayout);
		return this;
	}
}
