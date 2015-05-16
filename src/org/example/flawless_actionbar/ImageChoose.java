package org.example.flawless_actionbar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageChoose extends ActionBarActivity {

    private static int FILTER_IMAGE = 5;
    String picturePath;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_choose);
				
		Intent intent = getIntent();
		
		// device screen size
        DisplayMetrics display = getBaseContext().getResources().getDisplayMetrics();
		int deviceWidth = display.widthPixels;
		int deviceHeight = display.heightPixels;
		int imgSize = Math.min(deviceWidth, deviceHeight);
		
		LinearLayout choosePreview = (LinearLayout)findViewById(R.id.choosePreview);
		ImageView previewImage = new ImageView(this);
		picturePath = (String)intent.getStringExtra("path");
		Bitmap previewBitmap = (Bitmap)BitmapFactory.decodeFile(picturePath);
		
		choosePreview.addView(previewImage);
		previewImage.setImageBitmap(previewBitmap);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgSize, imgSize * 10 / 9);
		previewImage.setLayoutParams(layoutParams);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
    		case R.id.action_next:
    			// start image filter
    		    Intent intent = new Intent(getBaseContext(), ImageDecorate.class);
    		    intent.putExtra("path", picturePath);
    		    startActivityForResult(intent, FILTER_IMAGE);
    		    
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
		}
	}

	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        if(resultCode != RESULT_OK)
	        {
	          return;
	        }
	        
	        if (requestCode == FILTER_IMAGE && null != data){
	        	Intent intent = new Intent();
	        	final Bundle extras = data.getExtras();
	        	if (extras != null){
	        		picturePath = extras.getString("path");
	        	}
    			intent.putExtra("path", picturePath);
    			setResult(Activity.RESULT_OK, intent);
    		    finish();
	        }
	 }
    
}

