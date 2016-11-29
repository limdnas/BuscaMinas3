package com.sandamil.buscaminas;

public class Times {
	public final static long ZOOM = 75;
	public final static long ZOOM_STEP = 1;
	public final static long CHANGE_GAP = 1000;
	public final static long CHANGE_CELL = 400;
	public final static long SHOW_BOARD = 1200;
	public final static long MAX_OPEN_CELL = 1000;
	public final static long GO_BUTTON = 200;
	public final static long BUTTON_PRESSED = 250;
	
	public static void Wait(Object obj, long time){
		synchronized(obj){
			try {
				obj.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}