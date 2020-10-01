package spies.src;

import java.util.*;

public class Solution {
    
    private static int SolutionSize = 11;
    
    private static ArrayList<Grid> oneGrids;
    private static ArrayList<Grid2> oneGrid2s;

    public static void main(String[] args) {
        
        Date start = new Date();
        
       // Grid grid = firstTry();
        
        //Grid grid = secondTry();
        
        Grid2 grid = thirdTry();
        
        /*Grid2 grid = new Grid2(SolutionSize);
        Grid2 grid2 = new Grid2(SolutionSize);
        
        grid.insertSpy(1);
        grid.insertSpy(3);
        grid2.insertSpy(0);
        grid2.insertSpy(2);
        
        Grid2 grid3 = grid.merge(grid2);*/
        
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
    
    private static Grid2 thirdTry() {
        
        oneGrid2s = new ArrayList<Grid2>();
        
        for(int i = 0; i<SolutionSize;i++) {
            Grid2 grid = new Grid2(SolutionSize);
            grid.insertSpy(i);
            oneGrid2s.add(grid);
        }
        
        ArrayList<Grid2> solutions = getGrid2s(SolutionSize);
        
        if(solutions.isEmpty()) {
            return new Grid2(SolutionSize);
        }
        
        return solutions.get(0);
        
    }
    
    private static Grid secondTry() {
        
        oneGrids = new ArrayList<Grid>();
        
        for(int i = 0; i<SolutionSize;i++) {

            for(int j = 0; j<SolutionSize;j++) {
                Grid grid = new Grid(SolutionSize);
                grid.insertSpy(i, j);
                oneGrids.add(grid);
            }
        }
        
        ArrayList<Grid> solutions = getGrids(SolutionSize);
        
        if(solutions.isEmpty()) {
            return new Grid(SolutionSize);
        }
        
        return solutions.get(0);
        
    }
    
    private static ArrayList<Grid2> getGrid2s(int n){
        if(n==1) {
            return oneGrid2s;
        }
        
        if(n==0) {
            return new ArrayList<Grid2>();
        }
        ArrayList<Grid2> grids = getGrid2s(n/2);
        ArrayList<Grid2> result = new ArrayList<Grid2>();
        
        int nbG = grids.size();
        
        Grid2 grid1, grid2;
        
        for(int k1 = 0 ; k1<nbG; k1++) {
            grid1 = grids.get(k1);
            for(int k2 = 0 ; k2<nbG; k2++) {
                grid2 = grids.get(k2);
                Grid2 grid3 = grid1.merge(grid2);
                if(grid3!=null && grid3.getNbSpies()==2*(n/2)) {
                    result.add(grid3);
                }
            }
        }
        if(n%2==0) {
            return result;
        }
        
        ArrayList<Grid2> result2 = new ArrayList<Grid2>();
        
        for(Grid2 grid : result) {
            for(Grid2 grid4 : oneGrid2s) {
                Grid2 grid3 = grid.merge(grid4);
                if(grid3!=null) {
                    result2.add(grid3);
                }
            }
        }
        return result2;
    }
    
    private static ArrayList<Grid> getGrids(int n){
        if(n==1) {
            return oneGrids;
        }
        
        if(n==0) {
            return new ArrayList<Grid>();
        }
        ArrayList<Grid> grids = getGrids(n/2);
        ArrayList<Grid> result = new ArrayList<Grid>();
        
        int nbG = grids.size();
        
        Grid grid1, grid2;
        
        for(int k1 = 0 ; k1<nbG-1; k1++) {
            grid1 = grids.get(k1);
            for(int k2 = k1+1 ; k2<nbG; k2++) {
                grid2 = grids.get(k2);
                Grid grid3 = grid1.merge(grid2);
                if(grid3!=null && grid3.getNbSpies()==2*(n/2)) {
                    result.add(grid3);
                }
            }
        }
        if(n%2==0) {
            return result;
        }
        
        ArrayList<Grid> result2 = new ArrayList<Grid>();
        
        for(Grid grid : result) {
            for(Grid grid4 : oneGrids) {
                Grid grid3 = grid.merge(grid4);
                if(grid3!=null) {
                    result2.add(grid3);
                }
            }
        }
        return result2;
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