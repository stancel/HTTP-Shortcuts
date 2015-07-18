package ch.rmy.android.http_shortcuts.shortcuts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShortcutStorage {

	private DBCreator dbHelper;

	public ShortcutStorage(Context context) {
		dbHelper = new DBCreator(context);
	}

	public List<Shortcut> getShortcuts() {
		SQLiteDatabase database = null;
		Cursor cursor = null;

		try {
			database = dbHelper.getReadableDatabase();

			List<Shortcut> shortcuts = new ArrayList<Shortcut>();

			cursor = database.query(ShortcutTable.TABLE_NAME, null, null, null, null, null, ShortcutTable.COLUMN_POSITION + " ASC");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				shortcuts.add(new Shortcut(cursor));
				cursor.moveToNext();
			}
			return shortcuts;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	public Shortcut createShortcut() {
		return new Shortcut(0, "", "", Shortcut.PROTOCOL_HTTP, "", Shortcut.METHOD_GET, "", "", null, Shortcut.FEEDBACK_SIMPLE, 0);
	}

	public long storeShortcut(Shortcut shortcut) {
		long shortcutID;
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(ShortcutTable.COLUMN_NAME, shortcut.getName());
			values.put(ShortcutTable.COLUMN_PROTOCOL, shortcut.getProtocol());
			values.put(ShortcutTable.COLUMN_URL, shortcut.getURL());
			values.put(ShortcutTable.COLUMN_METHOD, shortcut.getMethod());
			values.put(ShortcutTable.COLUMN_USERNAME, shortcut.getUsername());
			values.put(ShortcutTable.COLUMN_PASSWORD, shortcut.getPassword());
			values.put(ShortcutTable.COLUMN_FEEDBACK, shortcut.getFeedback());
			values.put(ShortcutTable.COLUMN_DESCRIPTION, shortcut.getDescription());

			String iconName = null;
			if (shortcut.getIconName() != null) {
				iconName = shortcut.getIconName().toString();
			}
			values.put(ShortcutTable.COLUMN_ICON, iconName);

			if (shortcut.isNew()) {
				values.put(ShortcutTable.COLUMN_POSITION, getMaxPosition(database) + 1);
				shortcutID = database.insert(ShortcutTable.TABLE_NAME, null, values);
			} else {
				if (shortcut.getPosition() > 0 && shortcut.getPosition() <= getMaxPosition(database)) {
					database.execSQL("update " + ShortcutTable.TABLE_NAME + " SET " + ShortcutTable.COLUMN_POSITION + " = " + ShortcutTable.COLUMN_POSITION + "+1 where "
							+ ShortcutTable.COLUMN_POSITION + " < (select position from " + ShortcutTable.TABLE_NAME + " where " + ShortcutTable.COLUMN_ID + " = "
							+ shortcut.getID() + ") AND " + ShortcutTable.COLUMN_POSITION + " >= " + shortcut.getPosition() + ";");

					database.execSQL("update " + ShortcutTable.TABLE_NAME + " SET " + ShortcutTable.COLUMN_POSITION + " = " + ShortcutTable.COLUMN_POSITION + "-1 where "
							+ ShortcutTable.COLUMN_POSITION + " > (select position from " + ShortcutTable.TABLE_NAME + " where " + ShortcutTable.COLUMN_ID + " = "
							+ shortcut.getID() + ") AND " + ShortcutTable.COLUMN_POSITION + " <= " + shortcut.getPosition() + ";");

					values.put(ShortcutTable.COLUMN_POSITION, shortcut.getPosition());
				}
				database.update(ShortcutTable.TABLE_NAME, values, ShortcutTable.COLUMN_ID + " = ?", new String[] { Long.toString(shortcut.getID()) });
				shortcutID = shortcut.getID();
			}
		} finally {
			if (database != null) {
				database.close();
			}
		}

		return shortcutID;
	}

	public void deleteShortcut(Shortcut shortcut) {
		SQLiteDatabase database = null;
		try {
			database = dbHelper.getWritableDatabase();
			database.delete(ShortcutTable.TABLE_NAME, ShortcutTable.COLUMN_ID + " = ?", new String[] { Long.toString(shortcut.getID()) });
			database.execSQL("update " + ShortcutTable.TABLE_NAME + " SET " + ShortcutTable.COLUMN_POSITION + " = " + ShortcutTable.COLUMN_POSITION + "-1 where "
					+ ShortcutTable.COLUMN_POSITION + " > " + shortcut.getPosition() + ";");
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	public Shortcut getShortcutByID(long shortcutID) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dbHelper.getReadableDatabase();

			cursor = database.query(ShortcutTable.TABLE_NAME, null, ShortcutTable.COLUMN_ID + " = ?", new String[] { Long.toString(shortcutID) }, null, null,
					ShortcutTable.COLUMN_NAME + " ASC");
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				return new Shortcut(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}

		return null;
	}

	public Map<String, String> getPostParametersByID(long shortcutID) {
		SQLiteDatabase database = null;
		Cursor cursor = null;

		try {
			database = dbHelper.getReadableDatabase();

			Map<String, String> parameters = new HashMap<String, String>();

			cursor = database.query(PostParameterTable.TABLE_NAME, null, PostParameterTable.COLUMN_SHORTCUT_ID + " = ?", new String[] { Long.toString(shortcutID) }, null, null,
					null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				parameters.put(cursor.getString(2), cursor.getString(3));
				cursor.moveToNext();
			}
			return parameters;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	private int getMaxPosition(SQLiteDatabase database) {
		Cursor cursor = null;

		try {
			database = dbHelper.getReadableDatabase();
			cursor = database.query(ShortcutTable.TABLE_NAME, new String[] { ShortcutTable.COLUMN_POSITION }, null, null, null, null, ShortcutTable.COLUMN_POSITION + " DESC");

			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				return cursor.getInt(0);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return 0;
	}

}
