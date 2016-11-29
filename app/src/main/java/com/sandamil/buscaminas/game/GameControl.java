package com.sandamil.buscaminas.game;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.activities.GameActivity;
import com.sandamil.buscaminas.game.Cell;
import com.sandamil.buscaminas.game.CellState;
import com.sandamil.buscaminas.game.Neighborhood;
import com.sandamil.buscaminas.game.OpeningCells;
import com.sandamil.buscaminas.game.Player;
//import com.sandamil.buscaminas.game.Sound;
import com.sandamil.buscaminas.game.CellEnum;
//import com.sandamil.buscaminas.websocket.MyWebSocket;

public class GameControl {
	private GameActivity activity;
	private Cell[] cells;
	private Player players[];
	private int victoryPoints;
	private int rows;
	private int mines;
	private int curses;
	private float percentOfMines;
	private float percentOfCurses;
	private Neighborhood neighbor;
	private boolean started;
	private int turn;
	private int changePlayerCount;
	private Handler gameHandler;
	private Thread gameThread;
	private OpeningCells opening;
	private Runnable pauseRunnable;
	private Runnable resumeRunnable;
	private Runnable openCellsWithChange;
	private Runnable openCellsWithoutChange;
	private Bundle bundle;
	
	private final static int WITH_CHANGE = 0;
	private final static int WITHOUT_CHANGE = 1;
	
	public GameControl(GameActivity activity, GameConfig config) {
		this.activity = activity;
		started = false;
		gameHandler = null;
		players = new Player[2];
		if (config.player1 == GameConfig.PLAYER_IS_CPU){
			players[0] = new Cpu(activity, config.player1Name, config.player1Level);
		}
		else{
			players[0] = new Human(activity, config.player1Name);
		}
		if (config.player2 == GameConfig.PLAYER_IS_CPU){
			players[1] = new Cpu(activity, config.player2Name, config.player2Level);
		}
		else{
			players[1] = new Human(activity, config.player2Name);
		}
		rows = config.rows;
		opening = new OpeningCells(rows * rows);
		neighbor = new Neighborhood(rows, rows);
		percentOfMines = config.percentOfMines;
//		percentOfMines = 0.8f;
		percentOfCurses = config.percentOfCurses;
		initializeRunnables();
	}
	
	public GameControl(GameActivity activity, Bundle b) {
		this.activity = activity;
		gameHandler = null;
		cells = (Cell[]) b.getParcelableArray("cells");
		players = new Player[2];
		for (int i = 0; i < players.length; i++){
			String type = b.getString("player" + i);
			if (type == "Human"){
				players[i] = new Human(activity, b.getString("player" + i + "name"));
			}
			else if (type == "Cpu"){
				players[i] = new Cpu(activity, b.getString("player" + i + "name"), b.getInt("player" + i + "level"));
			}
			players[i].setScore(b.getInt("player" + i + "score"));
		}
		victoryPoints = b.getInt("victoryPoints");
		rows = b.getInt("rows");
		mines = b.getInt("mines");
		curses = b.getInt("curses");
		percentOfMines = b.getFloat("percentOfMines");
		percentOfCurses = b.getFloat("percentOfCurses");
		started = b.getBoolean("started");
		turn = b.getInt("turn");
		changePlayerCount = b.getInt("changePlayerCount");
		neighbor = new Neighborhood(rows, rows);
		initializeRunnables();
		if (b.containsKey("resumeRunnable")){
			opening = b.getParcelable("opening");
			if (b.getInt("resumeRunnable") == WITH_CHANGE){
				resumeRunnable = openCellsWithChange;
			}
			else{
				resumeRunnable = openCellsWithoutChange;
			}
		}
		else{
			opening = new OpeningCells(rows * rows);
		}
	}
	
