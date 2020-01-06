package assignment4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The RoadRace class assembles together many Cars into a "road race" that moves together. The cars are aggregated using
 * an ArrayList, since the number of cars is variable (randomly determined, but always 3 to 6). They are arranged on the 
 * screen in 3 rows. In general, we put smaller cars towards the top of the screen and draw them first (behind) so that
 * they appear slightly farther away. We put larger cars towards the bottom of the screen and draw them last (in front) 
 * so that they appear slightly closer up. All cars used are scaled by a factor from 0.5 to 2.0. Their motions are 
 * related yet separate. Together, they move across the screen (in the x direction) at the speed set by the slider
 * (which we choose to put in this class, since it is related to and controls this group of cars as a whole). They 
 * also, generally, move up and down larger hills in the y direction. However, they cars move up and down smaller hills
 * in the x direction independently.
 * A note to make here is that this class has 12 fields, which is greater than the typical maximum of 7. However, 7 of
 * these fields are final constants, that are used for scaling and constructing the cars on the screen and establishing 
 * their common motion across the screen. We choose to avoid the use of magic numbers (for clarity) at the cost of having
 * more final fields. 
 * Also, this class has 8 methods, one more than the typical maximum of 7. However, one of these is a simple getter
 * for the slider that controls the cars' speeds so there are only 7 methods that perform actual functions. We choose
 * to have one extra method and keep the slider private rather than make it public and have only 7 methods.
 * TESTING = 
 * We tested which constants to use for scaling and speed.
 * We set MAX_NUM_CARS to 6 (the assignment specifies 3 to 7, we wanted to create a grid pattern, which required more
 * divisibility from the maximum car number than 7, so we shifted our maximum for this system to 6. This allowed us, 
 * naturally, to create 3 rows of cars, that each had either 1 or 2 cars in them).
 * The assignment specified that the cars should be 0.5 to 2.0 times the unit set in Car, so we set MIN_SCALE and 
 * MAX_SCALE accordingly.
 * MAX_SPEED was set based on our previously set constant of DELAY in Runner. Once we settled on DELAY = 130, we noticed
 * that we were able to increase the speed values we were using. Although 20 felt fast, beyond this felt unreasonable so
 * we settled at a maximum of 20. 
 * For DEFAULT_SPEED, we observed the motion of the cars at 15 first, which felt a bit fast for a default value, however
 * 10 made it a bit easier to observe the details in the cars' motions (up big and small hills).
 * MAX_Y and MIN_Y were chosen based on our previously set constants of LARGE_HILL_SIZE and MAX_DY in Runner. Any initial 
 * y positions less than around 80 made the top car run off the screen vertically quite consistently. We added 10 to this
 * just to be sure we could avoid this as much as possible. Similarly, any y positions more than 500 made the bottom car
 * start to do the same, so we again subtracted 10 from this to be sure, arriving at MIN_Y = 90 AND MAX_Y = 490.
 *
 */
public class RoadRace extends JPanel{
	
	public RoadRace(){
		cars = new ArrayList<MovingObject>();
		scales = new ArrayList<Double>();
		yPos = new ArrayList<Integer>();
		arrangeCars();
		createSlider();
	}
	
	/**
	 * arranges the cars on the screen in a variation of a grid pattern (there will always be 3 general rows of
	 * cars, but the number of cars in each row is either 1 or 2 (keeps the total number of cars between 3 and 6) and
	 * the y coordinate of each car is chosen randomly). The cars are laid out so that the cars closer to the bottom of
	 * the screen are larger and those towards the top are smaller (for depth perception). For this, we use the ArrayLists
	 * storing the scaling factors (scales) and y coordinates (yPos), arranged in descending order. We construct the cars
	 * row by row, starting from those towards the bottom of the screen (largest y coordinate and largest size) up to the
	 * top of the screen (smallest y coordinate and smallest size). Then we reverse the order of the List so that the
	 * cars that are smaller and higher up (behind) get drawn first and those that are larger and lower down (in front) 
	 * get drawn last for better depth effects.
	 */
	private void arrangeCars(){
		createScales();
		createYPos();
		final int NUM_ROWS = 3;
		final int MAX_CARS_IN_ROW = 2;
		final int X_SPACING = 100;
		for (int i = 0; i < NUM_ROWS; i++){
			int numCarsInRow = (int)(Math.random()*MAX_CARS_IN_ROW + 1);
			for (int j = 0; j < numCarsInRow; j++){
				cars.add(new Car(X_SPACING*(j+1), yPos.get(j + i), scales.get(j + i)));
			}
		}
		Collections.reverse(cars);
	}
	
	/**
	 * Fills up the ArrayList storing scaling factors (scales) for all the cars. Since there can be a maximum of 6 cars
	 * created on the screen, it creates 6 different scaling factors. These scaling factors are all between 0.5 and 2.0.
	 * Then, the scaling factors are sorted in descending order so that they can be accessed for use in arrangeCars().
	 */
	private void createScales(){
		for (int i = 0; i < MAX_NUM_CARS; i++){
			double scale = (MAX_SCALE - MIN_SCALE)*Math.random() + MIN_SCALE;
			scales.add(scale);
		}
		Collections.sort(scales, Collections.reverseOrder());
	}
	
