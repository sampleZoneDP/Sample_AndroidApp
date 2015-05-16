package org.example.flawless_actionbar;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.WindowManager;

public class BitmapResize {


	public static Bitmap loadResizedBitmap(Context context, String imgPath){
		
		// Device size
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int deviceWidth = display.getWidth();
		int deviceHeight = display.getHeight();
		
		// image size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		
		float widthScale = options.outWidth / deviceWidth;
		float heightScale = options.outHeight / deviceHeight;
		float scale = widthScale > heightScale ? widthScale : heightScale;
	
		if (scale >= 8){
			options.inSampleSize = 8;
		} else if (scale >= 6){
			options.inSampleSize = 6;
		} else if (scale >= 4){
			options.inSampleSize = 4;
		} else if (scale >= 2){
			options.inSampleSize = 2;
		} else {
			options.inSampleSize = 1;
		}
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(imgPath, options);
	}

}