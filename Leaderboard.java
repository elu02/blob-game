import java.awt.*;

public class Leaderboard {
	private int n;
	private Blob[] top10;
	
	public Leaderboard(Blob[] top10, int n) {
		if(n<10) {
			this.n = n;
		}
		else {
			this.n = 10;
		}
		for(int i=0; i<n; i++) {
			this.top10[i] = top10[i];
		}
	}
	
	public void draw(Graphics g, double scale, double xOffset, double yOffset) {
		for(int i=0; i<n; i++) {
			Blob cur = top10[i];
			int score = (int)(3.14 * cur.getR() * cur.getR());
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.PLAIN, (int) (30*scale)));
			g.drawString(score+"", (int)(600*scale-xOffset), (int)(i*scale*20-yOffset));
		}
	}
	
}
