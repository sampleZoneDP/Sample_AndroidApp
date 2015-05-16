package org.example.flawless_actionbar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.ParseException;

class PhotoTag {
	
	View rootView;
	View v;
	PhotoDataHandler photoData;
	int myProfile;
	
	List<String> tagInfoList;
	List<String> tempTagList;
	Bitmap photo;
	
	Bitmap orangeCircle;
	
	ScrollView sv;
	LinearLayout ll;
	
	Typeface tf;
	Paint textPaint;
	Rect textRect;
	
	public PhotoTag(View rView, View photoIconView, PhotoDataHandler p, int myprofile) {
		rootView = rView;
		v = photoIconView;
		photoData = p;
		myProfile = myprofile;
		
		tagInfoList = photoData.getList("tags");
		if (tagInfoList == null)	// initialize if no tags were added to this photo before
			tagInfoList = new ArrayList<String>();
		
		tempTagList = new ArrayList<String>();
		
		byte[] photoByteArray = new byte[0];
		try {
			photoByteArray = photoData.getPhotoFile().getData();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		photo = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.length).copy(Bitmap.Config.ARGB_8888, true);
		
		orangeCircle = BitmapFactory.decodeResource(rootView.getContext().getResources() ,R.drawable.circle_orange);
	
		sv = new ScrollView(rootView.getContext());
		ll = new LinearLayout(rootView.getContext());
		
		tf = Typeface.create("Helvetica", Typeface.BOLD);
		textPaint = new Paint();
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.BLACK);
		textPaint.setTypeface(tf);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(10);
		textRect = new Rect();
	}
	
	// Dialog for adding/seeing tags in a photo
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void openDialog() {
		ll.setOrientation(1); // vertical

	    final ImageView iv = new ImageView(rootView.getContext());
	        	
	    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
	        photo.setHeight(photo.getWidth());
	        	
	    Canvas canvas = new Canvas(photo);
        		
        LinearLayout[] taglineList = new LinearLayout[tagInfoList.size()]; // list of tag descriptions to be displayed to users
	    for (int i=0; i<tagInfoList.size(); i++) {
	    	String s = tagInfoList.get(i);
			/*
			 * Format of tag info is NNN_X_Y_DESCRIPTION
			 * where NNN is a 3-digit tag index,
			 * X is the x-coordinate and Y is the y-coordinate of the tag,
			 * and DESCRIPTION is the full tag description
			 */
	        if (s.startsWith(String.format("%03d", i)+"_")) {
	        				
	        	float positionX = Float.parseFloat(s.split("_",4)[1]);
	        	float positionY = Float.parseFloat(s.split("_",4)[2]);
	        				
	        	// Mark the point in photo
	        	Bitmap tagIndexBitmap = Bitmap.createScaledBitmap(orangeCircle.copy(Bitmap.Config.ARGB_8888, true), 16, 16, false);
	        	canvas.drawBitmap(tagIndexBitmap, positionX-8, positionY-8, new Paint());
	        	textPaint.getTextBounds(Integer.toString(tagInfoList.size()+tempTagList.size()), 0, 1, textRect);
		        canvas.drawText(Integer.toString(i+1), positionX, positionY+5, textPaint);
		            		
		        // A line for one tag
		        LinearLayout tagline = new LinearLayout(rootView.getContext());
		            		
	        	// Show the tag index as an icon
		        ImageView tagIndexImage = new ImageView(rootView.getContext());
				Canvas tagIndexCanvas = new Canvas(tagIndexBitmap);
	        	tagIndexCanvas.drawText(Integer.toString(i+1), 
	        			tagIndexBitmap.getWidth()/2,
	        			tagIndexBitmap.getHeight()/2+5,
	        			textPaint);
	        	tagIndexImage.setImageBitmap(tagIndexBitmap);
	        	tagIndexImage.setBaselineAlignBottom(true);
	        	tagIndexImage.setPadding(5, 0, 0, 0);
	        	tagline.addView(tagIndexImage);
		            		
	        	// Followed by tag name
		        TextView tagText = new TextView(rootView.getContext());
				tagText.setText(" "+ s.split("_",4)[3]); // discard the prefix and coordinates
	        	tagText.setTextSize(20);
	        	tagline.addView(tagText);
	        	tagline.setGravity(Gravity.CENTER_VERTICAL);
	        	taglineList[i] = tagline;
	        }
	    }
	        	
	    iv.setImageBitmap(photo);
	    ll.addView(iv);
		// add tag index and description one by one
	    for (int j=0; j<taglineList.length; j++) {
	        ll.addView(taglineList[j]);
	    }
	    
		// start the dialog
	    AlertDialog.Builder dialog = new AlertDialog.Builder(rootView.getContext());
	    if (myProfile == 1)
	    	dialog.setTitle("Add tags to photo");
	    if (myProfile == 0)
	    	dialog.setTitle("Tags");
	    
	    sv.addView(ll);
	    dialog.setView(sv);
	    
	 if (myProfile == 1) { // let the user be able to add tags
	    iv.setOnTouchListener(new OnTouchListener() {
			@Override
	        public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					//-- These values are being weird. We should keep eyes on them. --//
					final float posX=event.getX();
	                final float posY=event.getY();
	                    
	                AlertDialog.Builder addTagDialog = new AlertDialog.Builder(v.getContext());
	                addTagDialog.setTitle("Add description for your tag");
	                    
	                final EditText input = new EditText(v.getContext());
	                addTagDialog.setView(input);
	                    
	                addTagDialog.setPositiveButton("Add tag", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// format the tag info
							String tagPrefix = String.format("%03d", tagInfoList.size()+tempTagList.size());
							String tagname = input.getText().toString();
							tempTagList.add(tagPrefix + "_" + 
										   Float.toString(posX) + "_" +
										   Float.toString(posY) + "_" +
										   tagname);
								
			            	Canvas canvas = new Canvas(photo);
			            		
			            	Bitmap tagIndexBitmap = Bitmap.createScaledBitmap(orangeCircle.copy(Bitmap.Config.ARGB_8888, true), 16, 16, false);
		        			canvas.drawBitmap(tagIndexBitmap, posX-8, posY-8, new Paint());
			            	textPaint.getTextBounds(Integer.toString(tagInfoList.size()+tempTagList.size()), 0, 1, textRect);
			            	canvas.drawText(Integer.toString(tagInfoList.size()+tempTagList.size()), posX, posY+5, textPaint);
							iv.setImageBitmap(photo);
								
							// A line for one tag
			            	LinearLayout tagline = new LinearLayout(rootView.getContext());
			            		
		        			// Show the tag index as an icon
			            	ImageView tagIndexImage = new ImageView(rootView.getContext());
		        			Canvas tagIndexCanvas = new Canvas(tagIndexBitmap);
		        			tagIndexCanvas.drawText(Integer.toString(tagInfoList.size()+tempTagList.size()), 
		        					tagIndexBitmap.getWidth()/2,
		        					tagIndexBitmap.getHeight()/2+5,
		        					textPaint);
		        			tagIndexImage.setImageBitmap(tagIndexBitmap);
		        			tagIndexImage.setBaselineAlignBottom(true);
		        			tagIndexImage.setPadding(5, 0, 0, 0);
		        			tagline.addView(tagIndexImage);
			            	
		        			// Followed by tag name
			            	TextView tagText = new TextView(rootView.getContext());
		        			tagText.setText(" "+ tagname);
		        			tagText.setTextSize(20);
		        			tagline.addView(tagText);
		        			tagline.setGravity(Gravity.CENTER_VERTICAL);
							
		        			ll.addView(tagline);
						}
			        });
			        	
			        addTagDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        	@Override
		        		public void onClick(DialogInterface dialog, int whichButton) {
		        			tempTagList.clear();
		        		}
		        	});
	                    
			        addTagDialog.show();
				}
	        	return true;
			}
	    });
	        		
	    dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (String s : tempTagList) {
					photoData.addUnique("tags", s);
				}
				photoData.saveInBackground();
				tempTagList.clear();
			}
	    });
	    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    		// Canceled.
	    	}
	    });    	
	    
	} else { // looking at other user's photo tags
		dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Close the tag window
			}
		});    
	}
	dialog.show();
	}
}
