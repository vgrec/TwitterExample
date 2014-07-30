package org.codelearn.twitter;

import org.codelearn.twitter.util.AppPreferences;
import org.codelearn.twitter.util.Constants;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.os.AsyncTask;

public class AsyncPostTweets extends AsyncTask<String, Void, Boolean> {
	private ComposeTweetActivity composeTweetActivity = null;

	public AsyncPostTweets(ComposeTweetActivity tweetListActivity) {
		this.composeTweetActivity = tweetListActivity;
	}

	@Override
	protected void onPreExecute() {
		composeTweetActivity.onStatusBeforePost();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(AppPreferences.getInstance(composeTweetActivity)
				.getAccessToken());
		twitter4j.Status status;
		try {
			status = twitter.updateStatus(params[0]);
			return status != null;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		composeTweetActivity.onStatusPosted(result);
	}
}
