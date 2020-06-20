import java.awt.Color;

public class Bot extends Blob {
	private int nextTime = -1, curTime = 0; //curTime is time since last reset, nextTime is time of next reset
	// not really time actually as it is measured in frames rather than seconds
	private double dx, dy;
	private boolean alive = true, tracking = false;
	private int target = -1;
	
	public Bot(double x, double y, int r, Color c, String name) {
		super(x, y, r, c, name);
	}
	
	public boolean ok() { //same idea as ok function from game class
		double x = getX(), y = getY(), r = getR();
		return (x-r+dx*10>0&&x+r+dx*10<Game.twidth&&y-r+dy*10>0&&y+r+dy*10<Game.theight);
	}
	
	public void track(Blob b) { //returns whether or not the state (tracking/not tracking) has changed
		dx = b.getX() - getX();
		dy = b.getY() - getY();
		double h = Math.sqrt(dx*dx + dy*dy);
		dx /= h;
		dy /= h;
		setX(getX() + dx * getSpeed());
		setY(getY() + dy * getSpeed());
	}
	
	public void move() {
		if(curTime > nextTime) { //time for something new
			double rand = Math.random();
			if(rand<0.8) { //20% CHANCE: start tracking someone
				tracking = true;
			}
			else { //80% CHANCE: move randomly again
				curTime = 0;
				nextTime = (int)(60 + Math.random() * 240); // I want the bot to (try to) move in some direction for between 1 to 4 seconds (note: between 60 and 240 frames (60FPS))
				double xDir = Math.random() * 2 - 1, yDir = Math.random() * 2 - 1; //generate random x and y direction vectors 
				//(when I googled it people said to use some normal distribution thing, but when I test it out my distribution looks pretty even)
				double h = Math.sqrt(xDir*xDir + yDir*yDir); 
				dx = xDir/h;
				dy = yDir/h;
			}
		}
		else { //just update curTime and x and y in previous direction
			if(ok()) {
				setX(getX() + dx * getSpeed()); 
				setY(getY() + dy * getSpeed());
				curTime++;
			}
			else {
				curTime = (int)1e9;
			}
		}
	}
	
	void setTarget(int x) {
		target = x;
	}
	
	public void setAlive(boolean b) {
		alive = b;
	}
	
	public void setTracking(boolean b) {
		tracking = b;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public boolean getTracking() {
		return tracking;
	}
	
	public int getTarget() {
		return target;
	}
	
}
