package org.codelearn.twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;
import org.codelearn.twitter.util.AppPreferences;
import org.codelearn.twitter.util.Constants;
import org.codelearn.twitter.util.Utils;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncFetchTweets extends AsyncTask<Void, Void, List<Tweet>> {
	private TweetListActivity tweetListActivity = null;

	public AsyncFetchTweets(TweetListActivity tweetListActivity) {
		this.tweetListActivity = tweetListActivity;
	}

	@Override
	protected List<Tweet> doInBackground(Void... params) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(AppPreferences.getInstance(tweetListActivity).getAccessToken());
		List<twitter4j.Status> statuses = new ArrayList<twitter4j.Status>();
		try {
			statuses = twitter.getHomeTimeline(new Paging(TweetListActivity.sinceId));
			for (twitter4j.Status status : statuses) {
				tweets.add(new Tweet(String.valueOf(status.getId()), status.getUser().getName(),
						status.getText()));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		if (statuses.size() > 0) {
			TweetListActivity.sinceId = statuses.get(0).getId();
			cacheTweets(tweets);
		}

		return tweets;
	}

	private void cacheTweets(List<Tweet> tweets) {
		try {
			Utils.writeObject(tweetListActivity, Constants.CACHE_KEY, tweets);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(List<Tweet> TweetRead) {
		Log.d("Call From on Prefetch", "Check");
		tweetListActivity.renderTweets(TweetRead);
	}
}
