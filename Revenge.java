package assignment1;

import java.util.List;
import java.util.ArrayList;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Revenge class implements the revenge strategy for part 5. It is designed as a subclass of Thrower since
 * it relies on the basic functionalities of Thrower but enhances them to better compete with the advanced strategies
 * of the sim added in step 4. 
 * The revenge strategy implemented here involves the computer tracking the last x throws of the sim (where x = the 
 * number of possible throws), obtained from the Talker class. The system already knows that the sim's two strategies 
 * are rotator and reflector and uses this information, as well as the sim's last n throws to determine the strategy the 
 * sim is using and fights it. With this strategy, the system is only not able to determine the sim's strategy when the sim 
 * has used the rotator strategy for less than x rounds. As a result, this system would not work that well for a sim that switches 
 * between its strategies very frequently (i.e. for values of N that are less than x). In step 4, we were satisfied with
 * the sim's performance with N = 2, so we see this as a concern. However, during testing, we observed that this fact
 * does not prevent the computer from consistently being able to defeat the sim. 
 * The computer's throw is then used by the Talker class to compute and display a summary of the outcome of the game.
 * In general with these assumptions, the system only fails to detect a strategy when the system has recently switched
 * to the rotator strategy from the reflector strategy, but this occurs with low enough frequency that it does not
 * prevent the computer's revenge strategy from being successful.
 * The final limitation to this strategy is that the computer cannot begin to implement the revenge strategy until there
 * have been at least x rounds played (so that it can collect enough data on the sim's previous throws for its strategies
 * to be of use. We are able to do this now, since we are using x = 5 and playing for a large enough number of games that this 
 * becomes negligible. We would revisit this strategy and rethink our algorithm if x were to be increased substantially,
 * to the point where this would hinder the computer's overall performance.
 * TESTING = We tested our computer's revenge strategy against our step 4 sim playing with its optimal value (N = 2) and 
 * found that our revenge strategy increased the computer's win percentage about 9% from around 36% to about 45.5%. We also
 * saw that the computer was able to defeat the sim quite consistently with its new strategy, with a win percentage typically 
 * 9.5% higher than the sim's. Thus, we see that our computer is now much more capable of taking on the sim. 
 */
public class Revenge extends Thrower {
	
	public Revenge (){
		simPrevMoves = new char[NUM_POSSIBLE_MOVES];
		computerPrevMove = 'f';
		computerRPS = 'f';
		winMoves = new ArrayList<Character>();
		
		winMoves.add('p');
		winMoves.add('s');
		winMoves.add('k');
		winMoves.add('l');
		winMoves.add('r');
	}
	
	
	/**
	 * 
	 * @param simThrow is the sim's throw to be added to the array simPrevMoves
	 * @param gameNum is the number of the game currently being played
	 * we insert the simThrow into the simPrevMoves array. The index it is inserted is computed so that
	 * once the array has been "filled up" (filled to the end), the next move is added at index 0, then index 1, etc
	 * allowing the throws to cycle through the array until they are no longer in the last x moves (at which point
	 * we overwrite them)
	 */
	public void addSimMove(char simThrow, int gameNum ){
		simPrevMoves[(gameNum - 1)%NUM_POSSIBLE_MOVES] = simThrow;
	}
	
	
	/**
	 * 
	 * @param gameNum is the number of the game currently being played
	 * @return the computer's ideal move ('r', 'p', 's', 'l', or 'k')
	 * for the first x games, the computer simply collects information about the sim's throws but cannot use this
	 * information successfully to implement any sort of strategy. As a result, the computer's first x throws will be 
	 * determined randomly. After the first x games the computer implements the following strategy = 
	 * if the sim is clearly rotating, then the computer selects the throw that beats the next throw in the sim's 
	 * rotating sequence. Otherwise, if the sim is clearly reflecting (i.e. playing the computer's previous move),
	 * then the computer selects the throw that beats its previous throw (which will be the sim's next throw). If the sim's 
	 * current strategy is not plainly obvious, then the computer has no choice but to select its throw randomly.
	 */
	public char calculateComputerMove(int gameNum){
		computerPrevMove = computerRPS;
		if (gameNum <= NUM_POSSIBLE_MOVES){
			computerRPS = computerTurnRandom();
		}
		else{
			if(isRotating()){
				int simLastThrowIndex = moves.indexOf(simPrevMoves[(gameNum - 1) % NUM_POSSIBLE_MOVES]);
				computerRPS = winMoves.get((simLastThrowIndex) % 5);
			}
			else if (computerPrevMove == simPrevMoves[NUM_POSSIBLE_MOVES - 2]){
				int reflectorIndex = moves.indexOf(simPrevMoves[NUM_POSSIBLE_MOVES - 2]);
				computerRPS = winMoves.get(reflectorIndex);
			}
			else{
				computerRPS = computerTurnRandom();
			}
		}
		
		return computerRPS;
	}
	
	
	/**
	 * 
	 * @return true if the system can clearly detect that the sim is rotating; false if it cannot
	 * this judges, on a very simple level, if the sim is rotating by examining the last x moves of the sim. If each has
	 * frequency of 1 (occurs just once in the array) then generally speaking, we can safely say that the sim
	 * is using the rotator strategy. for more complicated cases when the sim has only recently switched to the 
	 * rotator strategy and this fact is not necessarily be true, we are unable to detect the sim's rotation, but as explained
	 * above, this does not occur often enough to hinder any success of the revenge strategy
	 */
	private boolean isRotating(){
		for (int i = 0; i < moves.size(); i++){
			int characterCount = 0;
			for (int j = 0; j < simPrevMoves.length; j++){
				if (moves.get(i) == simPrevMoves[j]){
					characterCount++;
				}
			}
			if (characterCount != 1){
				return false;
			}
		}
		return true;	
	}
	
	
	/**
	 * We use an array simPrevMoves to organize the last n moves of the sim (n = number of possible throws) because we 
	 * will always be interested in looking at this number of the sim's moves (i.e. we do not require a data structure that 
	 * involves a dynamic size)
	 */
	private char[] simPrevMoves;
	private char computerPrevMove;
	private char computerRPS;
	private ArrayList<Character> winMoves;
	

}
