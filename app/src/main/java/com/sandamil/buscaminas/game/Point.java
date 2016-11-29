package com.sandamil.buscaminas.game;

public class Point {
	int x, y;
	
	public Point(float x, float y){
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public static boolean equals(Point a, Point b){
		if (a.x == b.x && a.y == b.y){
			return true;
		}
		return false;
	}
}
