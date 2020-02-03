// K-d Tree with 2 dimensions
//
// CS 201 exam 2


public class KdTree {

    // instance variables
    private Point point;    // the 2D point stored
    private boolean isX;    // whether to split by x or y coordinate
    private KdTree left;    // the smaller subtree 
    private KdTree right;   // the larger subtree

    // constructor
    public KdTree(Point point, boolean isX, KdTree left, KdTree right) {
        this.point = point;
        this.isX = isX;
        this.left = left;
        this.right = right;
    }

    // leaf constructor
    public KdTree(Point point, boolean isX) {
        this(point, isX, null, null);
    }

    // accessor methods (getters):

    public Point point() {
        return point;
    }

    public boolean isX() {
        return isX;
    }

    public KdTree left() {
        return left;
    }

    public KdTree right() {
        return right;
    }
    
    // add a new point to this K-d tree (as a new leaf)
    public void add(Point newpoint) {
        int val = point.coord(isX); // retrieve x or y coordinate
        int newval = newpoint.coord(isX);
        boolean newisX = ! isX; // negate isX for next level
        if (newval <= val) { // add on left
            if (left == null) {
                left = new KdTree(newpoint, newisX);
            } else {
                left.add(newpoint);
            }
        } else { // add on right
            if (right == null) {
                right = new KdTree(newpoint, newisX);
            } else {
                right.add(newpoint);
            }
        }
    }
}
