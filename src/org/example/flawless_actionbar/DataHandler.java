package org.example.flawless_actionbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler {


	public static final String USERID = "userid";
	//User Table Column_names
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String GENDER = "gender";
	public static final String GENERATION = "generation";
	public static final String HEIGHT = "height";
	public static final String WEIGHT = "weight";
	//Style
	public static final String VINTAGE = "vintage";
	public static final String CLASSIC = "classic";
	public static final String FORMAL = "formal";
	//Body type
	public static final String APPLE = "apple";
	public static final String BOTTLENECK = "bottleneck";
	public static final String PEAR = "pear";
	public static final String BLOG = "blog";

	//Favoriting, Favorited, Posts
	public static final String FAVORITING = "favoriting";
	public static final String FAVORITED = "favorited";
	public static final String POST = "post";

	//PhotoTable Column names
	public static final String PID = "photoid";
	public static final String P_DIR = "photo_directory";
	public static final String P_GENDER = "photo_gender";
	public static final String P_GENERATION = "photo_generation";
	public static final String P_LOOK = "photo_look";
	public static final String HEIGHT_RANGE = "height_range";
	public static final String WEIGHT_RANGE = "weight_range";
	public static final String HASHTAG = "hashtage";
	public static final String DATE = "date";

	//TableName, DatabaseName
	public static final String USER_TABLE = "UserTable";
	public static final String PHOTO_TABLE = "PhotoTable";
	public static final String DATA_BASE_NAME = "mydatabase";


	public static final int DATABASE_VERSION = 1;
	public static final String USER_TABLE_CREATE = "create table "+USER_TABLE+
			" ( " + USERID + " text PRIMARY KEY not null, " +NAME+ 
			" text not null, " +PASSWORD+ " text not null, " +EMAIL+ " text not null, "
			+ GENDER + " INT, "+GENERATION+ " INT, " +HEIGHT+ " INT, "+WEIGHT+" INT, "+VINTAGE+ " boolean , "
			+CLASSIC+" boolean , "+FORMAL+" boolean , "+APPLE+" boolean, "+BOTTLENECK+" boolean , "+PEAR+" boolean , "
			+BLOG+ " text, "  +FAVORITING+ " INT default 0, " +FAVORITED+ " INT default 0, " +POST+ " INT default 0);";

	public static final String PHOTO_TABLE_CREATE = "create table "+PHOTO_TABLE+
			" ( " + PID + " INTEGER PRIMARY KEY   AUTOINCREMENT, " +USERID + " text not null, " +P_DIR+ " text, " +VINTAGE+ 
			" boolean, " +CLASSIC+ " boolean, " +FORMAL+ " boolean, " +APPLE+ 
			" boolean, " +BOTTLENECK+ " boolean, " +PEAR+ " boolean, " +P_GENDER+ " boolean, "
			+ P_GENERATION + " INT, " +P_LOOK+ " boolean, " +HEIGHT_RANGE+ 
			" INT, " +WEIGHT_RANGE+ " INT, "+HASHTAG+" text, "+DATE+ " timestamp);";

	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler(Context ctx) {
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}

	public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}

	public static class DataBaseHelper extends SQLiteOpenHelper
	{
		public DataBaseHelper(Context ctx){
			super(ctx, DATA_BASE_NAME, null, DATABASE_VERSION);
		}
		public void onCreate(SQLiteDatabase db){
			try {
				db.execSQL(USER_TABLE_CREATE);
				Log.e("msg", "User Table Created");
				db.execSQL(PHOTO_TABLE_CREATE);
				Log.e("Photo msg", "Photo table created");
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}


		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS" + PHOTO_TABLE);
			onCreate(db);
		}
	}

	public DataHandler open(){
		db = dbhelper.getWritableDatabase();
		return this;
	}

	public void close(){
		dbhelper.close();
	}

	public long insertUserData(String userid, String name, String password, String email){
		ContentValues content = new ContentValues();
		content.put(USERID, userid);
		content.put(NAME, name);
		content.put(PASSWORD, password);
		content.put(EMAIL, email);

		return db.insertOrThrow(USER_TABLE, null, content);

	}

	public long insertPhotoData(String userid, String directory, boolean vintage, boolean classic, boolean formal, boolean apple,
			boolean bottleneck, boolean pear, boolean gender, int generation, int look, int heightR
			, int weightR, String hashtag) {
		ContentValues content = new ContentValues();
		content.put(USERID, userid);
		content.put(P_DIR, directory);
		content.put(VINTAGE, vintage);
		content.put(CLASSIC, classic);
		content.put(FORMAL, formal);
		content.put(APPLE, apple);
		content.put(BOTTLENECK, bottleneck);
		content.put(PEAR, pear);
		content.put(P_GENDER, gender);
		content.put(P_GENERATION, generation);
		content.put(P_LOOK, look);
		content.put(HEIGHT_RANGE, heightR);
		content.put(WEIGHT_RANGE, weightR);
		content.put(HASHTAG, hashtag);
		content.put(DATE, getDateTime());

		return db.insertOrThrow(PHOTO_TABLE, null, content);
	}


	public int updateUserData(String userid, String name, String email, int generation, int gender, int height, int weight, 
			String blog, boolean vintage, boolean classic, boolean formal, boolean apple, boolean bottleneck, boolean pear){

		ContentValues content = new ContentValues();
		content.put(NAME, name);
		content.put(EMAIL, email);
		content.put(GENDER, gender);
		content.put(GENERATION, generation);
		content.put(HEIGHT, height);
		content.put(WEIGHT, weight);
		content.put(BLOG, blog);
		content.put(VINTAGE, vintage);
		content.put(CLASSIC, classic);
		content.put(FORMAL, formal);
		content.put(APPLE, apple);
		content.put(BOTTLENECK, bottleneck);
		content.put(PEAR, pear);
		String[] whereArgs = {userid};
		return db.update(USER_TABLE, content, "userid = ?", whereArgs);

	}

	public long PhotoCount(String userid, int original, int update){
		ContentValues content = new ContentValues();
		int newValue = original + update;
		content.put(POST, newValue);
		String[] WhereArgs = {userid};
		return db.update(USER_TABLE, content , "userid = ?", WhereArgs);
	}

	public long updatePhotoData(String pid, boolean vintage, boolean classic, boolean formal, int look) {
		ContentValues content = new ContentValues();
		content.put(VINTAGE, vintage);
		content.put(CLASSIC, classic);
		content.put(FORMAL, formal);
		content.put(P_LOOK,look);
		String[] whereArgs = {pid};
		return db.update(PHOTO_TABLE, content, "pid = ?", whereArgs);
	}


	public Cursor returnUserData(){
		return db.query(USER_TABLE, new String[] {USERID, NAME, PASSWORD, EMAIL, GENDER, GENERATION, HEIGHT, WEIGHT, 
				VINTAGE, CLASSIC, FORMAL, APPLE, BOTTLENECK, PEAR, BLOG, 
				FAVORITING, FAVORITED, POST}, null, null, null, null, null);
	}
	public Cursor returnPhotoData(){
		return db.query(PHOTO_TABLE, new String[] {PID, USERID, P_DIR, VINTAGE, CLASSIC, FORMAL, APPLE, 
				BOTTLENECK, PEAR, P_GENDER, P_GENERATION, P_LOOK, 
				HEIGHT_RANGE, WEIGHT_RANGE, HASHTAG, DATE}, null, null, null, null, null);
	}


}