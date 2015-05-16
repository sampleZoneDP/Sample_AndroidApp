package org.example.flawless_actionbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.flawless_actionbar.ActivityFeed.Type;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a 
 * bigger preview image, the meal's rating, and a "favorite"
 * star. 
 */

public class PostsViewAdapter extends ParseQueryAdapter<PhotoDataHandler> {
	ProgressDialog mProgressDialog;
	static Context mContext;
	int clapCount;
	boolean alreadyClapped;
	
	private static int RESULT_COMMENT_USERPROFILE = 7;
	
	// sort by time, all posts
	public PostsViewAdapter(final Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<PhotoDataHandler>() {
			public ParseQuery<PhotoDataHandler> create() {
				mContext = context;
				
				// Here we can configure a ParseQuery to display
				// only top-rated PhotoDataHandler
				ParseQuery query = new ParseQuery("PhotoDataHandler");
				
				query.orderByDescending("createdAt");
				
				return query;
			}
		});
		
	}

	// sort by time, subscribed posts
	public PostsViewAdapter(Context context, final boolean onlySubscribed) {
		super(context, new ParseQueryAdapter.QueryFactory<PhotoDataHandler>() {
			public ParseQuery<PhotoDataHandler> create() {
				// Here we can configure a ParseQuery to display
				// only top-rated PhotoDataHandler
				ParseQuery query = new ParseQuery("PhotoDataHandler");
				
				if (onlySubscribed) {
					ParseQuery<SubscribeDataHandler> subsQuery = ParseQuery.getQuery("SubscribeDataHandler");
					subsQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
					try {
						if (subsQuery.getFirst() == null) {
							Toast.makeText(mContext, 
									"You have not subscribed to any users yet. Displaying all posts.", 
									Toast.LENGTH_SHORT).show();
						} else {
							query.whereMatchesKeyInQuery("user", "toUser", subsQuery);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				query.orderByDescending("createdAt");
				return query;
			}
		});
		
		mContext = context;
	}
	
	// sort by time, posted by current user
	public PostsViewAdapter(final Context context, final String user) {
		super(context, new ParseQueryAdapter.QueryFactory<PhotoDataHandler>() {
			public ParseQuery<PhotoDataHandler> create() {
				mContext = context;
				
				// Here we can configure a ParseQuery to display
				// only top-rated PhotoDataHandler
				ParseQuery query = new ParseQuery("PhotoDataHandler");
				
				query.whereEqualTo("username", user);
				query.orderByDescending("createdAt");
				return query;
			}
		});
	}
	
	// sort by given type # (0: mostly clapped; 1: recently liked)
	public PostsViewAdapter(final Context context, final int type) {
		super(context, new ParseQueryAdapter.QueryFactory<PhotoDataHandler>() {
			public ParseQuery<PhotoDataHandler> create() {
				mContext = context;
				
				// Here we can configure a ParseQuery to display
				// only top-rated PhotoDataHandler
				ParseQuery query = new ParseQuery("PhotoDataHandler");
				
				switch (type){
					case 0:
						query.orderByDescending("clap_count");
						break;
					case 1:
						query.orderByDescending("likes_recent");
						break;
					default:
						query.orderByDescending("createdAt");
				}
				
				return query;
			}
		});
	}
	
	// filtered posts in time order
	public PostsViewAdapter(final Context context, final SharedPreferences fsetting) {

		super(context, new ParseQueryAdapter.QueryFactory<PhotoDataHandler>() {
			public ParseQuery<PhotoDataHandler> create() {
				mContext = context;
				String userId;
				
				// Here we can configure a ParseQuery to display
				// only top-rated PhotoDataHandler
				ParseQuery query = new ParseQuery("PhotoDataHandler");
				
				// user check
				userId = fsetting.getString("userId", "");
				if (!userId.equals("")){
					query.whereEqualTo("username", userId);
				}
				
				
				// gender filter
				boolean gender; 
				if (fsetting.getBoolean("male", false)){
					gender = true;
					query.whereContainedIn("gender", Arrays.asList(0));
				} else {
					gender = false;
					query.whereContainedIn("gender", Arrays.asList(1));
				}

				// age group filter
				List<Integer> genList = new ArrayList<Integer>();
				if (fsetting.getBoolean("10", false)){
					genList.add(10);
				}if (fsetting.getBoolean("20", false)){
					genList.add(20);
				}if (fsetting.getBoolean("30", false)){
					genList.add(30);
				}if (fsetting.getBoolean("40", false)){
					genList.add(40);
				}if (!(genList.isEmpty())){
					query.whereContainedIn("age_Group", genList);
				}
				
				// Styles
				if (fsetting.getBoolean("vintage", false)){
					query.whereEqualTo("vintage", true);
				} if (fsetting.getBoolean("classic", false)){
					query.whereEqualTo("classic", true);
				} if (fsetting.getBoolean("casual", false)){
					query.whereEqualTo("casual", true);
				} if (fsetting.getBoolean("boho", false)){
					query.whereEqualTo("boho", true);
				} if (fsetting.getBoolean("sporty", false)){
					query.whereEqualTo("sporty", true);
				} if (fsetting.getBoolean("preppy", false)){
					query.whereEqualTo("preppy", true);
				} if (fsetting.getBoolean("dandy", false)){
					query.whereEqualTo("dandy", true);
				}
				
				
				// gender depends: body types
				if (gender){
					if (fsetting.getBoolean("ecto", false)){
						query.whereEqualTo("ecto", true);
					}if (fsetting.getBoolean("meso", false)){
						query.whereEqualTo("meso", true);
					}if (fsetting.getBoolean("endo", false)){
						query.whereEqualTo("endo", true);
					}if (fsetting.getBoolean("badboy", false)){
						query.whereEqualTo("badboy", true);
					}if (fsetting.getBoolean("chic", false)){
						query.whereEqualTo("chic", true);
					}

				} else {
					if (fsetting.getBoolean("apple", false)){
						query.whereEqualTo("apple", true);
					}if (fsetting.getBoolean("pear", false)){
						query.whereEqualTo("pear", true);
					}if (fsetting.getBoolean("banana", false)){
						query.whereEqualTo("banana", true);
					}if (fsetting.getBoolean("hourglass", false)){
						query.whereEqualTo("hourglass", true);
					}if (fsetting.getBoolean("boyfriend", false)){
						query.whereEqualTo("boyfriend", true);
					}if (fsetting.getBoolean("glamorous", false)){
						query.whereEqualTo("glamorous", true);
					}
				}
				
				if (fsetting.getBoolean("longleg", false)){
					query.whereEqualTo("long_legs", true);
				}if (fsetting.getBoolean("glamorous", false)){
					query.whereEqualTo("glamorous", true);
				}

				// Height group filter
				int heightLow, heightHigh, weightLow, weightHigh, heightloop, weightloop, i;
				heightLow = fsetting.getInt("heightLow", 0);
				heightHigh = fsetting.getInt("heightHigh", 10);
				weightLow = fsetting.getInt("weightLow", 0);
				weightHigh = fsetting.getInt("weightHigh", 12);
				
				List<Integer> HeightList = new ArrayList<Integer>();
				List<Integer> WeightList = new ArrayList<Integer>();
				heightloop = heightHigh - heightLow;
				weightloop = weightHigh - weightLow;
			
				i = heightLow;
				while(i <= heightHigh){
					HeightList.add(i);
					i++;
				}
				i = weightLow;
				while(i <= weightHigh){
					WeightList.add(i);
					i++;
				}

				query.whereContainedIn("height_range", HeightList);
				query.whereContainedIn("weight_range", WeightList);
				
				// in updated time order
				query.orderByDescending("createdAt");
				return query;
			}
		});
		
	}

	@Override
	public View getItemView(final PhotoDataHandler pData, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.posts_view_layout, null);
		}
		super.getItemView(pData, v, parent);
		
		ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon);

		ParseFile photoFile = pData.getParseFile("photo");
		if (photoFile != null) {
			postImage.setParseFile(photoFile);
			postImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
				}
			});
		}
     
		// device screen size & image size reset
      	Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      	int deviceWidth = display.getWidth();
    	int deviceHeight = display.getHeight();
    	int imgSize = Math.min(deviceWidth, deviceHeight);
    	
		LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(imgSize, imgSize * 10 / 9);
		postImage.setLayoutParams(params);

		ParseFile profile_imgFile;
		String nameUser = null;
		String pDate = null;
				
		// get user info

		// set user id
		final TextView titleTextView = (TextView) v.findViewById(R.id.postViewUserId);
		nameUser = pData.getString("username");
		titleTextView.setText(nameUser);
			
		// click event (user id)
		titleTextView.setClickable(true);
		titleTextView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (!(pData.getString("username").equals(ParseUser.getCurrentUser().getString("username")))){
					((Activity)mContext).getIntent().putExtra("clicked user", pData.getAuthor().getObjectId());
					((Activity)mContext).registerForContextMenu(titleTextView);
					((Activity)mContext).openContextMenu(titleTextView);
					((Activity)mContext).unregisterForContextMenu(titleTextView);
			
				}
			}
		});

		
		// user profile image
		ImageView postViewProfileImg = (ImageView) v.findViewById(R.id.postViewProfileImg);
		profile_imgFile = (ParseFile)pData.getParseFile("profilePhoto");
		byte[] byte_imgFile;
		if (profile_imgFile != null){
			try {
				byte_imgFile = profile_imgFile.getData();
				Bitmap profile_bitmap = BitmapFactory.decodeByteArray(byte_imgFile,0,byte_imgFile.length);
				postViewProfileImg.setImageBitmap(profile_bitmap);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// set description
		TextView detail_preview = (TextView) v.findViewById(R.id.detail_preview);
		String description = pData.getString("description");
		if (description != null)
			detail_preview.setText(description);
		else
			detail_preview.setText("No Description");
		
		// set date
		TextView dateTextView = (TextView) v.findViewById(R.id.postViewUpdateDate);
		pDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format((pData.getCreatedAt()));
		dateTextView.setText(pDate);
		
		// show "clap" count
		final TextView clap_count_view = (TextView) v.findViewById(R.id.poseViewclap_count);
		clapCount = pData.getInt("clap_count");
		clap_count_view.setText(Integer.toString(clapCount));
		
		// "clap" button
		ImageView clapButton = (ImageView) v.findViewById(R.id.clap_btn);
		final ClapDataHandler clapData = new ClapDataHandler();
		final ActivityFeed feed = new ActivityFeed();
		
		// new thread for getting clap count
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		    	alreadyClapped = ClapDataHandler.isDuplicate(ParseUser.getCurrentUser(), pData);
		    }
		};
		thread.start();
		
		clapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (alreadyClapped) {}
					// User already clapped to this photo. Now unclap?
				else {
					// immediately display +=1 count in only view level
					clapCount = pData.getInt("clap_count");
					clapCount++;
					clap_count_view.setText(Integer.toString(clapCount));
					
					// insert actual clap data
					clapData.insertClapData(ParseUser.getCurrentUser(), pData);
					
					// correspondingly update clap count
					pData.increment("clap_count");
					
					// to track hotness. has to be used with hotness cloud code
					pData.increment("likes_now");
					pData.increment("likes_recent");
					
					pData.saveEventually();
					
					// insert feed data
					try {
						feed.insertActivity(Type.CLAP, ParseUser.getCurrentUser(), pData.getAuthor().fetch(), pData);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					// update clap duplicate
					alreadyClapped = true;
				}
			}
		});
		
		// comments button
		Button commentsButton = (Button) v.findViewById(R.id.comment_btn);
		commentsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), AllCommentActivity.class);
				intent.putExtra("photoid", pData.getObjectId());
				((Activity)v.getContext()).startActivityForResult(intent, RESULT_COMMENT_USERPROFILE);
			}
		});
				
		return v;
	}

}
