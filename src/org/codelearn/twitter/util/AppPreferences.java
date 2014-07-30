package org.codelearn.twitter.util;

import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	public static AppPreferences instance;
	private final SharedPreferences prefs;

	public AppPreferences(Context context) {
		prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
	}

	public static AppPreferences getInstance(Context context) {
		if (instance == null) {
			instance = new AppPreferences(context);
		}
		return instance;
	}

	public void saveAccessToken(AccessToken token) {
		Editor edit = prefs.edit();
		edit.putString(Constants.PREF_KEY_TOKEN, token.getToken()).commit();
		edit.putString(Constants.PREF_KEY_SECRET, token.getTokenSecret()).commit();
	}

	public boolean isAuthenticated() {
		return getAccessToken() != null;
	}

	public AccessToken getAccessToken() {
		AccessToken accessToken = null;
		String token = prefs.getString(Constants.PREF_KEY_TOKEN, null);
		String tokenSecret = prefs.getString(Constants.PREF_KEY_SECRET, null);

		if (token != null && tokenSecret != null) {
			accessToken = new AccessToken(token, tokenSecret);
		}
		return accessToken;
	}

	public void setSinceId(long sinceId) {
		Editor edit = prefs.edit();
		edit.putLong(Constants.PREF_SINCE_ID, sinceId).commit();
	}

	public long getSinceId() {
		return prefs.getLong(Constants.PREF_SINCE_ID, 1);
	}

}
