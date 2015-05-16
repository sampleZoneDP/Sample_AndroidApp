package org.example.flawless_actionbar;

import org.json.JSONArray;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("PhotoDataHandler")
public class PhotoDataHandler extends ParseObject {
 
    public PhotoDataHandler() {
        // A default constructor is required.
    }
    
    public void insertPhotoData(ParseUser user, ParseFile photo, int height_range, int weight_range, int gender, 
    		int age_group, boolean vintage, boolean classic, boolean casual, boolean boho, boolean sporty,
			boolean preppy, boolean dandy, boolean apple, boolean pear, boolean banana, boolean hourglass,
			boolean ecto, boolean meso, boolean endo, boolean slim, boolean long_legs, boolean boyfriend,
			boolean glamorous, boolean chic, boolean badboy, int clap, JSONArray tagPeople, String description) {
		
		put("user", user);
		put("username", user.getString("username"));
		put("photo", photo);
		put("height_range", height_range);
		put("weight_range", weight_range);
		put("gender", gender);
		put("age_Group", age_group);
		put("vintage", vintage);
		put("classic", classic);
		put("casual", casual);
		put("boho", boho);
		put("sporty", sporty);
		put("preppy", preppy);
		put("dandy",  dandy);
		put("apple", apple);
		put("pear", pear);
		put("banana", banana);
		put("hourglass", hourglass);
		put("ecto", ecto);
		put("meso", meso);
		put("endo", endo);
		put("slim", slim);
		put("long_legs", long_legs);
		put("boyfriend", boyfriend);
		put("glamorous", glamorous);
		put("chic", chic);
		put("badboy", badboy);
		put("clip", 0);
		put("clap_count", 0);
		put("likes_previous", 0);
		put("likes_now", 0);
		put("likes_recent", 0);
		put("tag_people", tagPeople);
		put("description", description);
		
		//user profile photo 
		put("profilePhoto", user.getParseFile("profilePhoto"));
		
		saveInBackground();
    }
    
    public ParseUser getAuthor() {
    	return getParseUser("user");
    }
    
    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }
 
}