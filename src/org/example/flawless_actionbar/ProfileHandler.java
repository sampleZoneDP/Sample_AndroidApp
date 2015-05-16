package org.example.flawless_actionbar;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ProfileHandler")
public class ProfileHandler extends ParseObject {
	public ProfileHandler() {}
	
	public void insertProfileData(String Name, int Height, int Weight, int Gender, int Age_Group, boolean Vintage,
			boolean Classic, boolean Casual, boolean Boho, boolean Sporty, boolean Preppy, boolean Dandy, boolean Apple,
			boolean Pear, boolean Banana, boolean Hourglass, boolean Ecto, boolean Meso, boolean Endo, String Blog) {
		put("Name", Name);
		put("Height", Height);
		put("Weight", Weight);
		put("Gender", Gender);
		put("Age_Group", Age_Group);
		put("Vintage", Vintage);
		put("Classic", Classic);
		put("Casual", Casual);
		put("Boho", Boho);
		put("Sporty", Sporty);
		put("Preppy", Preppy);
		put("Dandy",  Dandy);
		put("Apple", Apple);
		put("Pear", Pear);
		put("Banana", Banana);
		put("Hourglass", Hourglass);
		put("Ecto", Ecto);
		put("Meso", Meso);
		put("Endo", Endo);
		put("Blog", Blog);
		saveInBackground();
	}
}
	