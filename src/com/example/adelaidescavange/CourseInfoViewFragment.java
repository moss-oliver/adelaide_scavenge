package com.example.adelaidescavange;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CourseInfoViewFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link CourseInfoViewFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class CourseInfoViewFragment extends Fragment implements OnClickListener {

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
	public int courseId;
	public SQLiteDatabase db;
	public boolean showPoints;
	// TODO: Rename and change types and number of parameters
	public static CourseInfoViewFragment newInstance(SQLiteDatabase db, int courseId, boolean showPoints) {
		CourseInfoViewFragment fragment = new CourseInfoViewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		fragment.courseId=courseId;
		fragment.db=db;
		fragment.showPoints = showPoints;
		return fragment;
	}

	public CourseInfoViewFragment() {
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
		View v = inflater.inflate(R.layout.fragment_course_info_view, container,
				false);
		 
		Cursor c = db.rawQuery("SELECT courseName, description FROM course WHERE courseId = " + courseId, null);
		
		while (c.moveToNext())
		{
			TextView nametv = ((TextView)v.findViewById(R.id.course_info_name));
			nametv.setText(c.getString(c.getColumnIndex("courseName")));

			TextView desctv = ((TextView)v.findViewById(R.id.course_info_description));
			desctv.setText(c.getString(c.getColumnIndex("description")));
			//WebView descwv = (WebView)v.findViewById(R.id.course_info_description);
			//String text = "<html><body>"
			//+ "<p align=\"justify\">"
			//+ c.getString(c.getColumnIndex("description"))
			//         + "</p> "
			//+ "</body></html>";
			//descwv.loadData(text, "text/html", "utf-8");
			
			TextView fcomptv = ((TextView)v.findViewById(R.id.course_info_countFound));
			fcomptv.setText(DatabaseHelper.getFractionCourseCompleted(db,courseId));
		}
		//((MainActivity)this.getActivity()).setCourseAs(courseId);
		c.close();
		v.findViewById( R.id.btn_course_info_start ).setOnClickListener(this);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//((MainActivity)this.getActivity()).setCourseAs(this.courseId);
		ViewPagerFragment.clearMapPointsAndShowNewCourse(db, courseId, getActivity(), true, true, showPoints);
		((MainActivity)this.getActivity()).uiInterface.showUI(R.id.lowercontent, PointViewPagerFragment.newInstance(db, courseId, true));
		
	}

}
