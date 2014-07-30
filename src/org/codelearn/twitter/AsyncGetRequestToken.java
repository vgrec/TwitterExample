package org.codelearn.twitter;

import org.codelearn.twitter.util.Constants;
import org.codelearn.twitter.util.Utils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;

public class AsyncGetRequestToken extends AsyncTask<Void, Void, RequestToken> {
	private final TaskFinishedListener<RequestToken> listener;

	public AsyncGetRequestToken(TaskFinishedListener<RequestToken> urlReceivedListener) {
		this.listener = urlReceivedListener;
	}

	@Override
	protected RequestToken doInBackground(Void... params) {
		Twitter twitter = Utils.createTwitterInstance();
		try {
			RequestToken requestToken = twitter.getOAuthRequestToken(Constants.CALLBACK_URL);
			return requestToken;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(RequestToken result) {
		super.onPostExecute(result);
		listener.onTaskFinished(result);
	}
}
