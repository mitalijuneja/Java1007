package assignment2;

import java.util.Scanner;


/**
 * 
 * @author Mitali Juneja (mj2944) 
 * The TextWordChanger class is a helper class with the methods to replace all occurrences of a certain word
 * or phrase (w command, option 1 for step 3). This includes determining which words to replace (referred to as "old" here), 
 * which words to replace with (referred to as "new" here), and performing the actual replacement in the ArrayList storing 
 * the lines of the file.
 * The system 2 counterpart to this class is CompressedWordChanger. It differs from CompressedWordChanger because it only uses
 * words in their regular text form, as input by the user.
 *
 */
public class TextWordChanger {
	
	/**
	 * 
	 * @param line is the line entered by the user, which contains the command and words to remove and replace with
	 * @param oldWord is the String that the user wishes to remove from the file
	 * @param newWord is the String that the user wishes to replace with in the file
	 * @param userFile is the TextFileFormat storing the lines of the file
	 * We first extract the text entered in the user's command. Then we take the first word from this text and search through
	 * the file for this word. If we find it, then we continue to incrementally add the next word from the user's text and 
	 * search through the file for this String. Once we are unable to find the String, we establish this spot as the 
	 * separation point between the new and old words. We replace all occurrences of the old word with the new word
	 * in the ArrayList storing the lines of the file.
	 */
	public void determineOldNewWords(String line, String oldWord, String newWord, TextFileFormat userFile){
		Scanner myScanner = new Scanner (line.substring(2));
		String words = "";
		while (myScanner.hasNext()){
			words = words + " " +  myScanner.next();
			if (!userFile.isInFile(words.substring(1))){
				oldWord = words.substring(1, words.lastIndexOf(" "));
				newWord = words.substring(words.lastIndexOf(" ") + 1);
				newWord = getFullNewWord(line, oldWord, newWord);
				changeWord(userFile, oldWord, newWord);
				break;
			}	
		}
	}	
				
	
	/**
	 * 
	 * @param line is the line entered by the user, which contains the command and words to remove and replace with
	 * @param oldWord is the String that the user wishes to remove from the file
	 * @param newWord is the String that the user wishes to replace with in the file
	 * @return the full String that contains the new word
	 * Once we establish the separation point, we capture all words beyond this as being part of the new word to replace with.
	 */
	private String getFullNewWord(String line, String oldWord, String newWord){
		if (line.substring(2).length() > oldWord.length() + newWord.length() + 1){
			newWord =  newWord + " " + line.substring(4 + oldWord.length() + newWord.length());
		}
		return newWord;
	}	
	
	
	/**
	 * 
	 * @param userFile is the TextFileFormat that stores the lines of the files
	 * @param oldWord is the old word
	 * @param newWord is the new word
	 * We replace all instances of the old word with the new word in the ArrayList storing the file.
	 */
	private void changeWord(TextFileFormat userFile, String oldWord, String newWord){
		for (int i = 0; i < userFile.getFileLines().size(); i++){
			if (userFile.getFileLines().get(i).contains(oldWord)){
				userFile.getFileLines().set(i, userFile.getFileLines().get(i).replaceAll(oldWord, newWord));
			}
		}
	}			
	
}
