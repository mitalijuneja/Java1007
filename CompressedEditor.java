package assignment2;

import java.util.Scanner;
import java.io.File;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The CompressedEditor is built off of the Editor interface. It allows the user's commands to be processed and performs them on the 
 * compressed file. It stores the names of the txt and cmp files, as well as the files themselves. Finally, it has
 * a Compressor, which contains the dictionary and fileLines ArrayLists.
 * The system 1 counterpart to this class is TextEditor. It differs from TextEditor because it helps operate on the lines of the
 * file by means of the dictionary and always creates both a cmp and txt file. Additionally, it uses an array of length 2 to 
 * store the file names for the cmp and txt file and an additional array of length 2 to store the Files for the cmp and txt 
 * file. For both arrays, we use index 0 for the txt file and index 1 for the cmp file.
 */
public class CompressedEditor implements Editor{
	
	public CompressedEditor(){
		myFile = new Compressor();
		myFileGenerator = new CompressedFileGenerator();
		myLineReplacer = new CompressedLineReplacer();
		myWordChanger = new CompressedWordChanger();
		userFiles = new File[2];
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
	public void run (String line) throws Exception {
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
		}
		else{
			System.out.println("Invalid command. Please try again.");
		}
	}
	
	
	
	/**
	 * @param firstLine is the line that contains both the command and possibly the name of the file to be retrieved.
	 * If no file name is specified (only "g" is typed), then a new file will be created by the CompressedFileGenerator.
	 * If a file name is specified, then the appropriate file is retrieved and loaded by the the CompressedFileGenerator.
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
	 * storing the file's compressed contents.
	 * If there is also some text specified by the user, then we take this to mean that the user wants to replace the text
	 * of the specified line with the newly entered text. We extract the line number from the command, create the compressed
	 * sequence of the newly entered text, and replace the specified line (ArrayList element) with the compressed form of this
	 * new line. Finally, if the replaced line was line n + 1, we add a new blank line to the ArrayList, which is the new
	 * n + 1 line.
	 */
	public void replaceLine(String line){
		int lineNum = 0;
		if ((line.charAt(line.length() - 1) >= 48) && (line.charAt(line.length() - 1) <= 57)  ){
			 lineNum = Integer.parseInt(line.substring(2));
			 myLineReplacer.removeLine(lineNum, myFile);
		}
		else{
			lineNum = myLineReplacer.retrieveLineNum(line);
			String newLineCompressed = myLineReplacer.getNewLineContents(line, myFile);
			myLineReplacer.replaceLine(lineNum, newLineCompressed, myFile);
			
			String lastLine = myFile.getFileLines().get(myFile.getFileLines().size() - 1);
			myLineReplacer.fixLastLine(lastLine, myFile);
		}	
	}
	
	/**
	 * @param line is the line that contains both the command, and for the creation of new files, the proper file name
	 * @throws Exception if the file cannot be created or found
	 * As long as this is not a newly created file, we use our previously established file names to search for the existing (out of date) 
	 * cmp and txt files.
	 * For newly created files, we use the user's specified file name and create both a new txt and cmp file with this name. (We choose to 
	 * always export both the compressed cmp file and the regular txt file, for the user's convenience and ease).
	 * We then write these files (for cmp, we include the dictionary words and compressed lines; for txt, we simply include the text of the lines)
	 * Finally, we notify the user that the file has been written and set the status of the file to saved.
	 */
	public void setFileToDir(String line) throws Exception{
		if (!myFileGenerator.getFileName(0).equals("")){
			userFiles[0] = new File(System.getProperty("user.dir") + "//" + myFileGenerator.getFileName(0));
			userFiles[1] = new File(System.getProperty("user.dir") + "//" + myFileGenerator.getFileName(1));
		}
		else{
			myFileGenerator.setFileName(0, line.substring(2) + ".txt");
			myFileGenerator.setFileName(1, line.substring(2) + ".cmp");
			userFiles[0] = new File (System.getProperty("user.dir") + "//" + myFileGenerator.getFileName(0));
			userFiles[1] = new File (System.getProperty("user.dir") + "//" + myFileGenerator.getFileName(1));
			userFiles[0].createNewFile();
			userFiles[1].createNewFile();
		}
		myFile.writeFile(userFiles[0]);
		myFile.writeCompressedFile(userFiles[1]);
		System.out.println(myFileGenerator.getFileName(0).substring(0, myFileGenerator.getFileName(0).length() - 4) + " written");
	}
	
	/**
	 * @param line is the line containing the command and the words to be replaced or deleted
	 * This command (w) was added in step 3
	 * If only the command is entered, without any words to remove or replace, then we notify the user that this is an
	 * invalid action.
	 * Otherwise, the user has entered a valid command.
	 * If the compressed form of the entire String entered by the user can be found in the file, then we assume that 
	 * the user would like to delete all occurrences of the compressed form of this String from the file. We remove 
	 * all occurrences of this String from the ArrayList storing the file's compressed lines.
	 * If the entire String is not in the file, then we assume that the user would like to replace all occurrences of 
	 * some String oldWord with some String newWord. We determine the point of separation between the oldWord and newWord
	 * in the user's String, and replace all occurrences of the compressed form of the oldWord with the compressed form 
	 * of the newWord in the ArrayList storing the file's compressed lines.
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
					myWordChanger.replaceOldNewWords(line, oldWord, newWord, myFile);
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
	
	
	
	/**
	 * We use an array of length 2 to store the two files names we will be creating (the txt file and the cmp file) so that
	 * these files are organized together in one common place. Since we know that there will always be only
	 * two "types" of files for any given text file the user is editing, we are not interested in a dynamic 
	 * data structure, so we utilize an array. As with the other array for used for system 2 (fileNames), we use index 0 for the txt file 
	 * and index 1 for the cmp file.
	 */
	
	private Compressor myFile;
	private CompressedFileGenerator myFileGenerator;
	private CompressedLineReplacer myLineReplacer;
	private CompressedWordChanger myWordChanger;
	private File[] userFiles;
}
