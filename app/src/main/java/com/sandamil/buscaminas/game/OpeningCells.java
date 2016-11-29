package com.sandamil.buscaminas.game;

import android.os.Parcel;
import android.os.Parcelable;

public class OpeningCells implements Parcelable{
	int[] cells;
    CellState[] states;
	int size;
    
    public static final Parcelable.Creator<OpeningCells> CREATOR =
    	new Parcelable.Creator<OpeningCells>() {
            public OpeningCells createFromParcel(Parcel in) {
                return new OpeningCells(in);
            }
 
            public OpeningCells[] newArray(int size) {
                return new OpeningCells[size];
            }
        };
    
    public OpeningCells(int cellSize){
    	size = 0;
    	cells = new int[cellSize];
    	states = new CellState[cellSize];
    }
    
	public OpeningCells(Parcel in){
    	size = in.readInt();
    	in.readIntArray(cells);
    	states = (CellState[]) in.readParcelableArray(null);
    }
    
    public int size(){
    	return size;
    }
    
    public void reset(){
    	size = 0;
    }
    
    public void add(int cell, CellState state){
    	cells[size] = cell;
    	states[size] = state;
    	size++;
    }
    
    public int getCell(int index){
    	return cells[index];
    }
    
    public CellState getState(int index){
    	return states[index];
    }

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(size);
		dest.writeIntArray(cells);
		dest.writeParcelableArray(states, 0);
	}
}
