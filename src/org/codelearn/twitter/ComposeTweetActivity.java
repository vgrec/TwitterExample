package org.codelearn.twitter;

import org.codelearn.twitter.util.AppPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeTweetActivity extends Activity {

	public static final int CHARS_ALLOWED = 140;
	private TextView charCountTextView;
	private Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);

		if (!AppPreferences.getInstance(this).isAuthenticated()) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}

		charCountTextView = (TextView) findViewById(R.id.char_count);
		charCountTextView.setText(String.valueOf(CHARS_ALLOWED));

		final EditText tweetEditText = (EditText) findViewById(R.id.fld_compose);
		tweetEditText.addTextChangedListener(watcher);

		submitButton = (Button) findViewById(R.id.btn_submit);
		submitButton.setEnabled(false);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String status = tweetEditText.getText().toString().trim();
				new AsyncPostTweets(ComposeTweetActivity.this).execute(status);
			}
		});
	}

	private final TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			int charsRemaining = CHARS_ALLOWED - s.length();
			charCountTextView.setText(String.valueOf(charsRemaining));

			if (s.length() == 0) {
				submitButton.setEnabled(false);
			} else {
				submitButton.setEnabled(true);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public void onStatusPosted(Boolean result) {
		if (result != null) {
			showToast("Successfully posted!");
		} else {
			showToast("Unable to post the tweet.");
		}
		submitButton.setEnabled(true);
	}

	public void onStatusBeforePost() {
		submitButton.setEnabled(false);
	}

	private void showToast(String message) {
		Toast.makeText(ComposeTweetActivity.this, message, Toast.LENGTH_LONG).show();
	}

}
