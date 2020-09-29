package spies.src;

import java.util.*;

public class Solution {
    
    private static int SolutionSize = 25;

    public static void main(String[] args) {
        
       /* grid.insertSpy(0, 0);
        grid.display();
        grid.insertSpy(1, 2);
        grid.insertSpy(2, 11);
        grid.insertSpy(3, 9);
        grid.insertSpy(4, 6);
        grid.insertSpy(5, 1);
        grid.insertSpy(6, 10);
        grid.insertSpy(7, 4);
        grid.insertSpy(8, 7);
        grid.insertSpy(9, 12);
        grid.insertSpy(10, 8);
        grid.insertSpy(11, 3);
        grid.insertSpy(12, 5);*/
        
        Date start = new Date();
        
        HashMap<Integer,Integer> iterations = new HashMap<Integer,Integer>();
        for(int i =0; i <SolutionSize;i++) {
            iterations.put(i,0);
        }
        
        int nbSpies = 0;
        int choice = 0;
        
       /* iterations.put(2, 6);
        iterations.put(3, 4);
        iterations.put(4, 2);
        iterations.put(6, 1);*/
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
        
        Date end = new Date();
        
        long timeInMilis = end.getTime()-start.getTime();
        
        grid.display();
        grid.displaySolution();
        System.out.println("number of spies : " +nbSpies);
        System.out.println(nbSpies==SolutionSize? "SUCCESS": "FAILURE");
        System.out.println("time in miliseconds : "+timeInMilis);
        
        
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
    
    private static class Grid {
        
        private ArrayList<ArrayList<Square>> squares;
        private ArrayList<Square> occupiedSquares;
        
        Grid(int n){
            squares = new ArrayList<ArrayList<Square>>();
            for(int i = 0; i<n; i++) {
                ArrayList<Square> line = new ArrayList<Solution.Square>();
                for(int j = 0; j<n; j++) {
                    line.add(new Square(i,j));
                }
                squares.add(line);
                
            }
            occupiedSquares = new ArrayList<Square>();
        }
        
        public Grid clone() {
            
            Grid g2 = new Grid(this.size());
            for(int i = 0; i<this.size(); i++) {
                for(int j = 0; j<this.size(); j++) {
                    g2.get(i, j).setState(this.get(i, j).getState());
                }
            }
            return g2;
            
        }
        
        private boolean insertSpy(int i, int j) {
            int n = squares.size();
            if(i>= n) {
                return false;
            }
            ArrayList<Square> line = squares.get(i);
            if(j>=n) {
                return false;
            }
            Square square = line.get(j);
            if(square.getState()!=SquareState.FREE) {
                return false;
            }
            square.setState(SquareState.OCCUPIED);
            for(int j2=0; j2< n;j2++) {
                if(j2!=j)
                line.get(j2).setState(SquareState.DANGER);
            }
            for(int i2=0; i2< n;i2++) {
                if(i2!=i) {
                    ArrayList<Square> line2 = squares.get(i2);
                    line2.get(j).setState(SquareState.DANGER);
                }
            }
            int i3 = i-1, j3=j-1;
            while(i3>=0 && j3>=0) {
                squares.get(i3).get(j3).setState(SquareState.DANGER);
                i3--;
                j3--;
            }
            
            i3 = i+1;
            j3=j-1;
            while(i3<n && j3>=0) {
                squares.get(i3).get(j3).setState(SquareState.DANGER);
                i3++;
                j3--;
            }
            
            i3 = i-1;
            j3=j+1;
            while(j3<n && i3>=0) {
                squares.get(i3).get(j3).setState(SquareState.DANGER);
                i3--;
                j3++;
            }
            
            i3 = i+1;
            j3=j+1;
            while(i3<n && j3<n) {
                squares.get(i3).get(j3).setState(SquareState.DANGER);
                i3++;
                j3++;
            }
            
            for(Square occupiedSquare : occupiedSquares) {
                int i4 = occupiedSquare.getI();
                int j4 = occupiedSquare.getJ();
                int di = i-i4;// à corriger pgcd
                int dj = j-j4;
                int pgdc = getPGCD(di, dj);
                di = di / pgdc;
                dj = dj / pgdc;
                int i5 = i + di;
                int j5 = j + dj;
                while(i5<n && j5<n && i5>=0 && j5>=0) {
                    if(i5!=i && i4!=i5) {
                        squares.get(i5).get(j5).setState(SquareState.DANGER);
                    }
                        i5 = i5 + di;
                        j5 = j5 + dj;
                }
                i5 = i4 - di;
                j5 = j4 - dj;
                while(i5<n && j5<n && i5>=0 && j5>=0) {
                    if(i5!=i && i4!=i5) {
                        squares.get(i5).get(j5).setState(SquareState.DANGER);
                    }
                        i5 = i5 - di;
                        j5 = j5 - dj;
                }
            }
            
            occupiedSquares.add(square);
            
            return true;
        }
        
        private int getPGCD(int i, int j) {
            if(i==0)
                return j;
            if(j==0)
                return i;
            int g = Math.max(i, j);
            int p = Math.min(i, j);
            int r = g % p;
            while (r!=0) {
                g=p;
                p=r;
                r = g % p;
            }
            return p;
        }
        
        private int findSquareJ(int i , int ite) {
            if(i>=size()) {
                return -1;
            }
            ArrayList<Square> line = squares.get(i);
            int nb = 0;
            for(int j = 0; j<size(); j++) {
                if(line.get(j).getState().equals(SquareState.FREE)) {
                    nb++;
                }
                if(nb>ite) {
                    return j;
                }
            }
            return -1;
            
        }
        /*
        private ArrayList<Integer> findSquaresJ(int i) {
            ArrayList<Integer> js = new ArrayList<Integer>();
            if(i>=size()) {
                return js;
            }
            ArrayList<Square> line = squares.get(i);
            for(int j = 0; j<size(); j++) {
                if(line.get(j).getState().equals(SquareState.FREE)) {
                    js.add(j);
                }
            }
            return js;
            
        }*/
        /*
        private boolean insertSpy(int i) {
            boolean inserted = false;
            for(int j =0 ;j<squares.size(); j++) {
                inserted = insertSpy(i, j);
                if (inserted) {
                    return inserted;
                }
                
            }
            return false;
        }*/
        
        public int getNbSpies() {
            return occupiedSquares.size();
        }
        
        public int getNbFreeSquare(int i) {
            int n = squares.size();
            if(i>= n) {
                return 0;
            }
            ArrayList<Square> line = squares.get(i);
            return (int)line.stream().filter(s -> s.getState().equals(SquareState.FREE)).count();
        }
        
        public void display()
        {
            for(ArrayList<Square>line : squares) {
                for(Square square : line) {
                    square.display();
                }
                System.out.println();
            }
            System.out.println();
        }
        
        public void displaySolution()
        {
            for(ArrayList<Square>line : squares) {
                for(Square square : line) {
                    if(square.getState()==SquareState.OCCUPIED) {
                        System.out.print(square.getJ()+1+" ");
                    }
                }
            }
            System.out.println();
        }
        
        public int size() {
            return this.squares.size();
        }
        
        public Square get(int i,int j) {
            return squares.get(i).get(j);
        }
    }
    
    private static class Square {
        private int i = 0;
        private int j = 0;
        
        private SquareState state;
        
        private Square(int i, int j) {
            this.i = i;
            this.j = j;
            this.state = SquareState.FREE;
        }
        
        public Square clone() {
            Square s2 = new Square(this.i, this.j);
            s2.setState(this.state);
            return s2;
        }
        
        public int getI()
        {
            return i;
        }
       /* public void setI(int i)
        {
            this.i = i;
        }*/
        public int getJ()
        {
            return j;
        }
        /*public void setJ(int j)
        {
            this.j = j;
        }*/
        public SquareState getState()
        {
            return state;
        }
        public void setState(SquareState state)
        {
            this.state = state;
        }
        
        public void display() {
            switch(this.state)
            {
                case FREE:
                    System.out.print("0 ");
                    break;
                case OCCUPIED:
                    System.out.print("1 ");
                    break;
                case DANGER:
                    System.out.print("X ");
                    break;

                default:
                    break;
            }
        }
        
        
    }
    
    private enum SquareState {
        FREE,
        OCCUPIED,
        DANGER;
        
    }
}