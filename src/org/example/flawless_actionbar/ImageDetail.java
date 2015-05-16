package org.example.flawless_actionbar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ImageDetail extends ActionBarActivity {

    String picturePath;
    int UPLOAD_IMAGE = 8;
    String img_description, img_tags;
    EditText detail_description, detail_tagField;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_detail);
		
		// get picture source
		Intent intent = getIntent();
		
		FrameLayout choosePreview = (FrameLayout)findViewById(R.id.detail_image);
		ImageView previewImage = new ImageView(this);
		picturePath = (String)intent.getStringExtra("path");
		Bitmap previewBitmap = (Bitmap)BitmapFactory.decodeFile(picturePath);
		
		choosePreview.addView(previewImage);
		previewImage.setImageBitmap(previewBitmap);
		
		// get data string from edittexts
		detail_description = (EditText)findViewById(R.id.detail_description);
		detail_tagField = (EditText)findViewById(R.id.detail_tagField);
		
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
    			
    			// get tag and description
    			img_description = detail_description.getText().toString(); 
    			img_tags = detail_tagField.toString();
    			
    			// start image filter
    		    Intent intent = new Intent(getBaseContext(), ImageUploader.class);
    		    intent.putExtra("path", picturePath);
    		    intent.putExtra("description", img_description);
    		    intent.putExtra("tags", img_tags);
    		    startActivityForResult(intent, UPLOAD_IMAGE);
    		    
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
	        
	        if (requestCode == UPLOAD_IMAGE && null != data){
	        	Intent intent = new Intent();
	        	setResult(Activity.RESULT_OK, intent);
	        	finish();
	        }
	 }
}

