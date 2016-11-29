package com.sandamil.buscaminas.game;

import android.os.Parcel;
import android.os.Parcelable;

public class GameConfig implements Parcelable {

	public int player1;
	public String player1Name;
	public int player1Level;
	
	public int player2;
	public String player2Name;
	public int player2Level;

	public int rows;
	public float percentOfMines;
	public float percentOfCurses;
	public int cpuLevel;

	public final static int PLAYER_IS_CPU = 0;
	public final static int PLAYER_IS_HUMAN = 1;

	public final static int CPU_LEVEL1 = 1;
	public final static int CPU_LEVEL2 = 2;
	public final static int CPU_LEVEL3 = 3;
	public final static int CPU_LEVEL4 = 4;
	public final static int DEFAULT_CPU_LEVEL = 2;
	
	public final static int MIN_P_MINES = 10;
	public final static int MAX_P_MINES = 35;
	public final static int DEFAULT_P_MINES = 19;
	
	public final static int MIN_P_CURSES = 0;
	public final static int MAX_P_CURSES = 0;
	public final static int DEFAULT_P_CURSES = 0;
	
	
	public final static int BOARD_MIN_SIZE = 8;
	public final static int BOARD_MAX_SIZE = 20;
	public final static int DEFAULT_BOARD_SIZE = 14;
	
	public final static String GAME_CONFIG_KEY = "GAME_CONFIG_KEY";
	
	public GameConfig(Parcel in) {
		readFromParcel(in);
	}

	public GameConfig() {			
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(player1);
		dest.writeString(player1Name);
		dest.writeInt(player1Level);
		
		dest.writeInt(player2);
		dest.writeString(player2Name);
		dest.writeInt(player2Level);
		
		dest.writeInt(rows);
		dest.writeFloat(percentOfMines);
		dest.writeFloat(percentOfCurses);
		dest.writeInt(cpuLevel);
	}

	private void readFromParcel(Parcel in) {
		player1 = in.readInt();
		player1Name = in.readString();
		player1Level = in.readInt();
		
		player2 = in.readInt();
		player2Name = in.readString();
		player2Level = in.readInt();
		
		rows = in.readInt();
		percentOfMines = in.readFloat();
		percentOfCurses = in.readFloat();
		cpuLevel = in.readInt();
	}

	public static final Parcelable.Creator<GameConfig> CREATOR = new Parcelable.Creator<GameConfig>() {
		public GameConfig createFromParcel(Parcel in) {
			return new GameConfig(in);
		}

		public GameConfig[] newArray(int size) {
			return new GameConfig[size];
		}
	};
	
    public void setDefaultOptions(){
		player1 = PLAYER_IS_HUMAN;
		player1Name = "Player";
		player1Level = DEFAULT_CPU_LEVEL;
		
		player2 = PLAYER_IS_CPU;
		player2Name = "Android";
		player2Level = DEFAULT_CPU_LEVEL;
		
		rows = DEFAULT_BOARD_SIZE;
		percentOfCurses = (float)(DEFAULT_P_CURSES) / 100;
		percentOfMines = (float)(DEFAULT_P_MINES) / 100;
		cpuLevel = DEFAULT_CPU_LEVEL;
    }
    
	public int describeContents() {
		return 0;
	}
}
