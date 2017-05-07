package com.example.adelaidescavange;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public abstract class LowerFragment extends Fragment {
	public boolean checkInteract(View v, UiInterface uiInterface)
	{
		if(uiInterface.getCurrentFragment() == this)
		{
			onInteract(v, uiInterface);
			return true;
		}
		return false;
	}
	protected abstract void onInteract(View v, UiInterface uiInterface);
	public void onBackReturned()
	{
	}
}
