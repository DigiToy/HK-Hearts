package tk.digitoy.kittyheartcollect.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

public class ScoreModel extends BaseModel {

	public final static String TABLE_NAME = "Score";
	public static final String KEY_VALUE = "value";
	public static final String KEY_NAME = "name";

	public ScoreModel(final Context context) {
		super(context, TABLE_NAME);
	}

	public Cursor getAllOrdered() {
		return database.query(TABLE_NAME, null, null, null, null, null,
				KEY_VALUE + " DESC");
	}

	public List<User> getAllUsers() {

		List<User> result = new ArrayList<User>();

		Cursor cursor = getAllOrdered();

		int limit = cursor.getCount() <= 5 ? cursor.getCount() : 5;

		for (int i = 0; i < limit; i++) {
			if (!cursor.moveToPosition(i)) {
				break;
			}
			int position = i + 1;
			int value = cursor.getInt(cursor
					.getColumnIndex(ScoreModel.KEY_VALUE));
			String name = cursor.getString(cursor
					.getColumnIndex(ScoreModel.KEY_NAME));
			result.add(new User(name, value, position));
		}

		return result;
	}

	public long getBestScore() {
		long result = 0;
		Cursor cursor = getAllOrdered();
		result = cursor.getLong(cursor.getColumnIndex(ScoreModel.KEY_VALUE));
		return result;
	}

	public void add(final String name, final int points) {
		final ContentValues values = new ContentValues();
		values.put(KEY_VALUE, points);
		values.put(KEY_NAME, name);

		database.insert(TABLE_NAME, null, values);
	}

	@Override
	public void remove(final int id) {
		database.delete(TABLE_NAME, BaseColumns._ID + " = " + id, null);
	}
}