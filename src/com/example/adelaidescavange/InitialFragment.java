package com.example.adelaidescavange;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InitialFragment extends LowerFragment {

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment InitialFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static InitialFragment newInstance() {
		InitialFragment fragment = new InitialFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	

	public InitialFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		
		View v = inflater.inflate(R.layout.fragment_initial, container, false);
		
		Cursor c = ((MainActivity)getActivity()).getDb().rawQuery("SELECT url FROM Ding WHERE dingId = " + MainActivity.FACT_DING_ID, null);
		c.moveToNext();
		String url = c.getString(c.getColumnIndex("url"));
		ImageDownloader.getInstance(getActivity()).downloadAndShowImage(url, (ImageView)v.findViewById(R.id.fact_pic));           	
		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	String url;
	Bitmap displayImage;
	//InputStream is;
	Drawable d;
	@Override
	protected void onInteract(View v, UiInterface uiInterface) {
		//System.out.println(v.getId());
		//System.out.println(R.id.btnExplore);
		//System.out.println(R.id.btnTrophies);
		//System.out.println(R.id.btnCourse);
	    if(v.getId() == R.id.btnExplore){
	    	uiInterface.showUI(R.id.lowercontent, ExploreFragment.newInstance(1, false, -1, false));
	    }
	    if(v.getId() == R.id.btnTrophies){
	    	//System.out.println("trophies pressed");
	    	//uiInterface.showUI(R.id.lowercontent, ExploreFragment.newInstance(3, false));
	    	ViewPagerFragment.clearMapPointsAndShowNewCourse(((MainActivity)getActivity()).getDb(), 0, getActivity(), false, false, true);
			uiInterface.showUI(R.id.lowercontent, PointViewPagerFragment.newInstance(((MainActivity)getActivity()).getDb(), 0, false));
			//uiInterface.showUI(R.id.lowercontent, PointViewPagerFragment.newInstance(((MainActivity)getActivity()).getDb(), MainActivity.COURSE_ID_SECRETS, false));
			//ViewPagerFragment.clearMapPointsAndShowNewCourse(((MainActivity)getActivity()).getDb(), MainActivity.COURSE_ID_SECRETS, getActivity(), false);
	    }
	    if(v.getId() == R.id.btnCourse){
	    	uiInterface.showUI(R.id.lowercontent, ExploreFragment.newInstance(2, true, -1, true));
	    }
	    if(v.getId() == R.id.btnfactbutton)
	    {
	    	//System.out.println("fact button pressed");
	    	//show ding
	    	//ding id = 10
	    	Intent dingIntent = new Intent(this.getActivity(), DingFragment.class);
	    	dingIntent.putExtra("dingId", MainActivity.FACT_DING_ID);
	    	this.startActivity(dingIntent);
	    }
	}
	@Override
	public void onBackReturned() {
		// TODO Auto-generated method stub
		super.onBackReturned();
		ViewPagerFragment.clearMapPoints(getActivity());
	}
}




