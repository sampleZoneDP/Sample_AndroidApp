package org.example.flawless_actionbar;

import org.example.flawless_actionbar.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HomeSettingActivity extends ActionBarActivity {

	static // Shared Data (for filter setting)
	SharedPreferences fsetting;
	
	private static int defaultWeightFrom = 40;
	private static int defaultWeightTo = 100;
	private static int defaultHeightFrom = 140;
	private static int defaultHeightTo = 190;
	
	// TextView displaying weight & height range
	TextView heightFrom, heightTo, weightFrom, weightTo;
	EditText filterUserId;
	static RadioButton radioGender, radioGeneration, radioStyleVintage, radioStyleClassic, radioStyleCasual, radioStyleBoho, radioStyleSporty, radioStylePreppy, radioStyleDandy, radioStyleExtra1, radioStyleExtra2,
		radioTypeApple, radioTypeBanana, radioTypePear, radioTypeHourglass,
		radioTypeEcto, radioTypeMeso, radioTypeEndo, 
		radioM, radioF, radio10, radio20, radio30, radio40,
		radioLookSlim, radioLookLongLeg, radioLookChic, radioLookBadboy, radioLookBoyfriend, radioLookGlamorous;
	RadioGroup radioGroupGender, radioGroupGeneration;
	static boolean vintageChecked, classicChecked, casualChecked, bohoChecked, sportyChecked, preppyChecked, dandyChecked, 
		appleChecked, bananaChecked, pearChecked, hourglassChecked, ectoChecked, mesoChecked, endoChecked, radioMChecked, radioFChecked
		, radio10Checked, radio20Checked, radio30Checked, radio40Checked, slimChecked, longLegChecked,
		chicChecked, badboyChecked, boyfriendChecked, glamorousChecked;
	static LinearLayout filterSettingTypeBoxM1, filterSettingTypeBoxF1, filterSettingTypeBoxF2,
		filterLookBoxM1, filterLookBoxF1;
	String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_setting);
		fsetting = getSharedPreferences("filter", MODE_PRIVATE);		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// TextViews to display changes in Seekbar
		heightFrom = (TextView)findViewById(R.id.heightFrom);
		heightTo = (TextView)findViewById(R.id.heightTo);
		weightFrom = (TextView)findViewById(R.id.weightFrom);
		weightTo = (TextView)findViewById(R.id.weightTo);
		filterSettingTypeBoxM1 = (LinearLayout)findViewById(R.id.filterSettingTypeBoxM1);
		filterSettingTypeBoxF1 = (LinearLayout)findViewById(R.id.filterSettingTypeBoxF1);
		filterSettingTypeBoxF2 = (LinearLayout)findViewById(R.id.filterSettingTypeBoxF2);
		filterLookBoxM1 = (LinearLayout)findViewById(R.id.filterLookBoxM1);
		filterLookBoxF1 = (LinearLayout)findViewById(R.id.filterLookBoxF1);
        
		// Radio Buttons for Filter
		radioStyleVintage = (RadioButton)findViewById(R.id.filterRadioStyleVintage);
		radioStyleClassic = (RadioButton)findViewById(R.id.filterRadioStyleClassic);
		radioStyleCasual = (RadioButton)findViewById(R.id.filterRadioStyleCasual);
		radioStyleBoho = (RadioButton)findViewById(R.id.filterRadioStyleBoho);
		radioStyleSporty = (RadioButton)findViewById(R.id.filterRadioStyleSporty);
		radioStylePreppy = (RadioButton)findViewById(R.id.filterRadioStylePreppy);
		radioStyleDandy = (RadioButton)findViewById(R.id.filterRadioStyleDandy);
		radioTypeApple = (RadioButton)findViewById(R.id.filterRadioTypeApple);
		radioTypeBanana = (RadioButton)findViewById(R.id.filterRadioTypeBanana);
		radioTypePear = (RadioButton)findViewById(R.id.filterRadioTypePear);  
		radioTypeHourglass = (RadioButton)findViewById(R.id.filterRadioTypeHourglass); 
		radioTypeEcto = (RadioButton)findViewById(R.id.filterRadioTypeEctomorph); 
		radioTypeMeso = (RadioButton)findViewById(R.id.filterRadioTypeMesomorph); 
		radioTypeEndo = (RadioButton)findViewById(R.id.filterRadioTypeEndomorph); 
		radioLookSlim = (RadioButton)findViewById(R.id.filterLookSlim);
		radioLookLongLeg = (RadioButton)findViewById(R.id.filterLookLongLeg);
		radioLookChic = (RadioButton)findViewById(R.id.filterLookChic);
		radioLookBadboy = (RadioButton)findViewById(R.id.filterLookBadboy);
		radioLookBoyfriend = (RadioButton)findViewById(R.id.filterLookBoyFriend);
		radioLookGlamorous = (RadioButton)findViewById(R.id.filterLookGlamorous); 
		radioM = (RadioButton)findViewById(R.id.filterRadioM);
		radioF = (RadioButton)findViewById(R.id.filterRadioF);
		radio10 = (RadioButton)findViewById(R.id.filterRadio10);
		radio20 = (RadioButton)findViewById(R.id.filterRadio20);
		radio30 = (RadioButton)findViewById(R.id.filterRadio30);
		radio40 = (RadioButton)findViewById(R.id.filterRadio40);
		filterUserId = (EditText)findViewById(R.id.filterUserId);
		
		// set user filter setting from data
		defaultHeightFrom = fsetting.getInt("heightLowSpec", defaultHeightFrom);
		defaultHeightTo =fsetting.getInt("heightHighSpec", defaultHeightTo);
		defaultWeightFrom = fsetting.getInt("weightLowSpec", defaultWeightFrom);
		defaultWeightTo = fsetting.getInt("weightHighSpec", defaultWeightTo);

		radioStyleVintage.setChecked(fsetting.getBoolean("vintage", false));
		radioStyleClassic.setChecked(fsetting.getBoolean("classic", false));
		radioStyleCasual.setChecked(fsetting.getBoolean("casual", false));
		radioStyleBoho.setChecked(fsetting.getBoolean("boho", false));
		radioStyleSporty.setChecked(fsetting.getBoolean("sporty", false));
		radioStylePreppy.setChecked(fsetting.getBoolean("preppy", false));
		radioStyleDandy.setChecked(fsetting.getBoolean("dandy", false));
		
		radioTypeApple.setChecked(fsetting.getBoolean("apple", false));
		radioTypeBanana.setChecked(fsetting.getBoolean("banana", false));
		radioTypePear.setChecked(fsetting.getBoolean("pear", false));
		radioTypeHourglass.setChecked(fsetting.getBoolean("hourglass", false));
		radioTypeEcto.setChecked(fsetting.getBoolean("ecto", false));
		radioTypeMeso.setChecked(fsetting.getBoolean("meso", false));
		radioTypeEndo.setChecked(fsetting.getBoolean("endo", false));
				
		radioLookSlim.setChecked(fsetting.getBoolean("slim", false)); 
		radioLookLongLeg.setChecked(fsetting.getBoolean("longleg", false));
		radioLookChic.setChecked(fsetting.getBoolean("chic", false)); 
		radioLookBadboy.setChecked(fsetting.getBoolean("badboy", false));
		radioLookBoyfriend.setChecked(fsetting.getBoolean("boyfriend", false));
		radioLookGlamorous.setChecked(fsetting.getBoolean("glamorous", false));
		
		radioM.setChecked(fsetting.getBoolean("male", true));
		radioF.setChecked(fsetting.getBoolean("female", false));
		radio10.setChecked(fsetting.getBoolean("10", false));
		radio20.setChecked(fsetting.getBoolean("20", false));
		radio30.setChecked(fsetting.getBoolean("30", false));
		radio40.setChecked(fsetting.getBoolean("40", false));

		// set visibility for gender dependencies
		if (radioM.isChecked()){
			filterSettingTypeBoxM1.setVisibility(View.VISIBLE);
    		filterSettingTypeBoxF1.setVisibility(View.INVISIBLE);
    		filterSettingTypeBoxF2.setVisibility(View.INVISIBLE);
    		filterLookBoxF1.setVisibility(View.INVISIBLE);
    		filterLookBoxM1.setVisibility(View.VISIBLE);
		} else if (radioF.isChecked()){
			filterSettingTypeBoxM1.setVisibility(View.INVISIBLE);
    		filterSettingTypeBoxF1.setVisibility(View.VISIBLE);
    		filterSettingTypeBoxF2.setVisibility(View.VISIBLE);
    		filterLookBoxF1.setVisibility(View.VISIBLE);
    		filterLookBoxM1.setVisibility(View.INVISIBLE);
		}
		
		// reset ratio buttons
		vintageChecked = radioStyleVintage.isChecked(); 
		classicChecked = radioStyleClassic.isChecked(); 
		bohoChecked = radioStyleBoho.isChecked();
		sportyChecked = radioStyleSporty.isChecked();
		preppyChecked = radioStylePreppy.isChecked();
		dandyChecked = radioStyleDandy.isChecked();
		appleChecked = radioTypeApple.isChecked(); 
		bananaChecked = radioTypeBanana.isChecked();
		pearChecked = radioTypePear.isChecked(); 
		hourglassChecked = radioTypeHourglass.isChecked(); 
		ectoChecked = radioTypeEcto.isChecked(); 
		mesoChecked = radioTypeMeso.isChecked(); 
		endoChecked = radioTypeEndo.isChecked(); 
		slimChecked = radioLookSlim.isChecked();
		longLegChecked = radioLookLongLeg.isChecked();
		chicChecked = radioLookChic.isChecked();
		badboyChecked = radioLookBadboy.isChecked();
		boyfriendChecked = radioLookBoyfriend.isChecked();
		glamorousChecked = radioLookGlamorous.isChecked();
		radioMChecked = radioM.isChecked();
		radioFChecked = radioF.isChecked(); 
		radio10Checked = radio10.isChecked();
		radio20Checked = radio20.isChecked();
		radio30Checked = radio30.isChecked();
		radio40Checked = radio40.isChecked();
						
		// ranged seekbar
		RangeSeekBar<Integer> heightSeekBar = new RangeSeekBar<Integer>(140, 190, getBaseContext());
		heightSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				// handle changed range values
				
				heightFrom.setText(String.valueOf(minValue));
				heightTo.setText(String.valueOf(maxValue));
			}
		});

        RangeSeekBar<Integer> weightSeekBar = new RangeSeekBar<Integer>(40, 100, getBaseContext());
        weightSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                    // handle changed range values
                        
                    weightFrom.setText(String.valueOf(minValue));
                    weightTo.setText(String.valueOf(maxValue));
            }
        });
			        
        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.setting_Layout);
        weightFrom.setText(String.valueOf(defaultWeightFrom));
        weightTo.setText(String.valueOf(defaultWeightTo));
        heightFrom.setText(String.valueOf(defaultHeightFrom));
        heightTo.setText(String.valueOf(defaultHeightTo));
        layout.addView(heightSeekBar);
		layout.addView(weightSeekBar);
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_setting, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
    		case R.id.home_settings_cancel:
    			finish();
    			return true;
    		case R.id.home_settings_apply:
    			// Put data in a file
				int heightLow = Integer.parseInt(heightFrom.getText().toString());
				int heightHigh = Integer.parseInt(heightTo.getText().toString());
				int weightLow = Integer.parseInt(weightFrom.getText().toString());
				int weightHigh = Integer.parseInt(weightTo.getText().toString());
				int heightLowIndex, heightHighIndex, weightLowIndex, weightHighIndex;
				
				// set height & weight range
				// height low
				if (heightLow < 145){
					heightLowIndex = 0;
				}else if (145 <= heightLow && heightLow < 150){
					heightLowIndex = 1;
				}else if (150 <= heightLow && heightLow < 155){
					heightLowIndex = 2;
				}else if (155 <= heightLow && heightLow < 160){
					heightLowIndex = 3;
				}else if (160 <= heightLow && heightLow < 165){
					heightLowIndex = 4;
				}else if (165 <= heightLow && heightLow < 170){
					heightLowIndex = 5;
				}else if (170 <= heightLow && heightLow < 175){
					heightLowIndex = 6;
				}else if (175 <= heightLow && heightLow < 180){
					heightLowIndex = 7;
				}else if (180 <= heightLow && heightLow < 185){
					heightLowIndex = 8;
				}else if (185 <= heightLow && heightLow < 190){
					heightLowIndex = 9;
				}else if (190 <= heightLow){
					heightLowIndex = 10;
				}else {
					heightLowIndex = 0;
				}
				
				// height high
				if (heightHigh < 145){
					heightHighIndex = 0;
				}else if (145 <= heightHigh && heightHigh < 150){
					heightHighIndex = 1;
				}else if (150 <= heightHigh && heightHigh < 155){
					heightHighIndex = 2;
				}else if (155 <= heightHigh && heightHigh < 160){
					heightHighIndex = 3;
				}else if (160 <= heightHigh && heightHigh < 165){
					heightHighIndex = 4;
				}else if (165 <= heightHigh && heightHigh < 170){
					heightHighIndex = 5;
				}else if (170 <= heightHigh && heightHigh < 175){
					heightHighIndex = 6;
				}else if (175 <= heightHigh && heightHigh < 180){
					heightHighIndex = 7;
				}else if (180 <= heightHigh && heightHigh < 185){
					heightHighIndex = 8;
				}else if (185 <= heightHigh && heightHigh < 190){
					heightHighIndex = 9;
				}else if (190 <= heightHigh){
					heightHighIndex = 10;
				}else {
					heightHighIndex = 10;
				}
				
				// weight low
				if (weightLow < 45){
					weightLowIndex = 0;
				}else if (45 <= weightLow && weightLow < 50){
					weightLowIndex = 1;
				}else if (50 <= weightLow && weightLow < 55){
					weightLowIndex = 2;
				}else if (55 <= weightLow && weightLow < 60){
					weightLowIndex = 3;
				}else if (60 <= weightLow && weightLow < 65){
					weightLowIndex = 4;
				}else if (65 <= weightLow && weightLow < 70){
					weightLowIndex = 5;
				}else if (70 <= weightLow && weightLow < 75){
					weightLowIndex = 6;
				}else if (75 <= weightLow && weightLow < 80){
					weightLowIndex = 7;
				}else if (80 <= weightLow && weightLow < 85){
					weightLowIndex = 8;
				}else if (85 <= weightLow && weightLow < 90){
					weightLowIndex = 9;
				}else if (90 <= weightLow && weightLow < 95){
					weightLowIndex = 10;
				}else if (95 <= weightLow && weightLow < 100){
					weightLowIndex = 11;
				}else if (100 <= weightLow){
					weightLowIndex = 12;
				}else {
					weightLowIndex = 0;
				}
				
				// weight High
				if (weightHigh < 45){
					weightHighIndex = 0;
				}else if (45 <= weightHigh && weightHigh < 50){
					weightHighIndex = 1;
				}else if (50 <= weightHigh && weightHigh < 55){
					weightHighIndex = 2;
				}else if (55 <= weightHigh && weightHigh < 60){
					weightHighIndex = 3;
				}else if (60 <= weightHigh && weightHigh < 65){
					weightHighIndex = 4;
				}else if (65 <= weightHigh && weightHigh < 70){
					weightHighIndex = 5;
				}else if (70 <= weightHigh && weightHigh < 75){
					weightHighIndex = 6;
				}else if (75 <= weightHigh && weightHigh < 80){
					weightHighIndex = 7;
				}else if (80 <= weightHigh && weightHigh < 85){
					weightHighIndex = 8;
				}else if (85 <= weightHigh && weightHigh < 90){
					weightHighIndex = 9;
				}else if (90 <= weightHigh && weightHigh < 95){
					weightHighIndex = 10;
				}else if (95 <= weightHigh && weightHigh < 100){
					weightHighIndex = 11;
				}else if (100 <= weightHigh){
					weightHighIndex = 12;
				}else {
					weightHighIndex = 12;
				}
			
				SharedPreferences.Editor editor = fsetting.edit();
				editor.putInt("heightLow", heightLowIndex);
				editor.putInt("heightHigh", heightHighIndex);
				editor.putInt("weightLow", weightLowIndex);
				editor.putInt("weightHigh", weightHighIndex);
				editor.putInt("heightLowSpec", heightLow);
				editor.putInt("heightHighSpec", heightHigh);
				editor.putInt("weightLowSpec", weightLow);
				editor.putInt("weightHighSpec", weightHigh);
				editor.putBoolean("vintage", radioStyleVintage.isChecked());
				editor.putBoolean("classic", radioStyleClassic.isChecked());
				editor.putBoolean("casual", radioStyleCasual.isChecked());
				editor.putBoolean("boho", radioStyleBoho.isChecked());
				editor.putBoolean("sporty", radioStyleSporty.isChecked());
				editor.putBoolean("preppy", radioStylePreppy.isChecked());
				editor.putBoolean("dandy", radioStyleDandy.isChecked());
				editor.putBoolean("apple", radioTypeApple.isChecked());
				editor.putBoolean("banana", radioTypeBanana.isChecked());
				editor.putBoolean("pear", radioTypePear.isChecked());
				editor.putBoolean("hourglass", radioTypeHourglass.isChecked());
				editor.putBoolean("ecto", radioTypeEcto.isChecked());
				editor.putBoolean("meso", radioTypeMeso.isChecked());
				editor.putBoolean("endo", radioTypeEndo.isChecked());
				editor.putBoolean("slim", radioLookSlim.isChecked());
				editor.putBoolean("longleg", radioLookLongLeg.isChecked());
				editor.putBoolean("chic", radioLookChic.isChecked());
				editor.putBoolean("badboy", radioLookBadboy.isChecked());
				editor.putBoolean("boyfriend", radioLookBoyfriend.isChecked());
				editor.putBoolean("glamorous",  radioLookGlamorous.isChecked());
				
				editor.putBoolean("male", radioM.isChecked());
				editor.putBoolean("female", radioF.isChecked());
				editor.putBoolean("10", radio10.isChecked());
				editor.putBoolean("20", radio20.isChecked());
				editor.putBoolean("30", radio30.isChecked());
				editor.putBoolean("40", radio40.isChecked());
				
				userId = filterUserId.getText().toString();
				editor.putString("userId", userId);
				editor.commit();
				
				Intent intent = new Intent();
    			setResult(Activity.RESULT_OK, intent);
				finish();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
		}
	}
	
	 // RadioButton Event
    public void onRadioButtonClicked(View view){

        switch(view.getId()) {
            
        	case R.id.filterRadioM:
        		filterSettingTypeBoxM1.setVisibility(View.VISIBLE);
        		filterSettingTypeBoxF1.setVisibility(View.INVISIBLE);
        		filterSettingTypeBoxF2.setVisibility(View.INVISIBLE);
        		filterLookBoxM1.setVisibility(View.VISIBLE);
        		filterLookBoxF1.setVisibility(View.INVISIBLE);
        		radioMChecked = true;
        		radioFChecked = false;
        		radioF.setChecked(false);
                break;
        	case R.id.filterRadioF:
        		filterSettingTypeBoxM1.setVisibility(View.INVISIBLE);
        		filterSettingTypeBoxF1.setVisibility(View.VISIBLE);
        		filterSettingTypeBoxF2.setVisibility(View.VISIBLE);
        		filterLookBoxF1.setVisibility(View.VISIBLE);
        		filterLookBoxM1.setVisibility(View.INVISIBLE);
        		radioFChecked = true;
        		radioMChecked = false;
        		radioM.setChecked(false);
                break;
        	case R.id.filterRadio10:
                if (radio10Checked){
                	radio10.setChecked(false);
                	radio10Checked=false;
                }else {
                	radio10.setChecked(true);
                	radio10Checked=true;
                }
                break;
        	case R.id.filterRadio20:
                if (radio20Checked){
                	radio20.setChecked(false);
                	radio20Checked=false;
                }else {
                	radio20.setChecked(true);
                	radio20Checked=true;
                }
                break;
        	case R.id.filterRadio30:
                if (radio30Checked){
                	radio30.setChecked(false);
                	radio30Checked=false;
                }else {
                	radio30.setChecked(true);
                	radio30Checked=true;
                }
                break;
        	case R.id.filterRadio40:
                if (radio40Checked){
                	radio40.setChecked(false);
                	radio40Checked=false;
                }else {
                	radio40.setChecked(true);
                	radio40Checked=true;
                }
                break;
            case R.id.filterRadioStyleVintage:
                if (vintageChecked){
                	radioStyleVintage.setChecked(false);
                	vintageChecked=false;
                }else {
                	radioStyleVintage.setChecked(true);
                	vintageChecked=true;
                }
                break;
            case R.id.filterRadioStyleClassic:
            	if (classicChecked){
            		radioStyleClassic.setChecked(false);
                	classicChecked=false;
                }else {
                	radioStyleClassic.setChecked(true);
                	classicChecked=true;
                }
                break;
            case R.id.filterRadioStyleCasual:
            	if (casualChecked){
            		radioStyleCasual.setChecked(false);
            		casualChecked=false;
                }else {
                	radioStyleCasual.setChecked(true);
                	casualChecked=true;
                }
                break;
            case R.id.filterRadioStyleBoho:
            	if (bohoChecked){
            		radioStyleBoho.setChecked(false);
            		bohoChecked=false;
                }else {
                	radioStyleBoho.setChecked(true);
                	bohoChecked=true;
                }
                break;
            case R.id.filterRadioStyleSporty:
            	if (sportyChecked){
            		radioStyleSporty.setChecked(false);
            		sportyChecked=false;
                }else {
                	radioStyleSporty.setChecked(true);
                	sportyChecked=true;
                }
                break;
            case R.id.filterRadioStylePreppy:
            	if (preppyChecked){
            		radioStylePreppy.setChecked(false);
            		preppyChecked=false;
                }else {
                	radioStylePreppy.setChecked(true);
                	preppyChecked=true;
                }
                break;
            case R.id.filterRadioStyleDandy:
            	if (dandyChecked){
            		radioStyleDandy.setChecked(false);
            		dandyChecked=false;
                }else {
                	radioStyleDandy.setChecked(true);
                	dandyChecked=true;
                }
                break;
            case R.id.filterRadioTypeApple:
            	if (appleChecked){
            		radioTypeApple.setChecked(false);
            		appleChecked=false;
                }else {
                	radioTypeApple.setChecked(true);
                	appleChecked=true;
                }
                break;
            case R.id.filterRadioTypeBanana:
            	if (bananaChecked){
            		radioTypeBanana.setChecked(false);
            		bananaChecked=false;
                }else {
                	radioTypeBanana.setChecked(true);
                	bananaChecked=true;
                }
                break;
            case R.id.filterRadioTypePear:
            	if (pearChecked){
            		radioTypePear.setChecked(false);
            		pearChecked=false;
                }else {
                	radioTypePear.setChecked(true);
                	pearChecked=true;
                }
                break;   
            case R.id.filterRadioTypeHourglass:
            	if (hourglassChecked){
            		radioTypeHourglass.setChecked(false);
            		hourglassChecked=false;
                }else {
                	radioTypeHourglass.setChecked(true);
                	hourglassChecked=true;
                }
                break;  
            case R.id.filterRadioTypeEctomorph:
            	if (ectoChecked){
            		radioTypeEcto.setChecked(false);
            		ectoChecked=false;
                }else {
                	radioTypeEcto.setChecked(true);
                	ectoChecked=true;
                }
                break;   
            case R.id.filterRadioTypeMesomorph:
            	if (mesoChecked){
            		radioTypeMeso.setChecked(false);
            		mesoChecked=false;
                }else {
                	radioTypeMeso.setChecked(true);
                	mesoChecked=true;
                }
                break;   
            case R.id.filterRadioTypeEndomorph:
            	if (endoChecked){
            		radioTypeEndo.setChecked(false);
            		endoChecked=false;
                }else {
                	radioTypeEndo.setChecked(true);
                	endoChecked=true;
                }
                break;   
            case R.id.filterLookSlim:
            	if (slimChecked){
            		radioLookSlim.setChecked(false);
            		slimChecked=false;
            	}else {
            		radioLookSlim.setChecked(true);
            		slimChecked=true;
            	}
            	break;
            case R.id.filterLookLongLeg:
            	if (longLegChecked){
            		radioLookLongLeg.setChecked(false);
            		longLegChecked=false;
            	}else {
            		radioLookLongLeg.setChecked(true);
            		longLegChecked=true;
            	}
            	break;
            case R.id.filterLookChic:
            	if (chicChecked){
            		radioLookChic.setChecked(false);
            		chicChecked=false;
            	}else {
            		radioLookChic.setChecked(true);
            		chicChecked=true;
            	}
            	break;
            case R.id.filterLookBadboy:
            	if (badboyChecked){
            		radioLookBadboy.setChecked(false);
            		badboyChecked=false;
            	}else {
            		radioLookBadboy.setChecked(true);
            		badboyChecked=true;
            	}
            	break;
            case R.id.filterLookBoyFriend:
            	if (boyfriendChecked){
            		radioLookBoyfriend.setChecked(false);
            		boyfriendChecked=false;
            	}else {
            		radioLookBoyfriend.setChecked(true);
            		boyfriendChecked=true;
            	}
            	break;
            case R.id.filterLookGlamorous:
            	if (glamorousChecked){
            		radioLookGlamorous.setChecked(false);
            		glamorousChecked=false;
            	}else {
            		radioLookGlamorous.setChecked(true);
            		glamorousChecked=true;
            	}
            	break;         
        }
    }
    
}

