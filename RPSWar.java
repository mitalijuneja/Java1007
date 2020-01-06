import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.Graphics;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import java.awt.*;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The RPSWar class controls the overall implementation of the game in the Applet. It aggregates the different components
 * (BlackHole object that stores all of the black holes and Throw[] to store all of the Throw objects, etc). 
 * A note to make here is that this class has 8 methods, which is 1 greater than the typical maximum of 7 methods.
 * However, 1 of these methods is a simple getter (getBlackHoles() ) and there are actually only 7 methods that perform
 * substantial functions.
 * Throughout this class, as well as the rest of the system, indexes are assigned to the 5 throws in the following way = 
 * 0 = rock, 1 = paper, 2 = scissors, 3 = spock, 4 = lizard
 *
 */
public class RPSWar {
	
	public RPSWar(FontRenderContext throwFontContext, String[][] throwParams){
		numThrows = throwParams.length;
		blackHoles = new BlackHole();
		throwContext = throwFontContext;
		htmlThrows = new Throw[numThrows];
		RPS_THROWS = new ArrayList<String>();
		RPS_THROWS.add("ROCK");
		RPS_THROWS.add("PAPER");
		RPS_THROWS.add("SCISSORS");
		RPS_THROWS.add("SPOCK");
		RPS_THROWS.add("LIZARD");
		for (int i = 0; i < htmlThrows.length; i++){
			String throwName = throwParams[i][0];
			Font throwFont = new Font(throwParams[i][1], Font.BOLD, Integer.parseInt(throwParams[i][2]));
			int throwIndex = RPS_THROWS.indexOf(throwName.toUpperCase());
			Throw gameThrow = new Throw
					.ThrowBuilder(throwName, throwFont.getStringBounds(throwName, throwContext))
					.x(Integer.parseInt(throwParams[i][3]))
					.y(Integer.parseInt(throwParams[i][4]))
					.xSpeed(Integer.parseInt(throwParams[i][5]))
					.ySpeed(Integer.parseInt(throwParams[i][6]))
					.color(COLORS[throwIndex])
					.font(throwFont)
					.buildThrow();
			htmlThrows[i] = gameThrow;
		}
	}
	
	
	/**
	 * 
	 * @param appletWidth is the width of the Applet
	 * @param appletHeight is the height of the Applet
	 * 
	 * Moves the Throws each time it is called (with repaint() from AppletRunner). The x and y coordinates of each Throw
	 * are moved by their x and y speeds. If a Throw happens to reach one of the borders of the Applet screen (so that
	 * it has entirely disappeared), then we move the Throw to the opposite edge of the Applet screen from the edge it 
	 * has hit (this creates the wrap-around effect). 
	 */
	public void translateThrows(int appletWidth, int appletHeight){
		for (int i = 0; i < htmlThrows.length; i ++){
			Throw gameThrow = htmlThrows[i];
			gameThrow.setX(gameThrow.getX() + gameThrow.getXSpeed());
			gameThrow.setY(gameThrow.getY() - gameThrow.getYSpeed());
			if (gameThrow.getX() + gameThrow.getRect().getWidth() < 0)
				gameThrow.setX(appletWidth);
			if (gameThrow.getY() < 0)
				gameThrow.setY(appletHeight);
			if (gameThrow.getX() > appletWidth)
				gameThrow.setX(0);
			if (gameThrow.getY() - gameThrow.getRect().getHeight() > appletHeight)
				gameThrow.setY(0);
		}
	}
	
	/**
	 * detects whether any collisions have occurred between a Throw and a black hole or between 2 Throws. 
	 */
	public void detectAllCollisions(){
		blackHoles.detectThrowCollision(htmlThrows, throwContext);
		detectRPSCollision();
	}
	
	
	/**
	 * Determines if 2 different Throws have collided. We simplify things (as suggested in the assignment) by choosing
	 * to only count an interaction as a collision if the two Throw rectangles strictly overlap or "intersect" (no
	 * question of "close enough"), which makes it extremely unlikely that more than 2 Throws collide at the exact same
	 * instant. This means that we do not have to worry about handling collisions between 3 or more Throws because
	 * 2 or more collisions will rarely occur after a single repaint() call (what actually happens is that one collision
	 * occurs, then repaint() is called, and then the next collision occurs and we handle these collisions individually).
	 */
	private void detectRPSCollision(){
		for (int i = 0; i < htmlThrows.length; i++){
			for (int j = i + 1; j < htmlThrows.length; j++){
				if (htmlThrows[i].getRect().intersects(htmlThrows[j].getRect())){
					int throwIndex1 = RPS_THROWS.indexOf(htmlThrows[i].getName().toUpperCase());
					int throwIndex2 = RPS_THROWS.indexOf(htmlThrows[j].getName().toUpperCase());
					removeLosingThrow(throwIndex1, throwIndex2, i, j);
				}	
			}
		}
	}
	
	
	
