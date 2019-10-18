package assignment1;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The User class handles all basic actions of this system that involve the computer's opponent and the 
 * processing of any higher level data that does not explicitly require the input of any other classes.
 * The decision to add the User class was made in order to distinguish between actions involving the computer's 
 * opponent playing of the game (Talker class) and other actions that merely involve the displaying or processing
 * of higher level information (User class).
 * As each game occurs, the Talker class updates a List that stores the counts of each game outcome. The User class
 * uses the counts in this List in order to calculate and display overall statistics at the end of the game.
 * 
 */

public class User {
	
	
	public User(){
		playerMove = 'f';
	}
	
	
	/**
	 * displays welcome text for the player of the game. This text provides basic rules to the game.
	 * Initally included instructions for RPS. Modified in step 3 to add instructions for RPSLK
	 */
	public void welcome(){
		System.out.println("Mitali Juneja (mj2944)\n");
		System.out.println ("welcome to Rock Paper Scissors Lizard Spock!\n");
		System.out.println("Play against the computer: enter you move according to the following guidelines");
		System.out.println("rock = 'r'");
		System.out.println("paper = 'p'");
		System.out.println("scissors = 's'");
		System.out.println("lizard = 'l'");
		System.out.println("spock = 'k'\n");
		System.out.println("Wait for the computer to reveal their move and learn the outcome of the game!");
		System.out.println("As a reminder, rock beats scissors and lizard, scissors beats paper and lizard, paper beats rock and spock, lizard beats paper and spock, and spock beats scissors and rock");
		System.out.println("______________________________________");
	}
	
	
	/**
	 * 
	 * @param move is the String input by the user when prompted for their throw
	 * @return the character that corresponds with a move if the input String is valid; 'f' to indicate failure otherwise 
	 * the system accepts any form (upper, lower case) of a character or word corresponding to a valid throw
	 * Initially only processed inputs for RPS. Modified in step 3 to handle inputs for RPSLK
	 */
	public char playerTurn(String move){
		
		if (move.toLowerCase().equals("r") || move.toLowerCase().equals("rock")){
			playerMove = 'r';
			return playerMove;
		}	
		else if (move.toLowerCase().equals("p") || move.toLowerCase().equals("paper")){
			playerMove = 'p';
			return playerMove;
		}	
		else if (move.toLowerCase().equals("s") || move.toLowerCase().equals("scissors")){
			playerMove = 's';
			return playerMove;
		}
		else if (move.toLowerCase().equals("k") || move.toLowerCase().equals("spock")){
			playerMove = 'k';
			return playerMove; 
		}
		else if (move.toLowerCase().equals("l") || move.toLowerCase().equals("lizard")){
			playerMove = 'l';
			return playerMove;
		}
		else{
			System.out.println("Invalid move. Please try again.");
			return 'f';
		}
	}
	
	
	/**
	 * 
	 * @param pWins is the number of games the player (computer's opponent) wins 
	 * @param cWins is the number of games the computer wins
	 * @param tie is the number of games that end in a draw
	 * @param numGames is the total number of games that the player and computer play
	 * computes the percentage of rounds that ended in the player winning, the computer winning, and a draw
	 * displays the percentages and counts on the console
	 */
	public void computeStats (int pWins, int cWins, int tie, double numGames){
		
		double percentPWins = (pWins/numGames)*100;
		double percentCWins = (cWins/numGames)*100;
		double percentTies = (tie/numGames)*100;
		
		System.out.println("\nGame Statistics");
		System.out.println("______________________");
		System.out.println("You won " + pWins + " rounds (" + percentPWins + "% of rounds played)");
		System.out.println("I won " + cWins + " rounds (" + percentCWins + "% of rounds played)");
		System.out.println("There were " + tie + " ties (" + percentTies + "% of rounds played)");
	}

	
	private char playerMove;
}
