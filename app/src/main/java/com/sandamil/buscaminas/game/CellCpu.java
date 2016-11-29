package com.sandamil.buscaminas.game;

import java.util.HashSet;


class CellCpu{
    int position;
    int around;
    boolean mine;
    boolean opened;
    boolean gap;
    HashSet<CellSet> listSets;
    double probability;


    public CellCpu(int position){
        listSets = new HashSet<CellSet>();
        around = 0;
        opened = false;
        this.position = position;
    }
}