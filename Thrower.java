package assignment1;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Thrower class is used in order to randomly generate the computer's throw. It generates a random integer, which
 * corresponds to the index of one of the possible throws. This throw is then used by the Talker class to compute and display 
 * the a summary of the outcome of the game.
 */

public class Thrower {
	/**
	 * as with the remainder of the program, rock is indexed as 0, paper as 1, and scissors as 2
	 * Modified in step 3 for RPSKL by adding new indices of 3 for spock and 4 for lizard
	 */
	public Thrower(){
		moves = new ArrayList <Character>();
		moves.add('r');
		moves.add('p');
		moves.add('s');
		moves.add('k');
		moves.add('l');
	}
	
	/**
	 * 
	 * @return the character ('r', 'p', 's', 'k', 'l') corresponding to the computer's throw
	 * in order to generate a random throw for the computer to play, we create a random integer between 0 and x (the number
	 * of possible moves). This integer corresponds to the index of a move in the ArrayList moves that holds all of the 
	 * possible throws, allowing us to determine the computer's throw
	 */
	public char computerTurnRandom(){
		int moveIndex = (int) (NUM_POSSIBLE_MOVES*Math.random());

		return moves.get(moveIndex);
		
	}
	
	
	/**
	 * 
	 * @param cMove is the character corresponding to the computer's throw
	 * @return the index corresponding to the computer's throw
	 */
	public int getCMoveIndex(char cMove){
		int cMoveIndex = moves.indexOf(cMove);
		
		return cMoveIndex;
	}
	
	
	/**
	 * for RPS, there are 3 possible throws (rock, paper, and scissors) that can be played.
	 * Modified in step 3 for RPSLK in which there are now 5 possible throws (rock, paper, scissors, spock, lizard) that
	 * can be played.
	 */
	public int NUM_POSSIBLE_MOVES = 5;
	
	/**
	 * As with the remainder of the program, in the List moves, we have index 0 representing rock, index 1 for paper,
	 * and index 2 for scissors.
	 * Modified in step 3 for RPSLK by adding index 3 for spock and index 4 for lizard.
	 */
	public ArrayList<Character> moves;

}
