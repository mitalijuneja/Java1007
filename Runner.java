package assignment1;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The RunnerRecorder class is used to run our RPSLK system.
 * Our system includes 5 other classes which are discussed below.
 * The Talker class essentially creates the sim's throw (for steps 4 and 5), processes the computer and sim's moves, and 
 * determines and displays the outcome of the game.
 * The Thrower class creates a random throw for the computer (step 1).
 * The Recorder class creates the computer's throw using a very simple AI algorithm.
 * The Revenge class attempts to create the computer's throw by determining the sim's current strategy and producing a 
 * throw to defeat it.
 * The User class handles all high level interactions with the user and displays to the console (e.g. welcoming, processing
 * a user input, displaying overall statistics, etc)
 * As with the remainder of this program, we use index 0 for rock, index 1 for paper, 2 for scissors.
 * Modified in step 3 for RPSKL with index 3 for spock and index 4 for lizard
 * In order to keep track of wins, losses, ties, we use index 0 for a win by the player (computer's opponent), index 1 for 
 * a win by the computer, and index 2 for a tie.
 *
 */
public class Runner {
	
	/**
	 * In step 4, prevComputerThrowIndex was added in order to keep track of the computer's last throw for use in the
	 * sim's reflector strategy. In step 4, NSim was added in order to set how frequently the sim switches between 
	 * the rotator and reflector strategies.
	 * In step 3, we added two additional elements to the playerMoves List to track how many times the player chooses
	 * each throw (for RPSKL we must now also keep track of the number of times spock and lizard are thrown in 
	 * addition to rock, paper, and scissors).
	 * In step 5, we eliminated the List playerMoves, because it was no longer needed for the new strategy we are now
	 * implementing. We summarize what was deleted in case we need it again at a later time.
	 * playerMoves initialized and added five 0 elements.
	 * int playerRPSIndex = gamePlayer.getPlMoveIndex();
	 * playerMoves.set(playerRPSIndex, playerMoves.get(playerRPSIndex) + 1);
	 */
	public static void main (String[] args){
		Talker gamePlayer = new Talker();
		Thrower gameComputer = new Thrower();
		User opponent = new User();
		Revenge smartComputer = new Revenge();
		
		double gameNum = 1;
		List<Integer> gameStats = new ArrayList<Integer>();
		int TOTAL_GAMES = 100000;
		char computerRPS = 'f';
		char playerRPS = 'f';
		int prevComputerThrowIndex = 0; 
		int NSim = 2;
		
		gameStats.add(0);
		gameStats.add(0);
		gameStats.add(0);
		
		opponent.welcome();
		while (gameNum <= TOTAL_GAMES){
			prevComputerThrowIndex = gameComputer.getCMoveIndex(computerRPS);
			
			playerRPS = gamePlayer.simulatedHumanStrategy(NSim, TOTAL_GAMES, (int)gameNum, prevComputerThrowIndex);
			computerRPS = smartComputer.calculateComputerMove((int) gameNum);
			smartComputer.addSimMove(playerRPS, (int) gameNum);
			
			gameStats = gamePlayer.gameOutcome(playerRPS, computerRPS,gameStats);
			gameNum++;
		}
		opponent.computeStats (gameStats.get(0), gameStats.get(1), gameStats.get(2), TOTAL_GAMES);
	}
}
