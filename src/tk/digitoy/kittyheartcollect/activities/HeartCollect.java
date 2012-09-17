package tk.digitoy.kittyheartcollect.activities;

import java.util.ArrayList;

import tk.digitoy.kittyheartcollect.activities.HeartCollect.GameView.GameFigure;
import tk.digitoy.kittyheartcollect.utils.AppSettings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class HeartCollect extends Activity {

	RelativeLayout collecthearts_rl;
	private TextView countTextView;

	public MediaPlayer gameSong;
	public MediaPlayer tapSound;
	public MediaPlayer breakSound;
	public MediaPlayer gameOverSound;
	public MediaPlayer wonScoreSound;
	public MediaPlayer catchSound;
	int onBackPressedCount = 0;
	static int bgHeight;
	static int bgWidth;
	GameView myView;
	ImageView imgAnim;
	ImageView hearthScoreImg;
	ImageView topLeftButton;
	ImageView topRightButton;
	ImageView downLeftButton;
	ImageView downRightButton;
	ImageButton pauseButton;
	ImageButton continuePlayButton;
	ImageButton replayButton;
	ImageButton okButton;
	RelativeLayout layoutPause;
	boolean isVisible;
	ImageView kittyBasket;
	ImageView kittyBasketLeft;
	ImageView kittyLife1;
	ImageView kittyLife2;
	ImageView kittyLife3;
	int kittyBasketPosition;
	TranslateAnimation ta;
	int startPointLeftTop;
	int endPointLeftTop;
	int startPointLeftDown;
	int endPointLeftDown;
	int startPointRightTop;
	int endPointRightTop;
	int startPointRightDown;
	int endPointRightDown;
	int i = 0;
	int dropCount = 0;
	LayoutParams lpKitty;

	// Button Params
	LayoutParams lpTopLeftBtn;
	LayoutParams lpDownLeftBtn;
	LayoutParams lpTopRightBtn;
	LayoutParams lpDownRightBtn;
	LayoutParams lpKittyLife1;
	LayoutParams lpKittyLife2;
	LayoutParams lpKittyLife3;
	LayoutParams lpPause;
	LayoutParams lpPlay;
	LayoutParams lpReplay;
	LayoutParams lpHearthSchore;
	LayoutParams lpOKButton;

	ArrayList<GameFigure> figuresArray;
	CountDownTimer countDownTimer;
	Runnable timerRun;
	Handler myHandler;
	Dialog alertDialog;
	int timePeriod;

	EditText nameEditText;
	private int cutchImages;
	int score;
	private boolean showAlertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hearts_collect);

		pauseButtonLayout();

		topLeftButton = (ImageView) findViewById(R.id.left_top_btn);
		topRightButton = (ImageView) findViewById(R.id.right_top_btn);
		downLeftButton = (ImageView) findViewById(R.id.left_dawn_btn);
		downRightButton = (ImageView) findViewById(R.id.right_dawn_btn);

		kittyBasketLeft = (ImageView) findViewById(R.id.kitty_left_img);
		kittyLife1 = (ImageView) findViewById(R.id.life_icon_img1);
		kittyLife2 = (ImageView) findViewById(R.id.life_icon_img2);
		kittyLife3 = (ImageView) findViewById(R.id.life_icon_img3);
		hearthScoreImg = (ImageView) findViewById(R.id.heart_img);

		gameSong = MediaPlayer.create(getBaseContext(), R.raw.game_music);
		catchSound = MediaPlayer.create(getBaseContext(), R.raw.catch_sound);
		breakSound = MediaPlayer.create(getBaseContext(), R.raw.break_sound);
		wonScoreSound = MediaPlayer.create(getBaseContext(), R.raw.won_sound);
		gameOverSound = MediaPlayer.create(getBaseContext(),
				R.raw.game_over_sound);
		setListeners();
		collecthearts_rl = (RelativeLayout) findViewById(R.id.activity_hearts_collect);
		countTextView = (TextView) findViewById(R.id.count_of_images_txt);
		countTextView.setTextColor(Color.BLACK);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		bgHeight = metrics.heightPixels;
		bgWidth = metrics.widthPixels;
		countTextView.setTextSize(bgHeight / 20);
		myView = new GameView(this, bgHeight, bgWidth);
		imgAnim = new ImageView(getBaseContext());
		collecthearts_rl.addView(myView);
		lpKitty = new LayoutParams(bgWidth * 246 / 1280, bgHeight * 264 / 768);
		lpKitty.leftMargin = bgWidth * 581 / 1280;
		lpKitty.topMargin = bgHeight * 320 / 768;
		kittyBasketLeft.setLayoutParams(lpKitty);

		// Button params initialization
		lpDownLeftBtn = new LayoutParams(bgWidth * 250 / 1280,
				bgHeight * 220 / 768);
		lpDownLeftBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		// lpDownLeftBtn.leftMargin = bgWidth * 10 / 1280;
		lpDownLeftBtn.topMargin = bgHeight * 350 / 768;

		lpTopLeftBtn = new LayoutParams(bgWidth * 250 / 1280,
				bgHeight * 220 / 768);
		lpTopLeftBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		// lpTopLeftBtn.leftMargin = bgWidth * 10 / 1280;
		lpTopLeftBtn.topMargin = bgHeight * 70 / 768;

		lpDownRightBtn = new LayoutParams(bgWidth * 250 / 1280,
				bgHeight * 220 / 768);
		lpDownRightBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// lpDownRightBtn.rightMargin = bgWidth * 10 / 1280;
		lpDownRightBtn.topMargin = bgHeight * 350 / 768;

		lpTopRightBtn = new LayoutParams(bgWidth * 250 / 1280,
				bgHeight * 220 / 768);
		lpTopRightBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// lpTopRightBtn.rightMargin = bgWidth * 10 / 1280;
		lpTopRightBtn.topMargin = bgHeight * 70 / 768;

		lpPause = new LayoutParams(bgWidth * 65 / 1280, bgHeight * 65 / 768);
		lpPlay = new LayoutParams(bgWidth * 112 / 1280, bgHeight * 112 / 768);
		lpReplay = new LayoutParams(bgWidth * 112 / 1280, bgHeight * 112 / 768);

		lpKittyLife1 = new LayoutParams(bgWidth * 106 / 1280,
				bgHeight * 82 / 768);
		lpKittyLife1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpKittyLife1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpKittyLife2 = new LayoutParams(bgWidth * 106 / 1280,
				bgHeight * 82 / 768);
		lpKittyLife2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpKittyLife2.addRule(RelativeLayout.LEFT_OF, kittyLife1.getId());
		lpKittyLife3 = new LayoutParams(bgWidth * 106 / 1280,
				bgHeight * 82 / 768);
		lpKittyLife3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpKittyLife3.addRule(RelativeLayout.LEFT_OF, kittyLife2.getId());

		lpHearthSchore = new LayoutParams(bgWidth * 68 / 1280,
				bgHeight * 64 / 768);
		lpHearthSchore.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpHearthSchore.addRule(RelativeLayout.CENTER_IN_PARENT);

		lpPause = new LayoutParams(bgWidth * 65 / 1280, bgHeight * 65 / 768);
		lpPause.leftMargin = bgWidth * 10 / 1280;
		lpPause.topMargin = bgHeight * 10 / 768;

		lpPlay = new LayoutParams(bgWidth * 80 / 1280, bgHeight * 80 / 768);
		lpPlay.leftMargin = bgWidth * 10 / 1280;
		lpPlay.topMargin = bgHeight * 10 / 768;

		lpReplay = new LayoutParams(bgWidth * 80 / 1280, bgHeight * 80 / 768);
		lpReplay.addRule(RelativeLayout.RIGHT_OF, continuePlayButton.getId());
		lpReplay.leftMargin = bgWidth * 10 / 1280;
		lpReplay.topMargin = bgHeight * 10 / 768;

		// Set Buttons Layout params
		topLeftButton.setLayoutParams(lpTopLeftBtn);
		downLeftButton.setLayoutParams(lpDownLeftBtn);
		topRightButton.setLayoutParams(lpTopRightBtn);
		downRightButton.setLayoutParams(lpDownRightBtn);

		kittyLife1.setLayoutParams(lpKittyLife1);
		kittyLife2.setLayoutParams(lpKittyLife2);
		kittyLife3.setLayoutParams(lpKittyLife3);
		hearthScoreImg.setLayoutParams(lpHearthSchore);
		pauseButton.setLayoutParams(lpPause);
		continuePlayButton.setLayoutParams(lpPlay);
		replayButton.setLayoutParams(lpReplay);

		myHandler = new Handler();

		runOnUiThread(timerRun);
		
		AppSettings.prefs = getSharedPreferences(AppSettings.settingsPref,MODE_PRIVATE);

		createAdLayout();
	}

	@Override
	protected void onResume() {
		playMusic();
		if (showAlertDialog) {
			alertDialog.show();
		}
		AdMobAdsRequest();
		super.onResume();

	}

	public void setKittyParams() {
		lpKitty.width = bgWidth * 246 / 1280;
		lpKitty.height = bgHeight * 264 / 768;
	}

	public void pauseButtonLayout() {
		pauseButton = (ImageButton) findViewById(R.id.pause_img);
		layoutPause = (RelativeLayout) findViewById(R.id.pause_layout);
		continuePlayButton = (ImageButton) findViewById(R.id.play_img);
		replayButton = (ImageButton) findViewById(R.id.replay_img);

		pauseButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!isVisible) {
					layoutPause.setVisibility(View.VISIBLE);
					layoutPause
							.setAnimation(new TranslateAnimation(0, 0, 0, 10));

					isVisible = true;
				}
				// else {
				// layoutPause.setAnimation(new TranslateAnimation(0, 0, 0,
				// -10));
				// layoutPause.setVisibility(View.GONE);
				// isVisible = false;
				// for (int i = 0; i < figuresArray.size(); i++) {
				// figuresArray.get(i).continueRun();
				//
				// }
				// runOnUiThread(timerRun);
				// }
				for (int i = 0; i < figuresArray.size(); i++) {
					figuresArray.get(i).pause();

				}

				myHandler.removeCallbacks(timerRun);
				if (gameSong != null && gameSong.isPlaying()) {
					gameSong.pause();
				}
				pauseButton.setVisibility(View.INVISIBLE);
			}

		});

		continuePlayButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				layoutPause.setAnimation(new TranslateAnimation(0, 0, 0, -10));
				layoutPause.setVisibility(View.INVISIBLE);
				isVisible = false;

				for (int i = 0; i < figuresArray.size(); i++) {
					figuresArray.get(i).continueRun();

				}
				runOnUiThread(timerRun);
				if (gameSong != null && !gameSong.isPlaying()) {
					gameSong.start();
				}
				pauseButton.setVisibility(View.VISIBLE);
			}
		});

		replayButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				closeAllThreads();

				Intent intent = new Intent(HeartCollect.this,
						HeartCollect.class);
				startActivity(intent);
				finish();
			}
		});

	}

	public void playTapSound() {
		if (tapSound == null && MainMenu.effectsISOn) {
			tapSound = MediaPlayer.create(getBaseContext(), R.raw.tap_sound);
		}
		if (tapSound != null && MainMenu.effectsISOn) {
			tapSound.start();
		}

	}

	public void playMusic() {
		if (MainMenu.soundIsOn) {
			gameSong.setLooping(true);
			gameSong.start();
		}
	}

	private void setListeners() {
		this.topLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 1;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_left);

				setKittyParams();
				lpKitty.topMargin = bgHeight / 8 + bgHeight / 76;
				lpKitty.leftMargin = bgWidth / 4;
				kittyBasketLeft.setLayoutParams(lpKitty);
				playTapSound();
			}
		});

		this.topRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 2;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_right);

				setKittyParams();
				lpKitty.topMargin = bgHeight / 8 + bgHeight / 76;
				lpKitty.leftMargin = 3 * bgWidth / 4
						- kittyBasketLeft.getWidth();
				kittyBasketLeft.setLayoutParams(lpKitty);
				playTapSound();
			}
		});

		this.downLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 3;

				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_left);

				setKittyParams();
				lpKitty.topMargin = 3 * bgHeight / 8 + bgHeight / 15;
				lpKitty.leftMargin = bgWidth / 4;
				kittyBasketLeft.setLayoutParams(lpKitty);
				playTapSound();
			}
		});

		this.downRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 4;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_right);

				setKittyParams();
				lpKitty.topMargin = 3 * bgHeight / 8 + bgHeight / 15;
				lpKitty.leftMargin = 3 * bgWidth / 4
						- kittyBasketLeft.getWidth();
				kittyBasketLeft.setLayoutParams(lpKitty);
				playTapSound();
			}
		});

	}

	@SuppressLint("DrawAllocation")
	public class GameView extends View {

		int height;
		int width;

		PathMeasure pm;
		ArrayList<Point> aCoordinates;

		Point currentPoint;
		Point topLeftPointStart;
		Point topRightPointStart;
		Point downLeftPointStart;
		Point downRightPointStart;
		Point topLeftPointTurn;
		Point topRightPointTurn;
		Point downLeftPointTurn;
		Point downRightPointTurn;
		Point topLeftPointEnd;
		Point topRightPointEnd;
		Point downLeftPointEnd;
		Point downRightPointEnd;
		Point fallDawnPoint1;
		Point fallDawnPoint2;

		int k = 0;

		public GameView(final Context context, final int height, final int width) {
			super(context);
			setFocusable(true);
			figuresArray = new ArrayList<GameFigure>();
			topLeftPointStart = new Point(width / 20 - width / 36, height / 6);
			topLeftPointTurn = new Point(width / 20 - width / 36 + 50 * bgWidth
					/ 1280, height / 6);
			topLeftPointEnd = new Point(width / 10 - width / 36 + 200 * bgWidth
					/ 1280, height / 6 + 110 * bgHeight / 768);
			topRightPointStart = new Point(19 * width / 20, height / 6);
			topRightPointTurn = new Point(
					19 * width / 20 - 50 * bgWidth / 1280, height / 6);
			topRightPointEnd = new Point(9 * width / 10 - 200 * bgWidth / 1280,
					height / 6 + 110 * bgHeight / 768);
			downLeftPointStart = new Point(width / 20 - width / 36, height / 2);
			downLeftPointTurn = new Point(width / 20 - width / 36 + 50
					* bgWidth / 1280, height / 2);
			downLeftPointEnd = new Point(width / 10 - width / 36 + 200
					* bgWidth / 1280, height / 2 + 110 * bgHeight / 768);
			downRightPointStart = new Point(19 * width / 20, height / 2);
			downRightPointTurn = new Point(19 * width / 20 - 50*bgWidth/1280, height / 2);
			downRightPointEnd = new Point(
					9 * width / 10 - 200 * bgWidth / 1280, height / 2 + 110
							* bgHeight / 768);
			fallDawnPoint1 = new Point(width / 10 - width / 36 + 200 * bgWidth
					/ 1280, height - 80 * bgHeight / 768);
			fallDawnPoint2 = new Point(9 * width / 10 - 200 * bgWidth / 1280,
					height - 80 * bgHeight / 768);
			this.height = height;
			this.width = width;

			timePeriod = 5000;
			timerRun = new Runnable() {

				public void run() {

					// if (dropCount == 4) {
					//
					// for (int i = 0; i < figuresArray.size(); i++) {
					// figuresArray.get(i).stop();
					//
					// }
					// myHandler.removeCallbacks(timerRun);
					// showWinDialog();
					//
					// } else {
					switch (timePeriod) {
					case 5000:
						if (cutchImages >= 20) {
							timePeriod -= 800;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;

					case 4200:
						if (cutchImages >= 50) {
							timePeriod -= 800;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 3400:
						if (cutchImages >= 75) {
							timePeriod -= 400;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 3000:
						if (cutchImages >= 100) {
							timePeriod -= 400;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 2600:
						if (cutchImages >= 130) {
							timePeriod -= 400;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 2200:
						if (cutchImages >= 150) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 2000:
						if (cutchImages >= 170) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 1800:
						if (cutchImages >= 200) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;

					case 1600:
						if (cutchImages >= 230) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 1400:
						if (cutchImages >= 300) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 1200:
						if (cutchImages >= 400) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					case 1000:
						if (cutchImages >= 500) {
							timePeriod -= 200;
							if (MainMenu.effectsISOn) {
								wonScoreSound.start();
							}
						}
						break;
					default:
						break;

					}
					int randomPoint = 1 + (int) (Math.random() * 4);
					int randomHearts = 1 + (int) (Math.random() * 15);
					switch (randomHearts) {

					case 1:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_1,
								coordinatesChooser(randomPoint), bgWidth / 25,
								bgHeight / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;

					case 2:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_2,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 3:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_3,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 4:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_4,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 5:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_5,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 6:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_6,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 7:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_7,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 8:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_8,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 9:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_9,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 10:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_10,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 11:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_11,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 12:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_12,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 0));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 13:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_13,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 1));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 14:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_14,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 2));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					case 15:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_15,
								coordinatesChooser(randomPoint), width / 25,
								height / 18, randomPoint, 3));
						figuresArray.get(i).start();
						figuresArray.get(i).setStartSpeed(randomPoint);

						break;
					}

					i++;
					myHandler.postDelayed(timerRun, timePeriod);
				}

				// }
			};

		}

		private Point coordinatesChooser(int randomPoint) {
			Point p = new Point();
			if (randomPoint == 1) {
				p = topLeftPointStart;
			}
			if (randomPoint == 2) {
				p = topRightPointStart;
			}
			if (randomPoint == 3) {
				p = downLeftPointStart;
			}
			if (randomPoint == 4) {
				p = downRightPointStart;
			}
			return p;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (figuresArray.size() > 0) {
				for (int i = 0; i < figuresArray.size(); i++) {
					canvas.drawBitmap(figuresArray.get(i).getBitmap(),
							figuresArray.get(i).getX(), figuresArray.get(i)
									.getY(), null);
					// figuresArray.get(i).fallDownFromPoint1();
				}
			}

			countTextView.setText("- " + cutchImages + " -");
			super.onDraw(canvas);
		}

		public class GameFigure implements Runnable {
			private Bitmap img;
			private int coordX = 0;
			private int coordY = 0;
			protected int xS = 0;
			protected int yS = 0;
			private int lastSpeedX;
			private int lastSpeedY;
			private int id;
			private int count = 0;
			protected boolean isRunning = false;

			private int imgWidth;
			private int imgHeight;
			private boolean isDrawable;
			Canvas myCanvas = null;
			Thread t = null;
			// protected boolean isRunning = false;
			protected boolean isPause = false;

			int startpoint;
			int type;

			public GameFigure(Context context, int drawable, Point point,
					int height, int width, int startpoint, int type) {

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				img = BitmapFactory.decodeResource(context.getResources(),
						drawable);
				img = Bitmap.createScaledBitmap(img, height, width, true);
				imgWidth = img.getWidth();
				imgHeight = img.getHeight();
				isDrawable = true;
				id = count;
				count++;
				coordX = point.x;
				coordY = point.y;
				this.startpoint = startpoint;
				this.type = type;
			}

			public int getCount() {
				return count;
			}

			void setX(int newValue) {
				coordX = newValue;
			}

			public int getX() {
				return coordX;
			}

			void setY(int newValue) {
				coordY = newValue;
			}

			public int getY() {
				return coordY;
			}

			public int getID() {
				return id;
			}

			public Bitmap getBitmap() {
				return img;
			}

			public int getWidth() {
				return imgWidth;
			}

			public void setWidth(int newWidth) {
				imgWidth = newWidth;
			}

			public int getHeight() {
				return imgHeight;
			}

			public void setSpeedX(int xS) {
				this.xS = xS;
			}

			public void setSpeedY(int yS) {
				this.yS = yS;
			}

			public int getSpeedX() {
				return xS;
			}

			public int getSpeedY() {
				return yS;
			}

			public boolean getIsDrawable() {
				return isDrawable;
			}

			public void setIsDragable(boolean value) {
				isDrawable = value;
			}

			public void move(int dX, int dY) {
				this.coordX += dX;
				this.coordY += dY;
			}

			private void setStartSpeed(int randomPoint) {
				if (randomPoint == 2 || randomPoint == 4) {
					this.setSpeedX(-2);
					this.setSpeedY(0);
				} else {
					this.setSpeedX(2);
					this.setSpeedY(0);
				}
			}

			private void setTurnSpeed(int randomPoint) {
				switch (type) {
				case 0:
					if (randomPoint == 2 || randomPoint == 4) {
						this.setSpeedX(-4);
						this.setSpeedY(2);
					} else {
						this.setSpeedX(4);
						this.setSpeedY(2);
					}
					break;
				case 1:
					if (randomPoint == 2 || randomPoint == 4) {
						this.setSpeedX(-4);
						this.setSpeedY(2);
					} else {
						this.setSpeedX(4);
						this.setSpeedY(2);
					}
					break;
				case 2:
					if (randomPoint == 2 || randomPoint == 4) {
						this.setSpeedX(-6);
						this.setSpeedY(3);
					} else {
						this.setSpeedX(6);
						this.setSpeedY(3);
					}
					break;

				case 3:
					if (randomPoint == 2 || randomPoint == 4) {
						this.setSpeedX(-8);
						this.setSpeedY(4);
					} else {
						this.setSpeedX(8);
						this.setSpeedY(4);
					}
					break;
				}

			}

			private boolean fallDownFromPoint() {
				boolean isFallDawn = false;
				switch (startpoint) {
				case 1:
					if (coordX >= topLeftPointTurn.x) {
						setTurnSpeed(1);
					}
					if (coordX >= topLeftPointEnd.x) {

						if (kittyBasketPosition == 1
								&& Math.abs(coordY - topLeftPointEnd.y) <= 10) {
							if (MainMenu.effectsISOn) {
								catchSound.start();
							}
							figuresArray.remove(this);
							this.stop();
							i--;
							switch (type) {
							case 0:
								cutchImages++;
								break;
							case 1:
								cutchImages += 3;
								break;
							case 2:
								cutchImages += 5;
								break;
							case 3:
								cutchImages += 10;
								break;
							}

						} else {

							this.setSpeedX(0);
							this.setSpeedY(15);

							if (coordY > fallDawnPoint1.y) {
								figuresArray.remove(this);
								if (MainMenu.effectsISOn) {
									breakSound.start();
								}

								this.stop();
								i--;

								lifeCount();
							}
							isFallDawn = true;

						}
					}

					break;
				case 2:
					if (coordX <= topRightPointTurn.x) {
						setTurnSpeed(2);
					}
					if (coordX <= topRightPointEnd.x) {
						if (kittyBasketPosition == 2
								&& Math.abs(coordY - topRightPointEnd.y) <= 10) {
							if (MainMenu.effectsISOn) {
								catchSound.start();
							}
							figuresArray.remove(this);
							this.stop();
							i--;

							switch (type) {
							case 0:
								cutchImages++;
								break;
							case 1:
								cutchImages += 3;
								break;
							case 2:
								cutchImages += 5;
								break;
							case 3:
								cutchImages += 10;
								break;
							}

						} else {

							this.setSpeedX(0);
							this.setSpeedY(15);
							if (coordY > fallDawnPoint2.y) {
								figuresArray.remove(this);
								if (MainMenu.effectsISOn) {
									breakSound.start();
								}
								this.stop();
								i--;
								lifeCount();

							}
							isFallDawn = true;
						}
					}

					break;
				case 3:
					if (coordX >= downLeftPointTurn.x) {
						setTurnSpeed(3);
					}
					if (coordX >= downLeftPointEnd.x) {
						if (kittyBasketPosition == 3
								&& Math.abs(coordY - downLeftPointEnd.y) <= 10) {
							if (MainMenu.effectsISOn) {
								catchSound.start();
							}
							figuresArray.remove(this);
							this.stop();
							i--;

							switch (type) {
							case 0:
								cutchImages++;
								break;
							case 1:
								cutchImages += 3;
								break;
							case 2:
								cutchImages += 5;
								break;
							case 3:
								cutchImages += 10;
								break;
							}

						} else {

							this.setSpeedX(0);
							this.setSpeedY(15);
							if (coordY > fallDawnPoint1.y) {
								figuresArray.remove(this);
								if (MainMenu.effectsISOn) {
									breakSound.start();
								}
								this.stop();
								i--;

								lifeCount();

							}
							isFallDawn = true;
						}
					}
					break;
				case 4:
					if (coordX <= downRightPointTurn.x) {
						setTurnSpeed(4);
					}
					if (coordX <= downRightPointEnd.x) {
						if (kittyBasketPosition == 4
								&& Math.abs(coordY - downRightPointEnd.y) <= 10) {
							if (MainMenu.effectsISOn) {
								catchSound.start();
							}

							figuresArray.remove(this);
							this.stop();
							i--;
							switch (type) {
							case 0:
								cutchImages++;
								break;
							case 1:
								cutchImages += 3;
								break;
							case 2:
								cutchImages += 5;
								break;
							case 3:
								cutchImages += 10;
								break;
							}

						} else {

							this.setSpeedX(0);
							this.setSpeedY(15);

							if (coordY > fallDawnPoint2.y) {
								figuresArray.remove(this);
								if (MainMenu.effectsISOn) {
									breakSound.start();
								}
								this.stop();
								i--;

								lifeCount();

							}
							isFallDawn = true;
						}
					}
					break;

				}
				return isFallDawn;

			}

			public void lifeCount() {
				if (type == 0) {
					dropCount++;
					if (dropCount == 1) {
						HeartCollect.this.runOnUiThread(new Runnable() {

							public void run() {
								kittyLife3
										.setBackgroundResource(R.drawable.life_icon_gone);
							}
						});

					}
					if (dropCount == 2) {
						HeartCollect.this.runOnUiThread(new Runnable() {

							public void run() {
								kittyLife2
										.setBackgroundResource(R.drawable.life_icon_gone);
							}
						});
					}
					if (dropCount == 3) {
						HeartCollect.this.runOnUiThread(new Runnable() {

							public void run() {
								kittyLife1
										.setBackgroundResource(R.drawable.life_icon_gone);
							}
						});
					}
					if (dropCount == 4) {
						if (MainMenu.effectsISOn) {
							gameOverSound.start();
						}
						closeAllThreads();
						HeartCollect.this.runOnUiThread(new Runnable() {

							public void run() {
								showWinDialog();
							}
						});

					}
				}

			}

			public void run() {
				while (isRunning) {
					try {

						fallDownFromPoint();

						move(xS, yS);
						postInvalidate();
						Thread.sleep(100);
						while (isPause) {
							synchronized (this) {
								wait();
							}
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}

			}

			public void start() {
				if (t != null) {
					try {
						stop();
						t.join();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

				}
				t = new Thread(this);

				isRunning = true;
				// setSpeedX(5);
				// setSpeedY(5);
				t.start();

			}

			public synchronized void continueRun() {
				isPause = false;

				setSpeedX(lastSpeedX);
				setSpeedY(lastSpeedY);
				notify();

			}

			public void stop() {
				continueRun();
				isRunning = false;
			}

			public void pause() {
				isPause = true;
				lastSpeedX = this.getSpeedX();
				lastSpeedY = this.getSpeedY();
				setSpeedX(0);
				setSpeedY(0);

			}

		}

	}

	public void showWinDialog() {
		alertDialog = new Dialog(this);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		alertDialog.setContentView(R.layout.dialog);
		alertDialog.setCancelable(false);

		WindowManager.LayoutParams dialogParams = new WindowManager.LayoutParams();

		dialogParams.copyFrom(alertDialog.getWindow().getAttributes());
		dialogParams.width = bgWidth;
		dialogParams.height = bgHeight;

		score = AppSettings.prefs.getInt("Score", 0);
		nameEditText = (EditText) alertDialog.findViewById(R.id.editName);
		okButton = (ImageButton) alertDialog.findViewById(R.id.ok_btn);
		if (score < cutchImages) {

			nameEditText.setVisibility(View.VISIBLE);
			okButton.setVisibility(View.VISIBLE);
		}

		Button replayButton = (Button) alertDialog
				.findViewById(R.id.replay_btn);
		Button exitButton = (Button) alertDialog.findViewById(R.id.exit_btn);

		replayButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showAlertDialog = false;
				alertDialog.cancel();
				Intent intent = new Intent(HeartCollect.this,
						HeartCollect.class);
				startActivity(intent);
				finish();
			}
		});

		exitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showAlertDialog = false;
				alertDialog.cancel();

				finish();
			}
		});

		okButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String name = nameEditText.getText().toString();
				int sl = name.length();
				if (sl >= 10) {
					Toast.makeText(HeartCollect.this,
							"Username too long", Toast.LENGTH_SHORT).show();
				} else if (score < cutchImages && !name.equals("")) {

					SharedPreferences.Editor editor = AppSettings.prefs.edit();
					editor.putString("Player Name", name);
					editor.putInt("Score", cutchImages);
					editor.commit();
					okButton.setVisibility(View.GONE);
					nameEditText.setVisibility(View.GONE);
				}
			}
		});

		// lpOKButton = new LayoutParams(bgWidth * 160 / 1280, bgHeight * 80 /
		// 768);
		// lpOKButton.addRule(RelativeLayout.BELOW, continuePlayButton.getId());
		// lpOKButton.addRule(RelativeLayout.RIGHT_OF, nameEditText.getId());

		// okButton.setLayoutParams(lpOKButton);

		alertDialog.show();
		showAlertDialog = true;
		alertDialog.getWindow().setAttributes(dialogParams);
	}

	public void closeAllThreads() {
		for (int i = 0; i < figuresArray.size(); i++) {
			figuresArray.get(i).stop();
		}
		figuresArray.removeAll(figuresArray);
		myHandler.removeCallbacks(timerRun);
	}

	private AdView adView;

	public void createAdLayout() {
		adView = new AdView(this, AdSize.BANNER, AppSettings.MY_AD_UNIT_ID);

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"

		LayoutParams adMobLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adMobLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adMobLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(adMobLayoutParams);

		// Add the adView to it
		collecthearts_rl.addView(adView);
	}

	public void AdMobAdsRequest() {

		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());
	}
	
	public void pauseGame() {
		if (!isVisible) {
			layoutPause.setVisibility(View.VISIBLE);
			layoutPause
					.setAnimation(new TranslateAnimation(0, 0, 0, 10));

			isVisible = true;
		}
		// else {
		// layoutPause.setAnimation(new TranslateAnimation(0, 0, 0,
		// -10));
		// layoutPause.setVisibility(View.GONE);
		// isVisible = false;
		// for (int i = 0; i < figuresArray.size(); i++) {
		// figuresArray.get(i).continueRun();
		//
		// }
		// runOnUiThread(timerRun);
		// }
		for (int i = 0; i < figuresArray.size(); i++) {
			figuresArray.get(i).pause();

		}

		myHandler.removeCallbacks(timerRun);
		if (gameSong != null && gameSong.isPlaying()) {
			gameSong.pause();
		}
		pauseButton.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		if (onBackPressedCount == 0) {
			if (gameSong.isPlaying()) {

				gameSong.release();
				gameSong = null;
			}
			closeAllThreads();
			finish();

			onBackPressedCount++;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		if (gameSong != null && gameSong.isPlaying()) {
			gameSong.pause();
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		pauseGame();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (gameSong != null) {
			gameSong.release();
			gameSong = null;
		}
		if (tapSound != null) {
			tapSound.release();
			tapSound = null;
		}
		pauseGame();
		super.onDestroy();
	}

}
