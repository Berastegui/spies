package spies.src;

import java.util.ArrayList;

import spies.src.Square;
import spies.src.SquareState;

public class Grid2
{
    private ArrayList<ArrayList<Square>> squares;
    private ArrayList<Square> occupiedSquares;
    private final int size;
    
    Grid2(int n){
        size = n;
        squares = new ArrayList<ArrayList<Square>>();
        for(int i = 0; i<2*n; i++) {
            ArrayList<Square> line = new ArrayList<Square>();
            for(int j = 0; j<n; j++) {
                line.add(new Square(i,j));
            }
            squares.add(line);
            
        }
        occupiedSquares = new ArrayList<Square>();
    }
    
    public Grid2 clone() {
        
        Grid2 g2 = new Grid2(this.size());
        for(int i = 0; i<this.size(); i++) {
            for(int j = 0; j<this.size(); j++) {
                g2.get(i, j).setState(this.get(i, j).getState());
            }
        }
        return g2;
        
    }
    
    boolean insertSpy(int j) {
        int i = size();
        int n2 = squares.size();
        ArrayList<Square> line = squares.get(i);
        if(j>=i) {
            return false;
        }
        Square square = line.get(j);
        if(square.getState()!=SquareState.FREE) {
            return false;
        }
        square.setState(SquareState.OCCUPIED);
        for(int j2=0; j2< i;j2++) {
            if(j2!=j)
            line.get(j2).setState(SquareState.DANGER);
        }
        for(int i2=0; i2< n2;i2++) {
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
        while(i3<n2 && j3>=0) {
            squares.get(i3).get(j3).setState(SquareState.DANGER);
            i3++;
            j3--;
        }
        
        i3 = i-1;
        j3=j+1;
        while(j3<i && i3>=0) {
            squares.get(i3).get(j3).setState(SquareState.DANGER);
            i3--;
            j3++;
        }
        
        i3 = i+1;
        j3=j+1;
        while(i3<n2 && j3<i) {
            squares.get(i3).get(j3).setState(SquareState.DANGER);
            i3++;
            j3++;
        }
        
        for(Square occupiedSquare : occupiedSquares) {
            int i4 = occupiedSquare.getI();
            int j4 = occupiedSquare.getJ();
            int di = i-i4;
            int dj = j-j4;
            int pgdc = getPGCD(di, dj);
            di = di / pgdc;
            dj = dj / pgdc;
            int i5 = i + di;
            int j5 = j + dj;
            while(i5<n2 && j5<i && i5>=0 && j5>=0) {
                if(i5!=i && i4!=i5) {
                    squares.get(i5).get(j5).setState(SquareState.DANGER);
                }
                    i5 = i5 + di;
                    j5 = j5 + dj;
            }
            i5 = i4 - di;
            j5 = j4 - dj;
            while(i5<n2 && j5<i && i5>=0 && j5>=0) {
                if(i5!=i && i4!=i5) {
                    squares.get(i5).get(j5).setState(SquareState.DANGER);
                }
                    i5 = i5 - di;
                    j5 = j5 - dj;
            }
        }
        
        occupiedSquares.add(square);
        
        removeLine();
        
        return true;
    }
    
    private void removeLine() {
        squares.remove(0);
        
        for(ArrayList<Square> line : squares) {
            for(Square square : line) {
                square.setI(square.getI()-1);
            }
        }
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
    
    boolean isCompatible(Grid2 grid) {
        if(size()!=grid.size()) {
            return false;
        }
        int nbSpies = grid.getNbSpies();
        for(Square square : grid.occupiedSquares) {
            /*if(square.getI()+nbSpies>=squares.size()) {
                display();
                grid.display();
            }*/
            if(get(square.getI()+nbSpies, square.getJ()).getState()!=SquareState.FREE) {
                return false;
            }
        }
        //nbSpies = getNbSpies();
        for(Square square : occupiedSquares) {
            /*if(square.getI()-nbSpies<=0) {
                display();
                grid.display();
            }*/
            if(grid.get(square.getI()-nbSpies, square.getJ()).getState()!=SquareState.FREE) {
                return false;
            }
        }
        return true;
    }
    
    Grid2 merge(Grid2 grid) {
        if(!isCompatible(grid)) {
            return null;
        }
        Grid2 result = new Grid2(size());

        for(Square square : occupiedSquares) {
            result.insertSpy( square.getJ());
        }
        
        for(Square square : grid.occupiedSquares) {
            result.insertSpy( square.getJ());
        }
        
        return result;
    }
    
    public int getNbSpies() {
        return occupiedSquares.size();
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
        return this.size;
    }
    
    public Square get(int i,int j) {
        return squares.get(i).get(j);
    }
}
