import java.awt.*;

public class Blob {
	private double x, y, r, speed = 6;
	Color c;
	
	public Blob(double x, double y, int r, Color c) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.c = c;
	}
	
	public void incRadius(Blob b) {
		double curArea = 3.14 * r * r; 
		curArea += 3.14 * b.getR() * b.getR();
		r = Math.sqrt(curArea/3.14);
	}
	
	public void draw(Graphics g) {
		g.setColor(c);
		g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
		g.setColor(Color.white);
		g.drawOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getR() {
		return this.r;
	}
	
	public double getSpeed() {
		return this.speed;
	}
}
