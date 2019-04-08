package polygon;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Draw_Polygon extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int x[];
	private int y[];
	
	public Draw_Polygon(int[] x, int[] y) {
		this.x = x;
		this.y = y;
	}
	
	public int[] getXval() {
		return x.clone();
	}
	
	public int[] getYval() {
		return y.clone();
	}
	

	public void paintComponent(Graphics g) {
        // Draw a polygon. Scales the value to fit a frame of 1000,1000 pixels.
		// Finds the largest x and y value, and scales up to 900 pixels to ensure it fits the frame.
		// If any value is negative, all values are shifted to ensure no negative values exist.
		// This is due to java painting only in positive values.
		
		super.paintComponent(g);
		
		int[] x = getXval();
		int[] y = getYval();
		int n = x.length;
		
		int[] sorted_x = x.clone();
		int[] sorted_y = y.clone();
		
		Arrays.sort(sorted_x);

		Arrays.sort(sorted_y);
		
		int max_x = sorted_x[x.length - 1];
		int max_y = sorted_y[y.length - 1];
		
		int min_x = sorted_x[0];
		int min_y = sorted_y[0];
		
		
		//shifts the values in either dimension if there are negative values.
		if(min_x <0) {
			for(int i = 0; i < n; i++) {
				x[i] = x[i] + Math.abs(min_x);
			}
			max_x = max_x + Math.abs(min_x);
		}
		if(min_y <0) {
			for(int i = 0; i < n; i++) {
				y[i] = y[i] + Math.abs(min_y);
			}
			max_y = max_y + Math.abs(min_y);
		}
		
		int scalex = 900/max_x;
		int scaley = 900/max_y;
		
		//scales the values for plotting.
		for(int i = 0; i < n; i++) {
	
			x[i] *=scalex;
			y[i] = -y[i]*scaley + 900;

		}
		
		
        g.drawPolygon(x, y, n);
	}
	
	
	//Creates a Jframe and draws the polygon.
    public static void frame(int polynumber, Polygons polygon) {
    	
    	int x[] = polygon.getX();
    	int y[] = polygon.getY();
    	

    	
    	String value = Integer.toString(polynumber + 1);
        JFrame frame = new JFrame();
        frame.setTitle("Polygon " + value);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentpane = frame.getContentPane();
        contentpane.add(new Draw_Polygon(x , y));  
        frame.setVisible(true);

}
}
