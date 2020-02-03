// Operations on K-d Tree with 2 dimensions
//
// CS 201 Exam 2
//
// Name: Caroline Hughes

import java.awt.*;
import java.util.Vector;

public class KdTreeOps {

    // problem 1 --------------------------------------------------

    // return number of nodes in t that have type isX
    public static int countNodes(KdTree t, boolean isX) {
        if (t == null) {
            return 0;
        }
        else if (t.isX() == isX)
        {
            return  1 + countNodes(t.left(), isX) + countNodes(t.right(), isX);
        }
        else
        {
        	return countNodes(t.left(), isX) + countNodes(t.right(), isX);
        }
    }

    // problem 2 --------------------------------------------------

    // return whether left and right subtrees of t have mirrored shape
    public static boolean isSymmetric(KdTree t) 
    {
    	if(t != null)
    		return symHelper(t.left(), t.right());
    	else
    		return true;
    }

    //helper method for isSymmetric
    public static boolean symHelper(KdTree t1, KdTree t2) 
    {
	  	if(t1 == null && t2 == null)
	        return true;
	    else if (t1 != null && t2 != null)
	        return symHelper(t1.left(), t2.right()) && symHelper(t1.right(), t2.left());
	    else
	        return false;
    }


    // problem 3 --------------------------------------------------

    // return width of t:
    //   0 if t is null
    //   2 if t is a leaf
    //   if one child is null, 1 + width of the other child
    //   otherwise, the sum of the widths of t's subtrees
    public static int width(KdTree t) 
    {
    	 if(t == null)
         	return 0;
         else if (t.left() == null && t.right() == null)
         	return 2;
         else if (t.left() != null && t.right() == null)
         	return 1 + width(t.left());
         else if (t.left() == null && t.right() != null)
           	return 1 + width(t.right());
         else
         	return width(t.left()) + width(t.right());
    }
    
    // return width of left "half" of t:
    //   0 if t is null
    //   otherwise, the width of t's left subtree (but at least 1)
    public static int leftWidth(KdTree t) 
    {
    	if (t == null)
    		return 0;
    	else if (t.left() == null)
        	return 1;
        else
        	return width(t.left());
     }

    // returns width of right "half" of t:
    //   0 if t is null
    //   otherwise, the width of t's right subtree (but at least 1)
    public static int rightWidth(KdTree t) 
    {
        if (t == null)
        	return 0;
        else if (t.right() == null)
           	return 1;
        else
           	return width(t.right());
     }
    

    // problem 4 --------------------------------------------------
    
    // draw the tree in rectangle x0...x1, y0...y1
    public static void draw(Graphics g, KdTree t, int level, 
            int x0, int y0, int x1, int y1) {

        // set color and line width according to level and node type (x or y)
        TreeCanvas.setNodeColor(g, level, t.isX());
        TreeCanvas.setLineWidth(g, 4 - level/2); // progressively thinner

        Point p = t.point();
        
        // draw the dividing lines        
        // FIX THE CODE BELOW for problem 4
        // use p.x, p.y, and/or parameters x0, y0, x1, y1 in the lines marked
       
        
        // vertical line
        if (t.isX()) 
        {
            g.drawLine(p.x, y0, p.x, y1);  // CHANGE THIS
            
            if (t.left() != null) {
               draw(g, t.left(),  level+1, x0, y0, p.x, y1);  // CHANGE THIS
            }
            
            if (t.right() != null) {
               draw(g, t.right(), level+1, p.x, y0, x1, y1);  // CHANGE THIS 
            }
        }
        
        // horizontal line
        else 
        { 
            g.drawLine(x0,  p.y,  x1,  p.y);  // CHANGE THIS
            
            if (t.left() != null) {
                draw(g, t.left(),  level+1, x0, y0, x1, p.y);  // CHANGE THIS
            }
                
            if (t.right() != null) {
                draw(g, t.right(), level+1, x0, p.y, x1, y1);  // CHANGE THIS
            }
            
        }
        
        // draw a black dot for the node
        g.setColor(Color.black);
        g.fillOval(p.x-4, p.y-4, 9, 9);
    }



    // problem 5 --------------------------------------------------

    // rebuild the tree so it is balanced (return as new tree)
    public static KdTree rebuild(KdTree t) {

        // first, collect all the points in the tree into a vector
        Vector<Point> points = new Vector<Point>();
        collectPoints(t, points);
        
        // then, build a new K-d tree recursively
        return buildKdTree(points, true, 0, points.size());
    }

    // add all the points in t to the vector using pre-order traversal
    private static void collectPoints(KdTree t, Vector<Point> points) {
        
    	if(t == null)
    		return;
    	else 
    	{
    		points.add(t.point());
    		collectPoints(t.left(), points);
    		collectPoints(t.right(), points);
    	}
    }
     
    // FILL IN for problem 5
    // build the tree recursively from the portion of the vector
    // with indices start (inclusive) through end (exclusive)
    private static KdTree buildKdTree(Vector<Point> points, boolean isX, int start, int end) 
    {        
    	if(start >= end)
    		return null;
    	else 
    	{
    		// 1. sort points[start:end] using helper function below
    		sortPoints(points, isX, start, end);
    		// 2. compute mid index (average of start, end)
    		int mid = (start+end)/2;
    		// 3. get point at mid index for node value
            // 4. call function recursively on first and second half
            // 5. return new KdTree node with mid point and two recursive results
    		return new KdTree(points.get(mid), isX, buildKdTree(points, !isX, start, mid),
    				buildKdTree(points, !isX, mid+1, end));
    	}
    }
    
    // sort vector from index start (inclusive) through end (exclusive)
    // according to coordinate isX using selection sort
    private static void sortPoints(Vector<Point> points, boolean isX, int start, int end)
    {
        for (int lastUnsorted = end-1; lastUnsorted > start; lastUnsorted--) {
            int maxIndex = start;
            for (int index = start+1; index <= lastUnsorted; index++) {
                if (points.get(index).coord(isX) >
                    points.get(maxIndex).coord(isX))
                {
                    maxIndex = index;
                }
            }
            // swap max value with last unsorted
            Point tmp = points.get(maxIndex);
            points.set(maxIndex, points.get(lastUnsorted));
            points.set(lastUnsorted, tmp);
        }
    }
}
