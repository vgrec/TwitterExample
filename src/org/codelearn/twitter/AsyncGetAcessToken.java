package org.codelearn.twitter;

import org.codelearn.twitter.util.Utils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.net.Uri;
import android.os.AsyncTask;

public class AsyncGetAcessToken extends AsyncTask<Uri, Void, AccessToken> {

	private final TaskFinishedListener<AccessToken> listener;
	private final RequestToken requestToken;

	public AsyncGetAcessToken(TaskFinishedListener<AccessToken> accessTokenReceivedListener,
			RequestToken requestToken) {
		this.listener = accessTokenReceivedListener;
		this.requestToken = requestToken;
	}

	@Override
	protected AccessToken doInBackground(Uri... params) {
		String verifier = params[0].getQueryParameter("oauth_verifier");
		Twitter twitter = Utils.createTwitterInstance();
		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			return accessToken;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(AccessToken result) {
		super.onPostExecute(result);
		listener.onTaskFinished(result);
	}
}
