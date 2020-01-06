
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Font;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * 
 * The Throw class is the outer class used to construct Throw objects according to the Builder pattern. We choose to use
 * the Builder pattern for construction because each Throw has many fields associated with it, and other than the 
 * requirements of having a throw name and associated Rectangle, there are many combinations of optional parameters
 * that can be provided. To provide more flexibility in the order for which these fields are given when constructing
 * a Throw, we choose to implement the Builder pattern. 
 * A parameter for name and an associated Rectangle are required for construction. Optionally, an initial x position, 
 * initial y position, an x speed, a y speed, a color, and a font can also be provided. The default values for these
 * parameters are 0, 0, 0, 0, Color.BLACK, and new Font("Courier", Font.BOLD, 12), respectively. The Throw objects that
 * are constructed are then aggregated by the RPSWar class for use in the Applet.
 * 
 *
 */

public class Throw {
	
	public Throw(ThrowBuilder userThrow){
		name = userThrow.name;
		rect = userThrow.rect;
		x = userThrow.x;
		y = userThrow.y;
		xSpeed = userThrow.xSpeed;
		ySpeed = userThrow.ySpeed;
		color = userThrow.color;
		font = userThrow.font;
	}
	
	/**
	 * 
	 * @param newName is the new name for the Throw
	 */
	public void setName(String newName){
		name = newName;
	}
	
	
	/**
	 * 
	 * @return the name of the Throw
	 */
	public String getName(){
		return name;
	}
	
	
	/**
	 * 
	 * @param newRect is the new Rectangle for the Throw
	 */
	public void setRectangle(Rectangle2D newRect){
		rect = newRect;
	}
	
	
	/**
	 * 
	 * @return the Rectangle of the Throw
	 */
	public Rectangle2D getRect(){
		return rect;
	}
	
	
	/**
	 * 
	 * @param newX is the new x coordinate for the Throw
	 */
	public void setX (int newX){
		x = newX;
	}
	
	
	/**
	 * 
	 * @return the x coordinate of the Throw
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * 
	 * @param newY is the new y coordinate for the Throw
	 */
	public void setY (int newY){
		y = newY;
	}
	
	
	/**
	 * 
	 * @return the y coordinate of the Throw
	 */
	public int getY(){
		return y;
	}
	
	
	/**
	 * 
	 * @return the x direction speed of the Throw
	 */
	public int getXSpeed(){
		return xSpeed;
	}
	
	
	/**
	 * 
	 * @return the y direction speed of the Throw
	 */
	public int getYSpeed(){
		return ySpeed;
	}
	
	
	/**
	 * 
	 * @return the Color used to display the text of the Throw
	 */
	public Color getColor(){
		return color;
	}
	
	
	/**
	 * 
	 * @return the Font used to display the text of the Throw
	 */
	public Font getFont(){
		return font;
	}
	
	
	/**
	 * 
	 * @author Mitali Juneja (mj2944)
	 * The ThrowBuilder class is the static inner class used to create a Throw in accordance with the Builder pattern. 
	 * It requires the throw name and its Rectangle in order to create a Throw, and optionally can take parameters 
	 * for the x and y coordinates, x and y speeds, Color, and Font. Default values for these fields are as specified in
	 * the class javadoc for the outer class Throw.
	 *
	 */
	public static class ThrowBuilder{
		
		public ThrowBuilder(String throwName, Rectangle2D rectangle){
			name = throwName;
			rect = rectangle;
			x = 0;
			y = 0;
			xSpeed = 0;
			ySpeed = 0;
			color = Color.BLACK;
			font = new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE);
		}
		
		
		/**
		 * 
		 * @param xPos is the initial x coordinate for the Throw 
		 * @return the ThrowBuilder with xPos as its x coordinate
		 */
		public ThrowBuilder x (int xPos){
			x = xPos;
			return this;
		}
		
		
		/**
		 * 
		 * @param yPos is the initial y coordinate for the Throw
		 * @return the ThrowBuilder with yPos as its y coordinate
		 */
		public ThrowBuilder y (int yPos){
			y = yPos;
			return this;
		}
		
		
		/**
		 * 
		 * @param newXSpeed is the x direction speed for the Throw
		 * @return the ThrowBuilder with newXSpeed as its x direction speed
		 */
		public ThrowBuilder xSpeed(int newXSpeed){
			xSpeed = newXSpeed;
			return this;
		}
		
		
		/**
		 * 
		 * @param newYSpeed is the y direction speed for the Throw
		 * @return the ThrowBuilder with newYSpeed as its y direction speed
		 */
		public ThrowBuilder ySpeed (int newYSpeed){
			ySpeed = newYSpeed;
			return this;
		}
		
		
		/**
		 * 
		 * @param newColor is the Color for displaying the Throw
		 * @return the ThrowBuilder with newColor as its Color
		 */
		public ThrowBuilder color(Color newColor){
			color = newColor;
			return this;
		}
		
		
		/**
		 * 
		 * @param newFont is the Font for displaying the Throw
		 * @return the ThrowBuilder with newFont as its Font
		 */
		public ThrowBuilder font(Font newFont){
			font = newFont;
			return this;
		}
		
		
		/**
		 * 
		 * @return the complete Throw object that is built, according to the required parameters and any optional 
		 * parameters the user has specified (with the default values listed in the class javadoc for outer class 
		 * Throw used if any optional parameter is not specified)
		 */
		public Throw buildThrow(){
			return new Throw(this);
		}
		
		
		
		
		private String name;
		private Rectangle2D rect;
		private int x;
		private int y;
		private int xSpeed;
		private int ySpeed;
		private Color color;
		private Font font;
		private final int DEFAULT_FONT_SIZE = 12;
		private final String DEFAULT_FONT_NAME = "Courier";
	}
	

	
	
	
	private String name;
	private Rectangle2D rect;
	private int x;
	private int y;
	private int xSpeed;
	private int ySpeed;
	private Color color;
	private Font font;
}
