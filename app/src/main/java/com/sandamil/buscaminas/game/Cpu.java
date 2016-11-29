package com.sandamil.buscaminas.game;

import java.util.LinkedList;

import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.activities.GameActivity;

public class Cpu extends Player{
    private int level;
    private CellCpu[] cells;
    private CellSet globalSet;
    private Neighborhood neighbor;
    
    public Cpu(GameActivity activity, String name, int level){
    	super(activity, name);
    	this.level = level;
    	if (level < 1){
    		this.level = 1;
    	}
    	if (level > 4){
    		this.level = 4;
    	}
    }

    public int getLevel(){
    	return level;
    }
    
    public int cpuMoves() {
    	neighbor = new Neighborhood(activity.game.getColumns(), activity.game.getRows());
        cells = new CellCpu[activity.game.getColumns() * activity.game.getRows()];
        for (int pos = 0; pos < activity.game.getColumns() * activity.game.getRows(); pos++) {
            cells[pos] = new CellCpu(pos);
            cells[pos].opened = activity.game.isCellOpened(pos);
            cells[pos].mine = activity.game.isCellMined(pos);
            cells[pos].around = activity.game.getMinesAround(pos);
        }
        int pos = 0;
        switch (level) {
            case GameConfig.CPU_LEVEL1: {
                pos = level1();
                break;
            }
            case GameConfig.CPU_LEVEL2: {
                pos = level2();
                break;
            }
            case GameConfig.CPU_LEVEL3: {
                pos = level3();
                break;
            }
            case GameConfig.CPU_LEVEL4: {
                pos = level4();
                break;
            }
        }
        return pos;
    }

    private int randomSearch(){
    	int pos = 0;
    	do {
            pos = new Double(Math.random() * activity.game.getColumns() * activity.game.getRows()).intValue();
        } while (cells[pos].opened);
    	return  pos;
    }
    
    public static int factorial(int x) {
        int i, f;
        i = 0;
        f = 1;
        while (i != x) {
            i++;
            f *= i;
        }
        return f;
    }

    public static int binomial(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    // 1� paso: crear el conjunto global y asignarlo a todas las celdas sin abrir
    private void step1_AssignGlobalSet() {
        globalSet = new CellSet();
        globalSet.owner = -1;
        globalSet.max = activity.game.getMines() - activity.game.getPlayer(0).score - activity.game.getPlayer(1).score;
        globalSet.min = globalSet.max;
        for (int i = 0; i < cells.length; i++) {
            cells[i].listSets.clear();
            if (!cells[i].opened) {
                globalSet.add(i);
                cells[i].listSets.add(globalSet);
            }
        }
    }

    // 2� paso: eliminar el conjunto global en aquellas celdas contiguas a una numero completado
    private void step2_ReduceGlobalSet() {
        for (int i = 0; i < cells.length; i++) {
            if (complete(i)) {
                for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();) {
                    if (!cells[it.get()].opened) {
                        cells[it.get()].listSets.clear();
                        globalSet.remove(it.get());
                    }
                }
            }
        }
    }

