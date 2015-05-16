package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ActivityFeed")
public class ActivityFeed extends ParseObject {
	public enum Type {
		COMMENT,
		SUBSCRIBE,
		CLAP
	}
	
	public ActivityFeed() {}
	
	// Constructor for comment and clap
	public void insertActivity(Type type, ParseUser fromUser, ParseUser toUser, PhotoDataHandler photoData) {
		put("type", type.ordinal());
		put("fromUserString", fromUser.getUsername());
		put("toUserString", toUser.getUsername());
		put("photoData", photoData);
		saveInBackground();
	}
	
	// Constructor for subscribe
	public void insertActivity(Type type, ParseUser fromUser, ParseUser toUser) {
		put("type", type.ordinal());
		put("fromUserString", fromUser.getUsername());
		put("toUserString", toUser.getUsername());
		saveInBackground();
	}
	
	public void saveInBackgroundActivityFeed() {
		saveInBackground();
	}
	
	public String print(Type type) {
		String res = getString("fromUserString");
		
		switch(type) {
		case COMMENT:
			res = res+" has commented on your photo.";
			break;
		case SUBSCRIBE:
			res = res+" has subscribed to you.";
			break;
		case CLAP:
			res = res+" has clapped to your photo.";
			break;
		}
		
		return res;
	}
}