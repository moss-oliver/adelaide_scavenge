package com.example.adelaidescavange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.*;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnFragmentInteractionListener, OnMyLocationChangeListener, OnInfoWindowClickListener {
	
	public static final int DATABASE_VERSION = 1;
  
	private SQLiteDatabase db;
	
	public UiInterface uiInterface;

	public static int FACT_DING_ID = 171;
	//public Marker[] activeMarkers;
	public Marker selectedMarker = null;
	public int selectedMarkerDingId = 0;
	public HashMap<Integer,Marker> activeMarkers;
	public Polyline mapLine;
	
	LocationManager locman;
	
	public static double DISTANCE_METERS = 30;

	public static int COURSE_ID_SECRETS=0;
	
	public static int COURSE_INDEX_SECRETS=0;
	public static int COURSE_INDEX_USER_SELECTED=1;
	public static int COURSE_ID_NONE = -1;
	public int[] courseIds = new int[]{0,0};

	static public String dbName = "scavenge.db";
	//String dbName = "scavenge.db";
	
	private boolean checkDataBase() {
	    File dbFile=this.getDatabasePath(dbName);
	    return dbFile.exists();
	}
	public SQLiteDatabase getDb()
	{
		//System.out.println("getting db");
		if (db == null)
		{
			System.out.println("DB is null");
			ScavengeDatabaseHelper helper = new ScavengeDatabaseHelper( this,dbName,null, DATABASE_VERSION);
	    	db = helper.getWritableDatabase();
		}
		return db;
	}
	
	public static String unescape(String in)
	{
		return in.replaceAll("\\\\n", "\\\n");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//android:name="com.example.adelaidescavange.InitialFragment"
		InitialFragment initfrag = InitialFragment.newInstance();
		getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.lowercontent, initfrag)
        .commit();
		
		for(int i = 0; i<courseIds.length; i++)
		{
			courseIds[i] = COURSE_ID_NONE;
		}
		
		courseIds[COURSE_INDEX_SECRETS] = COURSE_ID_SECRETS;

		Log.d("database","initializing database"); //this.getFilesDir()+"dataLoad.xml");

		//String dbName = "scavenge.db";
		
		//this.deleteDatabase(dbName);
		ScavengeDatabaseHelper helper;
	    if(!checkDataBase())
	    {
	    	Intent introIntent = new Intent(this, IntroViewPager.class);
	    	this.startActivity(introIntent);

	    	helper = new ScavengeDatabaseHelper( this,dbName,null, DATABASE_VERSION);
	    	db = helper.getWritableDatabase();
		    ScavengeXMLParser s = new ScavengeXMLParser(this, "scavengeDataLoad.xml" ,0);
	    	
	    }
	    else
	    {
	    	helper = new ScavengeDatabaseHelper( this,dbName,null, DATABASE_VERSION);
	    	db = helper.getWritableDatabase();
	    }
    	
    	//System.out.println("LOCATION OF xml:" + this.getFilesDir() +"scavengeDataLoad.xml"); //this.getFilesDir()+"dataLoad.xml");
    	//Log.d("database","LOCATION OF xml:" + this.getFilesDir() +"scavengeDataLoad.xml"); //this.getFilesDir()+"dataLoad.xml");
    	
    	
    	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//will this be a problem?
		
		//set actionbar to transparent
		this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

		this.getSupportActionBar().setTitle("Adelaide Scavenge");
		//initialise map
		//set map to vic square
        GoogleMap map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnInfoWindowClickListener((OnInfoWindowClickListener)this);
        map.setOnMyLocationChangeListener(this);
        LatLng vicSquare = new LatLng(-34.928445,138.599195);
        if(map!= null)
        {
	        map.setMyLocationEnabled(true);
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(vicSquare, 13));
	        //set settings
	        map.getUiSettings().setCompassEnabled(false);
	        map.getUiSettings().setMyLocationButtonEnabled(false);
	        map.getUiSettings().setZoomControlsEnabled(false);
	        //map.getUiSettings().setAllGesturesEnabled(false);
        }
        
        uiInterface = new UiInterface();
        uiInterface.setSupportFragmentManager(getSupportFragmentManager());
        
        uiInterface.setCurrentFragment( initfrag );
        //Toast.makeText(this, "This application uses data sourced from the Library of South Australia", Toast.LENGTH_LONG).show();
        
        
        
        
        
        //mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        //mEditor = mPrefs.edit();
 
        //mLocationClient = new LocationClient(this,this,this);
        
        //mUpdatesRequested = false;
        //locman = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//if( mLocationClient.isConnected()){
		//	mLocationClient.removeLocationUpdates(this);
		//}
		//mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//mLocationClient.connect();
		
	}

	public MenuItem closeButton;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		closeButton = menu.findItem(R.id.action_close);
		closeButton.setVisible(false);
		return true;
	}
	public void onAttachFragment (Fragment fragment)
	{
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		uiInterface.setCurrentFragment(getSupportFragmentManager().findFragmentById(R.id.lowercontent));
		((LowerFragment)uiInterface.getCurrentFragment()).onBackReturned();
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	public void onBtnClicked(View v){
		((LowerFragment)uiInterface.getCurrentFragment()).checkInteract(v, uiInterface);
	}
	
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        //case R.id.action_photo:
	        //	dispatchTakePictureIntent();
	        //    return true;
	        //case R.id.action_settings:
	        	//openSettings();
	            //return false;
	        case R.id.action_goto_course:
	        	int count = 0;
	        	while(!(uiInterface.getCurrentFragment() instanceof InitialFragment))
	        	{
	        		onBackPressed();
	        		count++;
	        	}
	        	for(int i = 0; i < count-1;i++)
	        	{
	        		this.getSupportFragmentManager().popBackStack();
	        	}
		    	uiInterface.showUI(R.id.lowercontent, ExploreFragment.newInstance(2, true, courseIds[COURSE_INDEX_USER_SELECTED], true));
		    	return true;
	        case R.id.action_close:
	        	unsetCourse();
	        	return true;
	        	
	        case R.id.action_about:
	        	Intent AboutIntent = new Intent(this, AboutActivity.class);
		    	this.startActivity(AboutIntent);
	        	return true;
	        case R.id.action_help:
	        	Intent introIntent = new Intent(this, IntroViewPager.class);
		    	this.startActivity(introIntent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*public void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity( getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent,  REQUEST_IMAGE_CAPTURE);
	    }
	}
	*/
	static final int REQUEST_TAKE_PHOTO = 1;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        //Uri photoUri;
	        try {
	        	 //photoUri = getOutputMediaFileUri(1);
	             photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	           // System.out.println((String)ex.printStackTrace());
	        }
	        // Continue only if the File was successfully created
	        if  (photoFile != null) {
	             takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	           // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//System.out.println("HERE1 [" + resultCode +"] result ok = [" +RESULT_OK +"]" + " reqCOde ["+requestCode);
		//System.out.println("REsource!" + REQUEST_TAKE_PHOTO);
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			//System.out.println("HERE2");
	        //Bundle extras = data.getExtras();
			//System.out.println("HERE3");
	        //Bitmap imageBitmap = (Bitmap) extras.get("data");
			//System.out.println("HERE4");
	        //ImageView editImage = (ImageView)findViewById(R.id.imageView1);
			//System.out.println("HERE5");
	        //editImage.setImageBitmap(imageBitmap);
			//System.out.println("HERE6");
			//Toast.makeText(this, "Image saved to:\n" +
            //        data.getData(), Toast.LENGTH_LONG).show();
			

	    }
	}
	
	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp  ;
	    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES),"myScavengeAdelaidePics");
	    if (! storageDir.exists()){
	        if (! storageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	  //  File image = File.createTempFile(
	  //      imageFileName,  /* prefix */
	  //      ".jpg",         /* suffix */
	  //      storageDir      /* directory */
	  //  );
 	    File image = new File(storageDir.getPath() + File.separator +
 	    		imageFileName + ".jpg");  
	    // Save a file: path for use with ACTION_VIEW intents
	   // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    mCurrentPhotoPath =   image.getAbsolutePath();
	    System.out.println(mCurrentPhotoPath);
	    galleryAddPic();
	    imageFileName = imageFileName +".jpg";
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    System.out.println(dateFormat.format(date));
	     
	    db.execSQL("INSERT INTO ASPhoto (photoName, photoLocation, photoDate, photoLat, photoLong) VALUES ('"+imageFileName+"','"+ mCurrentPhotoPath + "','" + dateFormat.format(date) +"','-34.92695','138.597225')");
	     
	    return image;
	}
	
	void createExternalStoragePublicPicture(InputStream is, String fileName) {
	    // Create a path where we will place our picture in the user's
	    // public pictures directory.  Note that you should be careful about
	    // what you place here, since the user often manages these files.  For
	    // pictures and other media owned by the application, consider
	    // Context.getExternalMediaDir().
	    File path = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File file = new File(path, fileName);

	    try {
	        // Make sure the Pictures directory exists.
	        path.mkdirs();

	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        //InputStream is = getResources().openRawResource(R.drawable.balloons);
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();

	        // Tell the media scanner about the new file so that it is
	        // immediately available to the user.
	        MediaScannerConnection.scanFile(this,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	            public void onScanCompleted(String path, Uri uri) {
	                Log.i("ExternalStorage", "Scanned " + path + ":");
	                Log.i("ExternalStorage", "-> uri=" + uri);
	            }
	        });
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }
	}

	
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

