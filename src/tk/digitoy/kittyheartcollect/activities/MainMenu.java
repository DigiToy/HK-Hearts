package tk.digitoy.kittyheartcollect.activities;

import tk.digitoy.kittyheartcollect.utils.AppSettings;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainMenu extends Activity {
	private LayoutParams paramPlay;
	RelativeLayout layout;
	RelativeLayout layoutSounds;
	ImageButton soundsButton;
	ImageButton musicButton;
	ImageButton soundEffectButton;
	ImageButton infoButton;
	ImageButton highScoresButton;
	ImageButton moreGamesButton;
	ImageButton wallpapersButton;

	LayoutParams lpSoundsButton;
	LayoutParams lpMusicButton;
	LayoutParams lpSoundEffectButton;
	LayoutParams lpInfoButton;
	LayoutParams lpHighScoresButton;
	LayoutParams lpMoreGamesButton;
	LayoutParams lpWallpapersButton;
	LayoutParams lpScoreView;

	public static int dispWidth;
	public static int dispHeight;
	boolean isVisible;
	public static boolean effectsISOn;
	public static boolean soundIsOn;
	// Kitty song
	public static MediaPlayer menuSong;

	// High score
	public static int score = 0;
	public static String nameHighScores = "Player";
	private TextView scoreView;

	// int scoreCount;

	// Shared preferences

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		AppSettings.newFont = Typeface.createFromAsset(getAssets(),
				"zombie.ttf");

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		dispHeight = metrics.heightPixels;
		dispWidth = metrics.widthPixels;

		soundsButton = (ImageButton) findViewById(R.id.sounds);
		musicButton = (ImageButton) findViewById(R.id.sounds_music);
		soundEffectButton = (ImageButton) findViewById(R.id.sounds_effects);

		lpSoundsButton = new LayoutParams(dispWidth * 224 / 1280,
				dispHeight * 112 / 768);
		lpSoundsButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpSoundsButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpSoundsButton.topMargin = dispHeight * 10 / 768;
		lpSoundsButton.rightMargin = dispWidth * 10 / 1280;

		lpSoundEffectButton = new LayoutParams(dispWidth * 224 / 1280,
				dispHeight * 112 / 768);
		lpSoundEffectButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpSoundEffectButton.addRule(RelativeLayout.BELOW, soundsButton.getId());
		lpSoundEffectButton.topMargin = dispHeight * 10 / 768;
		lpSoundEffectButton.rightMargin = dispWidth * 10 / 1280;

		lpMusicButton = new LayoutParams(dispWidth * 224 / 1280,
				dispHeight * 112 / 768);
		lpMusicButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpMusicButton.addRule(RelativeLayout.BELOW, soundEffectButton.getId());
		lpMusicButton.topMargin = dispHeight * 10 / 768;
		lpMusicButton.rightMargin = dispWidth * 10 / 1280;

		lpInfoButton = new LayoutParams(dispWidth * 112 / 1280,
				dispHeight * 112 / 768);
		lpInfoButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpInfoButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpInfoButton.bottomMargin = dispHeight * 10 / 768;
		lpInfoButton.rightMargin = dispWidth * 10 / 1280;

		lpHighScoresButton = new LayoutParams(dispWidth * 158 / 1280,
				dispHeight * 294 / 768);
		lpHighScoresButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpHighScoresButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lpHighScoresButton.bottomMargin = dispHeight * 10 / 768;
		lpHighScoresButton.leftMargin = dispWidth * 10 / 1280;

		lpMoreGamesButton = new LayoutParams(dispWidth * 224 / 1280,
				dispHeight * 112 / 768);
		lpMoreGamesButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpMoreGamesButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lpMoreGamesButton.bottomMargin = dispHeight * 10 / 768;

		lpWallpapersButton = new LayoutParams(dispWidth * 206 / 1280,
				dispHeight * 42 / 768);
		lpWallpapersButton.topMargin = dispHeight * 460 / 768;
		lpWallpapersButton.leftMargin = dispWidth * 870 / 1280;

		playButtonInit();
		infoButtonInit();
		highScoresButtonInit();
		moreGamesButtonInit();
		wallpaperButtonInit();

		isVisible = false;
		effectsISOn = true;
		soundIsOn = true;
		soundButtonLayout();

		soundsButton.setLayoutParams(lpSoundsButton);
		musicButton.setLayoutParams(lpMusicButton);
		soundEffectButton.setLayoutParams(lpSoundEffectButton);
		infoButton.setLayoutParams(lpInfoButton);
		highScoresButton.setLayoutParams(lpHighScoresButton);
		moreGamesButton.setLayoutParams(lpMoreGamesButton);
		wallpapersButton.setLayoutParams(lpWallpapersButton);

		createAdLayout();
	}

	public void getHighScores() {
		AppSettings.prefs = getSharedPreferences(AppSettings.settingsPref,
				MODE_PRIVATE);
		score = AppSettings.prefs.getInt("Score", 0);
		nameHighScores = AppSettings.prefs.getString("Player Name", "Player");

		scoreView = (TextView) findViewById(R.id.scoreView);
		lpScoreView = new LayoutParams(dispWidth * 300 / 1280,
				dispHeight * 50 / 768);
		lpScoreView.topMargin = dispHeight * 580 / 768;
		lpScoreView.leftMargin = dispWidth * 155 / 1280;
		scoreView.setLayoutParams(lpScoreView);
		scoreView.setTypeface(AppSettings.newFont);
		scoreView.setText(nameHighScores + " : " + score);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getHighScores();
		playMusic();
		AdMobAdsRequest();
	}

	private void playMusic() {
		if (soundIsOn && menuSong == null) {
			menuSong = MediaPlayer.create(getBaseContext(), R.raw.main_music);
		}

		if (soundIsOn && menuSong != null) {
			menuSong.start();
		}
	}

	public void playButtonInit() {
		// Image Parameters
		paramPlay = new LayoutParams(dispWidth * 130 / 1280,
				dispHeight * 130 / 768);
		paramPlay.leftMargin = dispWidth * 892 / 1280;
		paramPlay.topMargin = dispHeight * 200 / 768;

		ImageButton playButton = (ImageButton) findViewById(R.id.play);
		playButton.setLayoutParams(paramPlay);

		playButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenu.this, HeartCollect.class);
				startActivity(intent);
			}
		});

	}

	public void infoButtonInit() {
		infoButton = (ImageButton) findViewById(R.id.info_btn);
		infoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenu.this, Info.class);
				startActivity(intent);
			}
		});
	}

	public void wallpaperButtonInit() {
		wallpapersButton = (ImageButton) findViewById(R.id.wallpapers_btn);
		wallpapersButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenu.this, Wallpapers.class);
				startActivity(intent);
			}
		});
	}

	public void highScoresButtonInit() {
		highScoresButton = (ImageButton) findViewById(R.id.hiScore);

		highScoresButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), HighScores.class));
			}
		});
		// scoreCount = 0;
		//
		// highScoresButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (scoreCount == 0) {
		//
		// scoreView.setVisibility(View.VISIBLE);
		// scoreCount = 1;
		// } else {
		// scoreView.setVisibility(View.GONE);
		// scoreCount = 0;
		// }
		//
		// }
		// });
	}

	public void moreGamesButtonInit() {
		moreGamesButton = (ImageButton) findViewById(R.id.moreGames);
		moreGamesButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://search?q=pub:DigiToy"));
				startActivity(intent);
			}
		});
	}

	public void soundButtonLayout() {
		layoutSounds = (RelativeLayout) findViewById(R.id.layout_sounds);
		musicButton = (ImageButton) findViewById(R.id.sounds_music);
		soundEffectButton = (ImageButton) findViewById(R.id.sounds_effects);
		soundsButton = (ImageButton) findViewById(R.id.sounds);
		soundsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!isVisible) {
					layoutSounds.setVisibility(View.VISIBLE);
					layoutSounds.setAnimation(new TranslateAnimation(0, 0, 0,
							10));
					// soundsButton.setVisibility(View.INVISIBLE);
					isVisible = true;
				} else {
					layoutSounds.setAnimation(new TranslateAnimation(0, 0, 0,
							-10));
					layoutSounds.setVisibility(View.GONE);
					isVisible = false;
				}

			}
		});

		soundEffectButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (effectsISOn) {
					effectsISOn = !effectsISOn;
					soundEffectButton
							.setBackgroundResource(R.drawable.effects_off);
				} else {
					effectsISOn = !effectsISOn;
					soundEffectButton
							.setBackgroundResource(R.drawable.effects_on);
				}
			}
		});

		musicButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (menuSong.isPlaying()) {
					musicButton.setBackgroundResource(R.drawable.music_off);
					menuSong.pause();
					soundIsOn = false;
				} else {
					musicButton.setBackgroundResource(R.drawable.music_on);
					menuSong.start();
					soundIsOn = true;
				}
			}
		});
	}

	private AdView adView;

	public void createAdLayout() {
		adView = new AdView(this, AdSize.BANNER, AppSettings.MY_AD_UNIT_ID);

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		layout = (RelativeLayout) findViewById(R.id.main_menu);
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

	@Override
	public void onBackPressed() {
		if (menuSong != null) {
			menuSong.release();
			menuSong = null;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		if (menuSong != null && menuSong.isPlaying()) {
			menuSong.pause();
		}
		super.onPause();
	}

}
