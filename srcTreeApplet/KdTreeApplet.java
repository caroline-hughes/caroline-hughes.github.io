// An Applet for visualizing K-d Trees with 2 dimensions
//
// CS 201 Exam 2

import java.awt.*;        // abstract window toolkit
import java.awt.event.*;  // event handling
import java.applet.*;     // Applet classes

@SuppressWarnings("serial") // to avoid Eclipse warning
public class KdTreeApplet extends Applet implements ActionListener, ItemListener
{
    // instance variables

    private KdTree tree; // the K-d tree stored

    private Button clearButton, rebuildButton;
    private Choice drawChoice;

    private PointCanvas pc;   // shows points and lines in 2D plane
    private TreeCanvas tc;    // shows corresponding tree

    // initialize applet
    public void init() {  // layout of applet

        tree = null;

        setLayout(new BorderLayout());
        Label title = new Label("K-D-Tree");
        title.setAlignment(Label.CENTER);
        title.setBackground(Color.blue);
        title.setForeground(Color.white);

        pc = new PointCanvas(this);
        pc.setBackground(Color.white);

        tc = new TreeCanvas(this);
        tc.setBackground(new Color(240, 240, 255)); // light blue

        Panel canvasPanel = new Panel();
        canvasPanel.setLayout(new GridLayout(1, 2, 2, 2));
        canvasPanel.setBackground(Color.blue);
        canvasPanel.add(pc);
        canvasPanel.add(tc);

        add("North", title);
        add("Center", canvasPanel);
        add("South", southPanel());
    }

    // create panel with buttons and menu
    private Panel southPanel() {
        clearButton = new Button("Clear");
        clearButton.addActionListener(this);

        rebuildButton = new Button("Rebuild");
        rebuildButton.addActionListener(this);

        Label drawLabel = new Label("drawing style:");
        drawLabel.setAlignment(Label.RIGHT);

        drawChoice = new Choice();
        drawChoice.addItem("fixed");
        drawChoice.addItem("variable");
        drawChoice.addItemListener(this);

        Panel drawChoicePanel = new Panel();
        drawChoicePanel.setLayout(new BorderLayout());
        drawChoicePanel.add("West", drawLabel);
        drawChoicePanel.add("Center", drawChoice);       

        Panel p = new Panel();
        p.setLayout(new GridLayout(1, 3));
        p.add(clearButton);
        p.add(rebuildButton);
        p.add(drawChoicePanel);

        return p;
    }

    // accessor for tree variable
    public KdTree tree() {
        return this.tree;
    }

    // add a new point to the tree
    public void add(Point p) {
        //points.add(p);
        if (tree == null) {
            tree = new KdTree(p, true);
        } else {
            tree.add(p);
        }
        pc.repaint();
        tc.repaint();
    }

    // action handler for buttons
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == clearButton) {
            tree = null;
            pc.repaint();
            tc.repaint();

        } else if (evt.getSource() == rebuildButton) {
            tree = KdTreeOps.rebuild(tree);
            pc.repaint();
            tc.repaint();
        }
    }

    // action handler for choice menu
    public void itemStateChanged(ItemEvent e)  {
        if (e.getSource() == drawChoice) {
            int drawStyle = drawChoice.getSelectedIndex();
            tc.setDrawStyle(drawStyle);
        }
    }
}
