package com.sandamil.buscaminas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.sandamil.buscaminas.R;
import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.game.GameConfig;

public class PlayActivity extends MyActivity {
	private Handler handler;
	private Button jug1;
    private Button jug2;
    private Button jug3;
    private Button jug4;
    private Button jug5;
    Bundle bundle;
    GameConfig gameConfig;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        bundle = getIntent().getExtras();
        gameConfig = bundle.getParcelable(GameConfig.GAME_CONFIG_KEY);
        handler = new Handler();
        jug1 = (Button) findViewById(R.id.Button01);
        jug2 = (Button) findViewById(R.id.Button02);
        jug3 = (Button) findViewById(R.id.Button03);
        jug4 = (Button) findViewById(R.id.Button04);
        jug5 = (Button) findViewById(R.id.Button05);

        jug1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler.postDelayed(new Runnable() {
					public void run() {
		            	Intent intent = new Intent(PlayActivity.this, GameActivity.class);
		            	gameConfig.player2 = GameConfig.PLAYER_IS_CPU;
		            	intent.putExtras(bundle);
						startActivity(intent);
					}
				}, Times.BUTTON_PRESSED);
			}		
		});
        jug2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler.postDelayed(new Runnable() {
					public void run() {
		            	Intent intent = new Intent(PlayActivity.this, GameActivity.class);
		            	gameConfig.player2 = GameConfig.PLAYER_IS_HUMAN;
		            	gameConfig.player2Name = "Player_2";
		            	intent.putExtras(bundle);
						startActivity(intent);
					}
				}, Times.BUTTON_PRESSED);
			}
		});
        jug4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler.postDelayed(new Runnable() {
					public void run() {
		            	Intent intent = new Intent(PlayActivity.this, GameActivity.class);
		            	gameConfig.player2 = GameConfig.PLAYER_IS_HUMAN;
		            	gameConfig.player2Name = "Player_2";
		            	intent.putExtras(bundle);
						startActivity(intent);
					}
				}, Times.BUTTON_PRESSED);
			}
		});
        jug1.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
				} else {
				}
			}
		});        
        jug2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
				} else {
				}
			}
		});
        jug4.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
				} else {
				}
			}
		});
    }
}