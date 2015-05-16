package org.example.flawless_actionbar;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AllCommentActivity extends ActionBarActivity {
	
	static View commentsRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_all_comment);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* 
	 * Context menu to launch when a user name is clicked, applicable in comment activity.
	 * Identical to MainActivity's.
	 */
	@Override  
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
        menu.setHeaderTitle(((TextView)v).getText());  // user name as menu title
        menu.add(0, v.getId(), 0, "User profile for "+((TextView)v).getText());  
        menu.add(0, v.getId(), 0, "Subscribe "+((TextView)v).getText());  
    }  
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {
		// receiving data of user clicked from this activity (AllCommentActivity)
    	String objectId = getIntent().getStringExtra("clicked user");
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	ParseUser thisUser;
		try {
			thisUser = query.get(objectId);
			if(item.getTitle().toString().startsWith("User profile for")) {
	        	inflate_profile(thisUser);
			} else if(item.getTitle().toString().startsWith("Subscribe")) {
	        	if (thisUser.hasSameId(ParseUser.getCurrentUser())) {	// trying to subscribe to herself/himself
					Toast.makeText(getApplicationContext(), "You cannot subscribe yourself!", Toast.LENGTH_LONG).show();
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
					SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
					pref.edit().putInt("userFavoriting", pref.getInt("userFavoriting", 0)+1);
					pref.edit().commit();
					
					Toast.makeText(getApplicationContext(), "You subscribed to "+thisUser.getUsername(), Toast.LENGTH_LONG).show();
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
			commentsRootView = inflater.inflate(R.layout.fragment_all_comment,
					container, false);
			
			final LinearLayout commentList = (LinearLayout)commentsRootView.findViewById(R.id.comment_list);
			
			final Intent intent = ((Activity)commentsRootView.getContext()).getIntent();
			final String photoid = intent.getStringExtra("photoid");
			
			final LinearLayout addCommentLayout = (LinearLayout)commentsRootView.findViewById(R.id.add_comment_layout);
			
			// prepare Comment Typing Box ahead (add to view later)
			final EditText addCommentEditText = new EditText(commentsRootView.getContext());
			LayoutParams commentEditTextLP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			commentEditTextLP.weight=1;
			addCommentEditText.setHint("Enter comment");
			addCommentEditText.setLayoutParams(commentEditTextLP);
			addCommentEditText.setFocusable(true);
						
			// prepare Comment Submit Button ahead (add to view later)
			final Button addCommentSubmitBtn = new Button(commentsRootView.getContext());
			addCommentSubmitBtn.setTextSize(15);
			addCommentSubmitBtn.setText("comment");
			
			// view to add when there are no comments yet
			final TextView noCommentTV = new TextView(commentsRootView.getContext());
						
			// load comments
			ParseQuery<CommentDataHandler> query = ParseQuery.getQuery("CommentDataHandler");
			query.whereEqualTo("photoid", photoid);
			query.orderByAscending("createdAt");
			query.findInBackground(new FindCallback<CommentDataHandler>() {
			    public void done(List<CommentDataHandler> list, ParseException e) {
			    	if (e == null) {
			    		if (list.isEmpty()) {
							noCommentTV.setText("There are no comments for this post.");
							commentList.addView(noCommentTV);
			    		} else {
			    			for (final CommentDataHandler cdh : list) {
			    				try {
									addCommentView(commentList, 
												   cdh.getParseUser("user").fetchIfNeeded().getUsername(),
												   cdh.getString("comment"),
												   cdh.getUpdatedAt(),
												   cdh.getParseUser("user").fetchIfNeeded().getObjectId());
									
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
			    			}
			    		}
			    		// display comment adding section along with other comments
		    			addCommentLayout.addView(addCommentEditText);
						addCommentLayout.addView(addCommentSubmitBtn);
						addCommentLayout.requestFocus();
			    	} else {
			    		// something went wrong here
			    	}
			    }
			});
			
			// One additional line for adding a new comment
		//	final RelativeLayout addCommentLayout = new RelativeLayout(commentsRootView.getContext());
				
			addCommentSubmitBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String newComment = addCommentEditText.getText().toString();
					
					CommentDataHandler cdh = new CommentDataHandler();
					cdh.insertCommentData(ParseUser.getCurrentUser(), photoid, newComment);
					
					// Get the related photo's info
					ParseQuery<PhotoDataHandler> photoQuery = ParseQuery.getQuery("PhotoDataHandler");
					PhotoDataHandler photoData;
					try {
						photoData = photoQuery.get(photoid);
						ActivityFeed feed = new ActivityFeed();
						feed.insertActivity(ActivityFeed.Type.COMMENT, ParseUser.getCurrentUser(), photoData.getAuthor().fetchIfNeeded(), photoData);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Toast.makeText(commentsRootView.getContext(), "Comment submitted", Toast.LENGTH_SHORT).show();
					
					addCommentView(commentList, ParseUser.getCurrentUser().getUsername(), newComment, new Date(), ParseUser.getCurrentUser().getObjectId());
					noCommentTV.setVisibility(View.GONE);
					
					// empty the box after submit
					addCommentEditText.setText(null);
				}
			});
			
			return commentsRootView;
		}
	}
	
	public static void addCommentView(LinearLayout viewlist, final String username, String comment, Date date, final String userObjectId) {
		LinearLayout commentEntry = new LinearLayout(commentsRootView.getContext());
		commentEntry.setOrientation(LinearLayout.HORIZONTAL);
	
		// user who made the comment
		final TextView useridTV = new TextView(commentsRootView.getContext());
		useridTV.setText(username);
		useridTV.setTextColor(Color.parseColor("#398EB5"));
		useridTV.setPadding(0, 0, 10, 0);
		
		// link this username text to context menu
		useridTV.setClickable(true);
		useridTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(username.equals(ParseUser.getCurrentUser().getString("username")))){
					((Activity)commentsRootView.getContext()).getIntent().putExtra("clicked user", userObjectId);
					((Activity)commentsRootView.getContext()).registerForContextMenu(useridTV);
					((Activity)commentsRootView.getContext()).openContextMenu(useridTV);
					((Activity)commentsRootView.getContext()).unregisterForContextMenu(useridTV);
				}
			}
		});
		
		LinearLayout commentContent = new LinearLayout(commentsRootView.getContext());
		commentContent.setOrientation(LinearLayout.VERTICAL);
		
		// comment content
		TextView commentTV = new TextView(commentsRootView.getContext());
		commentTV.setText(comment);
		commentContent.addView(commentTV);
		
		// comment date
		TextView commentDate = new TextView(commentsRootView.getContext());
		commentDate.setText(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT).format(date));
		commentDate.setTextColor(Color.parseColor("#D3D3D3"));
		commentContent.addView(commentDate);
			
		commentEntry.addView(useridTV);
		commentEntry.addView(commentContent);
		viewlist.addView(commentEntry);
	}
	
	protected void inflate_profile(ParseUser user) {
		// go back to MainActivity and call inflate_profile
		Intent intent = new Intent();
		intent.putExtra("UserProfileFromComment", true);
		intent.putExtra("UserId", user.getObjectId());
		setResult(RESULT_OK, intent);
		finish();
	}

}
