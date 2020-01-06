package assignment4;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Car class creates a simple car graphic that is drawn on the screen. It contains methods for creating each
 * simple shape in the car (car body, 2 wheels, driver's head, and windshield), moving the car's position, drawing 
 * the complete car, and a paint method to paint the car to the screen.
 * A note to make here is that this class has 8 methods, which is one more than the typical maximum of 7 methods. This
 * is because 5 these are short methods used to create the simple Shapes that make up the car body. Rather than having 
 * an even larger draw() method and staying within the 2 to 7 method limit, we choose to add additional methods to 
 * break up the construction of each part of the car. Additionally, this class has 14 fields, which is more than the 
 * typical maximum of 7 fields. However, 11 of these fields are final constants to help scale and construct the Car 
 * object in coordinates and sizes relative to the Rectangle car body. We choose to avoid magic numbers (for clarity) at
 * the expense of including more final fields.
 * TESTING = 
 * We name constants with the convention that SCALE is a scaling factor that we multiply with unit to arrive at a final
 * size for the object and FRACTION is a fraction that determines how far along the car length we place an object.
 * We tested constants for positioning the different car components relative to the car body Rectangle. 
 * We settled on a 3 to 1 ratio for the rectangle, because we wanted to keep this ratio a whole number. 2 to 1 made the
 * car look oddly boxy and square shaped while 4 to 1 made it look very long, so we used 3 to 1 (CAR_LENGTH = 3, 
 * CAR_HEIGHT = 1).
 * We set the base unit to 30, initially (this changes for the construction of each car, when multiplied by a scaling
 * factor from 0.5 to 2.0). Testing values around 40 and 50 made the cars with higher scaling factors around 2.0 seem
 * very big (took up a lot of screen space), while values around 15 and 20 made the small cars (scaled by 0.5) seem
 * so small (especially when combined with the goal of achieving depth by moving these cars to the "back") that we 
 * increased unit to 30, which we felt balanced these two concerns best. 
 * For L_WHEEL_FRACTION and R_WHEEL_FRACTION, we initially had these set to 1/4 and 3/4 (how far along the rectangle
 * these wheels are positioned), but the wheels were too far into the middle of the car length. We adjusted to 1/8 and 
 * 7/8 which looked better visually. 
 * WHEEL_SCALE was used to set the radii of the two wheels relative to unit. We began by testing at 0.75, and at this
 * value the wheels seemed to be an appropriate size in proportion to the car body, so we left it this way. 
 * HEAD_FRACTION is used to position the head along the length of the car. We knew that the head should appear in between
 * the two wheels, and also wanted to leave room for the windshield (we didn't want the windshield to oddly intersect
 * with the head, so we used 2/3 (a "nice" fraction that also leaves 2 other fractions (3/4 and 5/6) for the windshield) 
 * and felt this looked fine. We used the double value 0.67. 
 * HEAD_SCALE was used to set the radius of the head relative to unit. The head is described in the assignment as 
 * being smaller than the wheels, so with WHEEL_SCALE set at 0.75, we first tried 0.6, but felt that the head looked too
 * big. We then tried 0.5, and this looked more in proportion so we kept this.
 * L_WS_FRACTION, R_WS_FRACTION were determined together. We decided on 2/3 for HEAD_FRACTION with the intent that it
 * left 2 "nice" fractions (3/4 and 5/6) for where the 2 ends of the windshield are along the car length. We tested out
 * these values to make sure any further issues with the windshield position and size were minor and could be fixed by 
 * scaling factors that also further set the slope of the windshield.
 * X_WS_SCALE, Y_WS_SCALE are set the most arbitrarily (0.7 and 0.9, respectively) because they were set last, in a 
 * way that "fit together" with the remaining values that were chosen. We simply tested values starting at 0.5 for each
 * of these until we arrived at a windshield that was not oddly short or too long and didn't intersect with the
 * head or appear to have a very shallow or steep slope. The combination of 0.7 and 0.9 was the first that looked 
 * reasonable so we settled on this.
 *  
 * 
 */

public class Car extends JPanel implements MovingObject {
	
	public Car(int xPos, int yPos, double scale){
		x = xPos;
		y = yPos;
		unit = unit * scale;
	}
	
/**
 * @param dx is the change in the x position of the car
 * @param dy is the change in the y position of the car
 * moves the car by the specified amounts in the x and y directions
 */
	public void translate(int dx, int dy){
		x += dx;
		y += dy;
	}
	
	/**
	 * @param window is the Graphics window
	 * paints the car to the screen
	 */
	public void paint(Graphics window){
		super.paint(window);
		draw(window);
	}
	
	/**
	 * 
	 * @param window is the Graphics window
	 * assembles the components of the car. First we create the car body (Rectangle), wheels (circle), 
	 * driver's head (circle), and windshield (Line). Then we assemble these parts into one GeneralPath so that the
	 * entire car can be drawn at once. Then we draw the car to the screen and fill in the car body, wheels, and driver 
	 * head with the appropriate colors.
	 */
	public void draw(Graphics window){
		Rectangle2D.Double carBody = drawCarBody();
		Ellipse2D.Double leftWheel = drawLeftWheel();
		Ellipse2D.Double rightWheel = drawRightWheel();
		Ellipse2D.Double driverHead = drawHead();
		Line2D.Double windshield = drawWindShield();
		
		GeneralPath car = new GeneralPath();
		car.append(carBody, false);
		car.append(leftWheel, false);
		car.append(rightWheel, false);
		car.append(driverHead, false);
		car.append(windshield, false);
		
		Graphics2D g2D = (Graphics2D) window;
		g2D.setColor(Color.BLACK);
		g2D.fill(leftWheel);
		g2D.fill(rightWheel);
		g2D.draw(car);
		g2D.setColor(Color.BLUE);
		g2D.fill(carBody);
		g2D.setColor(Color.lightGray);
		g2D.fill(driverHead);
	}
	
	/**
	 * 
	 * @return the Rectangle representing the car's body
	 */
	private Rectangle2D.Double drawCarBody(){
		return new Rectangle2D.Double(x, y, CAR_LENGTH*unit, CAR_HEIGHT*unit);
	}
	
	/**
	 * 
	 * @return the Ellipse representing the car's left wheel (drawn as a circle)
	 * coordinates are assigned relative to the top left corner of the car body
	 */
	private Ellipse2D.Double drawLeftWheel(){
		double yPos = y + CAR_HEIGHT*unit;
		double xPos = x + (double)(CAR_LENGTH * WHEEL_SCALE*unit)*L_WHEEL_FRACTION;
		return new Ellipse2D.Double(xPos, yPos, WHEEL_SCALE*unit, WHEEL_SCALE*unit);
	}
	
	/**
	 * @return the Ellipse representing the car's right wheel (drawn as a circle)
	 * coordinates are assigned relative to the top left corner of the car body
	 */
	private Ellipse2D.Double drawRightWheel(){
		double xPos = x + (double)(CAR_LENGTH * WHEEL_SCALE*unit)*R_WHEEL_FRACTION;
		double yPos = y + CAR_HEIGHT*unit;
		return new Ellipse2D.Double(xPos, yPos, WHEEL_SCALE*unit, WHEEL_SCALE*unit);
	}
	
	/**
	 * 
	 * @return the Ellipse representing the driver's head (drawn as a circle)
	 * coordinates are assigned relative to the top left corner of the car body
	 */
	private Ellipse2D.Double drawHead(){
		double xPos = x + (double)(CAR_LENGTH * unit)*HEAD_FRACTION - HEAD_SCALE*unit;
		double yPos = y - HEAD_SCALE*unit;
		return new Ellipse2D.Double(xPos, yPos, HEAD_SCALE*unit, HEAD_SCALE*unit);
	}
	
	/**
	 * 
	 * @return the Line representing the windshield of the car
	 * coordinates are assigned relative to the top left corner of the car body
	 */
	private Line2D.Double drawWindShield(){
		double xTop = x + (double)(CAR_LENGTH * unit)*L_WS_FRACTION - X_WS_SCALE*unit;
		double yTop = y - Y_WS_SCALE*unit;
		double xBottom = x + (double)(CAR_LENGTH * unit)*R_WS_FRACTION;
		double yBottom = y;
		return new Line2D.Double(xTop, yTop, xBottom, yBottom);
	}
	
	/**
	 * x and y are the coordinates of the top left corner of the Rectangle representing the car's body. We use these
	 * coordinates to assign positions to all other components of the Car.
	 */
	private int x;
	private int y;
	private double unit = 30;
	private final int CAR_LENGTH = 3;
	private final int CAR_HEIGHT = 1;
	private final double WHEEL_SCALE = 0.75;
	private final double L_WHEEL_FRACTION = 0.125;
	private final double R_WHEEL_FRACTION = 0.875;
	private final double HEAD_FRACTION = 0.67;
	private final double HEAD_SCALE = 0.5;
	private final double X_WS_SCALE = 0.7;
	private final double Y_WS_SCALE = 0.9;
	private final double L_WS_FRACTION = 0.75;
	private final double R_WS_FRACTION = 0.83;
	
}
