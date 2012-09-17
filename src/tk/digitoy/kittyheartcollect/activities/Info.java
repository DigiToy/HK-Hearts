package tk.digitoy.kittyheartcollect.activities;

import tk.digitoy.kittyheartcollect.utils.AppSettings;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Info extends Activity {

	RelativeLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		createAdLayout();
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
