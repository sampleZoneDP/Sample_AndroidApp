package org.example.flawless_actionbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

public class ListViewAdapter extends BaseAdapter {
	 
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<newObject> newObjList = null;
	
    private ArrayList<newObject> arraylist;
    protected int count;
 
    public ListViewAdapter(Context context, List<newObject> newList) {
    	mContext = context;
        newObjList = newList;
        inflater = LayoutInflater.from(mContext);
        arraylist = new ArrayList<newObject>();
        arraylist.addAll(newObjList);
    }
 
    public class ViewHolder {
    	ParseImageView postImage;
		TextView titleTextView;
		TextView dateTextView;
		ImageView postViewProfileImg;
 
    }
 
    @Override
    public int getCount() {
        return newObjList.size();
    }
 
    @Override
    public newObject getItem(int position) {
        return newObjList.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.posts_view_layout, null);
            
            // setup views
            holder.postImage = (ParseImageView) view.findViewById(R.id.icon);
            holder.titleTextView = (TextView) view.findViewById(R.id.postViewUserId);
            holder.dateTextView = (TextView) view.findViewById(R.id.postViewUpdateDate);
            holder.postViewProfileImg = (ImageView) view.findViewById(R.id.postViewProfileImg);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        
        
        // put data object into the view
		ParseFile photoFile = newObjList.get(position).photoFile;
		
		if (photoFile != null) {
			holder.postImage.setParseFile(photoFile);
			holder.postImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
				}
			});
		}
		
		// device screen size & image size reset
      	Display display = ((WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      	int deviceWidth = display.getWidth();
    	int deviceHeight = display.getHeight();
    	int imgSize = Math.min(deviceWidth, deviceHeight);

		LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(imgSize, imgSize * 10 / 9);
		holder.postImage.setLayoutParams(params);
        
 
        return view;
    }
}