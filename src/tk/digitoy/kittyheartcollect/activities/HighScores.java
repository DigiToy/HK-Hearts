package tk.digitoy.kittyheartcollect.activities;

import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import tk.digitoy.kittyheartcollect.utils.AppSettings;
import tk.digitoy.kittyheartcollect.utils.ScoreModel;
import tk.digitoy.kittyheartcollect.utils.User;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class HighScores extends Activity {

	private RelativeLayout layout;
	private List<User> highScores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.high_scores);

		createAdLayout();

		TextView noScores = (TextView) findViewById(R.id.tvHighScores);
		highScores = new ScoreModel(this).getAllUsers();

		TableLayout scoresTable = (TableLayout) findViewById(R.id.scoresTable);

		if (highScores.size() == 0) {
			noScores.setTypeface(AppSettings.newFont);
			noScores.setTextColor(Color.WHITE);
			noScores.setText(R.string.no_highscores);
			scoresTable.setVisibility(View.GONE);
		} else {
			noScores.setVisibility(View.GONE);
			initTable();
		}
	}

	private void initTable() {
		switch (highScores.size()) {
		case 1:
			findViewById(R.id.tableRow5).setVisibility(View.GONE);
			findViewById(R.id.tableRow4).setVisibility(View.GONE);
			findViewById(R.id.tableRow3).setVisibility(View.GONE);
			findViewById(R.id.tableRow2).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.tableRow5).setVisibility(View.GONE);
			findViewById(R.id.tableRow4).setVisibility(View.GONE);
			findViewById(R.id.tableRow3).setVisibility(View.GONE);
			break;
		case 3:
			findViewById(R.id.tableRow5).setVisibility(View.GONE);
			findViewById(R.id.tableRow4).setVisibility(View.GONE);
			break;
		case 4:
			findViewById(R.id.tableRow5).setVisibility(View.GONE);
			break;
		default:
			break;
		}

		int[] positionIds = new int[] { R.id.tvPosition1, R.id.tvPosition2,
				R.id.tvPosition3, R.id.tvPosition4, R.id.tvPosition5 };
		int[] nameIds = new int[] { R.id.tvName1, R.id.tvName2, R.id.tvName3,
				R.id.tvName4, R.id.tvName5 };
		int[] pointIds = new int[] { R.id.tvPoints1, R.id.tvPoints2,
				R.id.tvPoints3, R.id.tvPoints4, R.id.tvPoints5 };

		for (int i = 0; i < highScores.size(); i++) {
			TextView tvPosition = (TextView) findViewById(positionIds[i]);
			TextView tvName = (TextView) findViewById(nameIds[i]);
			TextView tvPoints = (TextView) findViewById(pointIds[i]);

			tvPosition.setTypeface(AppSettings.newFont);
			tvPosition.setTextColor(Color.WHITE);
			tvName.setTypeface(AppSettings.newFont);
			tvName.setTextColor(Color.WHITE);
			tvPoints.setTypeface(AppSettings.newFont);
			tvPoints.setTextColor(Color.WHITE);

			tvPosition.setText(highScores.get(i).getPosition() + ".");
			tvName.setText(highScores.get(i).getName());
			tvPoints.setText(String.valueOf(highScores.get(i).getPoints()));
		}
	}

	private void playMusic() {
		if (MainMenu.soundIsOn) {
			MainMenu.menuSong.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		playMusic();
		AdMobAdsRequest();
	}

	@Override
	public void onBackPressed() {
		if (MainMenu.menuSong != null) {
			MainMenu.menuSong.release();
			MainMenu.menuSong = null;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		if (MainMenu.menuSong != null && MainMenu.menuSong.isPlaying()) {
			MainMenu.menuSong.pause();
		}
		super.onPause();
	}

	private AdView adView;

	public void createAdLayout() {
		adView = new AdView(this, AdSize.BANNER, AppSettings.MY_AD_UNIT_ID);

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		layout = (RelativeLayout) findViewById(R.id.infoLayout);
		LayoutParams adMobLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adMobLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adMobLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(adMobLayoutParams);

		// Add the adView to it
		layout.addView(adView);

	}

	public void AdMobAdsRequest() {

		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());
	}
}
