package ch.rmy.android.http_shortcuts.shortcuts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import ch.rmy.android.http_shortcuts.R;

public class Shortcut {

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";

	public static final String PROTOCOL_HTTP = "http";
	public static final String PROTOCOL_HTTPS = "https";

	public static final int FEEDBACK_NONE = 0;
	public static final int FEEDBACK_ERRORS_ONLY = 1;
	public static final int FEEDBACK_SIMPLE = 2;
	public static final int FEEDBACK_FULL_RESPONSE = 3;

	public static final int DEFAULT_ICON = R.drawable.ic_launcher;

	public static final String[] METHODS = { METHOD_GET, METHOD_POST };
	public static final String[] PROTOCOLS = { PROTOCOL_HTTP, PROTOCOL_HTTPS };
	public static final int[] FEEDBACKS = { FEEDBACK_NONE, FEEDBACK_ERRORS_ONLY, FEEDBACK_SIMPLE, FEEDBACK_FULL_RESPONSE };
	public static final int[] FEEDBACK_RESOURCES = { R.string.feedback_none, R.string.feedback_errors_only, R.string.feedback_simple, R.string.feedback_full_response };

	private final long id;
	private String name;
	private String method;
	private String protocol;
	private String url;
	private String username;
	private String password;
	private String iconName;
	private int feedback;
	private int position;
	private String description;

	protected Shortcut(long id, String name, String description, String protocol, String url, String method, String username, String password, String iconName, int feedback,
			int position) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.protocol = protocol;
		this.url = url;
		this.method = method;
		this.username = username;
		this.password = password;
		this.iconName = iconName;
		this.feedback = feedback;
		this.position = position;
	}

	protected Shortcut(Cursor cursor) {
		id = cursor.getLong(0);
		name = cursor.getString(1);
		description = cursor.getString(11);
		protocol = cursor.getString(2);
		url = cursor.getString(3);
		method = cursor.getString(4);
		username = cursor.getString(5);
		password = cursor.getString(6);
		iconName = cursor.getString(7);
		feedback = cursor.getInt(8);
		position = cursor.getInt(10);
	}

	public long getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getURL() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getIconName() {
		return iconName;
	}

	public int getFeedback() {
		return feedback;
	}

	public int getPosition() {
		return position;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMethod(String method) {
		this.method = METHOD_POST.equals(method) ? METHOD_POST : METHOD_GET;
	}

	public void setProtocol(String protocol) {
		this.protocol = PROTOCOL_HTTPS.equals(protocol) ? PROTOCOL_HTTPS : PROTOCOL_HTTP;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isNew() {
		return id == 0;
	}

	public Shortcut duplicate(String newName) {
		return new Shortcut(0, newName, description, protocol, url, method, username, password, iconName, feedback, 0);
	}

	public Uri getIconURI(Context context) {
		if (iconName == null) {
			return Uri.parse("android.resource://" + context.getPackageName() + "/" + DEFAULT_ICON);
		} else if (iconName.startsWith("android.resource://")) {
			return Uri.parse(iconName);
		} else if (iconName.endsWith(".png")) {
			return Uri.fromFile(context.getFileStreamPath(iconName));
		} else {
			return Uri.parse("android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
		}
	}

}