	/**
	 * 
	 * @param throwIndex1 is the index of the first Throw in RPS_THROWS (the ArrayList storing the 5 possible throws)
	 * @param throwIndex2 is the index of the second Throw in RPS_THROWS (the ArrayList storing the 5 possible throws)
	 * 
	 * We determine the RPSKL outcome between these 2 throws using the code from assignment 1
	 * To explain the algorithm used to compute the game's outcome = 
	 * in the game of RPSKL, the first Throw wins if throw1Index - throw2Index = 1, 3, -2, or -4 (1 mod 5 or 3 mod 5)
	 * in the game of RPSKL, the second Throw wins if throw1Index - throw2Index = 2, 4, -1, or -3 (2 mod 5 or 4 mod 5)
	 * (and ties occur when throw1Index - throw2Index, but since in this case, both throws should survive, we don't
	 * have to do anything in response to this)
	 * A note about java's mod calculations = java calculates mod of a negative number by providing the negative answer 
	 * when we want the positive one for this algorithm to work 
	 * To prevent errors because of this, we add 5 to our result so that we will always 
	 * be performing the modulus on a positive number and therefore get the positive result of the modulus operation
	 * 
	 * The losing Throw is "destroyed", which is done by setting the name of the Throw to "" so that nothing appears on
	 * the screen. We then update the Throw's Rectangle based on this, so that it is now essentially a line that cannot
	 * "intersect" with anything (it no longer affects other Throws or black holes)  
	 */
	
	private void removeLosingThrow(int throwIndex1, int throwIndex2, int throwNum1, int throwNum2){
		int indexDiff = throwIndex1 - throwIndex2;
		indexDiff = indexDiff + NUM_POSSIBLE_THROWS;
		if (indexDiff % NUM_POSSIBLE_THROWS == 1 || indexDiff % NUM_POSSIBLE_THROWS== 3){
			htmlThrows[throwNum2].setName("");
			htmlThrows[throwNum2].setRectangle(htmlThrows[throwNum2].getFont().getStringBounds(
					htmlThrows[throwNum2].getName(), throwContext));
		}
		if (indexDiff % NUM_POSSIBLE_THROWS == 2 || indexDiff % NUM_POSSIBLE_THROWS == 4){
			htmlThrows[throwNum1].setName("");
			htmlThrows[throwNum1].setRectangle(htmlThrows[throwIndex2].getFont().getStringBounds(
					htmlThrows[throwNum1].getName(), throwContext));
		}
	}
	
	
	
	/**
	 * 
	 * @param g is the Graphics object the Applet is using for display
	 * 
	 * This method is called by paint() in AppletRunner because it helps redraw the Applet screen and refresh information
	 * about the objects on the Applet screen
	 * First, we update the coordinates of the Rectangles for each of the Throws. These updated coordinates are
	 * then used to detect any collisions that are occurring on the screen, either between a Throw and a black hole or
	 * 2 Throws. Then, we redraw the Throws and each black hole. 
	 * 
	 */
	public void updateWar(Graphics g){
		updateThrowCoords();
		detectAllCollisions();
		drawThrows(g);
		blackHoles.drawBlackHoles(g);
	}
	
	
	/**
	 * Updates the coordinates of the Rectangles for each of the Throws After they have been translated based 
	 * on their x and y speeds by translateThrows(). The new x and y coordinates are used to update the position
	 * of the Rectangle that bounds the Throw string on the Applet screen. 
	 */
	private void updateThrowCoords(){
		for (int i = 0; i < htmlThrows.length; i++){
			Throw gameThrow = htmlThrows[i];
			Rectangle2D.Double newThrowRect = new Rectangle2D.Double(gameThrow.getX(), gameThrow.getY(), gameThrow.getRect().getWidth(), gameThrow.getRect().getHeight());
			htmlThrows[i].setRectangle(newThrowRect);
		}
	}
	
	
	/**
	 * 
	 * @param g is the Graphics object the Applet is using for display
	 * 
	 * Draws each of the Throws on the screen. First the font is set for each throw. Then the paintbrush is set
	 * to the color for each throw. Finally, the throw name is drawn at its corresponding location (x,y point)
	 */
	private void drawThrows(Graphics g){
		for (int i = 0; i < numThrows; i++){
			g.setFont(htmlThrows[i].getFont());
			Throw gameThrow = htmlThrows[i];
			g.setColor(gameThrow.getColor());
			g.drawString(gameThrow.getName(), (int)gameThrow.getRect().getX(), (int)(gameThrow.getRect().getY() + gameThrow.getRect().getHeight()));
		}
	}
	
	
	/**
	 * 
	 * @return the BlackHole object that contains the black holes and their implementation
	 */
	public BlackHole getBlackHoles(){
		return blackHoles;
	}
	
	/**
	 * We use an array to store both the Throw objects and the different Colors for each Throw object because we know 
	 * exactly how many colors and throws there are (there are numThrows number of throws and 5 colors - one for each
	 * of the RPSKL throws), so we do not require a data structure with dynamic sizing. 
	 * 
	 * We choose to standardize the Colors for each Throw rather than allow them to be specified in the html file. They
	 * are chosen so that each color is related to the Throw it corresponds with (rock = dark gray, paper = light blue
	 * which is meant to function as a white color that is reminiscent of paper, scissors = red, spock = blue,
	 * lizard = green). 
	 */
	
	private final int NUM_POSSIBLE_THROWS = 5;
	private int numThrows;
	private Throw[] htmlThrows;
	private BlackHole blackHoles;
	private FontRenderContext throwContext;
	private final Color[] COLORS = {new Color (82, 85, 90), new Color(229, 232, 239), new Color(218, 46, 51), 
			new Color(28, 126, 219), new Color(22, 159, 36)};
	private final ArrayList<String> RPS_THROWS;
	

}
