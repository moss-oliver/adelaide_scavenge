package com.example.adelaidescavange;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class CourseListFragment extends ListFragment {
	
	public String[] values;
	public GoogleMap googleMap;
	
	public CourseListFragment(){
		
	}
	public static CourseListFragment newInstance(SQLiteDatabase db, int categoryId) {
		CourseListFragment fragment = new CourseListFragment();
		Bundle args = new Bundle();
		args.putInt("categoryId", categoryId);
		fragment.setArguments(args);
        return fragment;
	}
	SQLiteDatabase db;
	int categoryId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) { 

    	if(getArguments() != null)
    	{
    		categoryId = this.getArguments().getInt("categoryId");
    	}
    	
    	db = ((MainActivity)getActivity()).getDb();
    	Cursor cursor = db.rawQuery("SELECT courseName FROM Course WHERE categoryId = " + Integer.toString(categoryId), null);

		ArrayList<String> str = new ArrayList<String>();
        while (cursor.moveToNext()) {
        	str.add(cursor.getString(cursor.getColumnIndex("courseName")));
            //categoryids.add(cursor.getInt(cursor.getColumnIndex("categoryId")));
            //categorynames.add(cursor.getString(cursor.getColumnIndex("categoryName")));
        }
        cursor.close();
        Object[] obj = str.toArray();
        String[] stringArray = Arrays.asList(obj).toArray(new String[obj.length]);
        this.values = stringArray;
    	
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,values);
 
        /** Setting the list adapter for the ListFragment */
        setListAdapter(adapter);
  
         
                // Getting a reference to the map
                googleMap = ((SupportMapFragment) this.getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
         
                // Setting a click event handler for the map
                googleMap.setOnMapClickListener(new OnMapClickListener() {
         
                    public void onMapClick(LatLng latLng) {
         
                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();
         
                        // Setting the position for the marker
                        //markerOptions.position(latLng);
         
                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        //System.out.println("MARKER TOUCHED");
                        //markerOptions.title("TITLE OF MARKER:" + latLng.latitude + " : " + latLng.longitude);
         
                        // Clears the previously touched position
                        //googleMap.clear();
         
                        // Animating to the touched position
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
         
                        // Placing a marker on the touched position
                        //googleMap.addMarker(markerOptions);
                         
                    }
                });
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //v.setBackgroundColor(0xFF00FF00);
        v.setBackgroundColor(0xFFFFFFFF);//getActivity().getResources().getColor(android.R.attr.colorBackground));
        return v;
    }
    
    public void onListItemClick(ListView l, View v, int position, long id) { 
        //System.out.println("item Clicked [" + values[position] + "]  position [" + position + "]" + id);
        //Toast.makeText(this.getActivity(), "List Item Clicked [" + values[position] + "]  position [" + position + "]", Toast.LENGTH_LONG).show();
        if(ocl != null)
        {
            ocl.onItemClick(l, v, position, id);
        }
        //((MainActivity)getActivity()).setCourseAs(courseid);
       // ((MainActivity)this.getActivity()).setCourseAs(courseId);
    }
    private OnItemClickListener ocl;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		// TODO Auto-generated method stub
		ocl = onItemClickListener;
	}
     
}
