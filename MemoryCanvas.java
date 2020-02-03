package finalProject;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("serial")

public class MemoryCanvas extends Canvas implements MouseListener  {

	// instance variables representing the game
	Card[][] box;
	int selectedSize;
	int sizeOfBoard;
	int cardSize;
	int border;
	int numMatches;
	int numTries;
	int firstX; //keeps track of row number of first card flipped
	int firstY; //keeps track of column number of first card flipped
	int secX; //keeps track of row number of second card flipped
	int secY; //keeps track of column number of second card flipped
	boolean gameIsWon;
	public final Color lBlue = new Color(200,220,255);
	Memory parent;

	
	public MemoryCanvas(Memory memory) {
		parent = memory;
	}

	// draw the boxes
	public void paint(Graphics g) {

		Dimension d = getSize(); 
		int i;
		int j;

		for (i = 0; i < sizeOfBoard; i++) {
			for (j = 0; j < sizeOfBoard; j++) {

				if(box[i][j].theColor == 9){
					g.setColor(Color.white);
					drawRemoved(g, i, j);}
				else {
					if (box[i][j].isShown == false) { //called at beginning and every time a box is clicked. checks whether it has been clicked, and if it 
						g.setColor(Color.black);		//hasn't, its still the base color, otherwise shows color associated with number
						drawIt(g, i, j);}			
					else { 
						if(box[i][j].theColor == 1)	{
							g.setColor(Color.red);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 2){
							g.setColor(Color.blue);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 3){
							g.setColor(Color.pink);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 4){
							g.setColor(Color.yellow);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 5){
							g.setColor(Color.orange);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 6){
							g.setColor(Color.gray);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 7){
							g.setColor(Color.cyan);
							drawIt(g, i, j);}
						if(box[i][j].theColor == 8){
							g.setColor(Color.green);
							drawIt(g, i, j);}
					}}}}

		//checks whether the game has been won, if so prints winning statement
		if(gameIsWon) {
			g.setColor(Color.red);
			String s = "YOU WIN!";
			centerString(g, s, d.width/2, d.height/2);
			parent.stopTimer();
		}

		//draw the labels at the bottom for number of tries and number of matches
		g.setColor(Color.black);
		String s1 = "Number of Tries: " + numTries + "       Number of Matches: " + numMatches;
		centerString(g, s1, d.width/2, d.height-20);
	}

	//draw a card that is not shown
	public void drawIt(Graphics g, int a, int b)
	{
		int x = a * cardSize + border;
		int y = b * cardSize + border; 
		g.fillRect(x, y, cardSize, cardSize);
		g.setColor(Color.white);
		g.drawRect(x, y, cardSize, cardSize);
	}

	//draw a card that has been correctly matched and removed
	public void drawRemoved(Graphics g, int a, int b)
	{
		int x = a * cardSize + border;
		int y = b * cardSize + border; 
		g.fillRect(x, y, cardSize, cardSize);
		g.setColor(Color.white);
		g.drawRect(x, y, cardSize, cardSize);
	}

	// change board size if pull-down menu has changed
	public void setBoardSize(int a) {
		selectedSize = a;
	}

	//called at the beginning and whenever the restart button is pressed
	public void startNewGame()
	{
		cardSize = 80;
		border = 40;
		gameIsWon = false;
		numTries=0;
		numMatches=0;

		//set the size of the board
		if(selectedSize == -1) //4x4 as default
			sizeOfBoard = 4;
		else if(selectedSize == 0)  //4x4
			sizeOfBoard = 4;
		else 				   //6x6 
			sizeOfBoard = 6;

		//create 2D array based on chosen size of board
		box = new Card[sizeOfBoard][sizeOfBoard];

		//create 1D array with correct numbers
		Integer[] array;
		if(sizeOfBoard == 4)
			array= new Integer[]{1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8};
		else 
			array= new Integer[]{1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,1,2,3,4}; // change

		//shuffle the values in the 1D array
		Collections.shuffle(Arrays.asList(array));

		//add the shuffled values to the 2D array
		int w = 0; //keeps track of the index of the 1D array
		for(int i = 0; i < sizeOfBoard; i++) {
			for(int j = 0; j < sizeOfBoard; j++) {
				box[i][j] = new Card(array[w], false);
				w++;
			}
		}
		parent.startTimer();
		repaint();
	}

	// handle mouse clicks
	public void mousePressed(MouseEvent event) {

		int counter = 0;
		//check how many cards are shown / have been clicked
		for (int i = 0; i < sizeOfBoard; i++) {
			for (int j = 0; j < sizeOfBoard; j++) {
				if(box[i][j].isShown == true) {
					counter++;
				}}}

		//determine which  card the user clicked
		Point p = event.getPoint();
		int x = p.x - border;
		int y = p.y - border;
		if (x >= 0 && x < sizeOfBoard*cardSize && y >= 0 && y < sizeOfBoard*cardSize) 
		{
			int k = x / cardSize;
			int t = y / cardSize;
			if(box[k][t].theColor!=9) //as long as they didn't click in a square that has already been matched
			{
				//if it is the first click
				if(counter == 0) {
					firstX = k;
					firstY = t;
					show(k, t);
				}
				//if it is the second click
				else if(counter == 1) {
					secX = k;
					secY = t;
					show(k, t);
				}	
			}
		}
		//if it is the third click, compare the cards selected in the first two clicks
		if(counter ==2)
			compareCards(firstX, firstY, secX, secY);
	}

	//display the true color/image of the card
	public void show(int a, int b)
	{
		box[a][b].isShown = true; //show the most recently clicked card
		repaint(); 	
	}

	//compare the two cards that have been shown
	public void compareCards(int a, int b, int i, int j)
	{
		//if the cards are have the same color, make their color now black (remove them).
		if (box[a][b].theColor == box[i][j].theColor) {
			box[a][b].theColor = 9;
			box[i][j].theColor = 9;
			box[i][j].isShown = false;
			box[a][b].isShown = false;
			numMatches++; }

		//if the cards do not have the same color, flip them back over. 
		else {
			box[i][j].isShown = false;
			box[a][b].isShown = false; }

		numTries++;
		checkIfGameWon(); //check after every comparison of cards whether the player has made all the matches
		repaint();
	}

	//check if the user has won
	public void checkIfGameWon() {
		int counter=0;
		for (Card[] u: box) {
			for (Card elem: u) {
				if(elem.theColor != 9) {
					counter++;}
			}}

		if(counter == 0) {
			gameIsWon = true; }
	}

	public void mouseReleased(MouseEvent event) { }
	public void mouseClicked(MouseEvent event) { }
	public void mouseEntered(MouseEvent event) { }
	public void mouseExited(MouseEvent event) { }

	//draw a String centered at x, y
	public static void centerString(Graphics g, String s, int x, int y)
	{
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = x - fm.stringWidth(s)/2;
		int ys = y + fm.getAscent()/3;
		g.drawString(s, xs, ys);
	}

}

