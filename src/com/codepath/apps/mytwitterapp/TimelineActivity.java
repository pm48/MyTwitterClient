package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.models.Tweet;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private TweetsAdapter lvadapter;
	private ListView lvView;
	private long maxId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		lvView = (ListView) findViewById(R.id.lVtweets);
		lvView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void loadMore(int page, int totalItemsCount) {
				View v = getCurrentFocus();
				MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(),totalItemsCount,maxId);

			}
		});
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			

			public void onSuccess(JSONArray jsonTweets)
			{
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				lvadapter = new TweetsAdapter(getBaseContext(), tweets);
				if(tweets.size()==5)
				{
				       maxId = tweets.get(4).getId();
				}
				lvView.setAdapter(lvadapter);
				Log.d("DEBUG",jsonTweets.toString());
			}
			
			public void onFailure(Throwable e, JSONObject error) {
			    // Handle the failure and alert the user to retry
			    Log.e("ERROR", e.toString());
			  }
		},5,maxId);
		
	

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.Compose:
			Intent i = new Intent(this,ComposeActivity.class);
	    	 startActivityForResult(i, 1);
			Toast.makeText(this, "ComposeTweet ", Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			
          Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");
         // lvadapter.add(tweet);
          lvadapter.insert(tweet,0);
          
		}

}
}

