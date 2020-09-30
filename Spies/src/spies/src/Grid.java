package spies.src;

import java.util.ArrayList;

import spies.src.Square;
import spies.src.SquareState;

public class Grid
{
    private ArrayList<ArrayList<Square>> squares;
    private ArrayList<Square> occupiedSquares;
    
    Grid(int n){
        squares = new ArrayList<ArrayList<Square>>();
        for(int i = 0; i<n; i++) {
            ArrayList<Square> line = new ArrayList<Square>();
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
    
    boolean insertSpy(int i, int j) {
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
    
    int findSquareJ(int i , int ite) {
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
        System.out.println(size());
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
