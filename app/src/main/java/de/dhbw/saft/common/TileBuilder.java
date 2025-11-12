package de.dhbw.saft.common;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.function.Consumer;

import de.dhbw.saft.MainActivity;
import de.dhbw.saft.R;

public class TileBuilder {

	private static final int TILES_CARD_GRID_COLUMNS = 2;

	private final MainActivity activity;
	private final String[] TILE_TITLES;

	public TileBuilder(MainActivity activity) {
		this.activity = activity;
		TILE_TITLES = activity.getResources().getStringArray(R.array.all_tiles_title);
	}

	public TileBuilder addTile(int titleIndex, int iconResourceId, String link) {
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

	private TileBuilder addTile(int titleIndex, int iconResourceId, Consumer<View> onClickAction) {
		String cardViewID = "card_view_f" + titleIndex;
		int cardResID = activity.getResources().getIdentifier(cardViewID, "id", activity.getPackageName());
		CardView cardView = activity.getBinding().getRoot().findViewById(cardResID);

		String constraintID = "constraintLayout" + titleIndex;
		int constraintResID = activity.getResources().getIdentifier(constraintID, "id", activity.getPackageName());
		ConstraintLayout constraintLayout = activity.getBinding().getRoot().findViewById(constraintResID);
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
		if (parent1 != null) parent1.removeView(cardView);
		constraintLayout.addView(cardView);
		final GridLayout gridLayout = activity.getBinding().gridLayout;
		gridLayout.setUseDefaultMargins(false);
		ViewGroup parent = (ViewGroup) constraintLayout.getParent();
		if (parent != null) parent.removeView(constraintLayout);
		gridLayout.addView(constraintLayout);


		GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) constraintLayout.getLayoutParams();
		layoutParams.setMargins(layoutParams.leftMargin, 0, layoutParams.rightMargin, 0);
		constraintLayout.setLayoutParams(layoutParams);

		if (titleIndex == 1) cardView.setVisibility(INVISIBLE);

		return this;
		}
}
