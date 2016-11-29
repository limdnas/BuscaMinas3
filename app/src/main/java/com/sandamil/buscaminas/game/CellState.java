package com.sandamil.buscaminas.game;

import android.os.Parcel;
import android.os.Parcelable;

public class CellState implements Parcelable{
	public CellEnum state;
	public int minesAround;
	public int cursesAround;
	
	public static final Parcelable.Creator<CellState> CREATOR =
    	new Parcelable.Creator<CellState>() {
            public CellState createFromParcel(Parcel in) {
                return new CellState(in);
            }
 
            public CellState[] newArray(int size) {
                return new CellState[size];
            }
        };
	
	public CellState(CellEnum state, int minesAround, int cursesAround){
		this.state = state;
		this.minesAround = minesAround;
		this.cursesAround = cursesAround;
	}
	
	public CellState(Parcel in){
    	readFromParcel(in);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(minesAround);
		dest.writeInt(cursesAround);
		if (state != null){
			dest.writeInt(state.getIndex());
		}
		else{
			dest.writeInt(0);
		}
	}
	
	private void readFromParcel(Parcel in) {
		minesAround = in.readInt();
		cursesAround = in.readInt();
		state = CellEnum.get(in.readInt());
	}	
}