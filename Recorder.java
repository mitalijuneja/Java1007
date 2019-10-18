package assignment1;

import java.util.List;
import java.util.ArrayList;

import java.lang.Math;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Recorder class creates a simple AI approach to allow the computer to more logically select its move.
 * Instead of a random approach, the computer uses the number of times that the player (its opponent) has selected each throw 
 * so far to compute the percent of the time that the player has chosen each throw. These percentages are used as weights to
 * allow a randomly generated number to correspond to a throw based on a weighted average approach (unequal spectrum used to assign 
 * each integer to a throw) as opposed to a purely random approach (equal spectrum used to assign each integer to a throw). 
 * This means the computer is more likely to play a move that defeats a player who chooses its throws predictably. This
 * throw is then used by the Talker class in order to compute and display a summary of the outcome of the game.
 */

public class Recorder {
	
	public Recorder(){
		plMoveCounts = new ArrayList<Integer>();
		plMoveCounts.add(0);
		plMoveCounts.add(0);
		plMoveCounts.add(0);
		plMoveCounts.add(0);
		plMoveCounts.add(0);
	}
	
	/**
	 * 
	 * @param plMoves is the List that tracks the number of times the player has selected each throw
	 * we first extract these counts from the list and then use them to calculate the percent of the time that the computer
	 * should select the throw that defeats each of these (this is the calculation of the weights)
	 * Modified in step 3 for RPSKL by adding additional variables kCount and lCount to count how many times the player
	 * has thrown lizard and spock. We also take this into account when computing the player's total number of throws. 
	 * We now also calculate weights for spock and lizard. We also modify our computation of the weights. 
	 * Since there are now 2 throws that a given throw can defeat (eg. a throw of paper by the computer can now 
	 * beat a throw of rock or spock by the player), we take these two throws into account in our computation of the weights.
	 */
	
	private void calculateWeights(List<Integer> plMoves){
		double rCount = plMoves.get(0);
		double pCount = plMoves.get(1);
		double sCount = plMoves.get(2);
		double kCount = plMoves.get(3);
		double lCount = plMoves.get(4);
		
		double total = rCount + pCount + sCount + kCount + lCount;
		
		pWeight = ((rCount + kCount)/total)*100;
		sWeight = ((pCount + lCount)/total)*100;
		rWeight = ((sCount + lCount)/total)*100;
		lWeight = ((kCount + pCount)/total)*100;
		kWeight = ((sCount + rCount)/total)*100;
	}
	
	
	/**
	 * 
	 * @param plMoves is the List that tracks the number of times the player has selected each throw
	 * @return the computer's ideal move ('r', 'p', 's', 'l', or 'k')
	 * After calculating the weights, we generate a random integer from 1 to 100. This integer is then converted to
	 * an ideal throw for the computer using a weighted average scheme in which throws that defeat more frequently
	 * chosen moves of the player will be more likely to be selected for the computer to play
	 * Modified in step 3 for RPSKL by adding to additional else if statements to take into account the weights
	 * for lizard and spock
	 */
	public char computerTurnEnhnc(List<Integer> plMoves){
		calculateWeights(plMoves);
		
		int random = (int) (100*Math.random() + 1);
		if (random < pWeight){
			return 'p';
		}
		else if (random < pWeight + sWeight){
			return 's';
		}
		else if (random < pWeight + sWeight + lWeight){
			return 'l';
		}
		else if (random < pWeight + sWeight + lWeight + kWeight){
			return 'k';
		}
		return 'r';
		
	}
	
	

	/**
	 * the List plMoveCounts counts the number of times the computer's opponent (player) chooses each throw. Consistent with
	 * the rest of the system, index 0 corresponds to the number of times rock is played, index 1 for paper, and index 2
	 * for scissors.
	 * Modified in step 3 for RPSLK to add index 3 for spock and index 4 for lizard.
	 */
	private ArrayList<Integer> plMoveCounts;
	/**
	 * Since there are a fixed and relatively small (3 or 5) number of throws possible, we organize their weights
	 * simply as distinct fields. If we were to extend this to a much larger number of possible throws, we would
	 * rethink this structure and possibly organize them into an array or array list.
	 * Modified in step 3 for RPSLK to add additional weights for spock (kWeight) and lizard (lWeight)
	 */
	private double pWeight;
	private double sWeight;
	private double rWeight;
	private double kWeight;
	private double lWeight;

}
