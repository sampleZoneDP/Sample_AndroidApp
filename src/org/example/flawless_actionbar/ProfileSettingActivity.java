package org.example.flawless_actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ProfileSettingActivity extends Activity {
    
	// EditText for user ID & personal information
	Spinner userHeightEdit, userWeightEdit;
	EditText userIdEdit, userNameEdit, userEmailEdit, userBlogEdit;
	RadioButton radioGender, radioGeneration, radioStyleVintage, radioStyleClassic, radioStyleCasual, radioStyleBoho, radioStyleSporty, radioStylePreppy, radioStyleDandy, radioStyleExtra1, radioStyleExtra2,
				radioTypeApple, radioTypeBanana, radioTypePear, radioTypeHourglass,
				radioTypeEcto, radioTypeMeso, radioTypeEndo, 
				radioM, radioF, radio10, radio20, radio30, radio40;
	RadioGroup radioGroupGender, radioGroupGeneration;
	boolean vintageChecked, classicChecked, casualChecked, bohoChecked, sportyChecked, preppyChecked, dandyChecked, 
			appleChecked, bananaChecked, pearChecked, hourglassChecked, ectoChecked, mesoChecked, endoChecked;
	LinearLayout profSettingTypeBoxM1, profSettingTypeBoxF1, profSettingTypeBoxF2;
	
	// shared data
	SharedPreferences pref;
	
	// adapter for spinners
	ArrayAdapter<CharSequence> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_setting);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//Parse API
		ParseObject.registerSubclass(ProfileHandler.class);
		Parse.initialize(this, "SevVTEOjCQDpWtgF8c3ZYDlQZkLrZOJNU5B9meMz", "1tVvmG7OPQ8336LBQ6cnPHtiHb2JtVLEoYRQJcs2");
        
		// Shared Preference for UserInfo
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		
		ActionBar abar = getActionBar();
		abar.setTitle("HommeFamme");
		
		// Load Information
		userHeightEdit=(Spinner)findViewById(R.id.userHeightSpinner);
		userWeightEdit=(Spinner)findViewById(R.id.userWeightSpinner);		
		
		userIdEdit=(EditText)findViewById(R.id.userIdEdit);
		userNameEdit=(EditText)findViewById(R.id.userNameEdit);
		userEmailEdit=(EditText)findViewById(R.id.userEmailEdit);
		userBlogEdit=(EditText)findViewById(R.id.userBlogEdit);
		radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);
		radioGroupGeneration = (RadioGroup) findViewById(R.id.radioGroupGeneration);
		profSettingTypeBoxM1 = (LinearLayout) findViewById(R.id.profSettingTypeBoxM1);
		profSettingTypeBoxF1 = (LinearLayout) findViewById(R.id.profSettingTypeBoxF1);
		profSettingTypeBoxF2 = (LinearLayout) findViewById(R.id.profSettingTypeBoxF2);
		radioStyleVintage = (RadioButton) findViewById(R.id.radioStyleVintage);
		radioStyleClassic = (RadioButton) findViewById(R.id.radioStyleClassic);
		radioStyleCasual = (RadioButton) findViewById(R.id.radioStyleCasual);
		radioStyleBoho = (RadioButton) findViewById(R.id.radioStyleBoho);
		radioStyleSporty = (RadioButton) findViewById(R.id.radioStyleSporty);
		radioStylePreppy = (RadioButton) findViewById(R.id.radioStylePreppy);
		radioStyleDandy = (RadioButton) findViewById(R.id.radioStyleDandy);
		radioTypeApple = (RadioButton) findViewById(R.id.radioTypeApple);
		radioTypeBanana = (RadioButton) findViewById(R.id.radioTypeBanana);
		radioTypePear = (RadioButton) findViewById(R.id.radioTypePear);  
		radioTypeHourglass = (RadioButton) findViewById(R.id.radioTypeHourglass);
		radioTypeEcto = (RadioButton) findViewById(R.id.radioTypeEctomorph);
		radioTypeMeso = (RadioButton) findViewById(R.id.radioTypeMesomorph);
		radioTypeEndo = (RadioButton) findViewById(R.id.radioTypeEndomorph);
		radioM = (RadioButton) findViewById(R.id.radioM);
		radioF = (RadioButton) findViewById(R.id.radioF);
		radio10 = (RadioButton) findViewById(R.id.radio10);
		radio20 = (RadioButton) findViewById(R.id.radio20);
		radio30 = (RadioButton) findViewById(R.id.radio30);
		radio40 = (RadioButton) findViewById(R.id.radio40);
		
		// Put User information into Setting Activity: EditTexts
		int intHeight = pref.getInt("userHeight", 0);
		int intWeight = pref.getInt("userWeight", 0);
		if (intHeight != 0){
			userHeightEdit.setSelection(intHeight);
		}else{
			userHeightEdit.setSelection(0);
		}
		if (intWeight != 0){
			userWeightEdit.setSelection(intHeight);
		}else{
			userWeightEdit.setSelection(0);
		}
		
		userIdEdit.setText(pref.getString("userId", ""));
		userNameEdit.setText(pref.getString("userName", ""));
		userEmailEdit.setText(pref.getString("userEmail",""));
		userBlogEdit.setText(pref.getString("userBlog", ""));
		
		// Put User information into Setting Activity: Radio Buttons
		int intGender, intGeneration;
		intGender = pref.getInt("userGender", 0);
		intGeneration = pref.getInt("userGeneration", 0);
		
		switch(intGender){
			case 0 :
				radioM.setChecked(true);
				profSettingTypeBoxM1.setVisibility(View.VISIBLE);
				break;
			case 1 :
				radioF.setChecked(true);
            	profSettingTypeBoxF1.setVisibility(View.VISIBLE);
            	profSettingTypeBoxF2.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
		
		switch(intGeneration){
		case 10 :
			radio10.setChecked(true);
			break;
		case 20 :
			radio20.setChecked(true);
			break;
		case 30 :
			radio30.setChecked(true);
			break;
		case 40 :
			radio40.setChecked(true);
			break;
		default:
			break;
		}
		
		vintageChecked = false;
		classicChecked = false;
		casualChecked = false;
		bohoChecked = false;
		sportyChecked = false;
		preppyChecked = false;
		dandyChecked = false;
		appleChecked = false;
		bananaChecked = false;
		pearChecked = false;
		hourglassChecked = false;
		ectoChecked = false;
		mesoChecked = false;
		endoChecked = false;
		
		radioStyleVintage.setChecked(pref.getBoolean("userVintage", false)); 
		radioStyleClassic.setChecked(pref.getBoolean("userClassic", false)); 
		radioStyleCasual.setChecked(pref.getBoolean("userCasual", false)); 
		radioStyleBoho.setChecked(pref.getBoolean("userBoho", false));
		radioStyleSporty.setChecked(pref.getBoolean("userSporty", false));
		radioStylePreppy.setChecked(pref.getBoolean("userPreppy", false));
		radioStyleDandy.setChecked(pref.getBoolean("userDandy", false));
		radioTypeApple.setChecked(pref.getBoolean("userApple", false)); 
		radioTypeBanana.setChecked(pref.getBoolean("userBanana", false)); 
		radioTypePear.setChecked(pref.getBoolean("userPear", false)); 
		radioTypeHourglass.setChecked(pref.getBoolean("userHourglass", false)); 		
		radioTypeEcto.setChecked(pref.getBoolean("userEcto", false)); 
		radioTypeMeso.setChecked(pref.getBoolean("userMeso", false));
		radioTypeEndo.setChecked(pref.getBoolean("userEndo", false));
		
		

		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String getId, getName, getEmail, getBlog;
    	int getHeight, getWeight, getGender, getGeneration;
    	
        switch (item.getItemId()){
        	case R.id.profile_update:
        		Toast.makeText(getApplication(), "Profile updated", Toast.LENGTH_SHORT).show();
        		
        		// update Profile
				getId = userIdEdit.getText().toString();
				getName = userNameEdit.getText().toString();
				getEmail = userEmailEdit.getText().toString();
				getBlog = userBlogEdit.getText().toString();
				
				// spinner height/weight
				getHeight = userHeightEdit.getSelectedItemPosition();
				getWeight = userWeightEdit.getSelectedItemPosition();

				//Gender selection
				int genderSelected = radioGroupGender.getCheckedRadioButtonId();
				radioGender = (RadioButton) findViewById(genderSelected);
				String getGenderStr = radioGender.getText().toString();
		
				if (getGenderStr.equals("Male")){
					getGender = 0;
					
				} else{
					getGender = 1;
				}

				//Generation 
				int generationSelected = radioGroupGeneration.getCheckedRadioButtonId();
				radioGeneration = (RadioButton) findViewById(generationSelected);
				String getGenerationStr = radioGeneration.getText().toString();
				getGeneration = Integer.parseInt(getGenerationStr.substring(0,2));
			
				boolean getVintage = false;
				boolean getClassic = false;
				boolean getCasual = false;
				boolean getBoho = false;
				boolean getSporty = false;
				boolean getPreppy = false;
				boolean getDandy = false;
				boolean getApple = false;
				boolean getBanana = false;
				boolean getPear = false;
				boolean getHourglass = false;
				boolean getEcto = false;
				boolean getMeso = false;
				boolean getEndo = false;
				
				if (radioStyleVintage.isChecked()){
					getVintage = true;
				}
				if (radioStyleClassic.isChecked()){
					getClassic = true;
				}
				if (radioStyleCasual.isChecked()){
					getCasual = true;
				}
				if (radioStyleBoho.isChecked()){
					getBoho = true;
				}
				if (radioStyleSporty.isChecked()){
					getSporty = true;
				}
				if (radioStylePreppy.isChecked()){
					getPreppy = true;
				}
				if (radioStyleDandy.isChecked()){
					getDandy = true;
				}
				if (radioTypeApple.isChecked()){
					getApple = true;
				}
				if (radioTypeBanana.isChecked()){
					getBanana = true;
				}
				if (radioTypePear.isChecked()){
					getPear = true;
				}
				if (radioTypeHourglass.isChecked()){
					getHourglass = true;
				}
				if (radioTypeEcto.isChecked()){
					getEcto = true;
				}
				if (radioTypeMeso.isChecked()){
					getMeso = true;
				}
				if (radioTypeEndo.isChecked()){
					getEndo = true;
				}
				
				//Get current user object, Parse API
				ParseUser currentUser = ParseUser.getCurrentUser();
				if (currentUser == null){
					Toast.makeText(getApplication(), "No user!! Error!", Toast.LENGTH_SHORT).show();
				}
				else {
					
					 ProfileHandler pfh =  (ProfileHandler) currentUser.get("profile_detail");
					   pfh.insertProfileData(getName, getHeight, getWeight, getGender, getGeneration, getVintage, getClassic,
							   getCasual, getBoho, getSporty, getPreppy, getDandy, getApple, getPear, getBanana,
							   getHourglass, getEcto, getMeso, getEndo, getBlog);
					
					//update User detail information, Parse API
					try{
						currentUser.setUsername(getId);
						currentUser.setEmail(getEmail);
						currentUser.put("profile_detail", pfh);
						currentUser.saveInBackground();
					}catch(Exception e){
						Log.e("msg", "dup user id");
					}
					// update Local Datastore
			    	SharedPreferences.Editor editor = pref.edit();
			    	editor.putString("userId", getId);
			    	editor.putString("userName", getName);
			    	editor.putString("userEmail", getEmail);
			    	editor.putString("userBlog", getBlog);
			    	editor.putInt("userHeight", getHeight);
			    	editor.putInt("userWeight", getWeight);
			    	editor.putInt("userGender", getGender);
			    	editor.putInt("userGeneration", getGeneration);
			    	editor.putBoolean("userVintage", getVintage);
			    	editor.putBoolean("userClassic", getClassic);
			    	editor.putBoolean("userCasual", getCasual);
			    	editor.putBoolean("userBoho", getBoho);
			    	editor.putBoolean("userSporty", getSporty);
			    	editor.putBoolean("userPreppy", getPreppy);
			    	editor.putBoolean("userDandy", getDandy);
			    	editor.putBoolean("userApple", getApple);
			    	editor.putBoolean("userBanana", getBanana);
			    	editor.putBoolean("userPear", getPear);
			    	editor.putBoolean("userHourglass", getHourglass);
			    	editor.putBoolean("userEcto", getEcto);
			    	editor.putBoolean("userMeso", getMeso);
			    	editor.putBoolean("userEndo", getEndo);
			    	editor.commit();
					
					      		
					finish();
	        		return true;
				}
					
				
        		
        	case R.id.profile_update_cancel:
        		
        		finish();
				return true;
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
    
    
    
    
    // RadioButton Event
    public void onRadioButtonClicked(View view){

        switch(view.getId()) {
            
        	case R.id.radioM:
        		profSettingTypeBoxM1.setVisibility(View.VISIBLE);
            	profSettingTypeBoxF1.setVisibility(View.INVISIBLE);
            	profSettingTypeBoxF2.setVisibility(View.INVISIBLE);
                break;
        	case R.id.radioF:
        		profSettingTypeBoxM1.setVisibility(View.INVISIBLE);
            	profSettingTypeBoxF1.setVisibility(View.VISIBLE);
            	profSettingTypeBoxF2.setVisibility(View.VISIBLE);
                break;
            case R.id.radioStyleVintage:
                if (vintageChecked){
                	radioStyleVintage.setChecked(false);
                	vintageChecked=false;
                }else {
                	radioStyleVintage.setChecked(true);
                	vintageChecked=true;
                }
                break;
            case R.id.radioStyleClassic:
            	if (classicChecked){
            		radioStyleClassic.setChecked(false);
                	classicChecked=false;
                }else {
                	radioStyleClassic.setChecked(true);
                	classicChecked=true;
                }
                break;
            case R.id.radioStyleCasual:
            	if (casualChecked){
            		radioStyleCasual.setChecked(false);
            		casualChecked=false;
                }else {
                	radioStyleCasual.setChecked(true);
                	casualChecked=true;
                }
                break;
            case R.id.radioStyleBoho:
            	if (bohoChecked){
            		radioStyleBoho.setChecked(false);
            		bohoChecked=false;
                }else {
                	radioStyleBoho.setChecked(true);
                	bohoChecked=true;
                }
                break;
            case R.id.radioStyleSporty:
            	if (sportyChecked){
            		radioStyleSporty.setChecked(false);
            		sportyChecked=false;
                }else {
                	radioStyleSporty.setChecked(true);
                	sportyChecked=true;
                }
                break;
            case R.id.radioStylePreppy:
            	if (preppyChecked){
            		radioStylePreppy.setChecked(false);
            		preppyChecked=false;
                }else {
                	radioStylePreppy.setChecked(true);
                	preppyChecked=true;
                }
                break;
            case R.id.radioStyleDandy:
            	if (dandyChecked){
            		radioStyleDandy.setChecked(false);
            		dandyChecked=false;
                }else {
                	radioStyleDandy.setChecked(true);
                	dandyChecked=true;
                }
                break;
            case R.id.radioTypeApple:
            	if (appleChecked){
            		radioTypeApple.setChecked(false);
            		appleChecked=false;
                }else {
                	radioTypeApple.setChecked(true);
                	appleChecked=true;
                }
                break;
            case R.id.radioTypeBanana:
            	if (bananaChecked){
            		radioTypeBanana.setChecked(false);
            		bananaChecked=false;
                }else {
                	radioTypeBanana.setChecked(true);
                	bananaChecked=true;
                }
                break;
            case R.id.radioTypePear:
            	if (pearChecked){
            		radioTypePear.setChecked(false);
            		pearChecked=false;
                }else {
                	radioTypePear.setChecked(true);
                	pearChecked=true;
                }
                break;   
            case R.id.radioTypeHourglass:
            	if (hourglassChecked){
            		radioTypeHourglass.setChecked(false);
            		hourglassChecked=false;
                }else {
                	radioTypeHourglass.setChecked(true);
                	hourglassChecked=true;
                }
                break;  
            case R.id.radioTypeEctomorph:
            	if (ectoChecked){
            		radioTypeEcto.setChecked(false);
            		ectoChecked=false;
                }else {
                	radioTypeEcto.setChecked(true);
                	ectoChecked=true;
                }
                break;   
            case R.id.radioTypeMesomorph:
            	if (mesoChecked){
            		radioTypeMeso.setChecked(false);
            		mesoChecked=false;
                }else {
                	radioTypeMeso.setChecked(true);
                	mesoChecked=true;
                }
                break;   
            case R.id.radioTypeEndomorph:
            	if (endoChecked){
            		radioTypeEndo.setChecked(false);
            		endoChecked=false;
                }else {
                	radioTypeEndo.setChecked(true);
                	endoChecked=true;
                }
                break;   
        }
    }
    
}
