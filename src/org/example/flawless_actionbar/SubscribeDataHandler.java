package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("SubscribeDataHandler")
public class SubscribeDataHandler extends ParseObject {
	public SubscribeDataHandler() {}
	
	public void insertSubscribeData(ParseUser fromUser, ParseUser toUser) {
		put("fromUser", fromUser);
		put("toUser", toUser);
		saveInBackground();
	}
	
	public boolean checkDuplicate(ParseUser fromUser, ParseUser toUser) {
		boolean duplicate = false;
		
		ParseQuery<SubscribeDataHandler> query = ParseQuery.getQuery("SubscribeDataHandler");
		query.whereEqualTo("fromUser", fromUser);
		query.whereEqualTo("toUser", toUser);
		try {
			if (query.getFirst() != null)
				duplicate = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return duplicate;
	}
}