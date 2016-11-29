package com.sandamil.buscaminas.activities;

import com.sandamil.buscaminas.ButtonsAppearance;
import com.sandamil.buscaminas.R;
import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.game.GameConfig;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MenuActivity extends MyActivity {
	
	GameConfig gameConfig = new GameConfig();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        loadGameOptions();
        final Button op1 = (Button) findViewById(R.id.Button01);
        ButtonsAppearance.setDrawableTo(op1,1);
        final Button op4 = (Button) findViewById(R.id.Button04);
        ButtonsAppearance.setDrawableTo(op4,4);
        final Button op2 = (Button) findViewById(R.id.Button02);
        ButtonsAppearance.setDrawableTo(op2,1);
        final Button op3 = (Button) findViewById(R.id.Button03);
        ButtonsAppearance.setDrawableTo(op3,1);
        op1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reset();
				ButtonsAppearance.setDrawableTo(op1, 3);
				ButtonsAppearance.disminuirTamTexto(op1);				
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(op1);
		            	ButtonsAppearance.setDrawableTo(op1, 1);
		            	Intent intent = new Intent(MenuActivity.this, PlayActivity.class);
		            	Bundle bundle = new Bundle();
		            	bundle.putParcelable(GameConfig.GAME_CONFIG_KEY,gameConfig); 
						intent.putExtras(bundle);
		            	startActivity(intent);
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});        
        op4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//
			}
		});
        op2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reset();
				ButtonsAppearance.setDrawableTo(op2, 3);
				ButtonsAppearance.disminuirTamTexto(op2);
				
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(op2);
		            	ButtonsAppearance.setDrawableTo(op2, 1);
		            	Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
		            	Bundle bundle = new Bundle();
		            	bundle.putParcelable(GameConfig.GAME_CONFIG_KEY,gameConfig); 
		            	intent.putExtras(bundle);
		            	startActivityForResult(intent,0);
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
        op3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reset();
				ButtonsAppearance.setDrawableTo(op3, 3);
				ButtonsAppearance.disminuirTamTexto(op3);
				
				Handler h = new Handler(new Handler.Callback() {
		            public boolean handleMessage(Message msg) {
		            	ButtonsAppearance.aumentarTamTexto(op3);
		            	ButtonsAppearance.setDrawableTo(op3, 1);
		            	AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
						builder.setMessage(R.string.salir)
						       .setCancelable(false)
						       .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						                finish();
						           }
						       })
						       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						                dialog.cancel();
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
		                return true;
		            }
		        });
		        h.sendEmptyMessageDelayed(0, Times.BUTTON_PRESSED);
			}
		});
        
        // esta no cambia: mandar e-mail
        ImageView iv2 = (ImageView) findViewById(R.id.ImageView02);
        iv2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendEmail();
			}
		});
        
        op1.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ButtonsAppearance.setDrawableTo(op1, 2);
				} else {
					ButtonsAppearance.setDrawableTo(op1, 1);
				}					
			}
		});    
        op4.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				/*if (hasFocus) {
					ButtonsAppearance.setDrawableTo(op4, 2);
				} else {
					ButtonsAppearance.setDrawableTo(op4, 1);
				}*/						
			}
		});
        op2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ButtonsAppearance.setDrawableTo(op2, 2);
				} else {
					ButtonsAppearance.setDrawableTo(op2, 1);
				}						
			}
		});    
        op3.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ButtonsAppearance.setDrawableTo(op3, 2);
				} else {
					ButtonsAppearance.setDrawableTo(op3, 1);
				}					
			}
		});
    }
    
    public void reset() {
    	final Button op1 = (Button) findViewById(R.id.Button01);
        ButtonsAppearance.setDrawableTo(op1,1);
        /*final Button op4 = (Button) findViewById(R.id.Button04);
        ButtonsAppearance.setDrawableTo(op4,1);*/
        final Button op2 = (Button) findViewById(R.id.Button02);
        ButtonsAppearance.setDrawableTo(op2,1);
        final Button op3 = (Button) findViewById(R.id.Button03);
        ButtonsAppearance.setDrawableTo(op3,1);
    }
    
    public void sendEmail() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String[] mailto = { "admin@neblire.com" };
        sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo));
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.cuerpo));
        sendIntent.setType("text/csv");
        startActivity(Intent.createChooser(sendIntent, "Notificar a Neblire"));
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        super.onActivityResult(requestCode, resultCode, data); 
        if(requestCode == 0){ 
        		Bundle bundle = data.getExtras();
                gameConfig = bundle.getParcelable(GameConfig.GAME_CONFIG_KEY);
                saveGameOptions();
        }
    }
     
    private void loadGameOptions(){
        SharedPreferences prefs = getSharedPreferences("GameConfig",MODE_PRIVATE);
        gameConfig.player1 = prefs.getInt("player1",GameConfig.PLAYER_IS_HUMAN);
        gameConfig.player1Name = prefs.getString("player1Name", "player_1");
        gameConfig.player1Level = prefs.getInt("player1Level",GameConfig.DEFAULT_CPU_LEVEL);
        gameConfig.player2 = prefs.getInt("player2",GameConfig.PLAYER_IS_CPU);
        gameConfig.player2Name = prefs.getString("player2Name","Cpu_1");
        gameConfig.player2Level = prefs.getInt("player2Level",GameConfig.DEFAULT_CPU_LEVEL);
        gameConfig.rows = prefs.getInt("rows",GameConfig.DEFAULT_BOARD_SIZE);
        gameConfig.percentOfMines = prefs.getFloat("percentOfMines", ((float)GameConfig.DEFAULT_P_MINES / 100));
        //gameConfig.percentOfCurses = prefs.getFloat("percentOfCurses", ((float)GameConfig.DEFAULT_P_CURSES / 100));
        gameConfig.percentOfCurses = 0.0f;
        gameConfig.cpuLevel = prefs.getInt("cpuLevel",GameConfig.DEFAULT_CPU_LEVEL);
    }
    
    private void saveGameOptions(){
    	SharedPreferences prefs = getSharedPreferences("GameConfig",MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();	      
        editor.putInt("player1", gameConfig.player1);
        editor.putString("player1Name", gameConfig.player1Name);
        editor.putInt("player1Level",gameConfig.player1Level);
        editor.putInt("player2",gameConfig.player2);
        editor.putString("player2Name",gameConfig.player2Name);
        editor.putInt("player2Level",gameConfig.player2Level);
        editor.putInt("rows",gameConfig.rows);
        editor.putFloat("percentOfMines",gameConfig.percentOfMines);
        //prefs.getFloat("percentOfCurses",gameConfig.percentOfCurses);
        editor.putInt("cpuLevel",gameConfig.cpuLevel); 
        editor.commit();
    }
}