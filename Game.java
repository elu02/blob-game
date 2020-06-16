import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements Runnable, MouseMotionListener, MouseListener{
	
	//CONSTANTS:
	public static final int width = 800, height = 800; // for screen
	public static final int twidth = 8000, theight = 8000; // for entire board
	public static final int startingSize = 30, numFood = 1000; // other constants
	public static final double PI = 3.14;
	public static int nBots = 6;
	
	//NON JFRAME VARIABLES
	public static double scale; 
	public static double xOffset = 0, yOffset = 0; //how much the current screen has shifted relative to the starting one
	public static double px, py; //current coordinates of player blob, "player x", player y"
	public static double nx = -1000, ny = -1000; //coordinates of cursor, "next x", "next y"
	public static double difficulty = 1.4;
	Blob[] food = new Blob[numFood];
	Bot[] bots;
	Blob P;
	Color akwa = new Color(28, 214, 205);
	Color lime = new Color(50, 205, 50);
	STATE state = STATE.mainMenu;
	
	//JFRAME VARIABLES
	JFrame frame;
	
	enum STATE {
		mainMenu, botSelect, game, rules, dead;
	}
	
	public Game() {
		setPreferredSize(new Dimension(width, height));
		
		frame = new JFrame("Agar.io");
		frame.add(this);
		frame.pack();
		frame.setResizable(false); 
		frame.setVisible(true);
	}
	
	public void init() {
		px = width*scale/2; // I want the coordinates of the main blob to be the center of the blob
		py = height*scale/2; 
		P = new Blob(px, py, startingSize, akwa); //main blob that you control
		addMouseMotionListener(this);
		addMouseListener(this);
		for(int i=0; i<numFood; i++) {
			double x = Math.random()*(twidth-60)+20, y = Math.random()*(theight-60)+20;
			food[i] = new Blob(x, y, 10, Color.white);
		}
	}
	
	public boolean ok(Blob b, double dx, double dy) {
		/*
		 * Description: Returns whether or not it is ok to move. I don't want blob to move if cursor is on blob or if 
		 * blob will be moving out of bounds. Note the *10 in the second part of the return statement is to fix problem
		 * where blob gets stuck in wall. Probably are more correct ways to fix but I'm too lazy to think.
		 * Parameters: the x (dx) and y (dy) directions that you are attempting to move in (in unit vector form)
		 * Return: a boolean denoting if you can move in direction specified by dx and dy
		 */
		double r = b.getR(), x = b.getX(), y = b.getY();
		return (pyth(x-nx, y-ny)>r||b!=P) && 
				(x-r+dx*10>0&&y-r+dy*10>0&&x+r+dx*10<twidth&&y+r+dy*10<theight);
	}
	
	public double pyth(double a, double b) { 
		/* DESCRIPTION: Pythagorean theorem... C = sqrt(A*A+B*B).
		 * PARAMETERS: "A" and "B"
		 * RETURN: a double, "C"
		 */
		return Math.sqrt(a*a + b*b);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(state==STATE.mainMenu) { //main menu graphics
			g.setColor(Color.BLACK);
			g.fillRect(0,  0, width, height);
			g.setFont(new Font("arial", Font.PLAIN, 100));
			g.setColor(Color.WHITE);
			g.drawString("Agar.io", width/2-150, 250);
			g.setFont(new Font("arial", Font.PLAIN, 60));
			g.setColor(lime);
			g.drawRect(200, 450, 400, 65);
			g.drawRect(200, 550, 400, 65);
			g.setColor(akwa);
			g.drawString("Play", width/2-55, 500);
			g.drawString("Help", width/2-55, 600);
		}
		else if(state==STATE.rules) {
			g.setColor(Color.BLACK);
			
		}
		else if(state==STATE.botSelect) { //bot select graphics
			g.setColor(Color.BLACK);
			g.fillRect(0,  0, width, height);
			g.setColor(Color.GREEN);
			g.fillRect(300, 150, 200, 75);
			g.setColor(Color.RED);
			g.fillRect(300, 575, 200, 75);
			g.setColor(Color.WHITE);
			g.fillRect(200, 300, 400, 200);
			g.setColor(akwa);
			g.fillRect(550, 550, 200, 200);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.PLAIN, 120));
			g.drawString("++", 330, 230);
			g.drawString("--", 355, 645);
			g.setFont(new Font("arial", Font.PLAIN, 30));
			g.drawString("Choose the amount of bots you want to play against (0-9): ", 25, 100);
			g.setColor(Color.BLACK);
			g.setFont(new Font("arial", Font.PLAIN, 225));
			g.drawString(nBots+"", 340, 475);
			g.setFont(new Font("arial", Font.PLAIN, 50));
			g.drawString("START", 570, 670);
		}
		else if(state==STATE.game){ //the actual game: graphics and logic
			Graphics2D g2 = (Graphics2D)g;
			scale = Math.sqrt(P.getR())/5;
			g2.scale(1.0/scale, 1.0/scale);
			
			//PLAYER MOVEMENT
			double dx = nx - px, dy = ny - py;
			double h = Math.sqrt(dx * dx + dy * dy); //length of vector
			dx /= h; //create unit vector by multiplying and x and y components by length of vector
			dy /= h; //use unit vector rather than original dy and dx because for ex. cursor at 5, 5 and at 3, 3 should have same speed
			if(ok(P, dx, dy)) { //if statement conditions make it so that the blob doesn't move if cursor is on top of blob or if blob is about to move outside game bounds
				xOffset -= dx*P.getSpeed(); //changing the offset of the origin in the opposite direction of the movement of the main blob
				yOffset -= dy*P.getSpeed(); //makes it so that the main blob is always centered. Main blob stationary, everything else is what's being moved
				px = width*scale/2-xOffset;
				py = height*scale/2-yOffset;
				P.setX(px); 
				P.setY(py);
				nx += dx*P.getSpeed(); // nx and ny get updated when the cursor is moved (even if by one pixel), but if cursor is not moved, you can see how this
				ny += dy*P.getSpeed(); // will be a problem as the blob will eventually stop moving. Updating nx and ny in same trajectory will fix problem
			}
			g2.translate(xOffset, yOffset); //shifting the origin
			//Note to Self: this works because although translate translates the origin relative to the new origin, repaint resets the origin every time (so the translate effect does not stack)
			
			//BACKGROUND AND BORDER
			g.setColor(Color.black);
			g.fillRect(-(int)xOffset, -(int)yOffset, (int)(width*scale+5), (int)(height*scale+5)); //the black background
			g.setColor(Color.red);
			g.drawRect(-10, -10, twidth+20, theight+20);
			
			//BOT MOVEMENT AND INTERACTIONS WITH FOOD/OTHER BLOBS
			for(int i=0; i<nBots; i++) {
				if(!bots[i].getAlive()) continue; 
				if(bots[i].getR() > P.getR()) bots[i].track(P); 
				else bots[i].move();
				bots[i].draw(g);
				double dist = pyth(P.getX() - bots[i].getX(), P.getY() - bots[i].getY());// check interaction with player blob
				if(bots[i].getR() > P.getR() && dist < bots[i].getR() - P.getR()) { //bot[i] eats player -> game over
					state = STATE.dead;
				}
				else if(P.getR() > bots[i].getR() && dist < P.getR() - bots[i].getR()) { // bots[i] gets eaten by player -> player gains mass, bot dies
					bots[i].setAlive(false);
					P.incRadius(bots[i]);
				}
				for(int j=i+1; j<nBots; j++) { // check interaction with other bots
					dist = pyth(bots[j].getX() - bots[i].getX(), bots[j].getY() - bots[i].getY());
					if(bots[i].getR() > bots[j].getR() && dist < bots[i].getR() - bots[j].getR()) { //bots[i] eats bots[j]
						bots[i].incRadius(bots[j]);
						bots[j].setAlive(false);
					}
					else if(bots[j].getR() > bots[i].getR() && dist < bots[j].getR() - bots[i].getR()) { //bots[j] eats bots[i] 
						bots[j].incRadius(bots[i]);
						bots[i].setAlive(false);
					}
				}
			}
			
			// FOOD BLOB INTERACTIONS 
			for(int i=0; i<numFood; i++) {
				double curx = food[i].getX(), cury = food[i].getY();
				if(pyth(curx-px, cury-py)<P.getR()+10) { // if(main blob is touching a food blob)
					P.incRadius(food[i]);
					food[i].setX(Math.random()*(twidth-60)+20); //change coordinates of the touched food blob
					food[i].setY(Math.random()*(theight-60)+20); //every time a food disappears, a new food gets created elsewhere. number of food in the game is always equal to "numFood"
				}
				food[i].draw(g);
				for(int j=0; j<nBots; j++) {
					double botx = bots[j].getX(), boty = bots[j].getY();
					if(pyth(botx-curx, boty-cury)<bots[j].getR()+10) {
						food[i].setR(10*difficulty);
						bots[j].incRadius(food[i]);
						food[i].setR(10);
						food[i].setX(Math.random()*(twidth-60)+20);
						food[i].setY(Math.random()*(theight-60)+20);
					}
				}
			}
			P.draw(g);
			
			//MISC/DEBUGGING
			System.out.printf("OFFSET: %.2f, %.2f\n", xOffset, yOffset);
			System.out.printf("PLAYER: %.2f, %.2f\n", px, py);
		}
	}
	
	@Override 
	public void run() {
		long lastTime = System.nanoTime();
		double x = 1e9D/60D; // ns/update. set the "60" to whatever fps you want
		double delta = 0; // contains value which depends on how much time has passed since last update. 
		init();
		while(true) {
			long curTime = System.nanoTime();
			delta += (curTime - lastTime)/x;
			lastTime = curTime;
			// this clock system is for consistency purposes (so the speed that the game runs at does not depend on how fast your computer is). System.nanoTime() is same for everyone
			while(delta>=1) { //essentially updates when sum(curTime-lastTime) since last reset is equal to x
				repaint(); 
				delta--; //"reset" delta
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		nx = (e.getX())*scale-xOffset;
		ny = (e.getY())*scale-yOffset;
	}

	public boolean inRect(int mx, int my, int x, int y, int lx, int ly) {
		/*
		 * DESCRIPTION: Checks if mouse click is within bounds of a specified rectangle
		 * PARAMETERS: mouse x coord, mouse y coord, top left corner of rect (x coord), top right corner of rect (y coord), length horizontally, length vertically
		 * RETURN: whether or not mouse click is within bounds of a specified rectangle
		 */
		return (mx<x+lx && mx>x && my<y+ly && my>y);
	}
	
	@Override
	public void mousePressed(MouseEvent e) { //All the buttons ...
		int mx = e.getX(), my = e.getY();
		// I think JButtons look ugly
		if(state==STATE.mainMenu) {
			if(inRect(mx, my, 200, 450, 400, 65)) {
				System.out.println("Play");
				state=STATE.botSelect;
			}
			else if(inRect(mx, my, 200, 550, 400, 65)) {
				System.out.println("Help");
				state=STATE.rules;
			}
		}
		else if(state==STATE.rules) {
			
		}
		else if(state==STATE.botSelect) {
			if(inRect(mx, my, 300, 150, 200, 75)) { //increase botCount
				if(nBots < 9) nBots++;
				else nBots = 0;
			}
			else if(inRect(mx, my, 300, 575, 200, 75)) { //decrease botCount
				if(nBots > 0) nBots--;
				else nBots = 9;
			}
			else if(inRect(mx, my, 550, 550, 200, 200)) {
				bots = new Bot[10];
				for(int i=0; i<3; i++) {
					for(int j=0; j<3; j++) {
						bots[i*3+j] = new Bot((j+1)*twidth/4, (i+1)*theight/4, startingSize, Color.RED);
					}
					
				}
				state = STATE.game;
			}
		}
	}
	
	public static void main(String[] args) {
		scale = Math.sqrt(startingSize)/5; // I want the game to zoom out as the main blob gets bigger(otherwise it will just take up the entire screen)
		// I got the equation y = sqrt(x)/5 by messing around in desmos, it represents the rate at which I want the POV to zoom out at nicely enough
		new Thread(new Game()).start();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
