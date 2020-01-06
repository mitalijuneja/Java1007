

import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.font.FontRenderContext;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * 
 * The BlackHole class is used to implement step 2.5 option 2. It handles the creation of black holes as well as detecting
 * and responding to any Throw that comes near a BlackHole. We choose to display each black hole as a black circle since
 * this makes it extremely easy to see where the each one is and this is the most literal interpretation of the words
 * "black hole." 
 *
 */
public class BlackHole {
	
	public BlackHole(){
		blackHoles = new ArrayList<Ellipse2D.Double>();
	}
	
	
	/**
	 * 
	 * @param e is the MouseEvent created by the MouseListener when the mouse is clicked by the user
	 * 
	 * We first retrieve the Point where the user clicked the mouse. Then we create an Ellipse centered at this
	 * point with the appropriate radius. We add this Ellipse to the ArrayList storing the Ellipse2D objects representing
	 * the black holes
	 */
	public void mouseClicked(MouseEvent e){
		Point mousePoint = e.getPoint();
		Ellipse2D.Double blackHole = new Ellipse2D.Double(mousePoint.getX() - BLACK_HOLE_RADIUS, 
				mousePoint.getY() - BLACK_HOLE_RADIUS, 2*BLACK_HOLE_RADIUS, 2*BLACK_HOLE_RADIUS);
		blackHoles.add(blackHole);
	}
	
	
	/**
	 * 
	 * @param g is the Graphics object the Applet is using for display
	 * 
	 * We first reset the paintbrush color to Color.BLACK and then draw the filled in oval with x, y, width, and height
	 * corresponding to the black holes in the ArrayList storing the Ellipse2D objects representing the black holes
	 */
	public void drawBlackHoles(Graphics g){
		g.setColor(Color.BLACK);
		for (int i = 0; i < blackHoles.size(); i++){
			g.fillOval((int)blackHoles.get(i).getX(), (int)blackHoles.get(i).getY(), (int)blackHoles.get(i).getWidth(), 
					(int)blackHoles.get(i).getHeight());
		}
	}
	
	
	/**
	 * 
	 * @param gameThrows is the array storing the Throw objects being displayed in the Applet
	 * @param throwContext is the FontRenderContext for the Graphics2D object the Applet is using for display
	 * 
	 * We iterate through all of the Throw objects and all of the black holes. If a black hole intersects with the 
	 * Rectangle outlining a Throw, then that Throw is "destroyed"  
	 */
	public void detectThrowCollision(Throw[] gameThrows, FontRenderContext throwContext){
		for (int i = 0; i < gameThrows.length; i++){
			for (int j = 0; j < blackHoles.size(); j++){
				if (blackHoles.get(j).intersects(gameThrows[i].getRect()))
					destroyThrow(gameThrows[i], throwContext);
			}
		}
	}
	
	
	/**
	 * 
	 * @param collidingThrow is the Throw object that has collided with a black hole
	 * @param throwContext is the FontRenderContext for the Graphics2D object the Applet is using for display
	 *
	 * To "destroy" the Throw, we set the name of the Throw to "" so that nothing appears on the screen. We then
	 * update the Throw's Rectangle based on this, so that it is now essentially a line that cannot "intersect" with
	 * anything (it no longer affects other Throws or black holes) 
	 */
	private void destroyThrow(Throw collidingThrow, FontRenderContext throwContext){
		collidingThrow.setName("");
		collidingThrow.setRectangle(collidingThrow.getFont().getStringBounds(collidingThrow.getName(), throwContext));
	}
	
	
	/**
	 * 
	 * @return the ArrayList storing the ellipses representing the black holes
	 */
	public ArrayList<Ellipse2D.Double> getBlackHoles(){
		return blackHoles;
	}
	
	
	
	/**
	 * We use an ArrayList to store the black hole ellipses (circles) due to the inversion of control that occurs when
	 * we allow the user's mouse click to prompt the creation of a black hole. Since we have no way of knowing beforehand
	 * how many black holes the user wishes to create, we need a data structure with dynamic sizing. Since we are more
	 * comfortable with using ArrayLists than LinkedLists, we opt for an ArrayList. 
	 * The value of BLACK_HOLE_RADIUS was determined through repeated tests. A radius of 7 creates a black hole that is
	 * easily visible (not too small) but that does not appear to be overwhelmingly large.
	 */
	private final int BLACK_HOLE_RADIUS = 7;
	private ArrayList<Ellipse2D.Double> blackHoles;
}
