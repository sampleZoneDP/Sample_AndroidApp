package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("CommentDataHandler")
public class CommentDataHandler extends ParseObject {
	public CommentDataHandler() {}
	
	public void insertCommentData(ParseUser user, String photoid, String comment) {
		put("user", user);
		put("photoid", photoid);
		put("comment", comment);
		saveInBackground();
	}
}
	