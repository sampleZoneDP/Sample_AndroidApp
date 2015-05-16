package org.example.flawless_actionbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AndroidCamera extends Activity {
    MyCameraSurface mSurface;
    
    // shutter, directory, and flash button images
    ImageButton mShutter;
    ImageView dir_btn, flash_btn, grid_btn, face_btn, camera_bottomcover;
    // flash on/off
    boolean flash_on, grid_on;
    int facingCamera;
    
    //Database handler
  	DataHandler handler;
  	
  	//URI for picture
  	Uri selectedImage;
  	int imgSize;
  	 
  	FrameLayout uploadPicSetting, camera_topcover;
  	RelativeLayout btn_layout;
  	Bitmap bitmapPreview;
  	boolean previewRecycle = false;
  	
  	// Shared Data (for user info)
  	static SharedPreferences pref;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CAMERA = 2;
    private static int CROP_IMAGE = 3;
    private static int CHOOSE_IMAGE = 4;
    private static int FILTER_IMAGE = 5;
    int update_look, update_height, update_weight;
    int cropType;
    
    // Buttons in upload activity
    Button updatePicture_btn;
	Button updatePictureCancel_btn;
	RadioButton updateStyleVintage;
	RadioButton updateStyleClassic;
	RadioButton updateStyleCasual;
	RadioButton updateStyleBoho;
	RadioButton updateStyleSporty;
	RadioButton updateStylePreppy;
	RadioButton updateStyleDandy;
	RadioButton updateTypeApple;
	RadioButton updateTypeBanana;
	RadioButton updateTypePear;
	RadioButton updateTypeHourglass;
	RadioButton updateTypeEcto;
	RadioButton updateTypeMeso;
	RadioButton updateTypeEndo;
	RadioButton updateLookSlim; 
	RadioButton updateLookRound;
	RadioButton updateLookHealthy;
	EditText updateHashtag;
    
	// Linear layouts for different gender (in upload activity)
	LinearLayout uploadTypeBoxF1;
    LinearLayout uploadTypeBoxF2;
    LinearLayout uploadTypeBoxM1;

    // path of original picture in the device (for uploading)
    String picturePath;
    
    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.camera_main);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         
         pref = getSharedPreferences("pref", MODE_PRIVATE);
         mSurface = (MyCameraSurface)findViewById(R.id.previewSurface);
         uploadPicSetting = (FrameLayout)findViewById(R.id.uploadPicSetting);
         camera_topcover = (FrameLayout)findViewById(R.id.camera_topcover);
         btn_layout = (RelativeLayout)findViewById(R.id.btn_layout);
         camera_bottomcover = (ImageView)findViewById(R.id.camera_bottomcover);
         ViewGroup.LayoutParams msfParam = mSurface.getLayoutParams();
         facingCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
         flash_on = false;
         grid_on = false;
         
         // device screen size
         DisplayMetrics display = getBaseContext().getResources().getDisplayMetrics();
		 int deviceWidth = display.widthPixels;
		 int deviceHeight = display.heightPixels;
		 imgSize = Math.min(deviceWidth, deviceHeight);

         msfParam.width = imgSize;
         msfParam.height = imgSize /240 * 320;
         
         // set grid lines & containing layouts
         LinearLayout grid_display_hor = (LinearLayout)findViewById(R.id.grid_display_hor);
         LinearLayout grid_display_ver = (LinearLayout)findViewById(R.id.grid_display_ver);
         
         // resize layouts
         ViewGroup.LayoutParams params_hor = grid_display_hor.getLayoutParams();
         ViewGroup.LayoutParams params_ver = grid_display_ver.getLayoutParams();
         
         // Changes the height and width
         params_hor.height = imgSize / 9 * 10;
         params_hor.width = imgSize;
         params_ver.height = imgSize / 9 * 10;
         params_ver.width = imgSize;
         
         // set bottom cover 
         camera_topcover.getLayoutParams().height = imgSize / 9 * 10;
        
         // Button Clicked
 		 final ImageButton shutter_btn = (ImageButton) findViewById(R.id.shutter_btn); 
 		 dir_btn = (ImageView) findViewById(R.id.camera_picture_update); 
 		 flash_btn = (ImageView) findViewById(R.id.camera_flash); 
 		 grid_btn = (ImageView) findViewById(R.id.grid_view_on);
 		 face_btn = (ImageView)findViewById(R.id.camera_facing);
 		 

         // Take picture with HW camera
         mShutter = (ImageButton)shutter_btn;
         mShutter.setOnClickListener(new Button.OnClickListener() {
             public void onClick(View v) {
                  mSurface.mCamera.takePicture(null, null, mPicture);
             }
         });
         
         dir_btn.setOnClickListener(new Button.OnClickListener() {
             public void onClick(View v) {
            	 Intent i = new Intent(
            	 Intent.ACTION_PICK,
            	 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            	 startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
         
        flash_btn.setOnClickListener(new Button.OnClickListener() {
             public void onClick(View v) {
            	 flash_activity();
            }
        });
         
        grid_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
           	 grid_activity();
           }
        });
         
        face_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
              	 face_activity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_menu, menu);
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
    		
    		default:
    			return super.onOptionsItemSelected(item);
		}
	}
    
	// camera facing change
	private void face_activity(){
		mSurface.changeCameraFacing();
		if (facingCamera == Camera.CameraInfo.CAMERA_FACING_FRONT){
			facingCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
		} else{
			facingCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
			Camera mCamera = mSurface.mCamera;
			Camera.Parameters mCameraParameter;
			mCameraParameter = mCamera.getParameters();
			mCameraParameter.setFlashMode("off");
			flash_on = false;
			flash_btn.setImageResource(R.drawable.icon_flash_off);
			mCamera.setParameters(mCameraParameter);
		}
	}
	
	// grid view display activiry
	private void grid_activity(){
		LinearLayout grid_display_ver = (LinearLayout)findViewById(R.id.grid_display_ver);
		LinearLayout grid_display_hor = (LinearLayout)findViewById(R.id.grid_display_hor);
		if (grid_on){
			grid_display_ver.setVisibility(View.INVISIBLE);
			grid_display_hor.setVisibility(View.INVISIBLE);
			grid_on = false;
		} else{
			grid_display_ver.setVisibility(View.VISIBLE);
			grid_display_hor.setVisibility(View.VISIBLE);
			grid_on = true;
		}
	}
	
	// Camera flash on/off
	private void flash_activity(){
		if(facingCamera == Camera.CameraInfo.CAMERA_FACING_BACK){
			Camera mCamera = mSurface.mCamera;
			Camera.Parameters mCameraParameter;
			mCameraParameter = mCamera.getParameters();
			if (flash_on){
				mCameraParameter.setFlashMode("off");
				flash_btn.setImageResource(R.drawable.icon_flash_off);
				flash_on = false;
			} else{
				mCameraParameter.setFlashMode("torch");	
				flash_btn.setImageResource(R.drawable.icon_flash_on);
				flash_on = true;
			}
			mCamera.setParameters(mCameraParameter);
		}
	}
	
    // allow taking picture when auto-focusing is done
    AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
         public void onAutoFocus(boolean success, Camera camera) {
             mShutter.setEnabled(success);
         }
    };

    // Save picture taken
    PictureCallback mPicture = new PictureCallback() {
         public void onPictureTaken(byte[] data, Camera camera) {
			 
        	 // picture directory & new file
             File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"Pictures");
			  if(!dir.exists())
			   dir.mkdirs();
			 String nowTime = getDateTime();
			 String path = Environment.getExternalStorageDirectory()+File.separator+"Pictures"+File.separator+"img"+nowTime+".png";             

             File file = new File(path);
             try {
                  FileOutputStream fos = new FileOutputStream(file);
                  Bitmap bmpTemp = BitmapFactory.decodeByteArray(data, 0, data.length);
                  Bitmap bmpResized = Bitmap.createScaledBitmap(bmpTemp, 600, 600
          				* bmpTemp.getHeight() / bmpTemp.getWidth(), false);
                  Bitmap bmpRot, bmpfin;
                  if (facingCamera == Camera.CameraInfo.CAMERA_FACING_BACK){
                	  bmpRot = rotate(bmpResized, 90);
                  } else {
                	  bmpRot = rotate(bmpResized, 270);
                	  bmpRot = invert(bmpRot);
                  }
                  
                  // final picture size
                  int bmpsize = bmpRot.getWidth();
                  bmpfin = Bitmap.createBitmap(bmpRot, 0, 0, bmpsize, bmpsize*10/9);
                  bmpfin.compress(CompressFormat.PNG, 100, fos);
                  
                  // flush & recycle
                  fos.flush();
                  fos.close();
                  bmpTemp.recycle();
                  bmpfin.recycle();
                  bmpResized.recycle();
                  bmpRot.recycle();
                  bmpRot = null;
                  bmpTemp = null;
                  bmpResized = null;
                  bmpfin = null;
   	              System.gc();
             } catch (Exception e) {
                  Toast.makeText(AndroidCamera.this, "Error in Saving file: " +
                            e.getMessage(), 0).show();
                  return;
             }
            
             Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             Uri uri = Uri.parse("file://" + path);
             intent.setData(uri);
             sendBroadcast(intent);

             picturePath = path;

             // inflate crop option
			 cropType = RESULT_CAMERA;
			 selectedImage = Uri.fromFile(file);
			 
			 // call next view activity for camera
			 intent_imageChoose();
			 
			// reset facing
			 if (facingCamera == Camera.CameraInfo.CAMERA_FACING_BACK)
				 face_activity();
             
         }
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode != RESULT_OK)
        {
        	if (requestCode == CROP_IMAGE)
        		facingCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
            return;
        }
        
        if (requestCode == CROP_IMAGE && null != data){
   
        	final Bundle extras = data.getExtras();
        	if (extras != null){
        		bitmapPreview = extras.getParcelable("data");
        		// inflate upload setting
   			 	intent_imageChoose();
        	}
        	

        }
        else if (requestCode == RESULT_LOAD_IMAGE && null != data) {

        	selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            cropType = RESULT_LOAD_IMAGE;
            intent_crop();

        }
        else if (requestCode == CHOOSE_IMAGE && null != data) {
        	final Bundle extras = data.getExtras();
        	if (extras != null){
        		picturePath = extras.getString("path");
        	}
        	
        	// end activity
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);
		    finish();
        }
        else if (requestCode == FILTER_IMAGE && null != data) {
        
        }
    }
    
    // crop intent
    public void intent_crop(){
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(selectedImage, "image/*");
        intent.putExtra("output", selectedImage);
        
        intent.putExtra("outputX", 2000);
        intent.putExtra("outputY", 2000);
        intent.putExtra("aspectX", 9);
        intent.putExtra("aspectY", 10);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_IMAGE);
    }
    
    // start choosing image
    public void intent_imageChoose(){
    	Intent intent = new Intent(getBaseContext(), ImageChoose.class);
    	intent.putExtra("path", picturePath);
    	startActivityForResult(intent, CHOOSE_IMAGE);
    }
    
    // bitmap rotating
    public static Bitmap rotate(Bitmap bmp, int degrees) {
    	if ( degrees != 0 && bmp != null ) {
    		Matrix m = new Matrix();
    		m.setRotate( degrees, (float) bmp.getWidth() / 2, (float) bmp.getHeight() / 2 );
  
    		try {
    			Bitmap bmp2 = Bitmap.createBitmap( bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true );
    			if (bmp != bmp2) {
    				bmp.recycle();
    				bmp = null;
    				System.gc();
    				bmp = bmp2;
    			}

    		} catch (OutOfMemoryError ex) {
    		//Return the original bitmap.
    		}
    	}
    	return bmp;
    }
    
    // bitmap inverting
    public static Bitmap invert(Bitmap bmp) {
    	if ( bmp != null ) {
    		Matrix sideInversion = new Matrix();
    		sideInversion.setScale(-1, 1);
    		try {
    		    Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0,
    		    		bmp.getWidth(), bmp.getHeight(), sideInversion, true);
    		    bmp.recycle();
				bmp = null;
				System.gc();
				bmp = bmp2;

    		} catch (OutOfMemoryError ex) {
    		//Return the original bitmap.
    		}
    	}
    	return bmp;
    }
    
    //Return Time for new picture Name
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
          
	// Back Key Event
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch(keyCode) {
	      case KeyEvent.KEYCODE_BACK:
	        new AlertDialog.Builder(this)
	                       .setTitle("Finish Camera")
	                       .setMessage("Finish Camera Mode?")
	                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    	   public void onClick(DialogInterface dialog, int whichButton) {
	                    		    Intent intent = new Intent();
	               	        		setResult(Activity.RESULT_OK, intent);
	               	        		finish();
	                    	   }
	                       })
	                       .setNegativeButton("No", null).show();

	        return false;
	      default:
	        return false;
	    }
	}
	
}

