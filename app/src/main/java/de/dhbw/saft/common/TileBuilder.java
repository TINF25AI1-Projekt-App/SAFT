package de.dhbw.saft.common;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.function.Consumer;

import de.dhbw.saft.MainActivity;
import de.dhbw.saft.R;

/**
 * Utility class for dynamically creating clickable tiles in the
 * {@link MainActivity}.
 */
public class TileBuilder {

	private static final int TILES_CARD_GRID_COLUMNS = 2;

	private final MainActivity activity;
	private final String[] TILE_TITLES;

	/**
	 * Creates a new tile builder for {@link MainActivity}.
	 *
	 * @param activity
	 *            The activity to create the tiles for
	 */
	public TileBuilder(@NonNull MainActivity activity) {
		this.activity = activity;
		TILE_TITLES = activity.getResources().getStringArray(R.array.all_tiles_title);
	}

	/**
	 * Creates a new clickable tile opening a certain link.
	 *
	 * @param titleIndex
	 *            The tiles index used for their title
	 * @param iconResourceId
	 *            The icon id of the tile
	 * @param link
	 *            The link to open
	 * @return The used builder
	 */
	public TileBuilder addTile(int titleIndex, int iconResourceId, @NonNull String link) {
		Consumer<View> onClickAction = view -> {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			Intent chooser = Intent.createChooser(intent, activity.getString(R.string.title_browser_chooser));

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
	 * @param titleIndex
	 *            The tiles index used for their title
	 * @param iconResourceId
	 *            The icon id of the tile
	 * @param onClickAction
	 *            The action to take
	 * @return The used builder
	 */
	private TileBuilder addTile(int titleIndex, int iconResourceId, @NonNull Consumer<View> onClickAction) {
		CardView cardView = new CardView(activity);
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();

		params.width = 0;
		params.height = 0;
		params.columnSpec = GridLayout.spec(titleIndex % TILES_CARD_GRID_COLUMNS, 1f);
		params.rowSpec = GridLayout.spec(titleIndex / TILES_CARD_GRID_COLUMNS, 1f);
		params.setMargins(8, 8, 8, 8);

		cardView.setLayoutParams(params);
		cardView.setRadius(12);
		cardView.setCardElevation(8);
		cardView.setUseCompatPadding(true);

		LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setPadding(8, 8, 8, 8);

		TextView textView = new TextView(activity);
		textView.setText(TILE_TITLES[titleIndex]);
		textView.setTextSize(24);
		textView.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
		textView.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(activity);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
		imageView.setImageResource(iconResourceId);

		linearLayout.addView(textView);
		linearLayout.addView(imageView);
		cardView.addView(linearLayout);

		cardView.setOnClickListener(onClickAction::accept);

		final GridLayout gridLayout = activity.getBinding().tilesGridLayout;
		gridLayout.addView(cardView);
		return this;
	}
}
