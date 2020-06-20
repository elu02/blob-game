import java.awt.*;

public class Blob implements Comparable<Blob>{
	private double x, y, r, speed = 6;
	String name;
	Color c;
	
	public Blob(double x, double y, int r, Color c, String name) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.c = c;
		this.name = name;
	}
	
	public void incRadius(Blob b) {
		double curArea = 3.14 * r * r; 
		curArea += 3.14 * b.getR() * b.getR();
		r = Math.sqrt(curArea/3.14);
	}
	
	public void draw(Graphics g, double scale) {
		g.setColor(c);
		g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
		g.setColor(Color.white);
		g.drawOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, (int)(30*scale)));
		int fx = g.getFontMetrics().stringWidth(name), fy = g.getFontMetrics().getHeight();
		g.drawString(name, (int)(x-fx/2), (int)(y+fy/2));
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(Color c) {
		this.c = c;
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
	
	public String getName() {
		return name;
	}
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getR() {
		return r;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public int compareTo(Blob blob) {
		return (int) (blob.getR() - r);
	}
}
