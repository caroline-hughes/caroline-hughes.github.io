// PointCanvas.java
//
// CS 201 Exam 2

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial") // to avoid Eclipse warning
public class PointCanvas extends Canvas implements MouseListener {

    protected KdTreeApplet parent;  // access to main applet class

    // constructor
    public PointCanvas(KdTreeApplet app) {
        parent = app;
        setFont(new Font("Arial", Font.PLAIN, 16));
        addMouseListener(this);
    }

    // repaint this canvas
    public void paint(Graphics g) {
        // turn on anti-aliasing for smoother lines
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        KdTree t = parent.tree();
        Dimension d = getSize();  // size of canvas

        if (t == null) {
            g.setColor(Color.black);
            g.drawString("Click to add points", 50, 50);
        } else {
            int x0 = 0;
            int y0 = 0;
            int x1 = d.width;
            int y1 = d.height;
            // calls your code for problem 4:
            KdTreeOps.draw(g, t, 0, x0, y0, x1, y1);
        }
    }

    // handle mouse clicks: add new point to tree
    public void mousePressed(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        //System.out.println("Click" + p.x	+ "," + p.y);
        parent.add(p);
    }

    // also need to provide these since we're implementing MouseListener
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