//	@Override
//	public void onLocationChanged(Location arg0) {
//		// TODO Auto-generated method stub
//		//System.out.println("location updated: ");
//		//String message = "Updated location: " + Double.toString(arg0.getLatitude()) + ", " + Double.toString(arg0.getLongitude());
//		//Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//		if(((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)) != null)
//		{
//		GoogleMap map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//	    
//		//MarkerOptions m = new MarkerOptions();
//		//m.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_scavmarker_purple));
//		//map.addMarker(m.position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("you were here"));
//		}
//		checkDings(arg0, courseIds);	
//		//}
//}
//
//	@Override
//	public void onProviderDisabled(String arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onProviderEnabled(String arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//		// TODO Auto-generated method stub
//		
//	}

	public void checkDings(Location userLoc, int[] courseIds)
	{
		if(userLoc==null)
		{
			return;
		}
		//Log.d("location", "checking dings"); //distanceSquared);
		for(int ci = 0; ci < courseIds.length; ci++)
		{
			if(ci != COURSE_ID_NONE)
			{
				Cursor cursor = db.rawQuery("SELECT DingId FROM CourseLocation WHERE courseId = '" + courseIds[ci] + "' ORDER BY orderNumber ASC", null);
				
				while (cursor.moveToNext()) {
					int dingId = cursor.getInt(cursor.getColumnIndex("DingId"));
					Cursor cursor2 = db.rawQuery("SELECT found, unfoundTitle, foundTitle, loc_lat, loc_long FROM Ding WHERE DingId = "+ dingId + " AND found = 'FALSE'", null);
					if(cursor2.getCount() >0)
					{
						cursor2.moveToNext();
						
						LatLng dingLoc = new LatLng(cursor2.getDouble(cursor2.getColumnIndex("loc_lat")),
								cursor2.getDouble(cursor2.getColumnIndex("loc_long")));
						
						//double distanceSquared = (dingLoc.longitude-userLoc.getLongitude()) * (dingLoc.longitude-userLoc.getLongitude()) +
						//		(dingLoc.latitude-userLoc.getLatitude()) * (dingLoc.latitude-userLoc.getLatitude());
						float[] results = new float[1];
						Location.distanceBetween(dingLoc.latitude, dingLoc.longitude, 
								userLoc.getLatitude(), userLoc.getLongitude(), results);
						if(results != null)
						{
							//Log.d("location", "DistanceSquared: " + results[0]); //distanceSquared);
							
							if (results[0] < DISTANCE_METERS)
							{
								//System.out.println("MARKER WITH DING ID ="+ dingId);
								if (this.activeMarkers != null)
								{
									Marker marker = this.activeMarkers.get(dingId);
									if(marker!= null)
									{
										marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
									}
								}
								//Sets an ID for the notification
							    int mNotificationID =dingId;
						    	Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
							    				.setSmallIcon(R.drawable.ic_asicon) //ic_launcher)
							    				.setContentTitle("Adelaide Scavenge Notification")
							    				.setContentText("Ahoy! You have found a locale")
							    				.setContentInfo("C123D" + mNotificationID +"")
							    				.setSound(soundUri)
							    				.setAutoCancel(true);
							    // Intent resultIntent = new Intent(this.getActivity(), MainActivity.class);
							    //
							    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
							    stackBuilder.addParentStack(DingFragment.class);
							    
							    Intent resultIntent = new Intent( this , DingFragment.class);
							    resultIntent.putExtra("dingId", (int)dingId);
							    
							    
							    stackBuilder.addNextIntent(resultIntent);
							    PendingIntent resultPendingIntent = PendingIntent.getActivity(this,  0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							    
							    mBuilder.setContentIntent(resultPendingIntent);
							    
							    //Gets an instance of the NotificationManager service
							    NotificationManager mNotifyMgr = (NotificationManager)this.getSystemService(Activity.NOTIFICATION_SERVICE);
							    //Builds the notification and issues it.
							    mNotifyMgr.notify(mNotificationID, mBuilder.build());
								//this.starta
							    
								Calendar c = Calendar.getInstance();
								db.execSQL("UPDATE Ding SET found = 'TRUE', datefound = '" + c.getTime().toString() + "' WHERE DingId = "+ dingId + ";");
								//System.out.println(dingId);
							}
						}
					}
					cursor2.close();
				}
				cursor.close();
			}
		}
		
	}
	public void unsetCourse()
	{
		closeButton.setVisible(false);
		courseIds[MainActivity.COURSE_INDEX_USER_SELECTED] = MainActivity.COURSE_ID_NONE;

		this.getSupportActionBar().setTitle("Adelaide Scavenge");
		//this.getSupportActionBar().set
		this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
	}
	public void setCourseAs(int courseId){
		courseIds[MainActivity.COURSE_INDEX_USER_SELECTED] = courseId;
		
		closeButton.setVisible(true);
		
		this.getSupportActionBar().setTitle(DatabaseHelper.getCourseName(db, courseId));
		//this.getSupportActionBar().set
		this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_courseactive));
		
		//Location loc = this.locman.getLastKnownLocation(LOCATION_SERVICE);
		onMyLocationChange(null);
		
		//for (int i =0;i<locman.getAllProviders().size();i++)
		//{
		//	System.out.println("location service: "+ locman.getAllProviders().get(i));
		//}
	}
	@Override
	public void onInfoWindowClick(Marker arg0) {
		System.out.print("markerobj test");
		for (int i = 0; i < this.activeMarkers.size() ; i++)
		{
			Object markerobj = this.activeMarkers.values().toArray()[i];
			if(arg0.equals( markerobj ))
			{
				System.out.print("markerobj");
			}
		}
		
	}
	@Override
	public void onMyLocationChange(Location arg0) {
		
		if (arg0 == null)
		{
			GoogleMap map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		    
			arg0 = map.getMyLocation();
		}
		// TODO Auto-generated method stub
		//System.out.println("location updated: ");
		//String message = "Updated location: " + Double.toString(arg0.getLatitude()) + ", " + Double.toString(arg0.getLongitude());
		//Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		if(((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)) != null)
		{
		GoogleMap map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    
		//MarkerOptions m = new MarkerOptions();
		//m.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_scavmarker_purple));
		//map.addMarker(m.position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("you were here"));
		}
		checkDings(arg0, courseIds);	
	}
}
