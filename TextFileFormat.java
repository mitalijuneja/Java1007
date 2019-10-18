package assignment2;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TextFileFormat class is built off of the FileFormat interface. It stores the lines of the file that the user is modifying.
 * The system 2 counterpart to this class is Compressor. It differs from the Compressor class because it operates on and stores 
 * the lines of the file in their regular text form and does not need to store a dictionary.
 */
public class TextFileFormat implements FileFormat {
	
	public TextFileFormat(){
		fileLines = new ArrayList<String>();
	}
	
	/**
	 * We print out the lines of the file. 
	 * For each line in the file (element in fileLines ArrayList), we print out the contents of the line (element in List) with a prepended
	 * line number, printing the entire List (lines 0 to n + 1).
	 */
	public void printFile(){
		for (int i = 0; i < fileLines.size(); i++){
			System.out.println(i + " " + fileLines.get(i));
		}
	}
	
	/**
	 * @param userFile is the text file we will be writing to.
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs
	 * while writing to the file.
	 * We write  each entry in the fileLines ArrayList from elements 1 (the first line) to n (the last line) to the text file that was passed in. 
	 * We use the FileWriter and BufferedWriter classes, because those are the classes we are most familiar with 
	 * in order to accomplish this, and they seem to write to the file fairly efficiently. If we found a better
	 * method for doing this in the future, we may consider modifying our current strategy.
	 */
	public void writeFile(File userFile) throws Exception{
		FileWriter writer = new FileWriter(userFile);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		
		for (int i = 1; i < fileLines.size() - 1; i++){
			buffWriter.write(fileLines.get(i));
			buffWriter.newLine();
		}
		buffWriter.close();
		writer.close();
	}
	
	/**
	 * 
	 * @param words is the String containing the word or words that we check to see are in the file
	 * @return true if the sequence of words can be found in the file; false otherwise
	 * We iterate through the ArrayList storing the lines of the file (fileLines), and check if this sequence of words is in the original file.
	 *  
	 */
	public boolean isInFile(String words){
		for (int i = 0; i < fileLines.size(); i++){
			if (fileLines.get(i).contains(words)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param words is the sequence of words the user wishes to remove from the file (r)
	 * We iterate through the ArrayList storing the lines of the file (fileLines) and replace all occurrences
	 * of this word with "", effectively removing the word from the file. 
	 * Of course, if the specified word is not in the file, nothing will be removed, as expected.
	 */
	public void removeWord(String words){
		for (int i = 0; i< fileLines.size(); i++){
			if (fileLines.get(i).contains(words)){
				fileLines.set(i, fileLines.get(i).replaceAll(words, ""));
			}
		}
	}
	
	/**
	 * We require this method to make sure that lines are properly being displayed (no leading or trailing spaces, only one space between words)
	 * after then have been edited to replace words (w). We check for and remove any leading or trailing spaces in each line of the file (element
	 * of fileLines ArrayList). We then check for and remove any double spaces, by inserting in their place a single space.
	 */
	public void fixSpacing(){
		for (int i = 1; i < fileLines.size() - 1; i++){
			if (fileLines.get(i).substring(0,1).equals(" ")){
				fileLines.set(i, fileLines.get(i).substring(1));
			}
			if (fileLines.get(i).substring(fileLines.get(i).length() - 1).equals(" ")){
				fileLines.set(i, fileLines.get(i).substring(0, fileLines.get(i).length() - 1));
			}
			if (fileLines.get(i).contains("  ")){
				fileLines.set(i, fileLines.get(i).replaceAll("  ", " "));
			}
		}
	}
	
	/**
	 * 
	 * @return the ArrayList storing the lines of the file
	 */
	public ArrayList<String> getFileLines(){
		return fileLines;
	}
	

	
	/**
	 * We choose an ArrayList to store the lines of our file, as the point of the text editor is to continually change the file (i.e. adding
	 * lines). We wanted a dynamic data structure that also indexed our lines (for ease when printing out prepended line numbers). Finally, 
	 * we are frequently retrieving specific lines, which would be too slow with the node structure of the LinkedList. 
	 */
	private ArrayList <String> fileLines;
}
