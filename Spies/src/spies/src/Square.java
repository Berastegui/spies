package spies.src;

import spies.src.Square;

public class Square
{
    private int i = 0;
    private int j = 0;
    
    private SquareState state;
    
    Square(int i, int j) {
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
