// simple 2D Point class
//
// CS 201 Exam 2

public class Point {

    // public instance variables can be accessed and modified directly
    public int x, y;

    // constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // return x or y coordinate (x if isX==true, y o/w)
    public int coord(boolean isX) {
        if (isX)
            return x;
        else
            return y;
    }
}
