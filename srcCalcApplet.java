// Postfix Calculator Applet
// Name: Caroline Hughes

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; // for Stack


public class Calc extends Applet implements ActionListener {

    private static final long serialVersionUID = 1L; // to avoid Eclipse warning
    
    // instance variables
    protected Label result;         // label used to show result
    protected Stack<Integer> stack; // stack used for calculations
    protected int current;          // current number being entered
    protected boolean entered;      // whether current number has been entered
                                    // if so, show number in red

    // local color constants
    static final Color black = Color.black;
    static final Color white = Color.white;
    static final Color red = Color.red;
    static final Color blue = Color.blue;
    static final Color yellow = Color.yellow;
    static final Color orange = Color.orange;
    static final Color lblue = new Color(200, 220, 255);
    static final Color dred = new Color(160, 0, 100);
    static final Color dgreen = new Color(0, 120, 90);
    static final Color dblue = Color.blue.darker();

    //initializes the applet
    public void init() 
    {
    	//initializes the stack instance variable
    	stack = new Stack<Integer>();
    	
    	//font used in applet
        setFont(new Font("TimesRoman", Font.BOLD, 28));
       
        setSize(300, 400);
        setVisible(true);
    
        //creates southern section of border layout, which consists of a grid layout of two buttons 
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(1, 2, 2, 2));
        p2.setBackground(blue);
       Button b1=CButton("Enter", blue, lblue);
       b1.setForeground(dblue);
       Button b2=CButton("Clear", blue, lblue);
       b2.setForeground(dblue);
        p2.add(b1);
        p2.add(b2);
        
        
       //create the border layout
       this.setLayout(new BorderLayout());
       this.add("North", createTopPanel());
       this.add("South", p2);
       this.add("Center", makeCenterGrid());
    }

    //creates the 16 buttons that go in the center of the calc, puts them into an array and then adds each element 
    //in array to the center panel
    public Panel makeCenterGrid()
    {
 	   Panel p3 = new Panel();
       p3.setBackground(blue);
        p3.setLayout(new GridLayout(4, 4, 2, 2)); // one row, three columns
        Button[] arr = {CButton("7", blue, yellow), CButton("8", blue, yellow), CButton("9", blue, yellow), 
        		CButton("+", blue, orange), CButton("4", blue, yellow), CButton("5", blue, yellow), CButton("6", blue, yellow),
        		CButton("-", blue, orange), CButton("1", blue, yellow), CButton("2", blue, yellow), CButton("3", blue, yellow),
        		 CButton("*", blue, orange), CButton("0", blue, yellow), CButton("(-)", blue, yellow), CButton("Pop", blue, yellow),
        		 CButton("/", blue, orange)};
        arr[0].setForeground(dgreen);
        arr[1].setForeground(dgreen);
        arr[2].setForeground(dgreen);
        arr[3].setForeground(dblue);
        arr[4].setForeground(dgreen);
        arr[5].setForeground(dgreen);
        arr[6].setForeground(dgreen);
        arr[7].setForeground(dblue);
        arr[8].setForeground(dgreen);
        arr[9].setForeground(dgreen);
        arr[10].setForeground(dgreen);
        arr[11].setForeground(dblue);
        arr[12].setForeground(dgreen);
        arr[13].setForeground(dred);
        arr[14].setForeground(dred);
        arr[15].setForeground(dblue);
        for(int i =0; i<16; i++)
        {
        	p3.add(arr[i]);
        }
        return p3;
    }
    
    //creates northern section of border layout by creating a border layout of blue outline panels and a center result panel
    public Panel createTopPanel()
    {
    	Panel p1 = new Panel();
    	Panel p4 = new Panel();
    	Panel p5 = new Panel();
    	Panel p6 = new Panel();
    	Panel p7 = new Panel();
    	p4.setBackground(Color.blue);
    	p5.setBackground(Color.blue);
    	p6.setBackground(Color.blue);
    	p7.setBackground(Color.blue);
        p1.setLayout(new BorderLayout());
        p1.add("North", p4);
        p1.add("South", p5);
        result = new Label("0");
        result.setAlignment(Label.RIGHT);
        result.setForeground(dgreen);
        p1.add("Center", result);
        p1.setBackground(Color.white);
        p1.add("East", p6);
        p1.add("West", p7);
        return p1;
    }
    

    // create a colored button
    protected Button CButton(String s, Color fg, Color bg) {
        Button b = new Button(s);
        b.setBackground(bg);
        b.setForeground(fg);
        b.addActionListener(this);
        return b;
    }
    
    
    // handle button clicks
    public void actionPerformed(ActionEvent e) 
    {
    	
        if (e.getSource() instanceof Button) 
        {
            String label = ((Button)e.getSource()).getLabel();
            if (label.equals("+"))
                add();
            else if (label.equals("-"))
                sub();
            else if (label.equals("*"))
                mult();
            else if (label.equals("/"))
            	div();
            else if (label.equals("(-)"))
            	makeNeg();
            else if (label.equals("Pop"))
            	pop1();
            else if (label.equals("Enter"))
            	enter();
            else if (label.equals("Clear"))
            	clear();
            else 
            {     // number button
                int n = Integer.parseInt(label);
                number(n);
            }
        }
    }
    
    // display number n in result label
    protected void show(int n) 
    {
    	if(entered)
    		result.setForeground(red);
    	else
    		result.setForeground(dgreen);
    	
        result.setText(Integer.toString(n));
    }

    // if a digit button is pressed
    protected void number(int n) 
    {
    	entered = false;
    	current= 10*current + n;
        show(current);
    }

    // when user clicks enter, add current to the stack, display current in red, reset current to zero
    protected void enter() 
    {
    	entered = true;
    	stack.push(current);
    	show(current);
    	current = 0;
    }
    
    // when user clicks clear, clear the stack, reset current to zero and display zero in green
    protected void clear() 
    {
    	entered = false;
    	stack.clear();
    	current = 0;
    	show(0);
    }
    
    // adds the top two numbers from stack
    protected void add() 
    {
    	
    	if(entered == false)
	    	{
	    		if(!stack.empty())
	    			current = current + stack.pop();
	    			
	    	}
    	else if(stack.empty())
	        {
	     	   show(0);
	        }
        else
	        {
	     	  int a = stack.pop();
	     	  if(stack.empty())
	     	  {
	     		  show(a);
	     	  }
	     	  else
	     	  {
	     		  int b = stack.pop();
	              current= a+b;
	              
	     	  }  
	       }
        enter();
     }
     
    //subtracts the top two numbers from stack
    protected void sub() 
    {
    	if(entered == false)
    	{
    		if(!stack.empty())
    			current = stack.pop() - current;
    		else
    			current = -1*current;
    	}
	else if(stack.empty())
        {
     	   show(0);
        }
    else
        {
     	  int a = stack.pop();
     	  if(stack.empty())
     	  {
     		  show(a);
     	  }
     	  else
     	  {
     		  int b = stack.pop();
              current= b-a;
              
     	  }  
       }
    	enter();
 
     }
    
    //multiplies the top two numbers from stack
    protected void mult() 
    {
    	if(entered == false)
    	{
    		if(!stack.empty())
    			current = stack.pop() * current;
    		else
    			current = 0;
    	}
	else if(stack.empty())
        {
     	   show(0);
        }
    else
        {
     	  int a = stack.pop();
     	  if(stack.empty())
     	  {
     		  show(a);
     	  }
     	  else
     	  {
     		  int b = stack.pop();
              current= b*a;
     	  }  
       }
    	enter();
 
     }
    
    //divides the top two numbers from stack
    protected void div() 
    {
    	if(entered == false)
    	{
    		if(!stack.empty())
    			current = stack.pop()/current;
    		else
    			current =0;
    	}
	else if(stack.empty())
        {
     	   show(0);
        }
    else
        {
     	  int a = stack.pop();
     	  if(stack.empty())
     	  {
     		  show(a);
     	  }
     	  else
     	  {
     		  int b = stack.pop();
              current= b/a;
     	  }  
       }
    	enter();
 
     }
    
    //makes the current number negative and enters that number
    protected void makeNeg() 
    {
    	int b = -1 * current;
    	current = b;
    	enter();
    }
    
    //called when the button Pop is pressed. removes the top-most number from the stack
    protected void pop1() 
    {
    	if(entered = false)
    		{
    			if(!stack.empty())
    				current = stack.peek();
    			else
    				current = 0;
    		}
    	else if (stack.empty())
    		current = 0;
    	else
    	{
    		stack.pop();
        	if(stack.empty())
           		current=0;
           	else
        		current = stack.peek();
    	}
    	entered = true;
    	show(current);
    	current = 0;
    }
    
    //removes top-most number from stack
    protected int pop() 
    {
    	if(stack.empty())
    		return 0;
    	else
    	{
    		return stack.pop();
    	}

    }
   
}
