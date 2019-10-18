package assignment2;

import java.util.Scanner;
import java.io.File;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TextEditor is built off of the Editor interface. It allows the user's commands to be processed and performs them on text file. 
 * It stores the names of the txt file, as well as the file itself. Finally, it has
 * a TextFileFormat, which contains the fileLines ArrayList.
 * The system 2 counterpart to this class is CompressedEditor. It differs from the CompressedEditor class because it helps 
 * operate on the lines of the file in their regular text form and only creates txt files (rather then txt and cmp, which 
 * is why it does not require an array to aggregate file names and Files).
 *
 */
public class TextEditor implements Editor {
	public TextEditor(){
		myFileGenerator = new TextFileGenerator();
		myLineReplacer = new TextLineReplacer();
		myWordChanger = new TextWordChanger();
		myFile = new TextFileFormat();
	}
	
	/**
	 * @param line is the line that the user has just entered, containing the command and any other necessary
	 * information
	 * @throws Exception if file cannot be found or created
	 * We run the text editor through this method. When the user enters a command, the first letter entered
	 * corresponds to the the command they wish to perform (g relates to getting a file, p relates to printing 
	 * file contents, r relates to replacing a line, s relates to saving a file, and q relates to quitting the system.
	 * For each command, we also include other relevant actions, besides their respective method calls (e.g. for 
	 * quitting the system, we might give a warning statement if the last file has not been saved and print "goodbye"). If
	 * the first letter is not a valid command, we notify the user that their action is invalid.
	 *STEP 3 = we add the w command as a valid command that calls the method editWord to edit the file by either deleting 
	 *or replacing all occurrences of a word.
	 */
	public void run (String line) throws Exception{
		if (line.startsWith("g")){
			if (!myFile.getFileLines().isEmpty()){
				System.out.println("changes to last file unsaved");
				System.out.println("retrieving or creating new file");
				myFile.getFileLines().clear();
			}
			readFile(line);
		}
		else if (line.startsWith("p")){
			myFile.printFile();
		}
		else if (line.startsWith("r")){
			replaceLine(line);
		}
		else if (line.startsWith("s")){
			setFileToDir(line);
		}
		else if (line.startsWith("q")){
			quitSystem();
		}
		else if (line.startsWith("w")){
			editWord(line);
			myFile.fixSpacing();
		}
		else{
			System.out.println("Invalid command. Please try again.");
		}
	}
	
	
	
	/**
	 * 
	 * @param firstLine is the line that contains both the command and possibly the name of the file to be retrieved.
	 * If no file name is specified (only "g" is typed), then a new file will be created by the TextFileGenerator.
	 * If a file name is specified, then the appropriate file is retrieved and loaded by the the TextFileGenerator
	 */ 
	public void readFile(String firstLine) throws Exception{
		if (firstLine.equals("g")){
			myFileGenerator.createNewFile(myFile);
		}
		else{
			myFileGenerator.createExistingFile(firstLine, myFile);
		}
	}
	
	
	/**
	 * @param line is the line containing both the command, line number to replace, and possibly the contents of the new line
	 * If only a line number is specified by the user (e.g. "r 1"), then we take this to mean that user wants to remove
	 * this line from the file. We extract the line number from the user's command and remove this line from the ArrayList
	 * storing the file's contents.
	 * If there is also some text specified by the user, then we take this to mean that the user wants to replace the text
	 * of the specified line with the newly entered text. We extract the line number from the command, and replace the 
	 * specified line (ArrayList element) with the new line. Finally, if the replaced line was line n + 1, we add 
	 * a new blank line to the ArrayList, which is the new n + 1 line.
	 */
	public void replaceLine(String line){
		int lineNum = 0;
		if ((line.charAt(line.length() - 1) >= 48) && (line.charAt(line.length() - 1) <= 57)  ){
			 lineNum = Integer.parseInt(line.substring(2));
			 myLineReplacer.removeLine(lineNum, myFile);
		}
		else{
			lineNum = myLineReplacer.retrieveLineNum(line);
			String newLineContents = myLineReplacer.getNewLineContents(line);
			myLineReplacer.replaceLine(lineNum, newLineContents, myFile);
			}
		String lastLine = myFile.getFileLines().get(myFile.getFileLines().size() - 1);
		myLineReplacer.fixLastLine(lastLine, myFile);
	}
	
	/**
	 * @param line is the line that contains both the command, and for the creation of new files, the proper file name
	 * @throws Exception if the file cannot be created or found
	 * As long as this is not a newly created file, we use our previously established file names to search for the existing (out of date) 
	 * txt file.
	 * For newly created files, we use the user's specified file name and create a new txt file with this name. 
	 * We then write these files, by simply copying over the elements of the fileLines ArrayList as the lines of the txt file.
	 * Finally, we notify the user that the file has been written and set the status of the file to saved.
	 */
	public void setFileToDir(String line) throws Exception{
		if (!myFileGenerator.getFileName().equals("")){
			userFile = new File(System.getProperty("user.dir") + "//" + myFileGenerator.getFileName());
		}
		else{
			myFileGenerator.setFileName(line.substring(2) + ".txt"); 
			userFile = new File (System.getProperty("user.dir") + "//" + myFileGenerator.getFileName());
			userFile.createNewFile();
		}
		myFile.writeFile(userFile);
		System.out.println(myFileGenerator.getFileName().substring(0, myFileGenerator.getFileName().length() - 4) + " written");
	}
	
	/**
	 * @param line is the line containing the command and the words to be replaced or deleted
	 * This command (w) was added in step 3
	 * If only the command is entered, without any words to remove or replace, then we notify the user that this is an
	 * invalid action.
	 * Otherwise, the user has entered a valid command.
	 * If the entire String entered by the user can be found in the file, then we assume that the user would like
	 * to delete all occurrences of this String from the file. We remove all occurrences of this String from the 
	 * ArrayList storing the file's lines. If the entire String is not in the file, then we assume that the user would 
	 * like to replace all occurrences of some String oldWord with some String newWord. We determine the point of 
	 * separation between the oldWord and newWord in the user's String, and replace all occurrences of the oldWord 
	 * with the compressed form of the newWord in the ArrayList storing the file's lines.
	 */
	public void editWord (String line){
		String oldWord = "";
		String newWord = "";
		if (line.equals("w")){
			System.out.println("Invalid input. Please try again");
		}
		else{
			if (myFile.isInFile(line.substring(2))){
				myFile.removeWord(line.substring(2));
			}
			else{
					myWordChanger.determineOldNewWords(line, oldWord, newWord, myFile);
			}
		}
	}
	
	/**
	 * If the file contains some contents (some type of file has been created or modified), then we notify the user that
	 * they have not saved their changes. Then, we quit the system.
	 */
	public void quitSystem(){
		if (!myFile.getFileLines().isEmpty()){
			System.out.println("changes to last file unsaved");
		}
		System.out.println("Goodbye!");
	}
	
	
	
	
	private TextFileFormat myFile;
	private File userFile;
	private TextFileGenerator myFileGenerator;
	private TextLineReplacer myLineReplacer;
	private TextWordChanger myWordChanger;
}
