package com.sandamil.buscaminas.activities;

import com.sandamil.buscaminas.R;
import com.sandamil.buscaminas.Times;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class StartActivity extends MyActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        Handler h = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
            	ImageButton go = (ImageButton) findViewById(R.id.ImageButton01);
				go.setVisibility(1);
                return true;
            }
        });
        
        h.sendEmptyMessageDelayed(0, Times.GO_BUTTON);
        
        ImageButton go = (ImageButton) findViewById(R.id.ImageButton01);
        
        go.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StartActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        ImageView neblire = (ImageView) findViewById(R.id.ImageView01);
        neblire.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.neblire.com"));
				startActivity(intent);
			}
		});
        
    }
}