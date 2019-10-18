package assignment2;

import java.util.Scanner;



/**
 * 
 * @author Mitali Juneja (mj2944)
 * The CompressedLineReplacer class is a helper class with the methods to perform all tasks needed to replace a line in the 
 * file (r command). This includes retrieving the line number, checking it, and either removing a line or replacing a line 
 * with the compressed form of some new text.
 * The system 1 counterpart to this class is TextLineGenerator. It differs from TextLineGenerator because it creates 
 * compressed versions of any new lines specified by the user and must also account for adding new words to the dictionary.
 *
 */
public class CompressedLineReplacer {
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * If the line specified by the user is valid, then we remove the corresponding element from the ArrayList storing the
	 * compressed lines of the file. Otherwise, we notify the user that the line number is invalid.
	 */
	public void removeLine(int lineNum, Compressor userFile){
		if (isValidLineNum(lineNum, userFile)){
			userFile.getFileLines().remove(lineNum);
		}
		else{
			System.out.println("Invalid line number, please try again");
		}
	}
	
	
	/**
	 * 
	 * @param line is the line entered by the user, containing the command, line number, and possibly additional text to 
	 * replace with
	 * @return the line number the user has specified in their command
	 */
	public int retrieveLineNum(String line){
		String lineNumStr = line.substring(2, line.indexOf(" ", 2));
		int lineNum = Integer.parseInt(lineNumStr);
		
		return lineNum;
	}
	
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param newLineContents is the text the user wishes to replace the existing line with
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * If the line specified by the user is valid, then we replace the corresponding element in the ArrayList storing the
	 * compressed file lines with the compressed form of the user's newly specified text. Otherwise, we notify the user 
	 * that the line number is invalid.
	 */
	public void replaceLine(int lineNum, String newLineContents, Compressor userFile){
		if (isValidLineNum(lineNum, userFile)){
			userFile.getFileLines().set(lineNum, newLineContents);
		}
		else{
			System.out.println("Invalid line number, please try again");
		}
	}
	
	
	/**
	 * 
	 * @param lastLine is line n + 1 in the ArrayList storing the compressed lines of the file
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * If line n + 1, is no longer blank ("") (meaning it has just been replaced with actual text), then we add a new
	 * element to the ArrayList storing the compressed lines of the file that is the new blank ("") n + 1 line
	 */
	public void fixLastLine(String lastLine, Compressor userFile){
		if (!lastLine.equals("")){
			userFile.getFileLines().add("");
		}
	}
	
	
	/**
	 * 
	 * @param line is the line entered by the user, which contains the command, line number, and text to replace with
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * @return the compressed form of the user's newly specified line
	 * We create the compressed form of the line the user wishes to replace with. Then, we remove its trailing space, so
	 * that its format follows the format we are using to store the compressed lines (e.g. 1 2 3 4 5, with no trailing space)
	 */
	public String getNewLineContents (String line, Compressor userFile){
		String newLine = createCompressedLine(line, userFile);
		newLine = newLine.substring(0, newLine.length() - 1);
		
		return newLine;
	}
	
	
	/**
	 * 
	 * @param line is the line entered by the user, which contains the command, line number, and text to replace with
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * @return the compressed form of the user's newly specified line with a trailing space
	 * We extract the text form of the user's newly specified line. Then, we add any new words to the dictionary for this
	 * file. Finally, we construct the compressed line (with one trailing space), using the indices of the words in the 
	 * dictionary as the numbers representing the words.
	 */
	private String createCompressedLine(String line, Compressor userFile){
		String newLineText = line.substring(line.indexOf(" ", 2) + 1);
		Scanner myScanner = new Scanner(newLineText);
		String newLineCompressed = "";
		while (myScanner.hasNext()){
			String word = myScanner.next();
			if (userFile.getDictionary().indexOf(word) == -1){
				userFile.getDictionary().add(word);
			}
			newLineCompressed = newLineCompressed + userFile.getDictionary().indexOf(word) + " ";
		}
		return newLineCompressed;
	}
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param userFile is the Compressor storing the compressed lines of the file
	 * @return true if the line number entered by the user is valid (part of the file), false otherwise
	 */
	private boolean isValidLineNum(int lineNum, Compressor userFile){
		if (lineNum < 1 || lineNum >= userFile.getFileLines().size()){
			return false;
		}
		return true;
	}
	
}
