package org.example.flawless_actionbar;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ImageUploader extends ActionBarActivity {

    String picturePath, description, tags;
    static SharedPreferences pref;
    int update_look, update_height, update_weight;
    int intGender;
    
    //Database handler
  	DataHandler handler;
  	
    // Buttons in upload activity
    Button updatePicture_btn;
	Button updatePictureCancel_btn;
	RadioButton updateStyleVintage;
	RadioButton updateStyleClassic;
	RadioButton updateStyleCasual;
	RadioButton updateStyleBoho;
	RadioButton updateStyleSporty;
	RadioButton updateStylePreppy;
	RadioButton updateStyleDandy;
	RadioButton updateTypeApple;
	RadioButton updateTypeBanana;
	RadioButton updateTypePear;
	RadioButton updateTypeHourglass;
	RadioButton updateTypeEcto;
	RadioButton updateTypeMeso;
	RadioButton updateTypeEndo;
	RadioButton updateLookSlim;
	RadioButton updateLookLongLeg;
	RadioButton updateLookChic;
	RadioButton updateLookBadboy;
	RadioButton updateLookBoyfriend;
	RadioButton updateLookGlamorous;
	EditText updateHashtag;
    
	// Linear layouts for different gender (in upload activity)
	LinearLayout uploadTypeBoxF1;
    LinearLayout uploadTypeBoxF2;
    LinearLayout uploadTypeBoxM1;
    LinearLayout uploadLookBoxM1;
    LinearLayout uploadLookBoxF1;
    
	boolean vintageChecked, classicChecked, casualChecked, bohoChecked, sportyChecked, preppyChecked, dandyChecked, 
	appleChecked, bananaChecked, pearChecked, hourglassChecked, ectoChecked, mesoChecked, endoChecked, slimChecked, longLegChecked,
	chicChecked, badboyChecked, boyfriendChecked, glamorousChecked;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_setting);
		
		//Parse API
		ParseObject.registerSubclass(CommentHandler.class);
		ParseObject.registerSubclass(PhotoDataHandler.class);
		Parse.initialize(this, "SevVTEOjCQDpWtgF8c3ZYDlQZkLrZOJNU5B9meMz", "1tVvmG7OPQ8336LBQ6cnPHtiHb2JtVLEoYRQJcs2");
		
		// description, tags, and picture path of image
		Intent intent = getIntent();
		picturePath = (String)intent.getStringExtra("path");
		description = (String)intent.getStringExtra("description");
		tags = (String)intent.getStringExtra("tags");
		
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		
		update_look = 0;
		updateStyleVintage = (RadioButton)findViewById(R.id.updateStyleVintage);
		updateStyleClassic = (RadioButton)findViewById(R.id.updateStyleClassic);
		updateStyleCasual = (RadioButton)findViewById(R.id.updateStyleCasual);
		updateStyleBoho = (RadioButton)findViewById(R.id.updateStyleBoho);
		updateStyleSporty = (RadioButton)findViewById(R.id.updateStyleSporty);
		updateStylePreppy = (RadioButton)findViewById(R.id.updateStylePreppy);
		updateStyleDandy = (RadioButton)findViewById(R.id.updateStyleDandy);
		updateTypeApple = (RadioButton)findViewById(R.id.updateTypeApple);
		updateTypeBanana = (RadioButton)findViewById(R.id.updateTypeBanana);
		updateTypePear = (RadioButton)findViewById(R.id.updateTypePear);
		updateTypeHourglass = (RadioButton)findViewById(R.id.updateTypeHourglass);
		updateTypeEcto = (RadioButton)findViewById(R.id.updateTypeEcto);
		updateTypeMeso = (RadioButton)findViewById(R.id.updateTypeMeso);
		updateTypeEndo = (RadioButton)findViewById(R.id.updateTypeEndo);
		updateLookSlim = (RadioButton)findViewById(R.id.UpdateLookSlim);
		updateLookLongLeg = (RadioButton)findViewById(R.id.UpdateLookLongLeg);
		updateLookChic = (RadioButton)findViewById(R.id.UpdateLookChic);
		updateLookBadboy = (RadioButton)findViewById(R.id.UpdateLookBadboy);
		updateLookBoyfriend = (RadioButton)findViewById(R.id.UpdateLookBoyFriend);
		updateLookGlamorous = (RadioButton)findViewById(R.id.UpdateLookGlamorous); 
		updateHashtag = (EditText)findViewById(R.id.updateHashtag);
		
		uploadTypeBoxF1 = (LinearLayout)findViewById(R.id.uploadTypeBoxF1);
        uploadTypeBoxF2 = (LinearLayout)findViewById(R.id.uploadTypeBoxF2);
        uploadTypeBoxM1 = (LinearLayout)findViewById(R.id.uploadTypeBoxM1);
        uploadLookBoxM1 = (LinearLayout)findViewById(R.id.uploadLookBoxM1);
        uploadLookBoxF1 = (LinearLayout)findViewById(R.id.uploadLookBoxF1);
        
        // Set default photo details (from User profile).
     	// user photo style & body type
     	vintageChecked = pref.getBoolean("userVintage", false);
     	classicChecked = pref.getBoolean("userClassic", false);
     	casualChecked = pref.getBoolean("userCasual", false);
     	bohoChecked = pref.getBoolean("userBoho", false);
     	sportyChecked = pref.getBoolean("userSporty", false);
     	preppyChecked = pref.getBoolean("userPreppy", false);
     	dandyChecked = pref.getBoolean("userDandy", false);
     	appleChecked = pref.getBoolean("userApple", false);
     	bananaChecked = pref.getBoolean("userBanana", false);
     	pearChecked = pref.getBoolean("userPear", false);
     	hourglassChecked = pref.getBoolean("userHourglass", false);
     	ectoChecked = pref.getBoolean("userEcto", false);
     	mesoChecked = pref.getBoolean("userMeso", false);
     	endoChecked = pref.getBoolean("userEndo", false);
     		
     	// User photo Look
     	slimChecked = false;
     	longLegChecked =false;
     	chicChecked = false;
     	badboyChecked = false;
     	boyfriendChecked = false;
     	glamorousChecked = false;
        
     	// gender dependencies on View
		
		intGender = pref.getInt("userGender", 0);
		switch(intGender){
			case 0 :
				uploadTypeBoxM1.setVisibility(View.VISIBLE);
				uploadLookBoxM1.setVisibility(View.VISIBLE);
				appleChecked = false;
		     	bananaChecked = false;
		     	pearChecked = false;
		     	hourglassChecked = false;
				break;
			case 1 :
				uploadTypeBoxF1.setVisibility(View.VISIBLE);
				uploadTypeBoxF2.setVisibility(View.VISIBLE);
				uploadLookBoxF1.setVisibility(View.VISIBLE);
				ectoChecked = false;
		     	mesoChecked = false;
		     	endoChecked = false;
				break;
			default:
				break;
		}

		// radio buttons reset
		updateStyleVintage.setChecked(vintageChecked);
		updateStyleClassic.setChecked(classicChecked);
		updateStyleCasual.setChecked(casualChecked);
		updateStyleBoho.setChecked(bohoChecked);
		updateStyleSporty.setChecked(sportyChecked);
		updateStylePreppy.setChecked(preppyChecked);
		updateStyleDandy.setChecked(dandyChecked);
		updateTypeApple.setChecked(appleChecked);
		updateTypeBanana.setChecked(bananaChecked);
		updateTypePear.setChecked(pearChecked);
		updateTypeHourglass.setChecked(hourglassChecked);
		updateTypeEcto.setChecked(ectoChecked);
		updateTypeMeso.setChecked(mesoChecked);
		updateTypeEndo.setChecked(endoChecked);
		updateLookSlim.setChecked(slimChecked);
		updateLookLongLeg.setChecked(longLegChecked);
		updateLookChic.setChecked(chicChecked);
		updateLookBadboy.setChecked(badboyChecked);
		updateLookBoyfriend.setChecked(boyfriendChecked);
		updateLookGlamorous.setChecked(glamorousChecked);
	
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
    			// upload picture
    			Bitmap myBitmap;
				
				try{ 
					   // new bitmap image
					   myBitmap = BitmapFactory.decodeFile(picturePath);

					   //Uploading photo to byte array for storing into Parse Background Server, Parse API
					   ByteArrayOutputStream stream = new ByteArrayOutputStream();
					   myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					   byte[] photo = stream.toByteArray();

					   ParseFile Photo = new ParseFile("photo.png", photo);
			           Photo.saveInBackground();
					   
					   // upload 'pictures' number in user data
					   int pic_number = (pref.getInt("userPictures", 0)) + 1;
					   SharedPreferences.Editor editor = pref.edit();
					   editor.putInt("userPictures", pic_number);
					   editor.commit();			
					   

					   // get height range (140 ~ 190: index from 0)
					   int userHeightRange = pref.getInt("userHeight", 0);
					   
					   // get weight range (40 ~ 100: index from 0)
					   int userWeightRange =  pref.getInt("userWeight", 0);
					  
					   // picture user tags
					   JSONArray tagPeople = new JSONArray();
					   
					   //Photo Uploading to the background, Parse API 
					   PhotoDataHandler pdh = new PhotoDataHandler();
					   pdh.insertPhotoData(
							   ParseUser.getCurrentUser(), 
							   Photo, 
							   userHeightRange, userWeightRange, intGender,  pref.getInt("userGeneration", 0), updateStyleVintage.isChecked()
							   , updateStyleClassic.isChecked(), updateStyleCasual.isChecked(), updateStyleBoho.isChecked()
							   , updateStyleSporty.isChecked(), updateStylePreppy.isChecked(), updateStyleDandy.isChecked()
							   , updateTypeApple.isChecked(), updateTypePear.isChecked(), updateTypeBanana.isChecked(), updateTypeHourglass.isChecked()
							   , updateTypeEcto.isChecked(), updateTypeMeso.isChecked(), updateTypeEndo.isChecked(), updateLookSlim.isChecked()
							   , updateLookLongLeg.isChecked(), updateLookBoyfriend.isChecked(), updateLookGlamorous.isChecked(),
							   updateLookChic.isChecked(), updateLookBadboy.isChecked(), 0, tagPeople, description);
					   
				}catch(Exception e){
					  // exception
				}
    			
    			
    			// end activity
    			Intent intent = new Intent();
    			setResult(Activity.RESULT_OK, intent);
    		    finish();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
		}
	}
    
	   //Return Time for new picture Name
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
    

    // RadioButton Event
    public void onRadioButtonClicked(View view){

        switch(view.getId()) {
        	case R.id.updateStyleVintage:
        		if (vintageChecked){
        			updateStyleVintage.setChecked(false);
        			vintageChecked=false;
        		}else {
        			updateStyleVintage.setChecked(true);
        			vintageChecked=true;
        		}
        		break;
        	case R.id.updateStyleClassic:
        		if (classicChecked){
        			updateStyleClassic.setChecked(false);
            	classicChecked=false;
        		}else {
        			updateStyleClassic.setChecked(true);
            	classicChecked=true;
        		}
        		break;
        	case R.id.updateStyleCasual:
        		if (casualChecked){
        			updateStyleCasual.setChecked(false);
        			casualChecked=false;
        		}else {
        			updateStyleCasual.setChecked(true);
        			casualChecked=true;
        		}
        		break;
        	case R.id.updateStyleBoho:
        		if (bohoChecked){
        			updateStyleBoho.setChecked(false);
        			bohoChecked=false;
        		}else {
        			updateStyleBoho.setChecked(true);
        			bohoChecked=true;
        		}
        		break;
        	case R.id.updateStyleSporty:
        		if (sportyChecked){
        			updateStyleSporty.setChecked(false);
        			sportyChecked=false;
        		}else {
        			updateStyleSporty.setChecked(true);
        			sportyChecked=true;
        		}
        		break;
        	case R.id.updateStylePreppy:
        		if (preppyChecked){
        			updateStylePreppy.setChecked(false);
        			preppyChecked=false;
        		}else {
        			updateStylePreppy.setChecked(true);
        			preppyChecked=true;
        		}
        		break;
        	case R.id.updateStyleDandy:
        		if (dandyChecked){
        			updateStyleDandy.setChecked(false);
        			dandyChecked=false;
        		}else {
        			updateStyleDandy.setChecked(true);
        			dandyChecked=true;
        		}
        		break;
        	case R.id.updateTypeApple:
        		if (appleChecked){
        			updateTypeApple.setChecked(false);
        			appleChecked=false;
        		}else {
        			updateTypeApple.setChecked(true);
        			appleChecked=true;
        		}
        		break;
        	case R.id.updateTypeBanana:
        		if (bananaChecked){
        			updateTypeBanana.setChecked(false);
        			bananaChecked=false;
        		}else {
        			updateTypeBanana.setChecked(true);
        			bananaChecked=true;
        		}
        		break;
        	case R.id.updateTypePear:
        		if (pearChecked){
        			updateTypePear.setChecked(false);
        			pearChecked=false;
        		}else {
        			updateTypePear.setChecked(true);
        			pearChecked=true;
        		}
        		break;
        	case R.id.updateTypeHourglass:
        		if (hourglassChecked){
        			updateTypeHourglass.setChecked(false);
        			hourglassChecked=false;
        		}else {
        			updateTypeHourglass.setChecked(true);
        			hourglassChecked=true;
        		}
        		break;
        	case R.id.updateTypeEcto:
        		if (ectoChecked){
        			updateTypeEcto.setChecked(false);
        			ectoChecked=false;
        		}else {
        			updateTypeEcto.setChecked(true);
        			ectoChecked=true;
        		}
        		break;
        	case R.id.updateTypeMeso:
        		if (mesoChecked){
        			updateTypeMeso.setChecked(false);
        			mesoChecked=false;
        		}else {
        			updateTypeMeso.setChecked(true);
        			mesoChecked=true;
        		}
        		break;
        	case R.id.updateTypeEndo:
        		if (endoChecked){
        			updateTypeEndo.setChecked(false);
        			endoChecked=false;
        		}else {
        			updateTypeEndo.setChecked(true);
        			endoChecked=true;
        		}
        		break;
        	case R.id.UpdateLookSlim:
        		if (slimChecked){
        			updateLookSlim.setChecked(false);
        			slimChecked=false;
        		}else {
        			updateLookSlim.setChecked(true);
        			slimChecked=true;
        		}
        		break;
        	case R.id.UpdateLookLongLeg:
        		if (longLegChecked){
        			updateLookLongLeg.setChecked(false);
        			longLegChecked=false;
        		}else {
        			updateLookLongLeg.setChecked(true);
        			longLegChecked=true;
        		}
        		break;
        	case R.id.UpdateLookChic:
        		if (chicChecked){
        			updateLookChic.setChecked(false);
        			chicChecked=false;
        		}else {
        			updateLookChic.setChecked(true);
        			chicChecked=true;
        		}
        		break;
        	case R.id.UpdateLookBadboy:
        		if (badboyChecked){
        			updateLookBadboy.setChecked(false);
        			badboyChecked=false;
        		}else {
        			updateLookBadboy.setChecked(true);
        			badboyChecked=true;
        		}
        		break;
        	case R.id.UpdateLookBoyFriend:
        		if (boyfriendChecked){
        			updateLookBoyfriend.setChecked(false);
        			boyfriendChecked=false;
        		}else {
        			updateLookBoyfriend.setChecked(true);
        			boyfriendChecked=true;
        		}
        		break;
        	case R.id.UpdateLookGlamorous:
        		if (glamorousChecked){
        			updateLookGlamorous.setChecked(false);
        			glamorousChecked=false;
        		}else {
        			updateLookGlamorous.setChecked(true);
        			glamorousChecked=true;
        		}
        		break;
        }

    }
}

