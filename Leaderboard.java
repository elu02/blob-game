import java.awt.*;
import java.util.*;

public class Leaderboard {
	private int n, smallest = 100;
	private Blob[] top10;
	HashMap<String, Integer> mp = new HashMap<String, Integer>();
	
	public Leaderboard(Blob[] top10, int n) {
		if(n<10) {
			this.n = n;
		}
		else {
			this.n = 10;
		}
		this.top10 = new Blob[n];
		int cur = 1;
		mp.put(top10[0].getName(), 1);
		for(int i=0; i<n; i++) {
			this.top10[i] = top10[i];
			if(i>0) {
				if(top10[i]==top10[i-1]) mp.put(top10[i].getName(), cur);
				else {
					cur = i;
					mp.put(top10[i].getName(), cur);
					smallest = cur;
				}
			}
		}
		Arrays.sort(this.top10);
	}
	
	public int getPlacement(String s) {
		return mp.get(s);
	}
	
	public boolean isSmallest(String s) {
		return (smallest==mp.get(s));
	}
	
	public void draw(Graphics g, double scale, double xOffset, double yOffset) {
		g.setColor(new Color(50, 205, 50));
		g.setFont(new Font("arial", Font.PLAIN, (int) (25*scale)));
		g.drawString("- Leaderboard -", (int)(580*scale - xOffset), (int)(50*scale - yOffset));
		for(int i=0; i<n; i++) {
			int score = (int)(top10[i].getR() * top10[i].getR() * 3.14)/10-282;
			g.drawString((i+1) + ". " + top10[i].getName() + ": " + score, (int)(580*scale-xOffset), (int)(105*scale+i*scale*40-yOffset));
		}
	}
	
}
