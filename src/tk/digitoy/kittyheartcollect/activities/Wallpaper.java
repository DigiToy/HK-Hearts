package tk.digitoy.kittyheartcollect.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tk.digitoy.kittyheartcollect.utils.AppSettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Wallpaper extends Activity implements Runnable {

	private ProgressDialog pDialog;
	// Button Images
	private ImageButton buttonSet;
	private ImageButton buttonSave;

	// generate bitmap
	private int resID;
	private String imagePath;

	private int[] walls;

	int wallNumber;

	int saveOrSet;

	// decide wall size
	private String wSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_view);

		walls = new int[] { R.drawable.wt_01, R.drawable.wt_02,
				R.drawable.wt_03, R.drawable.wt_04, R.drawable.wt_05,
				R.drawable.wt_06, R.drawable.wt_07, R.drawable.wt_08,
				R.drawable.wt_09, R.drawable.wt_10 };

		wallNumber = getIntent().getIntExtra("wallNumber", 1000);
		resID = walls[wallNumber];

		if (MainMenu.dispHeight == 240) {
			wSize = "480x320";
		} else if (MainMenu.dispHeight == 320) {
			wSize = "640x480";
		} else if (MainMenu.dispHeight == 480) {
			wSize = "960x800";
		} else {
			wSize = "1080x960";
		}

		imagePath = wSize + "/w_" + (wallNumber < 9 ? "0" : "")
				+ (wallNumber + 1) + ".jpg";
		drawStaticLayout();
	}

	// Called when the activity returning to foreground.

	// Called when the activity returning to foreground.
	private void playMusic() {
		if (MainMenu.soundIsOn) {
			MainMenu.menuSong.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListeners();
		playMusic();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		if (MainMenu.menuSong != null && MainMenu.menuSong.isPlaying()) {
			MainMenu.menuSong.pause();
		}
		super.onPause();
	}

	// Activating click listeners
	private void setListeners() {
		// Set Wallpaper
		buttonSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setWallpaper();
			}
		});

		// Set Wallpaper
		buttonSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveWallpaper();
			}
		});

	}

	// Creating and adding views
	private void drawStaticLayout() {

		// Handling from XML
		buttonSet = (ImageButton) findViewById(R.id.buttonSetWall);
		buttonSave = (ImageButton) findViewById(R.id.buttonSaveWall);

		ImageView img = (ImageView) findViewById(R.id.wallPaper);
		img.setImageResource(resID);
	}

	public void setWallpaper() {
		saveOrSet = 0;
		pDialog = ProgressDialog.show(this, "", "", true, false);
		pDialog.setContentView(R.layout.wait_dialog);
		TextView wait = (TextView) pDialog.findViewById(R.id.wait);
		wait.setText(R.string.please_wait);
		Thread thread = new Thread(this);
		thread.start();
	}

	private void saveWallpaper() {
		saveOrSet = 1;
		pDialog = ProgressDialog.show(this, "", "", true, false);
		pDialog.setContentView(R.layout.wait_dialog);
		TextView wait = (TextView) pDialog.findViewById(R.id.wait);
		wait.setText(R.string.please_wait);
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		if (saveOrSet == 1) {
			String newFileName = Environment.getExternalStorageDirectory()
					+ AppSettings.folderName+AppSettings.fileName
					+ (wallNumber + 1) + ".jpg";

			AssetManager assetManager = this.getAssets();

			InputStream in = null;
			OutputStream out = null;
			try {
				new File(Environment.getExternalStorageDirectory()
						+ AppSettings.fileName).mkdirs();

				in = assetManager.open(imagePath);

				out = new FileOutputStream(newFileName);

				byte[] buffer = new byte[1024];
				int read;
				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse("file://"
								+ Environment.getExternalStorageDirectory())));
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								Wallpaper.this,
								getResources().getString(
										R.string.wallpaper_saved),
								Toast.LENGTH_LONG).show();
					}
				});
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			} finally {
				pDialog.dismiss();
			}
		} else {
			WallpaperManager wm = WallpaperManager.getInstance(Wallpaper.this);
			try {
				wm.setBitmap(BitmapFactory.decodeStream(getAssets().open(
						imagePath)));
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								Wallpaper.this,
								getResources().getString(
										R.string.wallpaper_changed),
								Toast.LENGTH_LONG).show();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pDialog.dismiss();
			}
		}
	}
}
