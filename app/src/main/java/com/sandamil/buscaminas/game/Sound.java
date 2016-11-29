package com.sandamil.buscaminas.game;

import java.util.ArrayList;
import com.sandamil.buscaminas.R;
import android.app.Activity;
import android.media.MediaPlayer;

/*public enum Sound {
	GAP() {
		public MediaPlayer create(Activity c) {
			return MediaPlayer.create(c, R.raw.casilla_hueco);
		}
	},
	CURSE() {
		public MediaPlayer create(Activity c) {
			return MediaPlayer.create(c, R.raw.calavera);
		}
	},
	MINE() {
		public MediaPlayer create(Activity c) {
			return MediaPlayer.create(c, R.raw.mina);
		}
	},
	HIGH() {
		public MediaPlayer create(Activity c) {
			return MediaPlayer.create(c, R.raw.puntuacion_alta);
		}
	},
	NUMBER() {
		public MediaPlayer create(Activity c) {
			return MediaPlayer.create(c, R.raw.casilla_numero);
		}
		
	},
	START(){
		public MediaPlayer create(Activity c) {
			MediaPlayer mp = MediaPlayer.create(c, R.raw.sonido_fondo);
			mp.setLooping(true);
			mp.setVolume(0.25f, 0.25f);
			return mp;
		}
	};
	
	public ArrayList<MediaPlayer> players;
	
	public abstract MediaPlayer create(Activity a);
	
	private Sound(){
		players = new ArrayList<MediaPlayer>();
	}
	
	public static void initialize(Activity a){
		Sound[] sounds = Sound.values();
		for (int i = 0; i < sounds.length; i ++){
			sounds[i].players.add(sounds[i].create(a));
		}
	}
		
	public void start(final Activity a){
		a.runOnUiThread(new Runnable() {
			public void run() {
				for (int i = 0; i < players.size(); i++){
					if (players.get(i) == null){
						players.set(i, create(a));
						players.get(i).start();
						return;
					}
					if (! players.get(i).isPlaying()){
						players.get(i).start();
						return;
					}
				}
				players.add(create(a));
				players.get(players.size() - 1).start();
			}
		});
	}
	
	public static void stop(){
		Sound[] sounds = Sound.values();
		for (int i = 0; i < sounds.length; i ++){
			for (int j = 0; j < sounds[i].players.size(); j++){
				MediaPlayer mp = sounds[i].players.get(j);
				if (mp != null && mp.isPlaying()){
					mp.stop();
					mp.release();
					sounds[i].players.set(j, null);
				}
			}
		}
	}
	
	public static void play(OpeningCells opening, int score, Activity c){
		if (opening.size() > 1){
			GAP.start(c);
			return;
		}
		if (opening.getState(0).state != null){
			if (opening.getState(0).state == CellEnum.CURSE){
				CURSE.start(c);
			}
			else{
				if (score < 20){
					MINE.start(c);
				}
				else{
					HIGH.start(c);
				}
			}		
		}
		else
			NUMBER.start(c);
	}
	
	public static void release(){
		Sound[] values = Sound.values();
		for (int i = 0; i < values.length; i++){
			for (MediaPlayer player: values[i].players){
				if (player == null ){
					continue;
				}
				if (player.isPlaying()){
					player.stop();
				}
				player.release();
			}
			values[i].players.clear();
		}
	}
}
*/