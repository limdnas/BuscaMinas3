package com.sandamil.buscaminas.game;

public class Neighborhood{
    final static int NEIGHBORS = 8;

    int n;
    int height, width;

    public Neighborhood(int width, int height){
        this.n = height * width;
        this.height = height;
        this.width = width;
    }

    public class Iterator{
    	int pos;
    	int cell;
    	Neighborhood n;
    	Integer neighbor;
    	
    	Iterator(Neighborhood n, int cell){
    		this.n = n;
    		this.cell = cell;
    		pos = 0;
    		neighbor = null;
    	}
    	
    	public boolean hasNext(){
    		do{
    			if (pos == NEIGHBORS){
    				return false;
    			}
    			neighbor = n.getNeighbor(cell, ++pos);
    		}while(neighbor == null);
    		return true;
    	}
    	
    	public int get(){
    		return neighbor;
    	}
    }
    
    public Iterator iterator(int cell){
    	return new Iterator(this, cell);
    }
    
    public Integer getNeighbor(int cell, int pos){
          switch (pos){
               case 1:{  // izquierda
                    if (cell % width > 0)
                         return cell - 1;
                    else
                         return null;
               }
               case 2:{  // derecha
                    if (cell % width < width - 1)
                         return cell + 1;
                    else
                         return null;
               }
               case 3:{  // arriba
                    if (cell >= width)
                         return cell - width;
                    else
                         return null;
               }
               case 4:{  // abajo
                    if (cell + width  < n)
                         return cell + width;
                    else
                         return null;
               }
               case 5:{  // izquierda arriba
                    if (cell % width > 0 && cell >= width)
                         return cell - width - 1;
                    else
                         return null;
               }
               case 6:{  // izquierda abajo
                    if (cell % width > 0 && (cell + width  < n))
                         return cell + width - 1;
                    else
                         return null;
               }
               case 7:{  // derecha arriba
                    if ((cell % width < width - 1) && cell >= width)
                         return cell - width + 1;
                    else
                         return null;
               }
               case 8:{  // derecha abajo
                    if ((cell % width < width - 1) && (cell + width  < n))
                         return cell + width + 1;
                    else
                         return null;
               }
          }
          return null;
     }
}
