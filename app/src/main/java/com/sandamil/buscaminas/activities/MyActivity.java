package com.sandamil.buscaminas.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MyActivity extends Activity{
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
