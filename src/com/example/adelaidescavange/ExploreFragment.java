package com.example.adelaidescavange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

import database.CategoryHeadingContract;
import database.CourseLayoutContract;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ExploreFragment#newInstance} factory
 * method to create an instance of this fragment.
 * @param <YourActivity>
 * 
 */
public class ExploreFragment extends LowerFragment {
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

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ExploreFragment.
	 */
	int headingId;
	boolean activateCourse;
	int initialcourse;
	int initialindex;
	boolean showMarkers;
	// TODO: Rename and change types and number of parameters
	public static ExploreFragment newInstance(int headingId, boolean activateCourse, int courseId, boolean showMarkers) {
		ExploreFragment fragment = new ExploreFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		fragment.headingId = headingId;
		fragment.activateCourse = activateCourse;
		fragment.initialcourse = courseId;
		fragment.showMarkers = showMarkers;
		return fragment;
	}

	public ExploreFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	//public UiInterface uiInterface;
	ToggleHGestureViewPager pager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_explore, container, false);
		
    	SQLiteDatabase db = ((MainActivity)this.getActivity()).getDb();

		TabPageIndicator indicator = (TabPageIndicator)(v.findViewById(R.id.pointindicator));
		pager = (ToggleHGestureViewPager)(v.findViewById(R.id.pager));
		
		CourseCategoryAdapter adapter = new CourseCategoryAdapter(this.getChildFragmentManager(), db, headingId);
		//FragmentPagerAdapter adapter = new CourseCategoryAdapter(getActivity().getSupportFragmentManager(), db, 1);

        pager.setAdapter(adapter);
        pager.setPagingEnabled(false);
        //pager.setOnPageChangeListener(adapter);
        indicator.setOnPageChangeListener(adapter);

        indicator.setViewPager(pager);
        
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
		System.out.println("detach1");
		if(sChildFragmentManagerField != null)
		{
			try {
				System.out.println("detach2");
				sChildFragmentManagerField.set(this, null);
				System.out.println("detach3");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("detach4");
				Log.e("logtag", "error setting mchildfragmentmanager field",e);
			}
		}
		System.out.println("detach5");
		mListener = null;
	}

	@Override
	protected void onInteract(View v, UiInterface uiInterface) {
		// TODO Auto-generated method stub
		//System.out.println("Ineraction - View [" + v.toString() + "]   interface [" + uiInterface.toString() + "]");
	}
	
	//private static final String[] CONTENT = new String[] { "Recent", "Artists", "Albums", "Songs", "Playlists", "Genres" };

	class CourseCategoryAdapter extends FragmentPagerAdapter implements OnPageChangeListener {
		java.util.ArrayList<Integer> categoryids;
		java.util.ArrayList<String> categorynames;
		java.util.ArrayList<Integer> layoutids;
		SQLiteDatabase db;
        public CourseCategoryAdapter(FragmentManager fm, SQLiteDatabase db, int headingID) {
            super(fm);
            this.db = db;
            
            //CategoryHeadingContract c = new CategoryHeadingContract();
            categoryids = new java.util.ArrayList<Integer>();
            categorynames = new java.util.ArrayList<String>();
            layoutids = new java.util.ArrayList<Integer>();
            //db.query(c.getName(), new String[]{c.CATEGORYID}, selection, selectionArgs, groupBy, having, orderBy)
            Cursor cursor = db.rawQuery("SELECT categoryId, categoryName FROM Category WHERE " +
            		"categoryId IN (SELECT categoryId FROM CategoryHeading WHERE headingId == "+Integer.toString(headingID)+")", null);
            //categoryids.add(1);
            while (cursor.moveToNext()) {
            	int categoryid = cursor.getInt(cursor.getColumnIndex("categoryId"));
	        	String categoryname = cursor.getString(cursor.getColumnIndex("categoryName"));
	            categoryids.add(categoryid);
	            categorynames.add(categoryname);
                
                Cursor cursor2 = db.rawQuery("SELECT courseLayoutId FROM CategoryHeading WHERE (categoryId = '"+ Integer.toString(categoryid) +"') AND (headingId = '"+ headingID +"')", null);
                cursor2.moveToNext();
                layoutids.add(cursor2.getInt(cursor2.getColumnIndex("courseLayoutId")));
				cursor2.close();
            }
			cursor.close();
			//System.out.println("initialcourse: " +initialcourse);
			initialindex = -1;
			if(initialcourse != -1)
			{
				//SELECT ALL CATEGORIES THAT HOLD A COURSE
				//System.out.println("SELECT categoryId FROM Course WHERE (courseId = '"+ initialcourse +"')");
				cursor = db.rawQuery("SELECT categoryId FROM Course WHERE (courseId = '"+ initialcourse +"')", null);
				cursor.moveToNext();
				int index = 0;
				int catId = cursor.getInt(cursor.getColumnIndex("categoryId"));
				//System.out.println("catId = " +catId);
				for(int i = 0; i < categoryids.size(); i++)
				{
					//System.out.println("categoryids (" +i+ ") = " +categoryids.get(i));
					if (categoryids.get(i) == catId)
					{
						index = i;
					}
				}
				cursor.close();
				initialindex = index;
                pager.setCurrentItem(index);
			}
			//System.out.println("initialindex" +initialindex);
        }

        public String getCategorynames(int position){
        	return categorynames.get(position);
        }
        
        public void onListItemClick(ListView l, View v, int position, long id) {  
        	//System.out.println("CLICKED");
        	
        }
        Map<Integer, Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();
        @Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
            mPageReferenceMap.remove(position);
			super.destroyItem(container, position, object);
		}

		@Override
        public Fragment getItem(int position) {
        	//System.out.println("GET ITEM:" + position);
            //return TestFragment.newInstance(categorynames.get(position));
        	// int is category
        	//return CourseListFragment.newInstance(0);
        	//this.
			//System.out.println("pos:"+position+ ", initialindex:" +initialindex);
        	
        	if(initialindex != -1)
        	{
        		if(position == initialindex)
        		{
        	    	Cursor cursor = db.rawQuery("SELECT courseId FROM Course WHERE categoryId = " + categoryids.get(position), null);
        	    	int foundIndex = 0;
        	    	int counter = 0;
        	        while (cursor.moveToNext()) {
        	        	if(cursor.getInt(cursor.getColumnIndex("courseId")) == initialcourse)
        	        	{
        	        		foundIndex = counter;
        	        	}
        	        	counter++;
        	        }
        	        cursor.close();
        			ViewPagerFragment f = ViewPagerFragment.newInstance(db, foundIndex, categoryids.get(position), activateCourse, showMarkers);
                	initialcourse = -1;
                	initialindex = -1;
                    mPageReferenceMap.put(position, f);
                	return f;
        		}
        	}
        	
        	if(layoutids.get(position) == CourseLayoutContract.VALUE_VERTICAL_ID)
        	{
        		//System.out.println("VERTICAL" +getCategorynames(position));
        		//Fragment tmpFragment = (Fragment) CourseListFragment.newInstance(db, categoryids.get(position));
        		//return tmpFragment; //CourseListFragment.newInstance(db, categoryids.get(position));
        		//this.destroyItem(this, position, this);
        		//System.out.println("2"+db);
        		CourseViewWrapperFragment cvwf = CourseViewWrapperFragment.newInstance(this,db, categoryids.get(position), activateCourse, showMarkers);
        		//cvwf.adapter = new ViewPagerFragment.CourseSliderAdapter();
                mPageReferenceMap.put(position, cvwf);
        		return cvwf;
        	}
        	if(layoutids.get(position) == CourseLayoutContract.VALUE_HORIZONTAL_ID)
        	{
        		//System.out.println("Horizontal"+getCategorynames(position));
        		//return CourseList.newInstance(getActivity().getSupportFragmentManager(), db, categoryids.get(position));
        		//ToggleHGestureViewPager thgvp = new ToggleHGestureViewPager(getActivity(),null);
        		//thgvp.setAdapter(new CourseSliderAdapter(getActivity().getSupportFragmentManager(), db, categoryids.get(position)));
        		//return thgvp;
        		//System.out.println("value Horizontal");
        		ViewPagerFragment f = ViewPagerFragment.newInstance(db, 0, categoryids.get(position), activateCourse, showMarkers);
        		//f.db = db;
        		//System.out.println("value Horizontal View pager created");
        		//f.thgvp = thgvp;
        		
        		//System.out.println("value Horizontal courseslider");
        		//f.getChildFragmentManager().
                mPageReferenceMap.put(position, f);
        		return f;
        	}
        	return null;
        }

         
        
        
        
        @Override
        public CharSequence getPageTitle(int position) {
        //	System.out.println("GETPAGETITLE   " +categorynames.get(position).toUpperCase());
            //return CONTENT[position % CONTENT.length].toUpperCase();
        	return categorynames.get(position).toUpperCase();
        }

        @Override
        public int getCount() {
        	//return CONTENT.length;
        	return categorynames.size();
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
		public void onPageSelected(int arg0) {
			System.out.println("arg0 = " + arg0);
			// TODO Auto-generated method stub
			CourseViewWrapperFragment v = (CourseViewWrapperFragment)(this.mPageReferenceMap.get(arg0));
			//v.
			ViewPagerFragment.clearMapPoints(getActivity());
			
			if(v != null)
			{
				if(v.showingPager)
				{
					ViewPagerFragment.clearMapPointsAndShowNewCourse(db, v.vpf.visiblecourseid, getActivity(), true, true, showMarkers);
					
				}
			}
		}
    }


}