	private void initializeRunnables(){
		resumeRunnable = null;
		pauseRunnable = new Runnable() {
			public void run() {
				gameHandler = null;
				Looper.myLooper().quit();
			}
		};
		openCellsWithChange = new Runnable() {
			public void run() {
				if (!openCells()){
					gamePostAtFront(pauseRunnable);
					resumeRunnable = this;
					if (bundle != null){
						bundle.putInt("resumeRunnable", WITH_CHANGE);
						bundle.putParcelable("opening", opening);
					}
					return;
				}
				if (players[turn].score == victoryPoints) {
					gameFinished();
					return;
				}
				changePlayer();
				nextTurn();
			}
		};
		openCellsWithoutChange = new Runnable() {
			public void run() {
				if (!openCells()){
					gamePostAtFront(pauseRunnable);
					resumeRunnable = this;
					if (bundle != null){
						bundle.putInt("resumeRunnable", WITHOUT_CHANGE);
						bundle.putParcelable("opening", opening);
					}
					return;
				}
				if (players[turn].score == victoryPoints) {
					gameFinished();
					return;
				}
				nextTurn();
			}
		};
	}
	
	public void gamePost(Runnable r){
		if (gameHandler != null){
			gameHandler.post(r);
		}
	}
	
	public void gamePostAtFront(Runnable r){
		if (gameHandler != null){
			gameHandler.postAtFrontOfQueue(r);
		}
	}
	
	public int getColumns() {
		return rows;
	}

	public int getRows() {
		return rows;
	}

