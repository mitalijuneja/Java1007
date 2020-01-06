package assignment4;

import java.awt.*;

/**
 * 
 * @author Mitali Juneja (mj2944), using courseworks code fragments
 * The MovingObject Interface lists out the common methods for any graphics object drawn on the screen that can move. 
 * The object must be able to be drawn on the screen and moved, or translated, across the screen.
 */

public interface MovingObject {
	public void draw(Graphics window);
	public void translate(int dx, int dy);
	

}
