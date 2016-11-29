package com.sandamil.buscaminas.game;

import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable{
    public int minesAround;
    public int cursesAround;
    public boolean mined;
    public boolean cursed;
    public boolean opened;
    
    public static final Parcelable.Creator<Cell> CREATOR =
    	new Parcelable.Creator<Cell>() {
            public Cell createFromParcel(Parcel in) {
                return new Cell(in);
            }
 
            public Cell[] newArray(int size) {
                return new Cell[size];
            }
        };
    
    public Cell(int count, boolean mined, boolean cursed, boolean opened){
    	this.minesAround = count;
    	this.mined = mined;
    	this.cursed = cursed;
    	this.opened = opened;
    }

    public Cell(Parcel in){
    	readFromParcel(in);
    }
    
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(minesAround);
		dest.writeInt(cursesAround);
		dest.writeBooleanArray(new boolean[]{mined, cursed, opened});
	}
	
	private void readFromParcel(Parcel in) {
		minesAround = in.readInt();
		cursesAround = in.readInt();
		boolean[] array = new boolean[3];
		in.readBooleanArray(array);
		mined = array[0];
		cursed = array[1];
		opened = array[2];
	}	
}