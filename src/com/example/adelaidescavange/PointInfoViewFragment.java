package com.example.adelaidescavange;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link PointInfoViewFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link PointInfoViewFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class PointInfoViewFragment extends Fragment implements OnClickListener {

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PointInfoViewFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static PointInfoViewFragment newInstance(SQLiteDatabase db, int courseId, int dingId, boolean isNotSecrets) {
		PointInfoViewFragment fragment = new PointInfoViewFragment();
		Bundle args = new Bundle();
		fragment.courseId = courseId;
		fragment.dingId = dingId;
		fragment.db = db;
		fragment.setArguments(args);
		fragment.isNotSecrets = isNotSecrets;
		return fragment;
	}
	int courseId;
	int dingId;
	SQLiteDatabase db;
	boolean isNotSecrets;

	public PointInfoViewFragment() {
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
		View v = inflater.inflate(R.layout.fragment_point_info_view, container,
				false);
		db = ((MainActivity)getActivity()).getDb();
		//((TextView)v.findViewById(R.id.point_info_hello_point)).setText(""+dingId);
		Cursor c = db.rawQuery("SELECT found, foundTitle, unfoundTitle FROM Ding WHERE DingId = " + dingId, null);
		
		while (c.moveToNext())
		{
			TextView nametv = ((TextView)v.findViewById(R.id.point_info_name));
			TextView desctv = ((TextView)v.findViewById(R.id.point_info_desc));
			//nametv.setText(c.getString(c.getColumnIndex("courseName"))); 
			if(!isNotSecrets)
			{
				
				nametv.setText(DatabaseHelper.getTitle(db, 
						c.getString(c.getColumnIndex("found")).equals("TRUE"), 
						c.getInt(c.getColumnIndex("foundTitle")),
						c.getInt(c.getColumnIndex("foundTitle")))
						);

				desctv.setMovementMethod(new ScrollingMovementMethod());
				desctv.setText(DatabaseHelper.getDescription(db, 
						c.getString(c.getColumnIndex("found")).equals("TRUE"), 
						c.getInt(c.getColumnIndex("foundTitle")),
						c.getInt(c.getColumnIndex("foundTitle")))
						);
			}
			else
			{
				nametv.setText(DatabaseHelper.getTitle(db, 
						c.getString(c.getColumnIndex("found")).equals("TRUE"), 
						c.getInt(c.getColumnIndex("unfoundTitle")),
						c.getInt(c.getColumnIndex("unfoundTitle")))
						);
	
				desctv.setMovementMethod(new ScrollingMovementMethod());
				desctv.setText(DatabaseHelper.getDescription(db, 
						c.getString(c.getColumnIndex("found")).equals("TRUE"), 
						c.getInt(c.getColumnIndex("unfoundTitle")),
						c.getInt(c.getColumnIndex("unfoundTitle")))
						);
			
			}
			TextView foundtv = ((TextView)v.findViewById(R.id.point_info_point_found));
			
			if(c.getString(c.getColumnIndex("found")).equals("TRUE"))
			{
				foundtv.setText( "Visited" );
			}
			else
			{
				foundtv.setText( "Not visited" );
			}

			Button bview = ((Button)v.findViewById(R.id.btn_point_info_start));

			if(c.getString(c.getColumnIndex("found")).equals("TRUE"))
			{
				
			}
			else
			{
				bview.setVisibility( View.INVISIBLE);
			}
			
			bview.setOnClickListener(this);
		}
		//((MainActivity)this.getActivity()).setCourseAs(courseId);
		c.close();
		
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

    	Intent dingIntent = new Intent(this.getActivity(), DingFragment.class);
    	dingIntent.putExtra("dingId", (int)dingId);
    	this.startActivity(dingIntent);
	}

}
