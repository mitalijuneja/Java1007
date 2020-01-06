

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;


/**
 * 
 * @author Mitali Juneja (mj2944) based on courseworks code fragments (Playground.java)
 * 
 * Our system consists of 3 classes, the BlackHole class, the Throw class, and the RPSWar class. 
 * The BlackHole class contains the BlackHoles that are created upon a mouse click and the methods concerning their
 * interactions with the Throws and display on the Applet screen.
 * The Throw class constructs Throw objects using the Builder patter. Each Throw object has 7 fields associated with
 * it = name, x coordinate, y coordinate, x direction speed, y direction speed, Color, Font. 
 * The RPSWar class brings together an aggregation of Throw objects as well as an instance of BlackHole to create the
 * overall game and handles the methods that deal with interactions between all the elements of the game (collisions, 
 * movement, drawing, etc). 
 * When indexes are assigned to each of the 5 throws in the system, they are assigned with 0 = rock, 1 = paper, 
 * 2 = scissors, 3 = spock, and 4 = lizard.
 * The file AppletParams.html contains the parameters that are used for running the system (although to run the applet
 * directly from Eclipse, this file was not needed and these parameters were simply input through run configurations
 * for this file). 
 * Conventions for parameters - take i to be the number of the Throw, beginning at 1 moving sequentially to the number
 * of throws (while in the program i is 1 greater than the index of the Throw in the Throw[]). "throw" is used to refer
 * to the name of the throw (so use "throw1" to give the name of the first throw). "x" gives the x position, "y" gives 
 * the y position, "xSpeed" gives the x direction speed, "ySpeed" gives the y direction speed).
 * Speeds are given as either -1, 0, or 1. 0 corresponds to no movement in that direction. 1 corresponds to North (y) or 
 * East (x) and -1 corresponds to South (y) or West(x). 
 * The parameters and their corresponding values (used in video for testing) are also listed below = 
 *  <applet code="AppletRunner.class" width="500" height="500">
 *	<param name="numThrows" value="7" />
 *	
 *	<param name="throw1" value="rOCk" />
 *	<param name="throw2" value="rOCk" />
 *	<param name="throw3" value="paper" />
 *	<param name="throw4" value="paper" />
 *	<param name="throw5" value="sciSSors" />
 *	<param name="throw6" value="SPOCK" />
 *	<param name="throw7" value="LiZaRd" />
 *
 *	<param name="fontname" value="Courier" />
 *	<param name="fontsize" value="20" />
 *
 *	<param name="delay" value="100" />
 *
 *	<param name="xPos1" value="10" />
 *	<param name="yPos1" value="450" />
 *
 *	<param name="xPos2" value="75" />
 *	<param name="yPos2" value="450" />
 *
 *	<param name="xPos3" value="150" />
 *	<param name="yPos3" value="20" />
 *
 *	<param name="xPos4" value="300" />
 *	<param name="yPos4" value="10" />
 *
 *	<param name="xPos5" value="50" />
 *	<param name="yPos5" value="100" />
 *
 *	<param name="xPos6" value="150" />
 *	<param name="yPos6" value="60" />
 *
 *	<param name="xPos7" value="400" />
 *	<param name="yPos7" value="420" />
 *
 *	<param name="xSpeed1" value="1" />
 *	<param name="ySpeed1" value="0" /> 
 *
 *	<param name="xSpeed2" value="-1" />
 *	<param name="ySpeed2" value="0" />
 *
 *	<param name="xSpeed3" value="-1" />
 *	<param name="ySpeed3" value="-1" />
 *
 *	<param name="xSpeed4" value="1" />
 *	<param name="ySpeed4" value="-1" />
 *
 *	<param name="xSpeed5" value="1" />
 *	<param name="ySpeed5" value="1" />
 *
 *	<param name="xSpeed6" value="-1" />
 *	<param name="ySpeed6" value="0" />
 *
 *	<param name="xSpeed7" value="1" />
 *	<param name="ySpeed7" value="-1" />
 *
 *	</applet>
 *
 *
 */

public class AppletRunner extends Applet {
	
	/**
	 * Method calls and idioms for applet initialization. 
	 * As stated below in the field javadoc for htmlParams, we use this 2D array to systematically provide all the
	 * information about parameters for each Throw to RPSWar, which aggregates and constructs the throws. Each array
	 * within htmlParams has length 7 since there are 7 parameters specified in the html file that correspond to each 
	 * Throw. The 7 elements in each of these arrays are organized so that index 0 = throw name, 1 = font name, 2 = font
	 * size, 3 = x coordinate, 4 = y coordinate, 5 = x direction speed, 6 = y direction speed. 
	 */
	public void init() {
		System.out.println("Mitali Juneja (mj2944)");
		numThrows = Integer.parseInt(getParameter("numThrows"));
		htmlParams = new String[numThrows][NUM_HTML_PARAMS];
		for (int i = 1; i <= htmlParams.length; i++){
			htmlParams[i - 1][0] = getParameter("throw" + i);
			htmlParams[i - 1][1] = getParameter("fontname");
			htmlParams[i - 1][2] = getParameter("fontsize");
			htmlParams[i - 1][3] = getParameter("xPos" + i);
			htmlParams[i - 1][4] = getParameter("yPos" + i);
			htmlParams[i - 1][5] = getParameter("xSpeed" + i);
			htmlParams[i - 1][6] = getParameter("ySpeed" + i);
		}
		Graphics2D g2D = (Graphics2D) getGraphics();
		throwContext = g2D.getFontRenderContext();
		myRPSWar = new RPSWar(throwContext, htmlParams);
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				myRPSWar.getBlackHoles().mouseClicked(e);
			}
		});
		htmlDelay = Integer.parseInt(getParameter("delay"));
		appletTimer = new Timer(htmlDelay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myRPSWar.translateThrows(getWidth(), getHeight());
				repaint();
			}
		});
	}

	
	
	/**
	 * start method for Applet, starts the timer
	 */
	public void start() {
		appletTimer.start();
	}

	
	/**
	 * paint method for Applet, which displays updates to the Applet screen
	 */
	public void paint(Graphics g) {
		myRPSWar.updateWar(g);
	}

	
	/**
	 * stop method for Applet, stops the timer
	 */
	public void stop() {
		appletTimer.stop();
	}

	
	/**
	 * destroy method for Applet
	 */
	public void destroy() {
	}
	
	

	
	/**
	 * We use a 2D array of Strings to store all of the parameters from the html file. This 2D array is then used when
	 * constructing an instance of RPSWar to ensure that all Throws are appropriately created. We do this because it is 
	 * a systematic way to provide all of the relevant html parameters related to Throw from this class (which acts a 
	 * Runner) to RPSWar, which takes these parameters and uses them. The arrays within htmlParams are ordered
	 * in the same way as the throws are in the Throw[] htmlThrows in RPSWar (so index 0 has all the parameters that 
	 * are used to construct htmlThrows[0], index 1 has parameters for htmlThrows[1], etc). Each array in htmlParams has
	 * length 7 (since there are 7 parameters in the html file corresponding to a single throw). Index 0 = name, 1 = font
	 * name, 2 = font size, 3 = x coordinate, 4 = y coordinate, 5 = x direction speed, 6 = y direction speed. 
	 */
	private int numThrows;
	private final int NUM_HTML_PARAMS = 7;
	private int htmlDelay;
	private Timer appletTimer;
	private FontRenderContext throwContext;
	private String[][] htmlParams; 
	private RPSWar myRPSWar;
	
}
