package tk.digitoy.kittyheartcollect.activities;

import tk.digitoy.kittyheartcollect.activities.R;
import tk.digitoy.kittyheartcollect.utils.AppSettings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Wallpapers extends Activity implements OnClickListener {

	// Layouts
	private LayoutParams paramWallScroll;
	private LayoutParams[] paramWall;
	RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpapers);

		drawStaticLayout();
		createAdLayout();
	}

	// Called when the activity returning to foreground.
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

	private int getHighScore() {
		return AppSettings.prefs.getInt("Score", 0);
	}

	// Creating and adding views
	private void drawStaticLayout() {

		// finding wallpaper thumbs
		ImageView[] walls = new ImageView[] {
				(ImageView) findViewById(R.id.imageView1),
				(ImageView) findViewById(R.id.imageView2),
				(ImageView) findViewById(R.id.imageView3),
				(ImageView) findViewById(R.id.imageView4),
				(ImageView) findViewById(R.id.imageView5),
				(ImageView) findViewById(R.id.imageView6),
				(ImageView) findViewById(R.id.imageView7),
				(ImageView) findViewById(R.id.imageView8),
				(ImageView) findViewById(R.id.imageView9),
				(ImageView) findViewById(R.id.imageView10) };

		ImageView[] locks = new ImageView[10];
		for (int i = 0; i < 10; i++) {
			locks[i] = new ImageView(this);
			locks[i].setId(i);
			locks[i].setBackgroundResource(R.drawable.lockedt);
		}

		HorizontalScrollView wallScroll = (HorizontalScrollView) findViewById(R.id.wallScroll);
		RelativeLayout linearScroll = (RelativeLayout) findViewById(R.id.linearScroll);

		paramWallScroll = new LayoutParams(MainMenu.dispWidth * 606 / 800,
				MainMenu.dispHeight * 256 / 480);
		paramWallScroll.leftMargin = MainMenu.dispWidth * 97 / 800;
		paramWallScroll.topMargin = MainMenu.dispHeight * 113 / 480;

		paramWall = new LayoutParams[10];

		for (int i = 0; i < 10; i++) {
			paramWall[i] = new LayoutParams(MainMenu.dispWidth * 186 / 800,
					MainMenu.dispHeight * 256 / 480);
			paramWall[i].leftMargin = MainMenu.dispWidth * 30 / 800 * i
					+ MainMenu.dispWidth * 186 / 800 * (i % 10);
		}

		wallScroll.setLayoutParams(paramWallScroll);
		for (int i = 0; i < 10; i++) {
			walls[i].setLayoutParams(paramWall[i]);
			walls[i].setScaleType(ScaleType.FIT_XY);
			walls[i].setId(i);
			walls[i].setOnClickListener(this);

			if (getHighScore() < AppSettings.lockscore[i]) {
				linearScroll.addView(locks[i]);
				locks[i].setLayoutParams(paramWall[i]);
				locks[i].setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(
								Wallpapers.this,
								getString(R.string.wallpaper_locked_1)
										+ " "
										+ AppSettings.lockscore[arg0.getId()]
										+ " "
										+ getString(R.string.wallpaper_locked_2),
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}

	}

	// Start playing sound if sound is on

	public void onClick(View v) {
		Intent go = new Intent(this, Wallpaper.class);
		go.putExtra("wallNumber", v.getId());
		startActivity(go);
	}
	private AdView adView;
	
	public void createAdLayout()
	{
		adView = new AdView(this, AdSize.BANNER, AppSettings.MY_AD_UNIT_ID);

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		layout = (RelativeLayout) findViewById(R.id.walpapers_rl);
		LayoutParams adMobLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
