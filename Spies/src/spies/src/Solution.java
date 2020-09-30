package spies.src;

import java.util.*;

public class Solution {
    
    private static int SolutionSize = 25;

    public static void main(String[] args) {
        
        Date start = new Date();
        
        Grid grid = firstTry();
        
        Date end = new Date();
        
        long timeInMilis = end.getTime()-start.getTime();
        
        grid.display();
        grid.displaySolution();
        int nbSpies = grid.getNbSpies();
        System.out.println("number of spies : " +nbSpies);
        System.out.println(nbSpies==SolutionSize? "SUCCESS": "FAILURE");
        System.out.println("time in miliseconds : "+timeInMilis);
        
        
    }
    
    private static Grid firstTry() {
        HashMap<Integer,Integer> iterations = new HashMap<Integer,Integer>();
        for(int i =0; i <SolutionSize;i++) {
            iterations.put(i,0);
        }
        
        int nbSpies = 0;
        int choice = 0;
        
        Grid grid = null;
        int ite = 0;
        while(choice>=0 && nbSpies<SolutionSize) {

            grid = new Grid(SolutionSize);
            choice = fillGrid(grid,iterations);
            nbSpies = grid.getNbSpies();
            if(choice>=0) {
                ite = iterations.get(choice);
                iterations.put(choice, ite+1);
                for(int i = choice + 1; i<SolutionSize; i++) {
                    iterations.put(i, 0);
                }
            }
        }
        return grid;
    }
    
    private static int fillGrid(Grid grid,HashMap<Integer,Integer> iterations) {
        int nb = 0;
        int choice = -1;
        int it = 0;
        int j = 0 ;
        for(int i=0;i<grid.size();i++) {
            nb = grid.getNbFreeSquare(i);
            if(nb==0) {
                break;
            }
            it = iterations.get(i);
            if ((nb-it)>1) {
                choice = i;
            }
            j = grid.findSquareJ( i , it);
            if(j==-1) {
                break;
            }
            grid.insertSpy(i,j);
        }
        return choice;
    }
}