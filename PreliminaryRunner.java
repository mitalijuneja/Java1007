package assignment1;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class PreliminaryRunner {
	
	public static void main (String[] args){
		
		Talker gamePlayer = new Talker();
		Thrower gameComputer = new Thrower();
		User opponent = new User();
		
		opponent.welcome();
		
		Scanner keyboardInput = new Scanner(System.in);
		
		double gameNum = 1;
		List<Integer> gameStats = new ArrayList<Integer>();
		int TOTAL_GAMES = 10;
		char playerRPS;
		
		gameStats.add(0);
		gameStats.add(0);
		gameStats.add(0);
		
		while (gameNum <= TOTAL_GAMES){
			System.out.print("Enter your move: ");
			String playerMove = keyboardInput.nextLine();
			
			
			playerRPS = opponent.playerTurn(playerMove);
			while (playerRPS =='f'){
				System.out.print("Enter your move: ");
				playerMove = keyboardInput.nextLine();
				playerRPS = opponent.playerTurn(playerMove);
			}
			
			char computerRPS = gameComputer.computerTurnRandom();
			
			gameStats = gamePlayer.gameOutcome(playerRPS, computerRPS,gameStats);
			gameNum++;
			
		}
		
		opponent.computeStats (gameStats.get(0), gameStats.get(1), gameStats.get(2), TOTAL_GAMES);
		
		
		
		
	}

}
