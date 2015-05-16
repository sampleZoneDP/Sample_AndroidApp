package org.example.flawless_actionbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginRegister extends Activity {
	Button login;
	ImageView email_register;
	static // Shared Data (for user info)
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		
		email_register = (ImageView) findViewById(R.id.email_register);
		email_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivityForResult(myIntent, 1002);
			}
		});
		
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivityForResult(myIntent, 1001);
				
			}
		});
		
		// login process (if user didn't log out)
		if (!(pref.getString("userId", "").equals(""))){
			// Send result to LoginRegister Activity
			Intent intent = new Intent();
			intent.putExtra("loginType", 0);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		
	}
	
	// Process Result from Login Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1001 && resultCode == RESULT_OK){
			String userId = data.getStringExtra("userId");
			Toast.makeText(getApplicationContext(), "Through LogIn, Login as User: " + userId, Toast.LENGTH_LONG).show();
			// Send result to LoginRegister Activity
			Intent intent = new Intent();
			intent.putExtra("loginType", 0);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} 
		if (requestCode == 1002 && resultCode == RESULT_OK){
			String userId = data.getStringExtra("userId");
			Toast.makeText(getApplicationContext(), "Through Register, Login as User: " + userId, Toast.LENGTH_LONG).show();
			// Send result to LoginRegister Activity
			Intent intent = new Intent();
			intent.putExtra("loginType", 1);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
}
