package com.sandamil.buscaminas.game;

import com.sandamil.buscaminas.activities.GameActivity;

public abstract class Player{
	protected int score;
	protected String name;
	protected GameActivity activity;

	public abstract void turn();
	
	public Player(GameActivity activity, String name){
		this.activity = activity;
		this.name = name;
	}
	
	public Player(){}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}    
        
	public boolean openCells(OpeningCells opening, int playerId) {
		if (activity.ready){
//			Sound.play(opening, score, activity);
			return activity.board.openCells(opening, playerId);
		}
		return false;
	}
}