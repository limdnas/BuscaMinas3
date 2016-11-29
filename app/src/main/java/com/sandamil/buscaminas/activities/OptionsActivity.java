package com.sandamil.buscaminas.activities;

import com.sandamil.buscaminas.ButtonsAppearance;
import com.sandamil.buscaminas.R;
import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.game.GameConfig;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class OptionsActivity extends MyActivity implements ViewSwitcher.ViewFactory{
	GameConfig gameConfig;
	String[] nameLevels;
	EditText et1;
	TextSwitcher txs1;
	SeekBar sb1;
	TextSwitcher txs2;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        Bundle bundle = getIntent().getExtras();
        gameConfig = bundle.getParcelable(GameConfig.GAME_CONFIG_KEY);
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        
        
        //SECCION NOMBRE DE USUARIO
        et1 = (EditText)findViewById(R.id.EditText01);
        et1.setText(gameConfig.player1Name); 
        //SECCION SELECCION DE NIVEL
        nameLevels = getResources().getStringArray(R.array.cpulevellist);
        txs1 = (TextSwitcher)findViewById(R.id.TextSwitcher01);
        txs1.setFactory(this);
        txs1.setInAnimation(in);
        txs1.setOutAnimation(out);
        txs1.setText(nameLevels[gameConfig.cpuLevel - 1]);
		final Button pre1 = (Button) findViewById(R.id.Button01);
		ButtonsAppearance.setDrawableTo(pre1,1);
		pre1.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ButtonsAppearance.setDrawableTo(pre1,3);
				ButtonsAppearance.disminuirTamTexto(pre1);	
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(pre1);
		            	ButtonsAppearance.setDrawableTo(pre1, 1);
						if (gameConfig.cpuLevel > GameConfig.CPU_LEVEL1){
							txs1.setText(nameLevels[--gameConfig.cpuLevel - 1]);
							gameConfig.player1Level = gameConfig.cpuLevel;
							gameConfig.player2Level = gameConfig.cpuLevel;
						}
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
		final Button nex1 = (Button) findViewById(R.id.Button02);
		ButtonsAppearance.setDrawableTo(nex1,1);
		nex1.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ButtonsAppearance.setDrawableTo(nex1,3);
				ButtonsAppearance.disminuirTamTexto(nex1);	
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(nex1);
		            	ButtonsAppearance.setDrawableTo(nex1, 1);
						if (gameConfig.cpuLevel < GameConfig.CPU_LEVEL4){
							txs1.setText(nameLevels[++gameConfig.cpuLevel - 1]);
							gameConfig.player1Level = gameConfig.cpuLevel;
							gameConfig.player2Level = gameConfig.cpuLevel;
						}
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
		
		//SECCION PORCENTAJE DE MINAS
		sb1 = (SeekBar)findViewById(R.id.SeekBar01);
		sb1.setMax(GameConfig.MAX_P_MINES - GameConfig.MIN_P_MINES);
		sb1.setProgress(((int)(gameConfig.percentOfMines * 100)) - GameConfig.MIN_P_MINES);
		final TextView tv5 = (TextView)findViewById(R.id.TextView05);
		tv5.setText(String.valueOf(sb1.getProgress() + GameConfig.MIN_P_MINES)+"%");
		sb1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				gameConfig.percentOfMines = ((float)(sb1.getProgress() + GameConfig.MIN_P_MINES)) / 100;
			}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				tv5.setText(String.valueOf(progress + GameConfig.MIN_P_MINES)+"%");
			}
		});
		
		//SECCION SELECCION TAMA�O TABLERO
		txs2 = (TextSwitcher)findViewById(R.id.TextSwitcher02);
        txs2.setFactory(this);
        txs2.setInAnimation(in);
        txs2.setOutAnimation(out);
        txs2.setText(String.valueOf(gameConfig.rows) + " X " + String.valueOf(gameConfig.rows)); 
        final Button pre2 = (Button) findViewById(R.id.Button03);
        ButtonsAppearance.setDrawableTo(pre2,1);
		pre2.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ButtonsAppearance.setDrawableTo(pre2,3);
				ButtonsAppearance.disminuirTamTexto(pre2);	
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(pre2);
		            	ButtonsAppearance.setDrawableTo(pre2, 1);
						if (gameConfig.rows > GameConfig.BOARD_MIN_SIZE){
							gameConfig.rows--;
							txs2.setText(String.valueOf(gameConfig.rows) + " X " + String.valueOf(gameConfig.rows));
						}
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
		final Button nex2 = (Button) findViewById(R.id.Button04);
		ButtonsAppearance.setDrawableTo(nex2,1);
		nex2.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ButtonsAppearance.setDrawableTo(nex2,3);
				ButtonsAppearance.disminuirTamTexto(nex2);	
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(nex2);
		            	ButtonsAppearance.setDrawableTo(nex2, 1);
						if (gameConfig.rows < GameConfig.BOARD_MAX_SIZE){
							gameConfig.rows++;
							txs2.setText(String.valueOf(gameConfig.rows) + " X " + String.valueOf(gameConfig.rows));
						}
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
		//SECCION RESTAURAR VALORES POR DEFECTO
		final Button rDefault = (Button) findViewById(R.id.Button05);
		ButtonsAppearance.setDrawableTo(rDefault,1);
		rDefault.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ButtonsAppearance.setDrawableTo(rDefault,3);
				ButtonsAppearance.disminuirTamTexto(rDefault);	
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(rDefault);
		            	ButtonsAppearance.setDrawableTo(rDefault, 1);
		            	RestoreDefaultOptions();
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
	}
	public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(20);
        return t;
	}
	private void returnToCaller(View view){
		final EditText et1 = (EditText)findViewById(R.id.EditText01);
		gameConfig.player1Name = et1.getText().toString();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable(GameConfig.GAME_CONFIG_KEY,gameConfig);
		intent.putExtras(bundle);
		setResult(0,intent);
		finish();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)  { 
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
	        returnToCaller(null); 
	    return true; 
	}
	private void RestoreDefaultOptions(){
		gameConfig.setDefaultOptions();
		et1.setText(gameConfig.player1Name);
		txs1.setText(nameLevels[gameConfig.cpuLevel - 1]);
		sb1.setProgress(((int)(gameConfig.percentOfMines * 100)) - GameConfig.MIN_P_MINES);
		txs2.setText(String.valueOf(gameConfig.rows) + " X " + String.valueOf(gameConfig.rows));
	}
}