	public void start() {
		if (started){
			return;
		}
		players[0].setScore(0);
		players[1].setScore(0);
		started = true;
		victoryPoints = (int) ((rows * rows * percentOfMines) / 2.0f) + 1;
		mines = victoryPoints * 2 - 1;
		curses = (int) (rows * rows * percentOfCurses);
		cells = new Cell[rows * rows];
		for (int pos = 0; pos < rows * rows; pos++) {
			cells[pos] = new Cell(0, false, false, false);
		}
		int seed = (short) (Math.random() * Short.MAX_VALUE);
		Random r = new Random(seed);
		for (int i = 0; i < mines; i++) {
			int pos;
			do {
				pos = new Double(r.nextDouble() * rows * rows).intValue();
			} while (cells[pos].mined == true);
			cells[pos].mined = true;
		}
		for (int i = 0; i < curses; i++) {
			int pos;
			do {
				pos = new Double(r.nextDouble() * rows * rows).intValue();
			} while (cells[pos].mined == true || cells[pos].cursed == true);
			cells[pos].cursed = true;
		}
		if (Math.random() > 0.5) {
			turn = 1;
		} else {
			turn = 0;
		}
		gameThread = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				gameHandler = new Handler();
				
//				activity.socket = new MyWebSocket(activity);
//				activity.socket.crear();
				
				while (! activity.ready){
					Times.Wait(this, 100);
				}
				activity.board.showBoard();
				if (activity.ready){
//					Sound.START.start(activity);
				}
				nextTurn();
				Looper.loop();
			}
		}, "GAME");
		gameThread.start();
		return;
	}
	
	public Bundle pause(){
		makeBundle();
		gamePostAtFront(pauseRunnable);
		boolean retry = true;
		while (retry) {
			try {
				gameThread.join();
				retry = false;
			} 	catch (InterruptedException e) {}
		}
//		Sound.stop();
		return bundle;
	}
	
	public void stop(){
		gamePostAtFront(pauseRunnable);
		boolean retry = true;
		while (retry) {
			try {
				gameThread.join();
				retry = false;
			} 	catch (InterruptedException e) {}
		}
//		Sound.stop();
	}
	
	public void resume() {
		gameThread = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				gameHandler = new Handler();
				while (! activity.ready){
					Times.Wait(this, 100);
				}
				activity.board.showBoard();
				if (activity.ready){
//					Sound.START.start(activity);
				}
				if (started){
					if (resumeRunnable != null){
						Runnable temp = resumeRunnable;
						resumeRunnable = null;
						temp.run();
					}
					else{
						players[turn].turn();
					}
				}
				Looper.loop();
			}
		}, "GAME");
		gameThread.start();
	}
	
	private void gameFinished() {
		started = false;
		changePlayerCount++;
		activity.runOnUiThread(new Runnable() {
			public void run() {
				setScore();
				activity.info.setText(players[turn].name + " wins");
			}
		});
	}

	private void changePlayer() {
		turn = (turn + 1) % 2;
		changePlayerCount++;
	}

	private void nextTurn(){
		updateInfo(turn);
		players[turn].turn();
	}
	
	private void updateInfo(final int turn) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				setScore();
				activity.info.setText(players[turn].name + "'s turn");
			}
		});
	}
	
	private void setScore(){
		activity.score.setText(players[0].name + "  " + players[0].score + " - " + players[1].score + "  " + players[1].name + " " + "   (Win: " + victoryPoints + " points)");
	}
	
	public boolean isCellOpened(int cell){
		return cells[cell].opened;
	}
	
	public int getMinesAround(int cell){
		return cells[cell].minesAround;
	}
	
	public int getCursesAround(int cell){
		return cells[cell].cursesAround;
	}
	
	public boolean isCellMined(int cell){
		if (!cells[cell].opened){
			return false;
		}
		return cells[cell].mined;
	}
	
	public int getMines(){
		return mines;
	}
	
	public Player getPlayer(int index){
		return players[index];
	}
	
	private void recursive(int p) {
		for (Neighborhood.Iterator it = neighbor.iterator(p); it.hasNext();) {
			if (cells[it.get()].mined) {
				cells[p].minesAround++;
			}
			else if (cells[it.get()].cursed) {
				cells[p].cursesAround++;
			}
		}
		setStateOfCell(p, cells[p].minesAround, cells[p].cursesAround);
		if (cells[p].minesAround == 0 && cells[p].cursesAround == 0) {
			for (Neighborhood.Iterator it = neighbor.iterator(p); it.hasNext();) {
				if (cells[it.get()].opened == false) {
					cells[it.get()].opened = true;
					recursive(it.get());
				}
			}
		}
	}
	
	public void cellSelected(int pos) {
		
//		activity.socket.enviar("Se ha seleccionado la celda: " + pos);
		
		cells[pos].opened = true;
		firstCellSelected(pos);
		boolean changePlayer = true;
		if (cells[pos].mined == true) {
			setStateOfCell(pos, CellEnum.getMine(turn));
			players[turn].score++;
			changePlayer = false;
		}
		else if (cells[pos].cursed == true) {
			setStateOfCell(pos, CellEnum.CURSE);
			players[turn].score--;
		}
		else {
			recursive(pos);
		}
		if (changePlayer){
			openCellsWithChange.run();
		}
		else{
			openCellsWithoutChange.run();
		}
	}
	
	
	private void firstCellSelected(int cell){
    	opening.reset();
    }
	
	private void setStateOfCell(int cell, CellEnum value) {
    	opening.add(cell, new CellState(value, 0, 0));
	}

	private void setStateOfCell(int cell, int minesAround, int cursesAround) {
    	opening.add(cell, new CellState(null, minesAround, cursesAround));
	}
	
	private boolean openCells(){
		return players[turn].openCells(opening, turn);
	}
	
	public void cellPressed(final int pos){
		final int count = changePlayerCount;
		gamePost(new Runnable() {
			public void run() {
				Cell c = cells[pos];
		    	if (!c.opened && started && activity.ready && count == changePlayerCount){
		    		cellSelected(pos);
		        }
			}
		});
    }
	
	public void makeBundle() {
		bundle = new Bundle();
		bundle.putParcelableArray("cells", cells);
		for (int i = 0; i < players.length; i++){
			if (players[i] instanceof Human){
				bundle.putString("player" + i, "Human");
			}
			else if (players[i] instanceof Cpu){
				bundle.putString("player" + i, "Cpu");
				bundle.putInt("player" + i + "level", ((Cpu)players[i]).getLevel());
			}
			bundle.putInt("player" + i + "score", players[i].getScore());
			bundle.putString("player" + i + "name", players[i].getName());
		}
		bundle.putInt("victoryPoints", victoryPoints);
		bundle.putInt("rows", rows);
		bundle.putInt("mines", mines);
		bundle.putInt("curses", curses);
		bundle.putFloat("percentOfMines", percentOfMines);
		bundle.putFloat("percentOfCurses", percentOfCurses);
		bundle.putBoolean("started", started);
		bundle.putInt("turn", turn);
		bundle.putInt("changePlayerCount", changePlayerCount);
	}
}
