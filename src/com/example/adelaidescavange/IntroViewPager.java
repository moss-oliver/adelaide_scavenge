package com.example.adelaidescavange;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class IntroViewPager extends ActionBarActivity  implements OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_view_pager);
		
		ViewPager vp = ((ViewPager)this.findViewById(R.id.introviewpager_pager));
		IntroViewPagerAdapter ivpa = new IntroViewPagerAdapter(this.getSupportFragmentManager());
		vp.setAdapter(ivpa);

		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public void onBtnClicked(View v)
	{
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.intro_view_pager, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	class IntroViewPagerAdapter extends FragmentPagerAdapter {

		public IntroViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			int viewnumber = arg0+1;
			
			switch(viewnumber)
			{
			case 1:
				return IntroFragment_1.newInstance();
			case 2:
				return IntroFragment_2.newInstance();
			case 3:
				return IntroFragment_3.newInstance();
			}
			
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	
}
