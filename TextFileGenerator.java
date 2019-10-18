package assignment2;

import java.io.File;
import java.util.Scanner;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TextFileGenerator class is a helper class with the methods to perform all tasks needed to retrieve and generate
 * the user's file (g command). This involves creating the file and loading it if necessary.
 * The system 2 counterpart to this class is CompressedFileGenerator. It differs from the CompressedFileGenerator because it
 * only names a txt file and loads the lines of an existing file in their regular text form, without a dictionary.
 */

public class TextFileGenerator {
	
	/**
	 * 
	 * @param userFile is the TextFileFormat that will store the dictionary and lines for this file
	 * If we are creating a new file, then its name is taken as "", two blank ("") lines are added
	 * (lines 0 and 1) to the ArrayList storing the lines of the file, and we print the file.
	 */
	public void createNewFile(TextFileFormat userFile){
			userFile.getFileLines().add("");
			userFile.getFileLines().add("");
			fileName = "";
			userFile.printFile();
	}
	
	
	/**
	 * 
	 * @param firstLine is the line the user enters, which contains the command and the name of the file they wish to retrieve
	 * @param userFile is the TextFileFormat that will store the dictionary and lines for this file
	 * @throws Exception if the file is not found or cannot be opened
	 * If we are retrieving an existing file, then its name is extracted from the user's command and set with the appropriate 
	 * extension. We then load the ArrayList storing the file's lines using this txt file
	 */

	public void createExistingFile(String firstLine, TextFileFormat userFile) throws Exception{
		if (firstLine.substring(firstLine.length() - 4).equals(".txt")){
			fileName = firstLine.substring(2);
		}
		else{
			fileName = firstLine.substring(2) + ".txt";
		}
		loadExistingFile(userFile);	
	}
	
	
	/**
	 * 
	 * @param userFile is the TextFileFormat that will store the dictionary and lines for this file
	 * @throws Exception if the file is not found or cannot be opened
	 * We load in each line of the file as an element from index 1 to n in the ArrayList storing the lines of the 
	 * file. Elements 0 and n + 1 are left as blank ("") lines
	 */
	private void loadExistingFile(TextFileFormat userFile) throws Exception{
		File userOldFile = new File(System.getProperty("user.dir") + "//" + fileName);
		userFile.getFileLines().add("");
		Scanner myScanner = new Scanner (userOldFile);
		while (myScanner.hasNextLine()){
			try{
				String line = myScanner.nextLine();
				userFile.getFileLines().add(line);
			}catch (Exception e){
			}
		}
		userFile.getFileLines().add("");	
	}
	
	/**
	 * 
	 * @return the name of the file (with extension .txt)
	 */
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * 
	 * @param name is the name of the file
	 */
	public void setFileName (String name){
		fileName = name;
	}
	
	private String fileName;
}
