package com.sandamil.buscaminas.game;


public class Position {
	private FloatPoint center;
	private int size;
	
	public Position(FloatPoint center, int size){
		this.size = size;
		this.center = center;
	}

	public FloatPoint getCenter() {
		return center;
	}

	public void setCenter(FloatPoint center) {
		this.center = center;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}