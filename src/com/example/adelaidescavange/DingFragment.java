package com.example.adelaidescavange;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
 
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CourseInfoViewFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link CourseInfoViewFragment#newInstance}
 * factory method to create an instance of this fragment.
 *  
 */
public class DingFragment extends ActionBarActivity {

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment CourseInfoViewFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static DingFragment newInstance() {
		DingFragment fragment = new DingFragment();
		return fragment;
	}
	private int dingNumber;
	public SQLiteDatabase db;
	
	public DingFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ding);
		String ding_dingID;
		String ding_FoundTitleId;
		String ding_FoundTitle;
		String ding_FoundDesc;
		//dingNumber
    	ScavengeDatabaseHelper helper = new ScavengeDatabaseHelper( this,MainActivity.dbName,null, MainActivity.DATABASE_VERSION);
    	db = helper.getWritableDatabase();
    	dingNumber = 2;
    	//this.getIntent().get
    	
    	Bundle b = this.getIntent().getExtras();

		for (String key: b.keySet())
		{
		  Log.d ("dingfragment", key + " is a key in the bundle");
		}
		
    	dingNumber = b.getInt("dingId");
    	System.out.println("dingnumner = " +dingNumber);
    	
		//set actionbar to transparent
		this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
		ding_dingID = getphotoURL(""+dingNumber, db);
		if((ding_dingID == null) || (ding_dingID.equals("")))
		{
			((ImageView)this.findViewById(R.id.imageView_ding) ).setImageResource(R.drawable.ic_asicon);
		}
		else{
			ImageDownloader.getInstance(this).downloadAndShowImage(ding_dingID, (ImageView)this.findViewById(R.id.imageView_ding));           	
		}
		
		ding_FoundTitleId = getDingFoundTitle(""+dingNumber, db);

		Cursor cursor = db.rawQuery("SELECT datefound FROM Ding WHERE dingId = " + dingNumber + ";", null);
     	cursor.moveToNext();
		
    	Cursor titleAndDesc = db.rawQuery("SELECT title, description FROM Title WHERE titleId = "+ding_FoundTitleId ,null);
    	titleAndDesc.moveToNext();
    	
    	ding_FoundTitle = titleAndDesc.getString(titleAndDesc.getColumnIndex("title"));
    	ding_FoundDesc = titleAndDesc.getString(titleAndDesc.getColumnIndex("description"));
    	
		TextView txtViewDingHeading = (TextView)this.findViewById(R.id.textDingHeading);
		txtViewDingHeading.setText(ding_FoundTitle);
		
		TextView txtViewDingDesc = (TextView)this.findViewById(R.id.textDingText);
		txtViewDingDesc.setText(ding_FoundDesc);
		titleAndDesc.close();
		
		TextView txtViewDingH2 = (TextView)this.findViewById(R.id.textDingHeading2);
		String dateFound = cursor.getString(cursor.getColumnIndex("datefound"));
		if(dateFound != null)
		{
			txtViewDingH2.setText("Found on "+ dateFound);
		}
		else
		{
			txtViewDingH2.setText("");
		}
		
		cursor.close();
	}
	
	public String getphotoURL(String lDingID, SQLiteDatabase pdb){
		String photoUrl ="";
		Cursor cursor = pdb.rawQuery("SELECT url FROM Ding WHERE dingId = " + lDingID + ";", null);
     	System.out.println("Ding Photo Url:  [" + cursor.getCount() + "]");
        while (cursor.moveToNext()) { 
        	//System.out.println("ADDING_pointsOnMap");
        	photoUrl = cursor.getString(cursor.getColumnIndex("url"));
        	//System.out.println(photoUrl);
        	
        }
        cursor.close();
        return photoUrl;
	}
	public String getDingFoundTitle(String lDingID, SQLiteDatabase pdb){
		String lfoundTitle ="NO HEADING";
		Cursor cursor = pdb.rawQuery("SELECT foundTitle FROM Ding WHERE dingId = " + lDingID + ";", null);
     	//System.out.println("Ding Photo Url:  [" + cursor.getCount() + "]");
        while (cursor.moveToNext()) { 
        	//System.out.println("ADDING_pointsOnMap");
        	lfoundTitle = cursor.getString(cursor.getColumnIndex("foundTitle"));
        	//System.out.println(lfoundTitle);
        	
        	
        }
        cursor.close();
        return lfoundTitle;
	}
 
	
	//public UiInterface uiInterface;
	
	LocationManager locman;

	 
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onAttachFragment (Fragment fragment)
	{
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		//uiInterface.setCurrentFragment(getSupportFragmentManager().findFragmentById(R.id.lowercontent));
	}

 
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        //case R.id.action_photo:
	        //	onBackPressed();
	        //    return true;
	        case R.id.action_settings:
	           // openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("HERE1 [" + resultCode +"] result ok = [" +RESULT_OK +"]" + " reqCOde ["+requestCode);
	/*	System.out.println("REsource!" + REQUEST_TAKE_PHOTO);
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			System.out.println("HERE2");
	        Bundle extras = data.getExtras();
			System.out.println("HERE3");
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
			System.out.println("HERE4");
	        ImageView editImage = (ImageView)findViewById(R.id.imageView1);
			System.out.println("HERE5");
	        editImage.setImageBitmap(imageBitmap);
			System.out.println("HERE6");
			Toast.makeText(this, "Image saved to:\n" +
                    data.getData(), Toast.LENGTH_LONG).show();

	    }*/
	}
}