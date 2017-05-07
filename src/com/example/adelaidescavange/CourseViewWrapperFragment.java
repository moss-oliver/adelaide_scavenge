package com.example.adelaidescavange;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CourseViewWrapperFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link CourseViewWrapperFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class CourseViewWrapperFragment extends Fragment {
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public SQLiteDatabase db; 
	public int categoryId;
	public FragmentPagerAdapter fpa;
	public ViewPagerFragment vpf;
	public boolean activateCourse;
	public boolean showingPager = false;
	public boolean showMarkers;
	public static CourseViewWrapperFragment newInstance(FragmentPagerAdapter fpa, SQLiteDatabase db, int categoryId, boolean activateCourse, boolean showMarkers) {
		CourseViewWrapperFragment fragment = new CourseViewWrapperFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		fragment.db = db;
		fragment.categoryId = categoryId;
		fragment.fpa = fpa;
		fragment.activateCourse = activateCourse;
		fragment.showMarkers = showMarkers;
		//fragment.adapter = adapter;
		return fragment;
	}

	public CourseViewWrapperFragment() {
		// Required empty public constructor
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // There has to be a view with id `container` inside `wrapper.xml`
        View v = inflater.inflate(R.layout.fragment_course_view_wrapper, container, false);

		if(db == null)
		{
			db = ((MainActivity)getActivity()).getDb();
		}
		
        CourseListFragment list = CourseListFragment.newInstance(db, categoryId);
       // list.setListAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override 
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
              // Create details fragment based on clicked item's position
              //Fragment details = new Fragment();
            	//CourseInfoViewFragment civf = CourseInfoViewFragment.newInstance(position);
            	vpf = ViewPagerFragment.newInstance(db, position, categoryId, activateCourse, true);
            	//vpf.db = db;
            	//vpf.'

        		View oldv = v.findViewById(R.id.courseviewwrapper_container);
        		if(oldv != null)
        		{
        			oldv.setVisibility(View.INVISIBLE);
        		}
        		showingPager = true;

            	getChildFragmentManager()
            	//getFragmentManager()
                  .beginTransaction()
                  .replace(R.id.courseviewwrapper_container, vpf)
                  .addToBackStack(null)
                  .commit();
            	fpa.notifyDataSetChanged();
            }
        });

        getChildFragmentManager()
            .beginTransaction()
            .add(R.id.courseviewwrapper_container, list)
            .commit();
        
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

}
