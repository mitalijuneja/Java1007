package assignment2;

import java.util.Scanner;


/**
 * 
 * @author Mitali Juneja (mj2944) 
 * The CompressedWordChanger class is a helper class with the methods to replace all occurrences of a certain word
 * or phrase (w command, option 1 for step 3). This includes determining which words to replace (referred to as "old" here), 
 * which words to replace with (referred to as "new" here), and performing the actual replacement in the ArrayList storing 
 * the compressed lines of the file.
 * The system 1 counterpart of this class is TextWordChanger. It differs from TextWordChanger because it creates 
 * and uses compressed forms of any words specified by the user.
 */
public class CompressedWordChanger {

	
	/**
	 * 
	 * @param line is the line entered by the user, which contains the command and words to remove and replace with
	 * @param oldWord is the String that the user wishes to remove from the file
	 * @param newWord is the String that the user wishes to replace with in the file
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * We first extract the text entered in the user's command. Then we take the first word from this text and search through
	 * the file for the compressed form of this word. If we find it, then we continue to incrementally add the compressed form of the 
	 * next word from the user's text and search through the file for this String. Once we are unable to find the String, we 
	 * establish this spot as the separation point between the new and old words. We replace the all occurrences of the old word's
	 * compressed form with the new word's compressed form in the ArrayList storing the compressed lines of the file.
	 */
	public void replaceOldNewWords(String line, String oldWord, String newWord, Compressor userFile){
		Scanner myScanner = new Scanner (line.substring(2));
		String words = "";
		while (myScanner.hasNext()){
			words = words + " " +  myScanner.next();
			if (!userFile.isInFile(words.substring(1))){
				oldWord = words.substring(1, words.lastIndexOf(" "));
				newWord = words.substring(words.lastIndexOf(" ") + 1);
				newWord = getFullNewWord(line, oldWord, newWord);
				userFile.addToDictionary(newWord);
				String oldSequence = userFile.getCompressedSequence(oldWord);
				String newSequence = userFile.getCompressedSequence(newWord);
				changeWords(userFile, oldSequence, newSequence);
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
			newWord = newWord+ " " + line.substring(4 + oldWord.length() + newWord.length());
		}
		return newWord;
	}
	
	
	/**
	 * 
	 * @param userFile is the Compressor that stores the compressed lines of the files
	 * @param oldSequence is the compressed form of the old word
	 * @param newSequence is the compressed form of the new word
	 * We replace all instances of the compressed form of the old word with the compressed form of the new word in the
	 * ArrayList storing the compressed lines of the file.
	 */
	private void changeWords(Compressor userFile, String oldSequence, String newSequence){
		for (int i = 0; i < userFile.getFileLines().size(); i++){
			if (userFile.getFileLines().get(i).contains(oldSequence)){
				userFile.getFileLines().set(i, userFile.getFileLines().get(i).replaceAll(oldSequence, newSequence));
			}
		}
	}

}
