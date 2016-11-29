package com.sandamil.buscaminas.game;

import com.sandamil.buscaminas.activities.GameActivity;

public class Human extends Player {

	public Human(GameActivity activity, String name) {
		super(activity, name);
	}

	@Override
	public void turn() {
		activity.board.enableInteraction();
	}
}
