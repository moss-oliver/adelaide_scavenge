package com.example.adelaidescavange;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class UiInterface {
	private FragmentManager sfm;
	public void setSupportFragmentManager(FragmentManager sfm)
	{
		this.sfm = sfm;
	}
	private Fragment currentFragment;
	public void setCurrentFragment(Fragment f)
	{
		currentFragment = f;
	}
	public Fragment getCurrentFragment()
	{
		return currentFragment;
	}
	public void showUI(int viewid,Fragment newfragment)
	{
		currentFragment = newfragment;
		FragmentTransaction ft = sfm.beginTransaction();
	    //ft.setTransition(FragmentTransaction.);
		//ft.setCustomAnimations(R.anim.slide_in_from_bottom,R.anim.nothing,R.anim.nothing,R.anim.slide_away);
		//ft.setCustomAnimations(R.anim.slide_away,R.anim.slide_in_from_bottom,R.anim.nothing,R.anim.slide_away);
		ft.setCustomAnimations(R.anim.slide_in_from_bottom,R.anim.slide_away,R.anim.nothing,R.anim.slide_away);
	    //Fragment abcFragment = new SupportMapFragment();
	    ft.replace(viewid,newfragment); 
	    ft.addToBackStack(null);
	    ft.commit();
	    
	    //this.getSupportActionBar().getCustomView().;
		//newfragment.getView().setBackgroundResource(android.R.color.white);
	}
}
