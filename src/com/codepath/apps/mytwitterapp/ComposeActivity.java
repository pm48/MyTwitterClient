package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	EditText etStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		Button btnPost= (Button) findViewById(R.id.btnPost);
		etStatus = (EditText) findViewById(R.id.editText1);
		btnPost.setOnClickListener(new OnClickListener() {
			
			String status=new String();
			@Override
			public void onClick(View v) {
				status =etStatus.getText().toString();
				MyTwitterApp.getRestClient().postTweet(new JsonHttpResponseHandler(){
					public void onSuccess(JSONObject jsonTweet)
					{
						Tweet tweet1 = Tweet.fromJson(jsonTweet);
					
						Intent data = new Intent();
						
						data.putExtra("tweet", tweet1);
					
						setResult(RESULT_OK, data);
						finish();
						
					}
					
					public void onFailure(Throwable e, JSONObject error) {
					    // Handle the failure and alert the user to retry
					    Log.e("ERROR", e.toString());
					  }
			},status);
				
				
		}
		
		
	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

}