//MyCameraSurface class
class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    private int facingCamera;

    public MyCameraSurface(Context context, AttributeSet attrs) {
         super(context, attrs);
         mHolder = getHolder();
         mHolder.addCallback(this);
         mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
    	 openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

         mCamera.setDisplayOrientation(90);
         try {
             mCamera.setPreviewDisplay(mHolder);
         } catch (IOException e) {
             mCamera.release();
             mCamera = null;
         }
    }

    // new open camera
    private void openCamera(int facing) {
    	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    	int cameraCount = Camera.getNumberOfCameras();
    	for (int i = 0; i < cameraCount; i++) {
    		Camera.getCameraInfo(i, cameraInfo);
    		if (cameraInfo.facing == facing) {
    			try {
    				mCamera = Camera.open(i);
    			} catch (RuntimeException e) {
    				Log.e("ERROR:", "Camera failed to open: " + e.getLocalizedMessage());
    			}

    			facingCamera = facing;
    		}
    	}
    }
    
    // change facing of camera <front/back>
    public void changeCameraFacing() {
    	if (mCamera != null) {
    		mCamera.stopPreview();
    		mCamera.setPreviewCallback(null);
    		mCamera.release();
    		mCamera = null;
    	}

    	if (facingCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
    		openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    	} else {
    		openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    	}

    	try {
    		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
    			mCamera.setDisplayOrientation(90);
             } else {
                Parameters parameters = mCamera.getParameters();
                parameters.setRotation(90);
                mCamera.setParameters(parameters);
             }

    		mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            Log.e("Error:", "Failed to set camera preview.", e);
            mCamera.release();
        }

    	mCamera.startPreview();
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
         if (mCamera != null) {
             mCamera.stopPreview();
             mCamera.release();
             mCamera = null;
         }
    }

   public void surfaceChanged(SurfaceHolder holder, int format, int width,   int height) {
         Camera.Parameters params = mCamera.getParameters();
       List<Size> arSize = params.getSupportedPreviewSizes();
       if (arSize == null) {
             params.setPreviewSize(width, height);
       } else {
            int diff = 10000;
            Size opti = null;
            for (Size s : arSize) {
              if (Math.abs(s.height - height) < diff) {
                   diff = Math.abs(s.height - height);
                   opti = s;
                  
              }
            }
       }
       mCamera.setParameters(params);
       mCamera.startPreview();
    } 
   
}




