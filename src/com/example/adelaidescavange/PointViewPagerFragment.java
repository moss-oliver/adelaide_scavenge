package com.example.adelaidescavange;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link PointViewPagerFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link PointViewPagerFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class PointViewPagerFragment extends LowerFragment {
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PointViewPagerFragment.
	 */
	// TODO: Rename and change types and number of parameters
	SQLiteDatabase db;
	int courseId;
	boolean showUnfoundPoints;
	public static PointViewPagerFragment newInstance(SQLiteDatabase db, int courseId, boolean showUnfoundPoints) {
		PointViewPagerFragment fragment = new PointViewPagerFragment();
		Bundle args = new Bundle();
		fragment.db = db;
		fragment.courseId = courseId;
		fragment.showUnfoundPoints = showUnfoundPoints;
		System.out.println("show unfound points: " + showUnfoundPoints);
		fragment.setArguments(args);
		return fragment;
	}

	public PointViewPagerFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

		}
		db = ((MainActivity)getActivity()).getDb();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_point_view_pager, container,
				false);

		TabPageIndicator indicator = (TabPageIndicator)(v.findViewById(R.id.pointindicator));
		ViewPager vp =  (ViewPager) v.findViewById(R.id.pagerpoints);
		PointSliderAdapter psa = new PointSliderAdapter(getChildFragmentManager(), db, courseId);
		vp.setAdapter(psa);
		vp.setOnPageChangeListener(psa);
        indicator.setOnPageChangeListener(psa);
		indicator.setViewPager(vp);
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

	class PointSliderAdapter extends FragmentPagerAdapter implements OnPageChangeListener {
		public PointSliderAdapter(FragmentManager fm, SQLiteDatabase db, int courseId) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.db = db;
			
			Cursor courseLocCursor = db.rawQuery("SELECT DingId FROM CourseLocation WHERE courseId = " + courseId +" ORDER BY orderNumber ASC", null);
			
			while(courseLocCursor.moveToNext())
			{
				int DingId = courseLocCursor.getInt(courseLocCursor.getColumnIndex("DingId"));
				Cursor dingCursor = db.rawQuery("SELECT found FROM Ding WHERE DingId = " + DingId +"", null);
				dingCursor.moveToNext();
				if(dingCursor.getString(dingCursor.getColumnIndex("found")).equals("TRUE") || showUnfoundPoints)
				{
					dingIds.add(DingId);
				}
				dingCursor.close();
			}
			courseLocCursor.close();
			
			if(dingIds.size()>0)
			{
				if(((MainActivity)getActivity()).activeMarkers != null)
				{
					
					System.out.println(dingIds.get(0));
					System.out.println(((MainActivity)getActivity()).activeMarkers.get(dingIds.get(0)));
					((MainActivity)getActivity()).activeMarkers.get(dingIds.get(0))
						.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					((MainActivity)getActivity()).selectedMarker = ((MainActivity)getActivity()).activeMarkers.get(dingIds.get(0));
					((MainActivity)getActivity()).selectedMarkerDingId = dingIds.get(0);
				}
			}
		}
		
        @Override
        public CharSequence getPageTitle(int position) {
        //	System.out.println("GETPAGETITLE   " +categorynames.get(position).toUpperCase());
            //return CONTENT[position % CONTENT.length].toUpperCase();

			if(dingIds.size()== 0)
			{
				return "No points found yet";
			}
			
        	return "Point " + (position+1);
        }

		SQLiteDatabase db;
		java.util.ArrayList<Integer> dingIds = new java.util.ArrayList<Integer>();

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			//ViewPagerFragment.clearMapPointsAndShowNewCourse(((MainActivity)getActivity()).getDb(), courseId, getActivity(), showUnfoundPoints);
			//((MainActivity)getActivity()).activeMarkers.get(dingIds.get(arg0))
			//.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));			
			if( ((MainActivity)getActivity()).selectedMarker != null )
			{
				Cursor dingCursor = db.rawQuery("SELECT found FROM Ding WHERE DingId = " + (((MainActivity)getActivity()).selectedMarkerDingId) +"", null);
				dingCursor.moveToNext();
				if(dingCursor.getString(dingCursor.getColumnIndex("found")).equals("TRUE") )
				{
					((MainActivity)getActivity()).selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				}else{
					((MainActivity)getActivity()).selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				}
			}
			
			((MainActivity)getActivity()).selectedMarker = ((MainActivity)getActivity()).activeMarkers.get(dingIds.get(arg0));
			(((MainActivity)getActivity()).selectedMarkerDingId) = dingIds.get(arg0);
			((MainActivity)getActivity()).selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			//System.out.println( ((MainActivity)getActivity()).activeMarkers.get(dingIds.get(arg0)).getTitle() );
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			if(dingIds.size()== 0)
			{
				return EmptyFragment.newInstance();
			}
			return PointInfoViewFragment.newInstance(db, courseId, dingIds.get(arg0), showUnfoundPoints);
			//return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(dingIds.size()== 0)
			{
				return 1;
			}
			return dingIds.size();
		}
	}

	@Override
	protected void onInteract(View v, UiInterface uiInterface) {
		// TODO Auto-generated method stub
		
	}

}