	/**
	 * Fills up the ArrayList storing y coordinates (yPos) for all the cars. Since there can be a maximum of 6 cars 
	 * created on the screen, it creates 6 different y coordinates. These coordinates are all between 90 and 490 (to
	 * ensure that during car motion up and down hills, the cars don't go far off the edges of the screen). Then, the y 
	 * coordinates are sorted in descending order so that they can be accessed for use in arrangeCars().
	 */
	private void createYPos(){
		for (int i = 0; i < MAX_NUM_CARS; i++){
			int y = (int)(MIN_Y + (MAX_Y - MIN_Y)*Math.random());
			yPos.add(y);
		}
		Collections.sort(yPos, Collections.reverseOrder());
	}
	
	/**
	 * creates the slider that controls the speeds of the cars. The slider is oriented horizontally, with a maximum speed
	 * of 20 and a minimum speed of -20. Its default location is set at 10, and its spacing is also set, with tick marks
	 * and speed values visible. Since the slider controls speed in only the x direction, this represents dx. We have to
	 * use an array of size 1 to represent dx so that its value can be adjusted in the anonymous class for the change
	 * listener (we want to get the new slider value and set it as dx, however, the anonymous class only allows the use
	 * of final of effectively final variables, so to get around this restriction we use an array of size 1). 
	 * The change listener gets the new value the slider is set at and uses it as dx (how much the car moves 
	 * in the x direction each time the screen is repainted, or the "speed").  
	 */
	private void createSlider(){
		mySlider = new JSlider(JSlider.HORIZONTAL, -1*MAX_SPEED, MAX_SPEED, DEFAULT_SPEED);
		dx = new int[1];
		dx[0] = mySlider.getValue();
		int majorSpacing = MAX_SPEED/DEFAULT_SPEED;
		int minorSpacing = majorSpacing/2;
		mySlider.setMajorTickSpacing(majorSpacing);
		mySlider.setMinorTickSpacing(minorSpacing);
		mySlider.setPaintTicks(true);
		mySlider.setPaintLabels(true);
		mySlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider speedValue = (JSlider) event.getSource();
				if (!speedValue.getValueIsAdjusting()){
					dx[0] = (int) speedValue.getValue();
				}
			}
		});
	}
	
	
	/**
	 * 
	 * @param window is the Graphics window
	 * draws each of the cars on the screen
	 */
	private void drawCars(Graphics window){
		for (int i= 0; i < cars.size(); i++){
			cars.get(i).draw(window);
		}
	}
	

	/**
	 * 
	 * @param dy is the change in the car's y position from the large hill that all cars go over
	 * @param signY is num/25 (integer division)  
	 * @param num is the number of times the screen has been redrawn already
	 * We use num to compute whether a car is going up or down a small hill (this alternates every time the screen is
	 * redrawn - first a car goes up a small hill then it comes back down).
	 * We use sign to calculate whether the cars are going up or down the large hills (every 25 times the screen is 
	 * redrawn, we increment sign and when sign is even, the cars go up the large hill and when sign is odd, the cars go
	 * down the large hill - basically, this means that every 50 times the screen is redrawn, the cars traverse one large
	 * hill (they first go up it and then go down it). 
	 */
	public void translate(int dy, int signY, int num){
		for (int i = 0; i < cars.size(); i++){
			int smallHill = 0;
			int smHillFreq = 3;
			final int MIN_SM_HILL_SIZE = 1;
			final int MAX_SM_HILL_SIZE = 11;
			int isSmallHill = (int)(Math.random() *smHillFreq);
			if (isSmallHill == 1){
				smallHill = (int)(Math.random()*(MAX_SM_HILL_SIZE - MIN_SM_HILL_SIZE) + MIN_SM_HILL_SIZE);
			}
			if (num % 2 == 0){
				smallHill = -1*smallHill;
			}
			if (signY % 2 == 0){
				cars.get(i).translate(dx[0], -dy + smallHill);
			}
			else{
				cars.get(i).translate(dx[0], dy + smallHill);
			}
		}
	}
	
	
	/**
	 * @param window is the Graphics window
	 * paints all of the cars on the screen
	 */
	public void paint(Graphics window){
		super.paint(window);
		drawCars(window);
	}
	
	public JSlider getSlider(){
		return mySlider;
	}
	
	private ArrayList<MovingObject> cars;
	private int[] dx;
	private JSlider mySlider;
	private ArrayList<Double> scales;
	private ArrayList<Integer> yPos;
	private final int MAX_NUM_CARS = 6;
	private final double MIN_SCALE = 0.5;
	private final double MAX_SCALE = 2.0;
	private final int MAX_SPEED = 20;
	private final int DEFAULT_SPEED = 10;
	private final int MIN_Y = 90;
	private final int MAX_Y = 490; 
}
