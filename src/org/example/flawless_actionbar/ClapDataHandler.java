package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("ClapDataHandler")
public class ClapDataHandler extends ParseObject {
	
	public ClapDataHandler() {}
	
	public void insertClapData(ParseUser user, PhotoDataHandler photoData) {
		put("user", user);
		put("photo_data", photoData);
		saveInBackground();
	}
	
	// Return true if the user already clapped for this photo
	public static boolean isDuplicate(ParseUser user, PhotoDataHandler photoData) {
		boolean duplicate = false;
		
		ParseQuery<ClapDataHandler> query = ParseQuery.getQuery("ClapDataHandler");
		query.whereEqualTo("photo_data", photoData);
		query.whereEqualTo("user", user);
		try {
			if (query.getFirst() != null)
				duplicate = true;	// duplicate clap
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return duplicate;
	}
}