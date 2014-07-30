package org.codelearn.twitter;

import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;
import org.codelearn.twitter.util.AppPreferences;
import org.codelearn.twitter.util.Constants;
import org.codelearn.twitter.util.Utils;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TweetListActivity extends ListActivity {

	private ArrayAdapter tweetItemArrayAdapter;
	private final List<Tweet> tweets = new ArrayList<Tweet>();
	public static long sinceId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_list);
		tweetItemArrayAdapter = new TweetAdapter(this, tweets);
		setListAdapter(tweetItemArrayAdapter);

		if (AppPreferences.getInstance(this).isAuthenticated()) {
			displayCachedTweets();
			new AsyncFetchTweets(this).execute();
		} else {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	private void displayCachedTweets() {
		try {
			List<Tweet> cachedTweets = (List<Tweet>) Utils.readObject(this, Constants.CACHE_KEY);
			renderTweets(cachedTweets);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void renderTweets(List<Tweet> newTweets) {
		tweets.addAll(0, newTweets);
		tweetItemArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tweet_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			new AsyncFetchTweets(this).execute();
			return true;
		} else if (item.getItemId() == R.id.action_compose) {
			startActivity(new Intent(this, ComposeTweetActivity.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, TweetDetailActivity.class);
		intent.putExtra("MyClass", (Tweet) getListAdapter().getItem(position));
		startActivity(intent);
	}
}
