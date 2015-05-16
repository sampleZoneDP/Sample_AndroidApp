package org.example.flawless_actionbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	Button done, back;
	EditText id, pw;

	static // Shared Data (for user info)
	SharedPreferences pref;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getActionBar().setDisplayShowHomeEnabled(true);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		ParseObject.registerSubclass(ProfileHandler.class);
		Parse.initialize(this, "SevVTEOjCQDpWtgF8c3ZYDlQZkLrZOJNU5B9meMz", "1tVvmG7OPQ8336LBQ6cnPHtiHb2JtVLEoYRQJcs2");
        
		done = (Button) findViewById(R.id.done);
		back = (Button) findViewById(R.id.back);
		id = (EditText) findViewById(R.id.id);
		pw = (EditText) findViewById(R.id.pw);

		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String getId = id.getText().toString();
				String getPassword = pw.getText().toString();
				
				//Logging In, Parse API
				ParseUser.logInInBackground(getId, getPassword, new LogInCallback(){
					public void done(ParseUser user, com.parse.ParseException e){
						if(user != null){
							Toast.makeText(getApplication(), "Logged In success", Toast.LENGTH_LONG).show();
							String userName = user.getString("username");
							
							// Set user information Data (local)
							pref = getSharedPreferences("pref", MODE_PRIVATE);
							final SharedPreferences.Editor editor = pref.edit();
							editor.putString("userId", user.getString("username"));
							editor.putString("userName", user.getString("Name"));
							editor.putString("userEmail", user.getString("email"));
							
							// user info
							ParseObject userinfo;
							try {
								// user info: parseobject 
								userinfo = (ParseObject)user.getParseObject("profile_detail").fetch();
								
								editor.putString("userBlog", userinfo.getString("Blog"));
						    	editor.putInt("userHeight", userinfo.getInt("Height"));
						    	editor.putInt("userWeight", userinfo.getInt("Weight"));
						    	editor.putInt("userGender", userinfo.getInt("Gender"));
						    	editor.putInt("userAgegroup",userinfo.getInt("Age_Group"));
							   	editor.putBoolean("userVintage", userinfo.getBoolean("Vintage"));
						    	editor.putBoolean("userClassic", userinfo.getBoolean("Classic"));
						    	editor.putBoolean("userCasual", userinfo.getBoolean("Casual"));
						    	editor.putBoolean("userBoho", userinfo.getBoolean("Boho"));
						    	editor.putBoolean("userSporty", userinfo.getBoolean("Sporty"));
						    	editor.putBoolean("userPreppy", userinfo.getBoolean("Preppy"));
						    	editor.putBoolean("userDandy", userinfo.getBoolean("Dandy"));
						    	editor.putBoolean("userApple", userinfo.getBoolean("Apple"));
						    	editor.putBoolean("userPear", userinfo.getBoolean("Pear"));
						    	editor.putBoolean("userBanana", userinfo.getBoolean("Banana"));
						    	editor.putBoolean("userHourglass", userinfo.getBoolean("Hourglass"));
						    	editor.putBoolean("userEcto", userinfo.getBoolean("Ecto"));
						    	editor.putBoolean("userMeso", userinfo.getBoolean("Meso"));
						    	editor.putBoolean("userEndo", userinfo.getBoolean("Endo"));
						    	
						    	// user data counting
						    	ParseQuery<SubscribeDataHandler> followersQuery = ParseQuery.getQuery("SubscribeDataHandler");
								followersQuery.whereEqualTo("toUser", user);
								followersQuery.countInBackground(new CountCallback() {
									public void done(int count, ParseException e) {
										int numFavorited;
										if (e == null) {
											numFavorited = count;
											editor.putInt("userFavorited", numFavorited);
										} else {
											numFavorited = 0;
											editor.putInt("userFavorited", 0);
										}
								    	
									}
								});
											
								// number of Favoriting
								ParseQuery<SubscribeDataHandler> favoritingQuery = ParseQuery.getQuery("SubscribeDataHandler");
								favoritingQuery.whereEqualTo("fromUser", user);
								favoritingQuery.countInBackground(new CountCallback() {
									public void done(int count, ParseException e) {
										int numFavoriting;
										if (e == null) {
											numFavoriting = count;
											editor.putInt("userFavoriting", numFavoriting);
										} else {
											numFavoriting = 0;
											editor.putInt("userFavoriting", 0);
										}
										
									}
								});
								
								// number of pictures
								ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoDataHandler");
								query.whereEqualTo("user", user);
								query.countInBackground(new CountCallback() {
									public void done(int count, ParseException e) {
										int numPictures;
										if (e == null) {
											numPictures = count;
											editor.putInt("userPictures", numPictures);
										} else {
											// something went wrong
											numPictures = 0;
											editor.putInt("userPictures", 0);
										}
								    	
									}
								});

							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
					    	editor.commit();

							// result okay
							Intent intent = new Intent();
							intent.putExtra("userId", user.getString("username"));
							setResult(Activity.RESULT_OK, intent);
					    	
							finish();
						}
						else {
							Log.e("Logging In Error",e.getMessage());
						}
					}
				});

			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Send result to LoginRegister Activity
				Intent intent = new Intent();
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});
	}
}