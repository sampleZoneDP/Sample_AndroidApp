package org.example.flawless_actionbar;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private String[] albumNames;
	private List<byte[]> photoFileList;
    private boolean isAlbum;
    private int myProfile;
    
    public ImageAdapter(Context c, String[] albumnames, List<byte[]> pfa, boolean isalbum, int myprofile) {
    	mContext = c;
    	albumNames = albumnames; 	// only applicable if usage is ALBUMS
    	photoFileList = pfa;	 	// only applicable if usage is not ALBUMS. This is null if ALBUMS
        isAlbum = isalbum;
        myProfile = myprofile;
        
    }
    

    public int getCount() {
    	if (isAlbum)
    		return albumNames.length;
    	else
    		return photoFileList.size();
    }

    public Object getItem(int position) {
    	return position;
    }

    public long getItemId(int position) {
    	return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	ImageView image;
        
        if (convertView == null) {
        	image = new ImageView(mContext);
        	
        	WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        	Display display = wm.getDefaultDisplay();
        	int width;
        	
			// dynamically set item(photo/album) size
        	if (android.os.Build.VERSION.SDK_INT >= 13) {
        		Point size = new Point();
        		display.getSize(size);
        		width = size.x;
        	} else {
        		width = display.getWidth();
        	}
        	
        	GridView.LayoutParams lp = new GridView.LayoutParams(width/3-10, width/3-10);
            image.setLayoutParams(lp);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setPadding(5, 5, 5, 5);
            
        } else {
        	image = (ImageView) convertView;
        }
        
        Bitmap bm;
        if (isAlbum) {
        	
        	if (position == albumNames.length-1 && myProfile == 1) { // album add button
        		bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.add_album);
        	} else {
        		String text = albumNames[position];
        	
				// overlay album name on fixed album image
        		bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.album).copy(Bitmap.Config.ARGB_8888, true);
        		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
        		Paint paint = new Paint();
        		paint.setStyle(Style.FILL);
        		paint.setColor(Color.BLACK);
        		paint.setTypeface(tf);
        		paint.setTextAlign(Align.CENTER);
        		paint.setTextSize(15);
        		Rect textRect = new Rect();
        		paint.getTextBounds(text, 0, text.length(), textRect);
        		Canvas canvas = new Canvas(bm);
        		canvas.drawText(text, bm.getWidth()/2, 2*bm.getHeight()/3, paint);
        	}
        	
        } else { // items are not albums
        	byte[] photoByteArray = photoFileList.get(position);
        	bm = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.length);
       }
        image.setImageBitmap(bm);
        return image;
    }
}