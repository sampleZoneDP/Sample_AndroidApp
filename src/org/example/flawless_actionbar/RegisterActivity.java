package org.example.flawless_actionbar;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {
	Button submit, load;
	EditText userId, password, name, email;
	DataHandler handler;
	String regUserId;
	
	static // Shared Data (for user info)
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		getActionBar().setDisplayShowHomeEnabled(false);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//Parse API
		ParseObject.registerSubclass(ProfileHandler.class);
		Parse.initialize(this, "SevVTEOjCQDpWtgF8c3ZYDlQZkLrZOJNU5B9meMz", "1tVvmG7OPQ8336LBQ6cnPHtiHb2JtVLEoYRQJcs2");
        
		submit = (Button) findViewById(R.id.submit);
		load = (Button) findViewById(R.id.load);
		userId = (EditText) findViewById(R.id.userId);
		password = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);
		submit.setOnClickListener(new View.OnClickListener() {
			
			//Signing Up, Parse API
			ParseUser user = new ParseUser();
			
			@Override
			public void onClick(View v) {
				String getId = userId.getText().toString();
				String getPassword = password.getText().toString();
				String getEmail = email.getText().toString();
				
		    	//Signing In, Parse API
					user.setUsername(getId);
					user.setPassword(getPassword);
					user.setEmail(getEmail);		
					
					// default profile photo
					Bitmap profile_bitmap_temp = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.profile);
					final Bitmap profile_bitmap = Bitmap.createScaledBitmap(profile_bitmap_temp, 100, 100, true);
					profile_bitmap_temp.recycle();
	        
					//Profile pictures to byte array for storing into Parse Background Server, Parse API
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] profile_photo = stream.toByteArray();

					final ParseFile profilePhoto = new ParseFile("photo.png", profile_photo);
			        profilePhoto.saveInBackground();
			        		
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e){
							if (e==null){
								
								ProfileHandler pfh = new ProfileHandler();
								   pfh.insertProfileData("", 0, 0, 0, 10, false, false,
										   false, false, false, false, false, false, false, false,
										   false, false, false, false, "");
								
								
								ParseUser currentUser = ParseUser.getCurrentUser(); 
								currentUser.put("profile_detail", pfh);
								currentUser.put("profilePhoto", profilePhoto);
								profile_bitmap.recycle();
								currentUser.saveInBackground();
							
							// Set user local data
							// Set user information Data (local)
								pref = getSharedPreferences("pref", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
							
								editor.putString("userId", currentUser.getString("username"));
								editor.putString("userName", currentUser.getString("Name"));
								editor.putString("userEmail", currentUser.getString("email"));
								editor.putString("userBlog", "");
								editor.putInt("userHeight", 0);
								editor.putInt("userWeight", 0);	
								editor.putInt("userGender", 0);
								editor.putInt("userAgegroup",0);
					    		editor.putBoolean("userVintage", false);
					    		editor.putBoolean("userClassic", false);
					    		editor.putBoolean("userCasual", false);
					    		editor.putBoolean("userBoho", false);
					    		editor.putBoolean("userSporty", false);
					    		editor.putBoolean("userPreppy", false);
					    		editor.putBoolean("userDandy", false);
					    		editor.putBoolean("userApple", false);
					    		editor.putBoolean("userPear", false);
					    		editor.putBoolean("userBanana", false);
					    		editor.putBoolean("userHourglass", false);
					    		editor.putBoolean("userEcto", false);
					    		editor.putBoolean("userMeso", false);
					    		editor.putBoolean("userEndo", false);
					    		editor.putInt("userFavorited", 0);
					    		editor.putInt("userFavoriting", 0);
					    		editor.putInt("userPictures", 0);
					    	
					    		editor.commit();
					    	
					    	String userName = pref.getString("userId", "");
							String Name = pref.getString("userName", "");
							String Email = pref.getString("userEmail", "");
							regUserId = userName;
							
							Toast.makeText(getApplicationContext(), "userName: "+userName+", Name: "+Name+", Email: "+Email, Toast.LENGTH_LONG).show();
					    	
					    	// Send result to LoginRegister Activity
							Intent intent = new Intent();
							intent.putExtra("userId", currentUser.getString("username"));
							setResult(Activity.RESULT_OK, intent);
							finish();
							
						}
						else {
							Toast.makeText(getApplicationContext(), "Please complete all sections or your username or email already exist", Toast.LENGTH_LONG).show();
						}
				    	
					}
					
				});
				
			}
		});
		
		load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});
	}
	

}
