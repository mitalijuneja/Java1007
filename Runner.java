package assignment4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.event.*;


/**
 * 
 * @author Mitali Juneja (mj2944) based on courseworks runner code and java tutorials from Oracle
 * Our system contains the Car class and the RoadRace class as well as the MovingObject interface. 
 * The MovingObject interface establishes two required methods that must be implemented by any graphics object that 
 * moves. We include the MovingObject interface even though we only have one class implementing it, so that if we were to 
 * improve our system by adding additional graphics objects (birds, people, etc), we would be able to do it more easily,
 * by programming only to the MovingObject interface.
 * The Car class includes the basic methods for a single car. This includes creating the various Shape components of the
 * car, drawing the complete Car object on the screen, and moving the Car object across the screen.
 * The RoadRace class aggregates a random number of Car objects (3 to 6) and contains the methods for drawing the cars
 * to show depth (smaller cars drawn first and towards the top, larger cars drawn last and towards the bottom), moving
 * them up both large hills (as a group) and smaller hills (individually and randomly). It also creates the slider used 
 * control the cars' speeds, since it is directly related to the Car aggregate. 
 * 
 * TESTING =  (video is in the submission zip file along with the programming and theory parts)
 * For this assignment, we tested which constants to use for motion in the y direction and timing.
 * For DELAY, we initially had it set at 100, following the courseworks code fragments. However, this seemed to be too
 * fast for the small hill motions to be visible (it required setting the speed at around 2, which we felt was too small
 * of a number). We tried increasing it drastically, to 200, which seemed too slow, since it made it obvious when the cars
 * were being redrawn (motion felt choppy, rather than smooth). We then tried decrementing by 10, starting from 150. 
 * 150 and 140 still felt slightly choppy, but the greater concern was that the motion was a bit unnaturally slow. 
 * We settled at 130, since the cars now moved across the screen at a natural pace (not too slow, but not so fast that
 * the small hill motions could no longer be easily detected). Additionally, at 130, we were able to set the speed at 10, 
 * which allowed for more of a range for the slider (we ultimately chose -20 to 20, since 20 felt fast enough to be labeled
 * as a maximum speed), giving more freedom to the user while sliding.
 * For LARGE_HILL_SIZE, we initially had it set at 40, but this felt a bit too big (if we wanted each large hill to be 
 * pronounced and noticeable then a movement of 40 each time caused the cars to tend to go off the screen (vertically), 
 * which we wanted to avoid). We then decreased to 30, but ran in to the same issue, although it was now less frequent. 
 * When decreased to 20, we felt that the effect of a large hill was not as obvious and pronounced, so we settled at 25, 
 * where the cars did not tend to consistently run off the screen vertically, but the large hills were very obviously
 * present, with this frequency of shifting the car's translation between upwards and downwards every 25 times the screen
 * is redrawn.
 * For MAX_DY, we essentially tested it to fit our previously determined value of LARGE_HILL_SIZE (these quantities are
 * very related, in that each time the screen is redrawn, the car can move a maximum distance up or down of MAX_DY, and
 * the direction is switched every LARGE_HILL_SIZE times - first it moves up 25 times (going up a large hill), then it 
 * moves down 25 times (going down a large hill). Presumably, different combinations of these two values could be balanced
 * to give similar results). A MAX_DY of 10 was first tried, but this felt slightly small and didn't give the
 * best impression of a pronounced hill. Then, 15 was tried, and this felt more appropriate so we settled on this.
 * (we describe the testing of other constants in the classes where they appear)
 *
 */

public class Runner {
	public static void main(String[] args){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame myFrame = new JFrame ("race");
		final RoadRace myRace = new RoadRace();
		myRace.setBackground(Color.white);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(new BorderLayout());
		myFrame.pack();
		myFrame.setSize(screenSize);
		myFrame.getContentPane().add(myRace);
		myFrame.getContentPane().add(BorderLayout.SOUTH, myRace.getSlider());
		myFrame.getContentPane().setSize(screenSize);
		myFrame.setVisible(true);
		final int DELAY = 130;
		final int MAX_DY = 15;
		final int LARGE_HILL_SIZE = 25;
		int[] numRedraw = {0,0};
		Timer myTimer = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myRace.translate((int)(MAX_DY*Math.random()), numRedraw[1], numRedraw[0]);
				if (numRedraw[0] % LARGE_HILL_SIZE== 0){
					numRedraw[1]++;
				}
				myRace.repaint();
				numRedraw[0]++;
			}
		});
		myTimer.start();
	}
}