    // 3� paso: crear el grupo para cada numero no completado
    private void step3_CreateLocalGroup() {
        for (int i = 0; i < cells.length; i++) {
            if (incomplete(i)) {
                //CellSet set = new CellSet(cells[i].count - minesAround(i));
                CellSet set = new CellSet();
                set.owner = i;
                set.min = cells[i].around - minesAround(i);
                set.max = set.min;
                cells[i].listSets.add(set);
                for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();) {
                    if (!cells[it.get()].opened && !cells[it.get()].listSets.isEmpty()) {
                        set.add(it.get());
                        cells[it.get()].listSets.add(set);
                    }
                }
            }
        }
    }

    
 // 4� paso: comprobar si hay alguna 100% seguro
    private int step4_Check100p() {
        for (int i = 0; i < cells.length; i++) {
            if (incomplete(i)) {
                for (CellSet set: cells[i].listSets){
                    if (set.min == set.cells.size() && set.cells.size() > 0) {
                        return set.cells.iterator().next();
                    }
                }
            }
        }
        return -1;
    }

    // 5� paso: comprobar si algun grupo sobrepasa a otro
    public boolean step5_DivideSet() {
        for (int i = 0; i < cells.length; i++) {
            if (incomplete(i)) {
                for (CellSet seti: cells[i].listSets){ 
                    for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();) {
                        if (incomplete(it.get())) {
                        	for (CellSet setj: cells[it.get()].listSets){
                                if (seti.checkedSets.contains(setj)) {
                                    continue;
                                }
                                seti.checkedSets.add(setj);  // IMPORTANTE
                                //System.out.println(setj.toString());
                                CellSet common = seti.contains(setj);
                                //if (common.cells.size() > 0 && common.cells.size() < seti.cells.size() && common.max > setj.max){
                                if (common.cells.size() > 0 && common.cells.size() < seti.cells.size()) {
                                    int iMin = seti.min - (seti.cells.size() - common.cells.size());
                                    if (iMin < 0) {
                                        iMin = 0;
                                    }
                                    int jMin = setj.min - (setj.cells.size() - common.cells.size());
                                    if (jMin < 0) {
                                        jMin = 0;
                                    }
                                    if (iMin > jMin) {
                                        common.min = iMin;
                                    } else {
                                        common.min = jMin;
                                    }
                                    int iMax = seti.max;
                                    if (iMax > common.cells.size()) {
                                        iMax = common.cells.size();
                                    }
                                    int jMax = setj.max;
                                    if (jMax > common.cells.size()) {
                                        jMax = common.cells.size();
                                    }
                                    if (iMax < jMax) {
                                        common.max = iMax;
                                    } else {
                                        common.max = jMax;
                                    }

                                    if (common.max == iMax && common.min == iMin) {
                                        continue;
                                    }

                                    boolean recursive = false;
                                    for (CellSet set: cells[i].listSets){
                                    	set = new CellSet(set);
                                        if (!set.cells.retainAll(common.cells) && set.min == common.min && set.max == common.max) {
                                            recursive = true;
                                            break;
                                        }
                                    }
                                    if (recursive) {
                                        continue;
                                    }

                                    CellSet rest = seti.notContains(setj);
                                    rest.min = seti.min - common.max;
                                    if (rest.min < 0) {
                                        rest.min = 0;
                                    }
                                    rest.max = seti.max - common.min;
                                    if (rest.max > rest.cells.size()) {
                                        rest.max = rest.cells.size();
                                    }

                                    cells[i].listSets.add(common);
                                    if (rest.max > 0) {
                                        cells[i].listSets.add(rest);
                                    }
                                    for (int cell: common.cells){
                                        cells[cell].listSets.add(common);
                                    }

                                    for (int cell: rest.cells){
                                        if (rest.max > 0) {
                                            cells[cell].listSets.add(rest);
                                        } else {
                                        	for (CellSet set: cells[cell].listSets){
                                        		set.cells.remove(cell);
                                            }
                                            cells[cell].listSets.clear();
                                        }
                                    }
                                    if (common.min + rest.min == seti.min) {
                                        seti.full = false;
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // 6� paso: eliminar aquellas celdas que puedan provocar un hueco
    private void step6_MarkGapCells() {
        forLabel:
    	for (int i = 0; i < cells.length; i++) {
        	if (!cells[i].opened/* && !cells[i].listSets.isEmpty()*/) {
                CellSet neighborSet = new CellSet();
                cells[i].gap = true;
                for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();) {
                    if (cells[it.get()].opened && cells[it.get()].mine) {
                        cells[i].gap = false;
                        break;
                    }
                    if (!cells[it.get()].opened && !cells[it.get()].listSets.isEmpty()) {
                        neighborSet.add(it.get());
                    }
                }
                if (!cells[i].gap) {
                    continue;
                }
                for (int cell: neighborSet.cells){
            		for (CellSet set: cells[cell].listSets){
                        CellSet common = set.contains(neighborSet);
                        common.min = set.min - (set.cells.size() - common.cells.size());
                        if (set.cells.contains(i)) {
                            common.min++;
                        }
                        if (common.min > 0) {
                            cells[i].gap = false;
                            continue forLabel;
                        }
                    }
                }
            }
        }
    }

    // 7� paso: seleccionar un solo conjunto para cada (celda, incompleta)
    private void step7_StayOnly1Set() {
        for (int i = 0; i < cells.length; i++) {
            if (incomplete(i)) {
            	for (Neighborhood.Iterator itn = neighbor.iterator(i); itn.hasNext();) {
                    if (!cells[itn.get()].opened && !cells[itn.get()].listSets.isEmpty()) {
                        CellSet stay = cells[itn.get()].listSets.iterator().next();
                        for (CellSet temp: cells[itn.get()].listSets){
                            if (cells[i].listSets.contains(temp) && temp.cells.size() <= stay.cells.size()) {
                                if (temp.cells.size() == stay.cells.size()) {
                                    if ((temp.max - temp.min) < (stay.max - stay.min)) {
                                        stay = temp;
                                    }
                                } else {
                                    stay = temp;
                                }
                            }
                        }
                        if (stay.min != stay.max) {
                            //System.out.println("####INCOMPLETO " + control.coordinate(i) + " celda " + Board.coordinate(j) + ": size=" + stay.cells.size() + ", min=" + stay.min + ", max=" + stay.max);
                        }
                        java.util.Iterator<CellSet>  it = cells[itn.get()].listSets.iterator();
                        while (it.hasNext()) {
                            CellSet temp = it.next();
                            if (cells[i].listSets.contains(temp) && temp != stay) {
                                /*if (temp.cells.size() == stay.cells.size()){ // kitar este IF
                                System.out.println("Uno igual del mismo tama�o");
                                }*/
                                it.remove();

                            }
                        }
                    }
                }
            }
        }
    }

    // 8� paso: crea el conjunto de candidatos   YA NO PETA
    private LinkedList<Integer> step8_CreateCandidates() {
        LinkedList<Integer> candidates = new LinkedList<Integer>();
        double percent = -1;
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].opened && !cells[i].listSets.isEmpty()) {
            	for (CellSet temp: cells[i].listSets){
                	if (temp.probability != 0 || cells[i].gap){
                		continue;
                	}
//                	System.out.println("PROBABILIDAD DE TAM: " + temp.cells.size());
//                	System.out.println("celda: " + i + ", gap: " + cells[i].gap);
                    if (temp.min == temp.max) {
                        temp.probability = (double) temp.min / (double) temp.cells.size();
//                        System.out.println("normal: " + temp.probability);
                    } else {
                        int posibilities = 0;
                        for (int m = temp.min; m <= temp.max; m++) {
                            posibilities += binomial(temp.cells.size(), m);
                        }
//                        System.out.println("posi: " + i + ", min: " + temp.min + ", max: " + temp.max);
//                        System.out.println("size: " + temp.cells.size() + ", prob: " + temp.probability + ", id: " + temp);
                        for (int m = temp.min; m <= temp.max; m++) {
                            temp.probability += ((double) binomial(temp.cells.size(), m) / (double) posibilities) * ((double) m / (double) temp.cells.size());
                        }
//                        System.out.println("binomial: " + temp.probability);
                    }
                    if (temp.probability >= percent && !cells[i].gap) {
                        if (temp.probability > percent) {
                            percent = temp.probability;
                            candidates.clear();
                        }
//                        System.out.println("CANDIDATO: " + i);
                        for (int cell: temp.cells){
                        	if (!cells[cell].gap){
                        		candidates.add(cell);
                        		cells[cell].probability = temp.probability;
                        	}
                        }
                    }
                }
            }
        }
        return candidates;
    }
    
 // 8� paso: crea el conjunto de candidatos sin tener en cuenta los espacios
    private LinkedList<Integer> step8_CreateCandidatesWithGap() {
        LinkedList<Integer> candidates = new LinkedList<Integer>();
        double percent = -1;
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].opened && !cells[i].listSets.isEmpty()) {
            	for (CellSet temp: cells[i].listSets){
                	if (temp.probability != 0){
                		continue;
                	}
//                	System.out.println("PROBABILIDAD DE TAM: " + temp.cells.size());
//                	System.out.println("celda: " + i + ", gap: " + cells[i].gap);
                    if (temp.min == temp.max) {
                        temp.probability = (double) temp.min / (double) temp.cells.size();
//                        System.out.println("normal: " + temp.probability);
                    } else {
                        int posibilities = 0;
                        for (int m = temp.min; m <= temp.max; m++) {
                            posibilities += binomial(temp.cells.size(), m);
                        }
//                        System.out.println("posi: " + i + ", min: " + temp.min + ", max: " + temp.max);
//                        System.out.println("size: " + temp.cells.size() + ", prob: " + temp.probability + ", id: " + temp);
                        for (int m = temp.min; m <= temp.max; m++) {
                            temp.probability += ((double) binomial(temp.cells.size(), m) / (double) posibilities) * ((double) m / (double) temp.cells.size());
                        }
//                        System.out.println("binomial: " + temp.probability);
                    }
                    if (temp.probability >= percent) {
                        if (temp.probability > percent) {
                            percent = temp.probability;
                            candidates.clear();
                        }
//                        System.out.println("CANDIDATO: " + i);
                        for (int cell: temp.cells){
                        	candidates.add(cell);
                        	cells[cell].probability = temp.probability;
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private int level1(){
    	return randomSearch();
    }
    
    public int level2() {
        step1_AssignGlobalSet();
        step2_ReduceGlobalSet();
        step3_CreateLocalGroup();
        do {
            int check = step4_Check100p();
            if (check != -1) {
            	System.out.println("100% SEGURO");
                return check;
            }
        } while (step5_DivideSet());
        step7_StayOnly1Set();
        LinkedList<Integer> candidates = step8_CreateCandidatesWithGap();
        if (candidates.size() > 0) {
            System.out.println("" + cells[candidates.getFirst()].probability * 100.0 + "% CON GAP");
            return candidates.getFirst();
        }
        System.out.println("RANDOM");
        return randomSearch();
    }

    public int level3(){
    	return level2();
    }
    
    private Integer chooseNoGap(){
    	int free = -1;
        int cellMostFree = -1;
        int cellZeroFree = -1;
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].opened && cells[i].listSets.isEmpty() && !cells[i].gap) {
            	int f = 0;
            	for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();){
            		if (!cells[it.get()].opened && !cells[it.get()].listSets.isEmpty()) {
            			f++;
            		}
            	}
            	if (f == 0){
            		cellZeroFree = i;
            	}
            	else if (f > free){
            		free = f;
            		cellMostFree = i;
            	}
            }	
        }
        if (cellMostFree != -1 && free > 1){
        	System.out.println("NO GAP, > 1");
        	return cellMostFree;
        }
        if (cellZeroFree != -1){
        	System.out.println("NO GAP, = 0");
        	return cellZeroFree;
        }
        if (cellMostFree != -1 && free == 1){
        	System.out.println("NO GAP, = 1");
        	return cellMostFree;
        }
        return null;
    }
    
    private int chooseBestCandidate(LinkedList<Integer> list){
    	int free = -1;
        int cellMostFree = -1;
        int cellZeroFree = -1;
        for (int i: list){
//        	System.out.println("CANDIDATO: " + i);
        	int f = 0;
            for (Neighborhood.Iterator it = neighbor.iterator(i); it.hasNext();){
            	if (!cells[it.get()].opened && !cells[it.get()].listSets.isEmpty()) {
            		f++;
            	}
            }
            if (f == 0){
            	cellZeroFree = i;
            }
            else if (f > free){
           		free = f;
            	cellMostFree = i;
            }
        }
        if (cellMostFree != -1 && free > 1){
        	System.out.println("CHOICE: NO GAP, > 1");
        	return cellMostFree;
        }
        if (cellZeroFree != -1){
        	System.out.println("CHOICE: NO GAP, = 0");
        	return cellZeroFree;
        }
        if (cellMostFree != -1 && free == 1){
        	System.out.println("CHOICE: NO GAP, = 1");
        	return cellMostFree;
        }
    	System.out.println("NUNCA DEBERIA DE PASAR");
        return 0;	
    }
    
    public int level4() {
        step1_AssignGlobalSet();
        step2_ReduceGlobalSet();
        step3_CreateLocalGroup();
        do {
            int check = step4_Check100p();
            if (check != -1) {
            	System.out.println("100% SEGURO");
                return check;
            }
        } while (step5_DivideSet());
        step6_MarkGapCells();
        step7_StayOnly1Set();
        LinkedList<Integer> candidates = step8_CreateCandidates();
        if (candidates.size() > 0) {
        	int choice = chooseBestCandidate(candidates);
            System.out.println("" + cells[choice].probability * 100.0 + "% SEGURO");
            return choice;
        }
        
        Integer choice = chooseNoGap();
        if (choice != null){
        	return choice;
        }
        
        candidates = step8_CreateCandidatesWithGap();
        if (candidates.size() > 0) {
        	choice = chooseBestCandidate(candidates);
            System.out.println("" + cells[choice].probability * 100.0 + "% CON GAP");
            return choice;
        }
        System.out.println("RANDOM");
        return randomSearch();
    }
    
    public int minesAround(int p) {
        int count = 0;
        for (Neighborhood.Iterator it = neighbor.iterator(p); it.hasNext();) {
            if (cells[it.get()].mine) {
                count++;
            }
        }
        return count;
    }

    public boolean incomplete(int pos) {
        if (cells[pos].opened && !cells[pos].mine && cells[pos].around > 0 && cells[pos].around > minesAround(pos)) {
            return true;
        }
        return false;
    }

    public boolean complete(int pos) {
        if (cells[pos].opened && !cells[pos].mine && cells[pos].around > 0 && cells[pos].around == minesAround(pos)) {
            return true;
        }
        return false;
    }

    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@Override
	public boolean openCells(OpeningCells opening, int playerId) {
		if (!activity.ready){
			return false;
		}
		Times.Wait(this, 500);
		if (! activity.board.isCompletelyVisible(opening.getCell(0))){
			if (!activity.ready){
				return false;
			}
			activity.board.lateralMove(opening.getCell(0));
			Times.Wait(this, 300);
		}
		return super.openCells(opening, playerId);
	}

	@Override
	public void turn() {
		if (activity.ready){
			activity.game.gamePost(new Runnable() {
				public void run() {
					activity.board.disableInteraction();
					activity.game.cellSelected(cpuMoves());
				}
			});
		}
	}
}