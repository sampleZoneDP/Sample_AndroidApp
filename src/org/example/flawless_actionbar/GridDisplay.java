package org.example.flawless_actionbar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;




class GridDisplay {
	
	View rootView;
	ParseUser user;
	Usage usage; // ALBUMS, ALL_PHOTOS, PHOTOS_IN_ALBUM, or TAGS
	int pos;
	Button albumTab;
	String[] albumNames;
	List<PhotoDataHandler> photoDataList;
	List<byte[]> photoFileList;
	
	int myProfile; // 0 if someone else's profile, 1 if mine
	
	public enum Usage {
		ALBUMS,
		ALL_PHOTOS,
		PHOTOS_IN_ALBUM,
		TAGS,
	}
	
	public GridDisplay(View v, ParseUser u, int p, Usage usage, Button albumButton) {
		if (u.hasSameId(ParseUser.getCurrentUser()))
			myProfile = 1;
		else
			myProfile = 0;
		
		rootView = v;
		user = u;
		this.usage = usage;
		if (usage == Usage.PHOTOS_IN_ALBUM)
			pos = p;
		albumTab = albumButton;
		
		int numAlbums;
		if (user.getList("albums") == null)
			numAlbums = 0;
		else
			numAlbums = user.getList("albums").size();
		
		albumNames = new String[numAlbums+1+myProfile];
        albumNames[0] = "All";
        
        if (numAlbums != 0)
        	System.arraycopy(user.getList("albums").toArray(new String[0]), 0, albumNames, 1, numAlbums);
        
        if (myProfile == 1)
        	albumNames[numAlbums+1] = "add_album";
		
		photoFileList = new ArrayList<byte[]>();
		
		if (usage == Usage.ALBUMS) { 
		} else {
			ParseQuery<PhotoDataHandler> query = ParseQuery.getQuery("PhotoDataHandler");
			query.whereEqualTo("user", user);
			
			switch(usage) {
				case ALL_PHOTOS:
					// query all photos
					break;
				case PHOTOS_IN_ALBUM:
					// query photos only in this album
					query.whereEqualTo("albums", albumNames[pos]);
					break;
				case TAGS:
					// Query search for taggable photos only
					break;
				default:
			}
			
			query.orderByDescending("createdAt");
			
			try {
				photoDataList = query.find();
				for (PhotoDataHandler pdh : photoDataList) {
	    			try {
	    				photoFileList.add(pdh.getParseFile("photo").getData());
	    			} catch (ParseException e1) {
	    				e1.printStackTrace();
	    			}
	    		}
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		
		// Callback method does not return any data. Maybe bug on Parse
		/*	
			query.findInBackground(new FindCallback<PhotoDataHandler>() {
				public void done(List<PhotoDataHandler> photoDataList, ParseException e) {
			    	if (e == null) {
			    		for (PhotoDataHandler pdh : photoDataList) {
			    			try {
			    				photoFileList.add(pdh.getParseFile("photo").getData());
			    			} catch (ParseException e1) {
			    				e1.printStackTrace();
			    			}
			    		}
			    	} else {
			    		Toast.makeText(rootView.getContext(), "Photo query problem", Toast.LENGTH_SHORT).show();
			    	}
			    }
			});
		*/
		}
	}
	
	// Display photos/albums in grid format, in bottom part of user profile
	public void display() {
		if (usage == Usage.ALBUMS) {
			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			ImageAdapter imageadapter = new ImageAdapter(rootView.getContext(), albumNames, null, true, myProfile);
			gridview.setAdapter(imageadapter);
			
			gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        	if (position == albumNames.length-1 && myProfile == 1) { // album add button
		        		AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());

		        		alert.setTitle("Name for the new album");

		        		// Set an EditText view to get user input 
		        		final EditText input = new EditText(rootView.getContext());
		        		alert.setView(input);

		        		alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
		        			@SuppressLint("NewApi")
		    				public void onClick(DialogInterface dialog, int whichButton) {
		        				String value = input.getText().toString();
		        				user.addUnique("albums", value); // save albums as a list of strings in user data
		        				user.saveInBackground();
		        				albumTab.callOnClick(); // refresh view after creating an album
		        			}
		        		});

		        		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        			public void onClick(DialogInterface dialog, int whichButton) {
		        		    // Canceled.
		        			}
		        		});

		        		alert.show();
		        	} else if (position == 0) { // All album
		        		GridDisplay gd = new GridDisplay(rootView, user, position, Usage.ALL_PHOTOS, albumTab);
		        		gd.display();
		        	} else { // photos in one particular album
		        		GridDisplay gd = new GridDisplay(rootView, user, position, Usage.PHOTOS_IN_ALBUM, albumTab);
		        		gd.display();
		        	}
		        }
			});
		} else if (usage == Usage.TAGS) {
			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			ImageAdapter imageadapter = new ImageAdapter(rootView.getContext(), albumNames, photoFileList, false, myProfile);
			gridview.setAdapter(imageadapter);
			
			gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        	PhotoTag phototag = new PhotoTag(rootView, v, photoDataList.get(position), myProfile);
		        	phototag.openDialog();
		        }
			});
		} else { // all photos or photos in an album
			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			ImageAdapter imageadapter = new ImageAdapter(rootView.getContext(), albumNames, photoFileList, false, myProfile);
			gridview.setAdapter(imageadapter);
		}
	}
	
    
}