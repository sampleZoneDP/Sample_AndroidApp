package org.example.flawless_actionbar;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;


public class MainActivity extends ActionBarActivity {

	
	// rootView and rootActivity
	static View rootView;
	static Activity rootActivity;
    static LinearLayout logo_layout;
    static ActionBar actionbar;
  
    // contents layouts
    static RelativeLayout contentsLayout, contentsLayoutHome, contentsLayoutHistory,
    contentsLayoutAllpost, contentsLayoutProfile, contentsLayoutProfileUser;
    
    // inflate checks
    static boolean ihome, iallpost, ihistory, iprofile, anew, ahot, abest, pfeed, pnews;
    
	// logo Layout
	static boolean logo_on = true;
	
	// login status
	static boolean login = true;
	
	// Animations
	static Animation translateRightAnim, slideViewAnim;
	
	// image size
	static int imgSize;
	
	static // Shared Data (for user info)
	SharedPreferences pref;
	static SharedPreferences fsetting;
	
	private static int RESULT_LOAD_PROFILE_PICTURE = 2;
	private static int RESULT_LOGIN = 3;
	private static int CAMERA_ACT = 4;
	private static int FILTER_ACT = 5;
	private static int CROP_IMAGE = 6;
	private static int RESULT_COMMENT_USERPROFILE = 7;
	
	// Set the limit of objects to show
    private static int loadLimit = 3;
    ProgressDialog mProgressDialog;
    
    // Parse data display View
 	static ListView homeView_main;
	
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayShowHomeEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // parse initialization
        ParseObject.registerSubclass(PhotoDataHandler.class);
        ParseObject.registerSubclass(ProfileHandler.class);
        ParseObject.registerSubclass(CommentDataHandler.class);
        ParseObject.registerSubclass(ActivityFeed.class);
        ParseObject.registerSubclass(ClapDataHandler.class);
        ParseObject.registerSubclass(SubscribeDataHandler.class);
        Parse.initialize(this, "SevVTEOjCQDpWtgF8c3ZYDlQZkLrZOJNU5B9meMz", "1tVvmG7OPQ8336LBQ6cnPHtiHb2JtVLEoYRQJcs2");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Animation Event
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        slideViewAnim = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
        
        // device screen size
      	Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      	int deviceWidth = display.getWidth();
    	int deviceHeight = display.getHeight();
    	imgSize = Math.min(deviceWidth, deviceHeight);
        
    	// shared data
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        fsetting = getSharedPreferences("filter", MODE_PRIVATE);
        
