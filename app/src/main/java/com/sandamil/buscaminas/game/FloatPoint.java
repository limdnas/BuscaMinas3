package com.sandamil.buscaminas.game;

public class FloatPoint {
	float x, y;
	
	public FloatPoint(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public FloatPoint(FloatPoint p){
		this.x = p.x;
		this.y = p.y;
	}

	public void sum(FloatPoint p){
		this.x += p.x;
		this.y += p.y;
	}
	
	public void div(float d){
		this.x /= d;
		this.y /= d;
	}
	
}

