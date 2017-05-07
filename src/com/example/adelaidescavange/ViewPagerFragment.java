package com.example.adelaidescavange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ViewPagerFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ViewPagerFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class ViewPagerFragment extends Fragment {
	private static final Field sChildFragmentManagerField;
	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			
			f.setAccessible(true);
			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sChildFragmentManagerField = f;
	}
	
	private OnFragmentInteractionListener mListener;
	
	public CourseSliderAdapter pageradapter;
	private boolean activateCourse;
	public int visiblecourseid = -1;
	
	public boolean showPoints;
	
	private int startPos = 0;
	public static ViewPagerFragment newInstance(SQLiteDatabase db, int startPos, int categoryId, boolean activateCourse, boolean showPoints) {
		ViewPagerFragment fragment = new ViewPagerFragment();
		//fragment.startPos = startPos;
		Bundle args = new Bundle();
		args.putInt("startPos", startPos);
		args.putInt("categoryid", categoryId);
		args.putBoolean("activateCourse", activateCourse);
		fragment.setArguments(args);
		fragment.db = db;
		fragment.showPoints = showPoints;
		return fragment;
	}

	public ViewPagerFragment() {
		// Required empty public constructor
	}
	static public void clearMapPoints(FragmentActivity activity)
	{
		MainActivity ma = (MainActivity)(activity);
		if(ma.activeMarkers != null)
		{
			Collection<Marker> ema = ma.activeMarkers.values();
			//while(ema.hasMoreElements())
			//{
			//	ema.nextElement().remove();
			//}
			Iterator<Marker> i = ma.activeMarkers.values().iterator();
			while(i.hasNext())
			{
				Marker m = i.next();
				m.remove();
			}
			ma.activeMarkers = null;
		}
		if(ma.mapLine != null)
		{
			ma.mapLine.remove();
		}
	}
	static public void clearMapPointsAndShowNewCourse(SQLiteDatabase db, int id, FragmentActivity activity, boolean showUnfoundPoints, boolean showLine, boolean showMarker)
	{
		clearMapPoints(activity);

		MainActivity ma = (MainActivity)(activity);
		
		ma.activeMarkers = new HashMap<Integer,Marker>();
		GoogleMap map = ((SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	//map.clear();
		//System.out.println("courseid: " + id);
    	Cursor cursor = db.rawQuery("SELECT DingId FROM CourseLocation WHERE courseId = "+ id + " ORDER BY orderNumber ASC", null);

    	Builder b = LatLngBounds.builder();
    	int pointcount= 0;
		PolylineOptions pl = new PolylineOptions();
		
    	while (cursor.moveToNext()) {
    		pointcount++;
			int dingId = cursor.getInt(cursor.getColumnIndex("DingId"));
			//System.out.println("dingid: " + dingId);
			Cursor cursor2 = db.rawQuery("SELECT found, unfoundTitle, foundTitle, loc_lat, loc_long FROM Ding WHERE dingId = "+ dingId, null);
			cursor2.moveToNext();
			if(cursor2.getString(cursor2.getColumnIndex("found")).equals("TRUE") || showUnfoundPoints )
			{
				LatLng loc = new LatLng(cursor2.getDouble(cursor2.getColumnIndex("loc_lat")),
						cursor2.getDouble(cursor2.getColumnIndex("loc_long")));
				String markerTitle = DatabaseHelper.getTitle(db, cursor2.getString(cursor2.getColumnIndex("found")).equals("TRUE"),cursor2.getInt(cursor2.getColumnIndex("unfoundTitle")),cursor2.getInt(cursor2.getColumnIndex("foundTitle")));
				String markerDesc = DatabaseHelper.getDescription(db, cursor2.getString(cursor2.getColumnIndex("found")).equals("TRUE"),cursor2.getInt(cursor2.getColumnIndex("unfoundTitle")),cursor2.getInt(cursor2.getColumnIndex("foundTitle")));
	
				if(markerDesc.length()>20)
				{
					markerDesc = markerDesc.substring(0, 17) + "...";
				}
				
				Marker tempmarker= map.addMarker(new MarkerOptions().position(loc)
						.title(markerTitle)
						.snippet(markerDesc)
						);
				System.out.println(showMarker);
    			tempmarker.setVisible(showMarker);
    			pl.add(loc);
				//System.out.println("found index: " + Integer.toString(cursor2.getColumnIndex("found")));cursor2.getString(cursor2.getColumnIndex("found"))=="TRUE"
				//System.out.println("found value: " + cursor2.getString((cursor2.getColumnIndex("found"))));
				//System.out.println("found value: " + (cursor2.getString((cursor2.getColumnIndex("found"))).equals("TRUE")));
				//System.out.println("found value: " + Integer.toString(cursor2.getInt(cursor2.getColumnIndex("found"))));
				if (cursor2.getString(cursor2.getColumnIndex("found")).equals("TRUE"))
				{
					tempmarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				}
				tempmarker.setDraggable(false);
				ma.activeMarkers.put(dingId, tempmarker);
				//System.out.println("dingID: " + dingId);
	        	b.include(loc);
			}
        	cursor2.close();
		}
    	if(pointcount != 0)
    	{
			pl.color(0x640EBFF9);
			
			if(showLine)
			{
				ma.mapLine = map.addPolyline(pl);
			}
			
    		try{
    			map.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 100));
    		}
    		catch(java.lang.RuntimeException e){
    		}
    	}
    	cursor.close();
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			System.out.println("getArguments() != null");
			startPos = getArguments().getInt("startPos");
			categoryid= getArguments().getInt("categoryid");
			activateCourse =  getArguments().getBoolean("activateCourse");
		}
		System.out.println("oncreate called: " + categoryid);
	}
	
	private SQLiteDatabase db;
	private int categoryid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_view_pager, container, false);
		ToggleHGestureViewPager pager = (ToggleHGestureViewPager)(v.findViewById(R.id.pagercourses));
		System.out.println("2"+db);
		db = ((MainActivity)getActivity()).getDb();
		pageradapter = new CourseSliderAdapter(getChildFragmentManager(), db, categoryid, getActivity());
		pager.setAdapter(pageradapter);
		pager.setOnPageChangeListener(pageradapter);
		pager.setCurrentItem(startPos);
		pageradapter.onPageSelected(startPos) ;
		//v.setBackground(Drawable. android.R.attr.colorBackground);
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
		//System.out.println("detach1");
		if(sChildFragmentManagerField != null)
		{
			try {
				//System.out.println("detach2");
				sChildFragmentManagerField.set(this, null);
				//System.out.println("detach3");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//System.out.println("detach4");
				Log.e("logtag", "error setting mchildfragmentmanager field",e);
			}
		}
		//System.out.println("detach5");
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	
	class CourseSliderAdapter extends FragmentPagerAdapter implements OnPageChangeListener {
		SQLiteDatabase db;
		ArrayList<String> coursenames = new ArrayList<String>();
		ArrayList<Integer> courseids = new ArrayList<Integer>();
        public CourseSliderAdapter(FragmentManager fm, SQLiteDatabase db, int categoryId, FragmentActivity activity) {
        	super(fm);
        	this.db = ((MainActivity)getActivity()).getDb();
    		System.out.println("category id: "+categoryId);
        	Cursor cursor = db.rawQuery("SELECT courseId, courseName FROM Course WHERE categoryId = " + Integer.toString(categoryId), null);
    		//Cursor cursor = db.rawQuery("SELECT categoryId, categoryName FROM Category WHERE " +
        	//	"categoryId IN (SELECT categoryId FROM CategoryHeading WHERE headingId == "+Integer.toString(headingID)+")", null);
            //categoryids.add(1);
            while (cursor.moveToNext()) {
            	String cn = cursor.getString(cursor.getColumnIndex("courseName"));
            	coursenames.add(cn);
            	//System.out.println(cn);
            	int ci = cursor.getInt(cursor.getColumnIndex("courseId"));
            	courseids.add(ci);
                //categoryids.add(cursor.getInt(cursor.getColumnIndex("categoryId")));
                //categorynames.add(cursor.getString(cursor.getColumnIndex("categoryName")));
            }
            cursor.close();
            if(courseids.size() >0)
            {
            	if(activity != null)
            	{
            		clearMapPointsAndShowNewCourse(db, courseids.get(0), activity, true, true, showPoints);
            	}
            	else
            	{
            		clearMapPointsAndShowNewCourse(db, courseids.get(0), getActivity(), true, true, showPoints);
            	}
            }
        }
        public Fragment getItem(int position) {
        	//System.out.println("getItem 1");
        	//return TestFragment.newInstance(values[position]);
        	return CourseInfoViewFragment.newInstance(db,courseids.get(position), showPoints);
        }
        
        public int getCount(){
        	return courseids.size();
        }
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageSelected(int position) {
			visiblecourseid = courseids.get(position);
			if(activateCourse)
			{
				((MainActivity)getActivity()).setCourseAs(courseids.get(position));
			}
        	clearMapPointsAndShowNewCourse(db, courseids.get(position), getActivity(), true, true, showPoints);
		}
	}

}




