        SlidingPageAnimationListener animListener1 = new SlidingPageAnimationListener();
        SlidingPageAnimationListener animListener2 = new SlidingPageAnimationListener();
        translateRightAnim.setAnimationListener(animListener1);
        slideViewAnim.setAnimationListener(animListener2);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
    }

	// Slide Animation Listener
	private class SlidingPageAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (logo_on){
				logo_layout.setVisibility(View.INVISIBLE);
				logo_on = false;
			} else{
				logo_on = true;
			}
			
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
        	case R.id.home_settings:
        		inflate_home_setting();
        		return true;
        	case R.id.home_logout:
        		user_logout();
        		return true;
        	case R.id.home_refresh:
        		if (ihome){
        			inflate_home();
        		} else if(ihistory){
        			inflate_history();
        			init_tabs(1);
        		} else if(iallpost){
        			inflate_allPosts();
        			init_tabs(0);
        		} else if(iprofile){
        			inflate_profile();
        		} else {
        			Toast.makeText(rootActivity, "Wrong View Refreshing", Toast.LENGTH_SHORT).show();
        		}
        		return true;
            default:
            	return super.onOptionsItemSelected(item);
        }

    }
    
    // id click event
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
        menu.setHeaderTitle(((TextView)v).getText());  // user name as menu title
        menu.add(0, v.getId(), 0, "User profile for "+((TextView)v).getText());  
        menu.add(0, v.getId(), 0, "Subscribe "+((TextView)v).getText());  
    }  
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {
		// receiving data of user clicked from MainActivity
    	String objectId = getIntent().getStringExtra("clicked user");
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	ParseUser thisUser;
		try {
			thisUser = query.get(objectId);
			if(item.getTitle().toString().startsWith("User profile for")) {
				infate_profile_user(thisUser);
			} else if(item.getTitle().toString().startsWith("Subscribe")) {
	        	if (thisUser.hasSameId(ParseUser.getCurrentUser())) {
					Toast.makeText(rootView.getContext(), "You cannot subscribe yourself!", Toast.LENGTH_LONG).show();
					return false;
				} else {
					// Subscribe is many-to-many relation
					SubscribeDataHandler sdh = new SubscribeDataHandler();
					if (sdh.checkDuplicate(ParseUser.getCurrentUser(), thisUser)) {
						Toast.makeText(rootView.getContext(), "You already subscribed to this user.", Toast.LENGTH_LONG).show();
						return false;
					}
					sdh.insertSubscribeData(ParseUser.getCurrentUser(), thisUser);
					
					ActivityFeed feed = new ActivityFeed();
					feed.insertActivity(ActivityFeed.Type.SUBSCRIBE, ParseUser.getCurrentUser(), thisUser);
					
					// update cache
					pref.edit().putInt("userFavoriting", pref.getInt("userFavoriting", 0)+1);
					pref.edit().commit();
					
					Toast.makeText(rootView.getContext(), "You subscribed to "+thisUser.getUsername(), Toast.LENGTH_LONG).show();
				}
	        } else {
	        	return false;
	        }  
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return true;  
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rootActivity = getActivity();
            actionbar= rootActivity.getActionBar();

            // Buttons at the below: Home, History, Camera, Profile, Favorite
            final Button home_btn = (Button)rootView.findViewById(R.id.home_btn);
            final Button camera_btn = (Button)rootView.findViewById(R.id.camera_btn);
            final Button history_btn = (Button)rootView.findViewById(R.id.history_btn);
            final Button profile_btn = (Button)rootView.findViewById(R.id.profile_btn);
            final Button allpost_btn = (Button)rootView.findViewById(R.id.allpost_btn);
            contentsLayout = (RelativeLayout)rootView.findViewById(R.id.contentsLayout);
            contentsLayoutHome = (RelativeLayout)rootView.findViewById(R.id.contentsLayoutHome);
            contentsLayoutHistory = (RelativeLayout)rootView.findViewById(R.id.contentsLayoutHistory);
            contentsLayoutAllpost = (RelativeLayout)rootView.findViewById(R.id.contentsLayoutAllpost);
            contentsLayoutProfile = (RelativeLayout)rootView.findViewById(R.id.contentsLayoutProfile);
            contentsLayoutProfileUser = (RelativeLayout)rootView.findViewById(R.id.contentsLayoutOtherUserProfile);
            
            logo_layout = (LinearLayout)rootView.findViewById(R.id.logo_layout);    
            
            home_btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				Toast.makeText(getActivity(), "Home Button Tap", Toast.LENGTH_LONG).show();
    				if (!ihome){
    					// button change
    					btn_color_reset();
    					home_btn.setBackgroundColor(Color.parseColor("#ff373737"));
    					
    					// Change Title
    					ActionBar abar = rootActivity.getActionBar();
    					abar.setTitle("Favorite");
    					
    					// inflate check
    			    	ihome = true;
    			    	iallpost = false;
    			    	ihistory = false;
    			    	iprofile = false;
    			    	
    					// set visibility
    			    	visibility_view(0);
    	                
    	                actionbar.removeAllTabs();
    	                actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    					//inflate_home();
    				}
    			}
    						
    		});
            
            camera_btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				Toast.makeText(getActivity(), "Camera Button Tap", Toast.LENGTH_LONG).show();
    				contentsLayout.removeAllViews();
    				inflate_camera();

    			}
    						
    		});
            
            history_btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				Toast.makeText(getActivity(), "History Button Tap", Toast.LENGTH_LONG).show();
    				if (!ihistory){
    					// button change
    					btn_color_reset();
    		            history_btn.setBackgroundColor(Color.parseColor("#ff373737"));
    		            
    		            // Change Title
    		    		ActionBar abar = rootActivity.getActionBar();
    		    		abar.setTitle("History");
    		    		
    		            // inflate check
    		        	ihome = false;
    		        	iallpost = false;
    		        	ihistory = true;
    		        	iprofile = false;
    		        	
    		            // set visibility
    		        	visibility_view(2);
    	                
    	                init_tabs(1);
    				}
    			}
    						
    		});
            
            profile_btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				Toast.makeText(getActivity(), "Profile Button Tap", Toast.LENGTH_LONG).show();
    				if (!iprofile){
    					// button change
    					btn_color_reset();
    		            profile_btn.setBackgroundColor(Color.parseColor("#ff373737"));
    		            
    		            // Change Title 
    		    		ActionBar abar = rootActivity.getActionBar();
    		    		abar.setTitle(pref.getString("userId", "Profile"));
    		    		
    		            // inflate check
    		        	ihome = false;
    		        	iallpost = false;
    		        	ihistory = false;
    		        	iprofile = true;
    		        	
    		            // set visibility
    		        	visibility_view(3);
    	                
    		        	// remove existing tabs
    	                actionbar.removeAllTabs();
    	                actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    				}
    			}
    						
    		});
            
            allpost_btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				Toast.makeText(getActivity(), "Hot Button Tap", Toast.LENGTH_LONG).show();
    				if (!iallpost){
    					// button change
    					btn_color_reset();
    		            allpost_btn.setBackgroundColor(Color.parseColor("#ff373737"));
    		            
    		            // Change Title
    		    		ActionBar abar = rootActivity.getActionBar();
    		    		abar.setTitle("All Posts");
    		    		
    		            // inflate check
    		        	ihome = false;
    		        	iallpost = true;
    		        	ihistory = false;
    		        	iprofile = false;
    		        	
    		            // set visibility
    		        	visibility_view(1);
    	             
    	                // initialize tabs in action bar
    	                init_tabs(0);
    				}
    			}
    						
    		});
            
            if (login){
            	inflate_login();
            }

            return rootView;
        }
    }
    
    private static void inflate_login(){
    	Intent intent = new Intent(rootView.getContext(), LoginRegister.class);
    	rootActivity.startActivityForResult(intent, RESULT_LOGIN);
    }
    
    private static void inflate_home(){	

    	// set visibility
    	visibility_view(0);
        contentsLayoutHome.removeAllViews();
        
    	Context mContext = rootView.getContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.home_main, contentsLayoutHome, true);
		
		homeView_main = (ListView)rootView.findViewById(R.id.homeView_main);
		
		// Parse data display View
		final ListView homeView_main = (ListView)rootView.findViewById(R.id.homeView_main);
        // custom adapter View (display posts)
        PostsViewAdapter postsViewAdapter;
        postsViewAdapter = new PostsViewAdapter(rootActivity);
        postsViewAdapter.loadObjects();
        homeView_main.setAdapter(postsViewAdapter);	

    }
    
    private static void inflate_home_setting(){
    	Intent intent = new Intent(rootView.getContext(), HomeSettingActivity.class);
    	rootActivity.startActivityForResult(intent, FILTER_ACT);
    }
    
    private static void inflate_camera(){
        Intent intent = new Intent(rootView.getContext(), AndroidCamera.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        rootActivity.startActivityForResult(intent, CAMERA_ACT);
    }
    
    private static void inflate_history(){

    	// set visibility
    	visibility_view(2);
        contentsLayoutHistory.removeAllViews();
        
    	Context mContext = rootView.getContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.history_main, contentsLayoutHistory, true);
		
		final ListView listview = (ListView) rootView.findViewById(R.id.feedListView);  	                    			

		ParseQuery<ActivityFeed> query = ParseQuery.getQuery("ActivityFeed");
		query.whereEqualTo("toUserString", ParseUser.getCurrentUser().getUsername());
		query.orderByDescending("createdAt");
		
		query.findInBackground(new FindCallback<ActivityFeed>() {
		    public void done(List<ActivityFeed> list, ParseException e) {
		    	String[] statements = new String[list.size()];
		    	
		    	for (int i=0; i<list.size(); i++) {
		    		statements[i] = 
		    		/* statement */	list.get(i).print(ActivityFeed.Type.values()[list.get(i).getInt("type")])
		    		/* newline	*/	+ System.getProperty("line.separator") 
		    		/* date */		+ DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT).format(list.get(i).getCreatedAt());
		    	}
		    	// design layout is shitty for now
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, statements);
		    	listview.setAdapter(adapter);
		    }
		});
		
    }
    
    private static void inflate_allPosts(){
    	
    	// set visibility
    	visibility_view(1);
        contentsLayoutAllpost.removeAllViews();
        
        contentsLayoutAllpost.removeAllViews();
    	Context mContext = rootView.getContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.allpost_main, contentsLayoutAllpost, true);
		
		final ListView allpostView_new = (ListView)rootView.findViewById(R.id.allpostView_new);
		final ListView allpostView_hot = (ListView)rootView.findViewById(R.id.allpostView_hot);
		final FrameLayout allpostOtherContents = (FrameLayout)rootView.findViewById(R.id.allpostOtherContents);
		
		// custom adapter View (display posts)
    	PostsViewAdapter postsViewAdapter1;
		postsViewAdapter1 = new PostsViewAdapter(rootActivity, fsetting);
		postsViewAdapter1.loadObjects();
		allpostView_new.setAdapter(postsViewAdapter1);	
		
		PostsViewAdapter postsViewAdapter2;
		postsViewAdapter2 = new PostsViewAdapter(rootActivity, 1);
		postsViewAdapter2.loadObjects();
		allpostView_hot.setAdapter(postsViewAdapter2);	
		
    }
    
    // other user profile
    private static void infate_profile_user(final ParseUser user){

    	// set visibility
    	visibility_view(4);
        contentsLayoutProfileUser.removeAllViews();
        
        // inflate layout
        Context mContext = rootView.getContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.profile_main, contentsLayoutProfileUser, true);
		
		// Change Title & Add Actionbar Tabs 
		ActionBar abar = rootActivity.getActionBar();
		abar.setTitle(user.getUsername());
				
		// Buttons
		final Button album_btn = (Button)rootView.findViewById(R.id.album_btn);
		final Button allPictures_btn = (Button)rootView.findViewById(R.id.allPictures_btn);
		final Button tags_btn = (Button)rootView.findViewById(R.id.tags_btn);
		final Button setting_btn = (Button)rootView.findViewById(R.id.profileSetting_btn);
		
		// change button text
		if (!(ParseUser.getCurrentUser().hasSameId(user)))
			setting_btn.setText(R.string.subscribe);

		// update numbers of favorited, favoriting, and pictures uploaded
		TextView favorited = (TextView)rootView.findViewById(R.id.favorited);
		TextView favoriting = (TextView)rootView.findViewById(R.id.favoriting);
		TextView pictures = (TextView)rootView.findViewById(R.id.pics);
		TextView profUserName = (TextView)rootView.findViewById(R.id.profUserName);
		ImageView profile_picture = (ImageView)rootView.findViewById(R.id.profile_picture);
		
		// get user data (counting)
		// number of Favorited
		ParseQuery<SubscribeDataHandler> followersQuery = ParseQuery.getQuery("SubscribeDataHandler");
		followersQuery.whereEqualTo("toUser", user);
		followersQuery.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				int numFavorited;
				if (e == null) {
					numFavorited = count;
				} else {
					numFavorited = 0;
				}
				String stringFavorited = String.valueOf(numFavorited);
				TextView favorited = (TextView)rootView.findViewById(R.id.favorited);
				favorited.setText(stringFavorited);
			}
		});
					
		// number of Favoriting
		ParseQuery<SubscribeDataHandler> favoritingQuery = ParseQuery.getQuery("SubscribeDataHandler");
		favoritingQuery.whereEqualTo("fromUser", user);
		favoritingQuery.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				int numFavoriting;
				if (e == null) {
					numFavoriting = count;
				} else {
					numFavoriting = 0;
				}
				String stringFavoriting = String.valueOf(numFavoriting);
				TextView favoriting = (TextView)rootView.findViewById(R.id.favoriting);
				favoriting.setText(stringFavoriting);
			}
		});
		
		// number of pictures
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoDataHandler");
		query.whereEqualTo("user", user);
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				int numPictures;
				if (e == null) {
					numPictures = count;
				} else {
					// something went wrong
					numPictures = 0;
				}
				String stringPictures = String.valueOf(numPictures);
				TextView pictures = (TextView)rootView.findViewById(R.id.pics);
				pictures.setText(stringPictures);
			}
		});

		final String stringUserName = user.getUsername();
		
		// Image View & set profile picture (if exist)
		ParseFile profile_imgFile = (ParseFile)user.getParseFile("profilePhoto");
			
		byte[] byte_imgFile;
		if (profile_imgFile != null){
			try {
				byte_imgFile = profile_imgFile.getData();
				Bitmap profile_bitmap = BitmapFactory.decodeByteArray(byte_imgFile,0,byte_imgFile.length);
				profile_picture.setImageBitmap(profile_bitmap);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Inflate to show all pictures (default View)
		inflate_allPictures(user.getUsername());
			
		album_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
		    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
		    	
		    	userProfileAllView.setVisibility(View.INVISIBLE);
		    	gridview.setVisibility(View.VISIBLE);
		    	
				Toast.makeText(rootView.getContext(), "Album Button Tap", Toast.LENGTH_LONG).show();
				
				(new GridDisplay(rootView, user, 0, GridDisplay.Usage.ALBUMS, album_btn)).display();
			}
						
		});
		
		allPictures_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "All Album Button Tap", Toast.LENGTH_LONG).show();
				inflate_allPictures(stringUserName);
			}
						
		});
		
		tags_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// custom adapter View (display posts made by the current user)
		    	ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
		    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
		    	
		    	userProfileAllView.setVisibility(View.INVISIBLE);
		    	gridview.setVisibility(View.VISIBLE);
		    	
				Toast.makeText(rootView.getContext(), "Tags Button Tap", Toast.LENGTH_SHORT).show();

				(new GridDisplay(rootView, user, 0, GridDisplay.Usage.TAGS, tags_btn)).display();
			}
						
		});
		
		setting_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// only subscribe other users
				if (!(ParseUser.getCurrentUser().hasSameId(user))) { 
					SubscribeDataHandler sdh = new SubscribeDataHandler();
					if (sdh.checkDuplicate(ParseUser.getCurrentUser(), user)) {
						Toast.makeText(rootView.getContext(), "You already subscribed to this user.", Toast.LENGTH_LONG).show();
						return;
					}
					sdh.insertSubscribeData(ParseUser.getCurrentUser(), user);
					
					ActivityFeed feed = new ActivityFeed();
					feed.insertActivity(ActivityFeed.Type.SUBSCRIBE, ParseUser.getCurrentUser(), user);
					
					pref.edit().putInt("userFavoriting", pref.getInt("userFavoriting", 0)+1);
					pref.edit().commit();
					
					Toast.makeText(rootView.getContext(), "You subscribed to "+user.getUsername(), Toast.LENGTH_LONG).show();
				}
			}			
		});
        
    }
    
    // user profile
    private static void inflate_profile(){

    	// set visibility
    	visibility_view(3);
        contentsLayoutProfile.removeAllViews();
        
        // inflate layout
    	Context mContext = rootView.getContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.profile_main, contentsLayoutProfile, true);
		
		// Buttons
		final Button album_btn = (Button)rootView.findViewById(R.id.album_btn);
		Button allPictures_btn = (Button)rootView.findViewById(R.id.allPictures_btn);
		final Button tags_btn = (Button)rootView.findViewById(R.id.tags_btn);
		Button setting_btn = (Button)rootView.findViewById(R.id.profileSetting_btn);
		
		// update numbers of favorited, favoriting, and pictures uploaded
		TextView favorited = (TextView)rootView.findViewById(R.id.favorited);
		TextView favoriting = (TextView)rootView.findViewById(R.id.favoriting);
		TextView pictures = (TextView)rootView.findViewById(R.id.pics);
    	ImageView profile_picture = (ImageView)rootView.findViewById(R.id.profile_picture);
		String stringFavorited = String.valueOf(pref.getInt("userFavorited", 0));
		String stringFavoriting = String.valueOf(pref.getInt("userFavoriting", 0));
		String stringPictures = String.valueOf(pref.getInt("userPictures", 0));
		final String stringUserName = pref.getString("userId", "");

		favorited.setText(stringFavorited);
		favoriting.setText(stringFavoriting);
		pictures.setText(stringPictures);
		// Image View & set profile picture (if exist)
		final ParseUser currentUser = ParseUser.getCurrentUser();
		ParseFile profile_imgFile = (ParseFile)currentUser.getParseFile("profilePhoto");
		
		byte[] byte_imgFile;
		if (profile_imgFile != null){
			try {
				byte_imgFile = profile_imgFile.getData();
				Bitmap profile_bitmap = BitmapFactory.decodeByteArray(byte_imgFile,0,byte_imgFile.length);
				profile_picture.setImageBitmap(profile_bitmap);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Inflate to show all pictures (default View)
		inflate_allPictures(stringUserName);
		
		album_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
		    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
		    	
		    	userProfileAllView.setVisibility(View.INVISIBLE);
		    	gridview.setVisibility(View.VISIBLE);
		    	
				Toast.makeText(rootView.getContext(), "Album Button Tap", Toast.LENGTH_LONG).show();
				
				(new GridDisplay(rootView, ParseUser.getCurrentUser(), 0, GridDisplay.Usage.ALBUMS, album_btn)).display();
			}
						
		});
		
		allPictures_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "User picture Button Tap", Toast.LENGTH_LONG).show();
				ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
		    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
		    	
		    	userProfileAllView.setVisibility(View.VISIBLE);
		    	gridview.setVisibility(View.INVISIBLE);
		    	
				//inflate_allPictures(stringUserName);
				
			}
						
		});
		
		tags_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// custom adapter View (display posts made by the current user)
		    	ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
		    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
		    	
		    	userProfileAllView.setVisibility(View.INVISIBLE);
		    	gridview.setVisibility(View.VISIBLE);
		    	
				Toast.makeText(rootView.getContext(), "Tags Button Tap", Toast.LENGTH_SHORT).show();

				(new GridDisplay(rootView, currentUser, 0, GridDisplay.Usage.TAGS, tags_btn)).display();
			}
						
		});
		
		setting_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Profile Setting Button Tap", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(rootView.getContext(), ProfileSettingActivity.class);
				rootView.getContext().startActivity(intent);
			}
						
		});
		
		profile_picture.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(
                        	Intent.ACTION_PICK,
                        	android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                rootActivity.startActivityForResult(i, RESULT_LOAD_PROFILE_PICTURE);
			}
		});
		
    }

    // display user posts in profile view
    private static void inflate_allPictures(String userId){
    	// custom adapter View (display posts made by the current user)
    	ListView userProfileAllView = (ListView)rootView.findViewById(R.id.userProfileAllView);
    	GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
    	
    	userProfileAllView.setVisibility(View.VISIBLE);
    	gridview.setVisibility(View.INVISIBLE);
		PostsViewAdapter postsViewAdapter;
		postsViewAdapter = new PostsViewAdapter(rootActivity, userId);
		postsViewAdapter.loadObjects();
		userProfileAllView.setAdapter(postsViewAdapter);	
		
			
    }
    
    // set visibility of views: 0:home, 1:allpost, 2:history, 3:profile, 4:other user's profile
    private static void visibility_view(int type){
    	contentsLayoutHome.setVisibility(View.INVISIBLE);
        contentsLayoutHistory.setVisibility(View.INVISIBLE);
        contentsLayoutAllpost.setVisibility(View.INVISIBLE);
        contentsLayoutProfile.setVisibility(View.INVISIBLE);
        contentsLayoutProfileUser.setVisibility(View.INVISIBLE);
    	switch(type){
    		case 0:
    			contentsLayoutHome.setVisibility(View.VISIBLE);
    			break;
    		case 1:
    			contentsLayoutAllpost.setVisibility(View.VISIBLE);
    			break;
    		case 2:
    			contentsLayoutHistory.setVisibility(View.VISIBLE);
    			break;
    		case 3:
    			contentsLayoutProfile.setVisibility(View.VISIBLE);
    			break;
    		case 4:
    			contentsLayoutProfileUser.setVisibility(View.VISIBLE);
    			break;
    		default:
    			
    	}
    }
    
    // button change events (reset button colors)
    private static void btn_color_reset(){
    	if (ihome){
    		rootView.findViewById(R.id.home_btn).setBackgroundColor(Color.parseColor("#ffe6e6e5"));
    	} else if (ihistory){
    		rootView.findViewById(R.id.history_btn).setBackgroundColor(Color.parseColor("#ffc7c9c6"));
    	} else if (iallpost){
    		rootView.findViewById(R.id.allpost_btn).setBackgroundColor(Color.parseColor("#ffc7c9c6"));
    	} else if (iprofile){
    		rootView.findViewById(R.id.profile_btn).setBackgroundColor(Color.parseColor("#ffe6e6e5"));
    	}
    	
    }

    // user logout
    private static void user_logout(){
    	final RelativeLayout contentsLayout = (RelativeLayout)rootView.findViewById(R.id.contentsLayout);
    	
    	new AlertDialog.Builder(rootActivity)
        .setTitle("Log Out")
        .setMessage("Do you want to Log Out?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
     	   public void onClick(DialogInterface dialog, int whichButton) {
     		   //Log Out, Parse API
     			ParseUser.logOut();
     			
     	    	// Delete all previous user data (For safety)
     			SharedPreferences.Editor editor = pref.edit();
     			editor.putString("userId", "");
     			editor.putString("userName", "");
     			editor.putString("userEmail", "");
     	    	editor.putString("userBlog", "");
     	    	editor.putInt("userHeight", 0);
     	    	editor.putInt("userWeight", 0);
     	    	editor.putInt("userFavorited", 0);
     	    	editor.putInt("userFavoriting", 0);;
     	    	editor.putInt("userPictures", 0);
     	    	editor.putInt("userGender", 0);
     	    	editor.putInt("userGeneration", 0);
     	    	editor.putBoolean("userVintage", false);
     	    	editor.putBoolean("userClassic", false);
     	    	editor.putBoolean("userFormal", false);
     	    	editor.putBoolean("userApple", false);
     	    	editor.putBoolean("userBottleneck", false);
     	    	editor.putBoolean("userPear", false);
     	    	editor.commit();
     	    	contentsLayout.removeAllViews();
     	    	btn_color_reset();
     	    	ihome = false;
     	    	iallpost = false;
     	    	ihistory = false;
     	    	iprofile = false;
     	    	
     	    	// reset tabs & actionbar state
     	    	actionbar.removeAllTabs();
                actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
     	    	
     	    	login = true;
     			inflate_login();
     	   }
        })
        .setNegativeButton("No", null).show();

    }
    
    // reset/initialize actionbar tabs
    private static void init_tabs(int type){
    	ActionBar.TabListener tabListener; // tab listener
    	
    	switch(type){
    		// initialize all_post tabs
    		case 0:
    			final ListView allpostView_new = (ListView)rootView.findViewById(R.id.allpostView_new);
    			final ListView allpostView_hot = (ListView)rootView.findViewById(R.id.allpostView_hot);
    			final FrameLayout allpostOtherContents = (FrameLayout)rootView.findViewById(R.id.allpostOtherContents);
    			
    			// Specify that tabs should be displayed in the action bar.
                actionbar.removeAllTabs();
        		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                //actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                
                // Create a tab listener that is called when the user changes tabs.
                tabListener = new ActionBar.TabListener() {
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                        // show the given tab
                    	Log.e("msg", "tab pressed: "+String.valueOf(tab.getPosition()));
                    	PostsViewAdapter postsViewAdapter;
                    	
                    	switch(tab.getPosition()){
                    		case 0:
                    			// custom adapter View (display posts)
                    			allpostView_new.setVisibility(View.VISIBLE);
                    			allpostView_hot.setVisibility(View.INVISIBLE);
                				allpostOtherContents.setVisibility(View.INVISIBLE);
                			
                    			break;
                    			
                    		case 1:
                    			allpostView_new.setVisibility(View.INVISIBLE);
                    			allpostView_hot.setVisibility(View.VISIBLE);
                				allpostOtherContents.setVisibility(View.INVISIBLE);
                				
                    			break;
                    	
                    		case 2:
                    			allpostView_new.setVisibility(View.INVISIBLE);
                    			allpostView_hot.setVisibility(View.INVISIBLE);
                				allpostOtherContents.setVisibility(View.VISIBLE);
                    			break;
                    	}
                    }

                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                        // hide the given tab
                    }

                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                        // probably ignore this event
                    }
                };

                // Add 3 tabs, specifying the tab's text and TabListener
                actionbar.addTab(
                		actionbar.newTab()
                                //.setText("New")
                                .setIcon(R.drawable.icon_new)
                                .setTabListener(tabListener));
                actionbar.addTab(
                		actionbar.newTab()
                                //.setText("Hot")
                                .setIcon(R.drawable.icon_hot)
                                .setTabListener(tabListener));
                actionbar.addTab(
                		actionbar.newTab()
                                //.setText("Best")
                                .setIcon(R.drawable.icon_best)
                                .setTabListener(tabListener));
    			break;
    		case 1:
    			// Specify that tabs should be displayed in the action bar.
                actionbar.removeAllTabs();
        		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                //actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                
        		final RelativeLayout feedDataView_Layout = (RelativeLayout)rootView.findViewById(R.id.feedDataView_Layout);
        		final RelativeLayout newsDataView_Layout = (RelativeLayout)rootView.findViewById(R.id.newsDataView_Layout);
        		
                // Create a tab listener that is called when the user changes tabs.
                tabListener = new ActionBar.TabListener() {
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    	// show the given tab
                    	Log.e("msg", "tab pressed: "+String.valueOf(tab.getPosition()));
                    	PostsViewAdapter postsViewAdapter;
         
                    	switch(tab.getPosition()){
                    		case 0:
                    			
                    			Toast.makeText(rootView.getContext(), "Feeds Button Tap", Toast.LENGTH_LONG).show();	
                    			feedDataView_Layout.setVisibility(View.VISIBLE);
                    			newsDataView_Layout.setVisibility(View.INVISIBLE);
                    			
                    			break;
                    			
                    		case 1:
                    			Toast.makeText(rootView.getContext(), "News Button Tap", Toast.LENGTH_LONG).show();
                    			feedDataView_Layout.setVisibility(View.INVISIBLE);
                    			newsDataView_Layout.setVisibility(View.VISIBLE);
                    			break;
                    	}
                    }

                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                        // hide the given tab
                    }

                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                        // probably ignore this event
                    }
                };

                // Add 2 tabs, specifying the tab's text and TabListener
                actionbar.addTab(
                		actionbar.newTab()
                                .setText("Feeds")
                                .setTabListener(tabListener));
                actionbar.addTab(
                		actionbar.newTab()
                                .setText("News")
                                .setTabListener(tabListener));
                
    			break;
    			
    		default:
    			
    	}
    }
    
    // result activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // profile picture edit
        if (requestCode == RESULT_LOAD_PROFILE_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            cursor.close();

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(selectedImage, "image/*");
            intent.putExtra("output", selectedImage);
            
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_IMAGE);
        } 
        // getting cropped image of profile picture
        else if (requestCode == CROP_IMAGE && resultCode == RESULT_OK && null != data){   
     
        	// get cropped data
        	final Bundle extras = data.getExtras();
        	if (extras != null){
        		ImageView profilePicture = (ImageView)rootView.findViewById(R.id.profile_picture);
        		Bitmap profile_bitmap_temp = extras.getParcelable("data");
				Bitmap profile_bitmap = Bitmap.createScaledBitmap(profile_bitmap_temp, 100, 100, true);
				profile_bitmap_temp.recycle();
				profilePicture.setImageBitmap(profile_bitmap);
        
				//Profile pictures to byte array for storing into Parse Background Server, Parse API
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] profile_photo = stream.toByteArray();

				ParseFile profilePhoto = new ParseFile("photo.png", profile_photo);
		        profilePhoto.saveInBackground();

		        ParseUser currentUser = ParseUser.getCurrentUser();
		        if (currentUser != null) {
		          	currentUser.put("profilePhoto", profilePhoto);
		           	currentUser.saveInBackground();
		           	
		           	// update profile photos of posts
		           	ParseQuery query = new ParseQuery("PhotoDataHandler");
					query.whereContainedIn("username", Arrays.asList(currentUser.getString("username")));
					query.orderByAscending("createdAt");
				
					// change all photos
					List<ParseObject> ob;
					try {
						ob = query.find();
						for (ParseObject post : ob) {
							post.put("profilePhoto", profilePhoto);
		                	post.saveInBackground();
		                }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		        } else {
		           Toast.makeText(getBaseContext(), "Can't find current User", Toast.LENGTH_LONG).show();
		           // login required
		        }
        	}
        	
        } 
        // user login activity is done (two type of login: login | register)
        else if (requestCode == RESULT_LOGIN && resultCode == RESULT_OK){
        	int loginType = data.getIntExtra("loginType", 0);
        	
        	// load data on view
        	inflate_home();
    		inflate_allPosts();
    		inflate_history();
    		inflate_profile();
    		
    		//set default visibility
    		contentsLayoutHome.setVisibility(View.INVISIBLE);
    		contentsLayoutHistory.setVisibility(View.INVISIBLE);
            contentsLayoutAllpost.setVisibility(View.INVISIBLE);
            contentsLayoutProfile.setVisibility(View.INVISIBLE);
            contentsLayoutProfileUser.setVisibility(View.INVISIBLE);
    		
        	if (loginType == 0){
        		findViewById(R.id.home_btn).setBackgroundColor(Color.parseColor("#ff373737"));
        		
        		// set visibility
            	contentsLayoutHome.setVisibility(View.VISIBLE);
                
        		// Change Title
        		ActionBar abar = rootActivity.getActionBar();
        		abar.setTitle("Favorite");
        		
                // inflate check
            	ihome = true;
            	iallpost = false;
            	ihistory = false;
            	iprofile = false;
            	
        		logo_on = true;
				logo_layout.setVisibility(View.VISIBLE);
	            if (logo_on){
	            	logo_layout.startAnimation(translateRightAnim);
	            }
	            
        	} else{
        		findViewById(R.id.profile_btn).setBackgroundColor(Color.parseColor("#ff373737"));
        		
        		// set visibility
                contentsLayoutProfile.setVisibility(View.VISIBLE);
                
        		// Change Title
        		ActionBar abar = rootActivity.getActionBar();
        		abar.setTitle(pref.getString("userId", "Profile"));
        		
                // inflate check
            	ihome = false;
            	iallpost = false;
            	ihistory = false;
            	iprofile = true;
            	
        		logo_on = true;
				logo_layout.setVisibility(View.VISIBLE);
	            if (logo_on){
	            	logo_layout.startAnimation(translateRightAnim);
	            }
	            
        	}
        	login = false;
        }
        // returning from Camera Activity: display most recently displayed View
        else if (requestCode == CAMERA_ACT && resultCode == RESULT_OK){
        	if (ihome){
        		inflate_home();
        	} else if (iallpost){
        		inflate_allPosts();
        	} else if (ihistory){
        		inflate_history();
        	} else if (iprofile){
        		inflate_profile();
        	}

        } 
        // return from filtering / preference setting (show changes)
        else if (requestCode == FILTER_ACT && resultCode == RESULT_OK && iallpost){
        	// custom adapter View (display posts)
        	final ListView allpostView_main = (ListView)rootView.findViewById(R.id.allpostView_new);
    		final FrameLayout allpostOtherContents = (FrameLayout)rootView.findViewById(R.id.allpostOtherContents);
			allpostView_main.setVisibility(View.VISIBLE);
			allpostOtherContents.setVisibility(View.INVISIBLE);
			PostsViewAdapter postsViewAdapter;
			postsViewAdapter = new PostsViewAdapter(rootActivity, fsetting);;
			postsViewAdapter.loadObjects();
			allpostView_main.setAdapter(postsViewAdapter);	
        } 
        // commenting activity result
        else if (requestCode == RESULT_COMMENT_USERPROFILE && resultCode == RESULT_OK && data != null) {

        	// if true, clicked user profile from comment activity
        	if (data.getBooleanExtra("UserProfileFromComment", false)) {
        		String userId = data.getStringExtra("UserId");
        		ParseQuery<ParseUser> query = ParseUser.getQuery();
        		try {
					ParseUser user = query.get(userId);
					if (!(ParseUser.getCurrentUser().hasSameId(user)))
						infate_profile_user(user);
						
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Toast.makeText(rootView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
					return;
				}

        	}
        }
    }
 
}


