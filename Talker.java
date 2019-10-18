package assignment1;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Talker class generates a throw if the sim is playing, by using either the rotator or reflector strategy with a given value 
 * of N. Using this throw (or a human player's input throw, retrieved from the User class) and the computer's throw, 
 * it then computes the outcome of the game (this is discussed in the method comment for gameOutcome in detail)  and displays it.
 * As the Talker class determines the outcome of each game, it updates the counts of each outcome in an List that is
 * ultimately used by the User class to calculate overall statistics at the end of the game.
 * After testing, we found that the optimal value of N is 2.
 * TESTING = to find the optimal value of N in step 4. We tested 18 values for N, starting at the maximum value (where
 * N = the number of total games played = 100,000), working our way down to the minimum, N = 1. We found that the maximum
 * win percentage for the sim was achieved in our first test of N = 100,000, with the sim winning 39.668% of the time. However,
 * this still resulted in the computer winning more games overall, so we looked towards lower values of N in hopes of
 * finding a value for N that both maximized the sim's win percentage while also allowing the sim to defeat the computer.
 * We found this at N = 2, which allowed the sim to win 36.726% of the time, which was also more than the computer's win 
 * percentage.
 */
public class Talker {
	
	/**
	 * We initialize NUM_POSSIBLE_MOVES to 3 because there are 3 possible throws (rock, paper, scissors).
	 * Modified in step 3 for RPSLK with NUM_POSSIBLE_MOVES now set to 5 for 5 possible throws (addition of lizard and spock).
	 * We initialize rotatorIndex to 0 because the sim always begins with the rotator strategy and always throws rock (index 
	 * 0) first.
	 * As with the rest of the program, indices are designated as 0 = rock, 1 = paper, 2 = lizard, 3 = spock, and 4 = lizard
	 */
	public Talker(){
		moveCodes = new ArrayList<Character>();
		moveWords = new ArrayList<String>();
		NUM_POSSIBLE_MOVES = 5; 
		rotatorIndex = 0; 
		
		moveCodes.add('r');
		moveCodes.add('p');
		moveCodes.add('s');
		moveCodes.add('k');
		moveCodes.add('l');
		
		moveWords.add("rock");
		moveWords.add("paper");
		moveWords.add("scissors");
		moveWords.add("spock");
		moveWords.add("lizard");	
	}
	
	
	/**
	 * 
	 * @return the character corresponding to the sim's move
	 * The sim's rotator strategy involves repeatedly cycling through all of the possible throws
	 */
	private char rotator(){
		playerMove = moveCodes.get(rotatorIndex % NUM_POSSIBLE_MOVES);
		rotatorIndex++;
		
		return playerMove;
	}
	
	
	/**
	 * 
	 * @param prevCThrowIndex is the index corresponding to the computer's last throw
	 * @return the character corresponding to the sim's move
	 * The sim's reflector strategy involves repeatedly playing the computer's last throw 
	 */
	private char reflector(int prevCThrowIndex){
		playerMove = moveCodes.get(prevCThrowIndex);
		
		return playerMove;
	}
	
	
	/**
	 * 
	 * @param N is the integer that determines after how many games the sim switches between its two strategies
	 * @param totalGames is the total number of games that will be played
	 * @param gameNum is the number of the game currently being played
	 * @param prevCThrowIndex is the index corresponding to the computer's last throw
	 * @return the character corresponding to the sim's throw
	 * If N is larger than the number of games to be played, then the sim repeatedly uses the rotator strategy.
	 * Otherwise, we have the sim switch between strategies after every N games, always starting with the 
	 * rotator strategy by computing whether the integer result of the gameNum - N is even (in which case we use 
	 * the rotator strategy) or odd (in which case we use the reflector strategy)
	 */
	public char simulatedHumanStrategy (int N, int totalGames, int gameNum, int prevCThrowIndex){
		if (N >= totalGames){
			return rotator();
		}
		else{
			if (((gameNum - 1)/N) % 2 == 0){
				return rotator();
			}
			else{
				return reflector(prevCThrowIndex);
			}
		}	
	}
	

	/**
	 * 
	 * @return the index corresponding to the player's throw
	 */
	public int getPlMoveIndex(){
		return moveCodes.indexOf(playerMove);
	}
	
	/**
	 * 
	 * @param playerMove is the character corresponding to the player's throw
	 * @param computerMove is the character corresponding to the computer's throw
	 * @return the output summarizing the player's throw and the computer's throw
	 */
	public String throwReveal(char playerMove, char computerMove){
		int playerIndex = moveCodes.indexOf(playerMove);
		int computerIndex = moveCodes.indexOf(computerMove);
		
		String gameThrows = "Your move = " + moveWords.get(playerIndex) + "\nMy move = " + moveWords.get(computerIndex);
		
		return gameThrows;
	}
	
	/**
	 * 
	 * @param playerMove is the character corresponding to the player's throw
	 * @param computerMove is the character corresponding to the computer's throw
	 * @param gameStats is the List that tracks the number of games the player has won at index 0, the number of games 
	 * the computer has won at index 1, and the number of games ending in a tie at index 2
	 * @return the List that updates the number of games the player has won, the computer has won, or that have ended in a
	 * tie after determining the outcome of the most recent game
	 * To explain the algorithm used to compute the game's outcome = 
	 * in the game of RPS, it is only possible for the player to win if playerIndex - computerIndex = 1 or -2 (1 mod 3)
	 * in the game of RPS, it is only possible for the computer to win if playerIndex - computerIndex = 2 or -1 (2 mod 3)
	 * in the game of RPS, ties occur when playerIndex - computerIndex = 0
	 * this removes the need to hard code each of the 9 possible outcomes - we can rely on this mathematical 
	 * approach instead to dictate the outcome of the game
	 * Modified in step 3 for RPSLK = 
	 * by adding the possible throws of lizard and spock, we now have 5 possible throws instead of 3 so our 
	 * previous mathematical approach must now rely on mod 5 rather than mod 3
	 * in the game of RPSKL, the player wins if playerIndex - computerIndex = 1, 3, -2, or -4 (1 mod 5 or 3 mod 5)
	 * in the game of RPSKL, the computer wins if playerIndex - computerIndex = 2, 4, -1, or -3 (2 mod 5 or 4 mod 5)
	 * as before, ties occur only when playerIndex - computerIndex = 0
	 * A not about java's mod calculations = java calculates mod of a negative number by providing the negative answer 
	 * when we want the positive one for this algorithm to work 
	 * To prevent errors because of this, we add 3 (or 5 in the case of RPSKL) to our result so that we will always 
	 * be performing the modulus on a positive number and therefore get the positive result of the modulus operation
	 */
	public List<Integer> gameOutcome(char playerMove, char computerMove, List<Integer> gameStats){
		
		int playerIndex = moveCodes.indexOf(playerMove);
		int computerIndex = moveCodes.indexOf(computerMove);
		
		System.out.println(throwReveal(playerMove, computerMove));
		
		int indexDiff = playerIndex - computerIndex;
		
		indexDiff = indexDiff + NUM_POSSIBLE_MOVES;
		
		
		if (indexDiff % NUM_POSSIBLE_MOVES == 1 || indexDiff % NUM_POSSIBLE_MOVES== 3){
			System.out.println(endOfGameMessage(playerIndex, computerIndex, "pWin"));
			int prevPlWins = gameStats.get(0);
			gameStats.set(0, prevPlWins + 1);
		}
		else if (indexDiff % NUM_POSSIBLE_MOVES == 2 || indexDiff % NUM_POSSIBLE_MOVES == 4){
			System.out.println(endOfGameMessage(playerIndex, computerIndex, "cWin"));
			int prevCWins = gameStats.get(1);
			gameStats.set(1, prevCWins + 1);;
		}
		else{
			System.out.println(endOfGameMessage(playerIndex, computerIndex, "tie"));
			int prevTies = gameStats.get(2);
			gameStats.set(2, prevTies + 1);
		}
		
		return gameStats;
	}
	
	
	/**
	 * 
	 * @param pMove is the index corresponding to the player's move
	 * @param cMove is the index corresponding to the computer's move
	 * @param outcome is the String the describes the outcome of the game ("pWin" is used for a player win, "cWin" is 
	 * used for a computer win, and "tie" is used for a tie)
	 * @return the output that summarizes the outcome of the game
	 */
	public String endOfGameMessage(int pMove, int cMove, String outcome){
		String message = "";
		if (outcome.equals("pWin")){
			message = "You win! Your " + moveWords.get(pMove)+ " covers my " + moveWords.get(cMove) + "!\n";
		}
		else if (outcome.equals("cWin")){
			message = "I win! My " + moveWords.get(cMove)+ " covers your " + moveWords.get(pMove) + "!\n";
		}
		else{
			message = "It's a tie! We both played " + moveWords.get(pMove) + "!\n";
		}			
		
		return message;
	}
	
	
	private ArrayList<Character> moveCodes;
	private ArrayList<String> moveWords;
	private char playerMove;
	private int NUM_POSSIBLE_MOVES;
	private int rotatorIndex;
	
}
