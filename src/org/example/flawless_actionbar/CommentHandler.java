package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("CommentDataHandler")
public class CommentHandler extends ParseObject {
	public CommentHandler() {}

	public void insertCommentData(ParseUser user, String comment) {
		put("user", user);
		//put("photoid", photoid);
		put("comment", comment);
		saveInBackground();
	}
}