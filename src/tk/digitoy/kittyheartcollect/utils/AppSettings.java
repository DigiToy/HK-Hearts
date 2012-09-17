package tk.digitoy.kittyheartcollect.utils;

import android.content.SharedPreferences;
import android.graphics.Typeface;

public class AppSettings {

	// AdMob ID
	public static final String MY_AD_UNIT_ID = "a15040b767dfeee";

	// Folder name
	public static final String folderName = "/Hello Kitty Hearts/";
	public static final String fileName = "HKH_wallpaper_";

	// Prefs
	public static SharedPreferences prefs;
	public static final String settingsPref = "Settings";

	// Unlock scores
	public static final int[] lockscore = { 50, 100, 150, 200, 250, 300, 400,
			600, 800, 1000 };

	// TypeFace
	public static Typeface newFont;
}
