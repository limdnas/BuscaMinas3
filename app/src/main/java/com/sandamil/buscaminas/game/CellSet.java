package com.sandamil.buscaminas.game;

import java.util.HashSet;



public class CellSet{
    HashSet<Integer> cells;
    int min, max;
    HashSet<CellSet> checkedSets;
    boolean full;
    int owner;
    double probability;

    
    public CellSet(){
        full = true;
        checkedSets = new HashSet<CellSet>();
        cells = new HashSet<Integer>();
    }

    public CellSet(CellSet set){
        full = true;
        cells = new HashSet<Integer>(set.cells);
        this.min = set.min;
        this.max = set.max;
        checkedSets = new HashSet<CellSet>();
    }

    public boolean equal(CellSet set){
        if (set.min == min && set.max == max && set.cells.size() == cells.size() ){
        }
        return false;
    }
    
    public void add(int cell){
        cells.add(cell);
    }

    public void remove(int cell){
        cells.remove(cell);
    }

    public boolean greaterAndContains(CellSet set){
        if (cells.containsAll(set.cells) && cells.size() > set.cells.size())
            return true;
        return false;
    }
    
    public CellSet contains(CellSet set){
    	CellSet result = new CellSet(set);
        result.owner = this.owner;
        result.cells.retainAll(this.cells);
        return result;
    }

//    public CellSet contains(CellSet set){
//        CellSet result = new CellSet(this);
//        result.owner = this.owner;
//        result.cells.retainAll(set.cells);
//        //result.min -= this.cells.size() - result.cells.size();
//        /*result.min = set.min - (set.cells.size() - result.cells.size());
//        if (result.min < 0)
//            result.min = 0;
//        if (result.max > result.cells.size())
//            result.max = result.cells.size();*/
//        return result;
//    }

    public CellSet notContains(CellSet set){
        CellSet result = new CellSet(this);
        result.owner = this.owner;
        result.cells.removeAll(set.cells);
        /*result.min -= this.cells.size() - result.cells.size();
        if (result.min < 0)
            result.min = 0;
        if (result.max > result.cells.size())
            result.max = result.cells.size();*/
        return result;
    }
}