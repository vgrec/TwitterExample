package org.codelearn.twitter;

import org.codelearn.twitter.util.AppPreferences;
import org.codelearn.twitter.util.Constants;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button loginBtn;
	private RequestToken requestToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginBtn = (Button) findViewById(R.id.btn_login);

		if (AppPreferences.getInstance(MainActivity.this).isAuthenticated()) {
			startTweetListActivity();
		}

		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				authenticate();
			}
		});
	}

	protected void authenticate() {
		new AsyncGetRequestToken(requestTokenListener).execute();
	}

	private final TaskFinishedListener<RequestToken> requestTokenListener = new TaskFinishedListener<RequestToken>() {
		@Override
		public void onTaskFinished(RequestToken requestToken) {
			if (requestToken != null) {
				MainActivity.this.requestToken = requestToken;
				String authUrl = requestToken.getAuthenticationURL();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			}
		}
	};

	void startTweetListActivity() {
		Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		processTwitterResponse(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		processTwitterResponse(intent);
	}

	private void processTwitterResponse(Intent intent) {
		if (intent != null && intent.getData() != null) {
			Uri uri = intent.getData();
			if (uri != null && uri.toString().startsWith(Constants.CALLBACK_URL)) {
				new AsyncGetAcessToken(accessTokenReceivedListener, requestToken).execute(uri);
			}
		}
	}

	private final TaskFinishedListener<AccessToken> accessTokenReceivedListener = new TaskFinishedListener<AccessToken>() {
		@Override
		public void onTaskFinished(AccessToken token) {
			if (token != null) {
				AppPreferences.getInstance(MainActivity.this).saveAccessToken(token);
				startTweetListActivity();
			}
		}
	};
}
