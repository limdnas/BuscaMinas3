package com.sandamil.buscaminas.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.sandamil.buscaminas.R;
import com.sandamil.buscaminas.game.BoardView;
import com.sandamil.buscaminas.game.GameConfig;
import com.sandamil.buscaminas.game.GameControl;

import static com.sandamil.buscaminas.R.styleable.BoardView;
/*import com.sandamil.buscaminas.game.Sound;
import com.sandamil.buscaminas.websocket.MyWebSocket;*/

//	NOTA:	Debido a que no hay ningun activity que se vaya a colocar parcialmente delante de este,
//			los eventos onPause() y onResume() no son necesario implementarlos.

public class GameActivity extends MyActivity {
	public BoardView board;
	public TextView score;
	public TextView info;
	public TextView servidor;
	public ZoomControls zoom;
	public GameControl game;
	public int rows;
	
	//public MyWebSocket socket;
	
	public boolean ready;
	
	@Override
	protected void onSaveInstanceState(Bundle save) {
		ready = false;
		super.onSaveInstanceState(save);
		save.putBundle("GameControl", game.pause());
		save.putString("score", score.getText().toString());
		save.putString("info", info.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle restore) {
		super.onRestoreInstanceState(restore);
	}
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ready = false;
		//Sound.initialize(this);

		GameConfig config = (GameConfig) getIntent().getExtras().get(GameConfig.GAME_CONFIG_KEY);
		
		rows = config.rows;
		createLayout();
		if (bundle != null){
			Bundle b = bundle.getBundle("GameControl");
			score.setText(bundle.getString("score"));
			info.setText(bundle.getString("info"));
			game = new GameControl(this, b);
			game.resume();
			return;
		}
		game = new GameControl(this, config);
		game.start();
	}

	public void onDestroy() {
		super.onDestroy();
		game.stop();
		//Sound.release();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		game.resume();
	}

	private void createLayout() {
		setContentView(R.layout.game);
		board = (BoardView) findViewById(R.id.game_view);
		score = (TextView) findViewById(R.id.marcador);
		info = (TextView) findViewById(R.id.info);
		
		//servidor = (TextView) findViewById(R.id.info);
		
		zoom = (ZoomControls) findViewById(R.id.zoom);
		zoom.setVisibility(View.INVISIBLE);
		board.setFocusable(true);
		board.setFocusableInTouchMode(true);
		board.setZoomControls(zoom);
	}
}