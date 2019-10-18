package assignment2;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TextLineReplacer class is a helper class with the methods to perform all tasks needed to replace a line in the 
 * file (r command). This includes retrieving the line number, checking it, and either removing a line or replacing a line 
 * some new text.
 * The system 2 counterpart to this class is CompressedLineReplacer. It differs from the CompressedLineReplacer class because
 * it keeps all new lines in their regular text form, as input by the user, and does not need to add any words to a dictionary.
 *
 */
public class TextLineReplacer {
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param userFile is the TextFileFormat storing the lines of the file
	 * If the line specified by the user is valid, then we remove the corresponding element from the ArrayList storing the
	 * lines of the file. Otherwise, we notify the user that the line number is invalid.
	 */
	public void removeLine(int lineNum, TextFileFormat userFile){
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
	 * @param line is the line entered by the user, which contains the command, line number, and text to replace with
	 * @return the user's new text specified for this line
	 * 
	 */
	public String getNewLineContents(String line){
		String newLineContents = line.substring(line.indexOf(" ", 2) + 1);
		return newLineContents;
	}
	
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param newLineContents is the text the user wishes to replace the existing line with
	 * @param userFile is the TextFileFormat storing the lines of the file
	 * If the line specified by the user is valid, then we replace the corresponding element in the ArrayList storing the
	 * file lines with the user's newly specified text. Otherwise, we notify the user 
	 * that the line number is invalid.
	 */
	public void replaceLine (int lineNum, String newLineContents, TextFileFormat userFile){
		if (isValidLineNum(lineNum, userFile)){
			userFile.getFileLines().set(lineNum, newLineContents);
		}
		else{
			System.out.println("Invalid line number, please try again");
		}
	}
	
	
	/**
	 * 
	 * @param lastLine is line n + 1 in the ArrayList storing the lines of the file
	 * @param userFile is the TextFileFormat storing the lines of the file
	 * If line n + 1, is no longer blank ("") (meaning it has just been replaced with actual text), then we add a new
	 * element to the ArrayList storing the compressed lines of the file that is the new blank ("") n + 1 line
	 */
	public void fixLastLine(String lastLine, TextFileFormat userFile){
		if (!lastLine.equals("")){
			userFile.getFileLines().add("");
		}
	}
	
	/**
	 * 
	 * @param lineNum is the line number specified by the user
	 * @param userFile is the TextFileFormat storing the lines of the file
	 * @return true if the line number entered by the user is valid (part of the file), false otherwise
	 */
	private boolean isValidLineNum(int lineNum, TextFileFormat userFile){
		if (lineNum < 1 || lineNum >= userFile.getFileLines().size()){
			return false;
		}
		return true;
	}
	
}	
	
	