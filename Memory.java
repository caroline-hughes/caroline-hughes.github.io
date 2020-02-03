// Memory game
// Caroline Hughes and Halcyon Brown

package finalProject;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Memory extends Applet implements ActionListener, Runnable, ItemListener
{
	private static final long serialVersionUID = 1L;

	MemoryCanvas c;
	Button restartButton;
	Label timerLabel;
	Thread t;
	long starttime;
	public final Color lBlue = new Color(200,220,255);
	Choice sizeChoice;
	boolean timerOn;

	public void init () { //called once at beginning
		
		//createBorderLayout
		setLayout(new BorderLayout());
		
		//create restart button and label for timer, add them to southern border
		restartButton = new Button("Restart");
		restartButton.setBackground(Color.white);
		restartButton.addActionListener(this);
		Panel p1 = new Panel();
		timerLabel = new Label("xxxxxxxxxxxx");
		timerLabel.setBackground(Color.white);
		p1.add(timerLabel);
		p1.setBackground(Color.white);
		p1.add(restartButton);
		
		//add drop down menu to pick size
		sizeChoice = new Choice();
		sizeChoice.add("4x4");
		sizeChoice.add("6x6");
		sizeChoice.addItemListener(this);
		p1.add(sizeChoice);

		add("South", p1);
		p1.setBackground(Color.white);
		
		//create label for the title, add to north
		Label title = new Label("The Game of Memory: click two cards");
		title.setBackground(Color.blue);
		title.setForeground(Color.white);
		title.setAlignment(Label.CENTER);
		Font newFont = new Font ("TimesRoman", Font.BOLD, 23);
		title.setFont(newFont);
		add("North", title);

		//create center canvas where the game play will occur
		c = new MemoryCanvas(this);
		c.setBackground(Color.white);
		c.addMouseListener(c);
		add("Center", c);
		c.startNewGame();
	}

	//for timer
	public void start() {
		t = new Thread(this);
		t.start();
		starttime = System.currentTimeMillis(); // record start time to reset timer
	}

	//for timer
	public void stop() {
		t = null;
	}

	//for timer
	public void run() {
		Thread currentThread = Thread.currentThread();
		while (currentThread == t) {
			try {
				Thread.sleep(200); // wait 0.2 sec
			} catch (InterruptedException e) {
			}
			double time = (System.currentTimeMillis() - starttime) / 1000.0;
			if(timerOn)
			timerLabel.setText("t = " + time);
			else
			timerLabel.setText("t = " + 0);
		} 
	}

	//event handler for click on the restart button
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == restartButton) 
			c.startNewGame();
		starttime = System.currentTimeMillis(); // reset timer here too
	}

	// event handler for pull-down menu... if -1 (default), 4x4... if 0, 4x4... if 1, 6x6.
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == sizeChoice) {
			int selectedSize = sizeChoice.getSelectedIndex();
			c.setBoardSize(selectedSize); // 0 = 4 by 4, 1 = 6 by 6
		}
	}

	public void stopTimer() {
		timerOn = false;
		
	}
	
	public void startTimer() {
		timerOn = true;
		
	}
}



