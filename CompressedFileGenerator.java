package assignment2;

import java.io.File;
import java.util.Scanner;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The CompressedFileGenerator class is a helper class with the methods to perform all tasks needed to retrieve and generate
 * the user's file (g command). This involves creating the file and loading the dictionary and file (in its compressed form), 
 * if necessary.
 * The system 1 counterpart top this class is TextFileGenerator. It differs from TextFileGenerator because it names both a 
 * cmp and a txt file and must first load the dictionary before it loads the (compressed) lines of an existing file.
 */
public class CompressedFileGenerator {
	
	public CompressedFileGenerator(){
		fileNames = new String[2];
	}
	
	
	/**
	 * 
	 * @param userFile is the Compressor that will store the dictionary and compressed lines for this file
	 * If we are creating a new file, then its name (for both txt and cmp) is taken as "", two blank ("") lines are added
	 * (lines 0 and 1) to the ArrayList storing the compressed lines of the file, and we print the file.
	 */
	public void createNewFile(Compressor userFile){
		userFile.getFileLines().add("");
		userFile.getFileLines().add("");
		fileNames[0] = "";
		fileNames[1] = "";
		userFile.printFile();
		
	}
	
	
	/**
	 * 
	 * @param firstLine is the line the user enters, which contains the command and the name of the file they wish to retrieve
	 * @param userFile is the Compressor that will store the dictionary and compressed lines for this file
	 * @throws Exception if the file is not found or cannot be opened
	 * If we are retrieving an existing file, then its name (for both txt and cmp) is extracted from the user's command and
	 * set with the appropriate extension. We then load the ArrayList storing the file's compressed lines using the cmp file
	 */
	public void createExistingFile (String firstLine, Compressor userFile) throws Exception{
		if (firstLine.substring(firstLine.length() - 4).equals(".txt")){
			fileNames[0] = firstLine.substring(2);
			fileNames[1] = firstLine.substring(2, firstLine.length() - 4) + ".cmp";
		}
		else{
			fileNames[0] = firstLine.substring(2) + ".txt";
			fileNames[1] = firstLine.substring(2) + ".cmp";
		}
		loadExistingFile(userFile);
	}
	
	
	/**
	 * 
	 * @param userFile is the Compressor that will store the dictionary and compressed lines for this file
	 * @throws Exception if the file is not found or cannot be opened
	 * We use the first line of the cmp file to load in the contents of the dictionary (since the first line contains the 
	 * dictionary words, in order, separated by spaces). Then, we load in each compressed line as an element from index 1 to n
	 * in the ArrayList storing the compressed lines of the file. Elements 0 and n + 1 are left as blank ("") lines
	 */
	private void loadExistingFile(Compressor userFile) throws Exception{
		File userOldFile = new File(System.getProperty("user.dir") + "//" + fileNames[1]);
		Scanner myScanner = new Scanner (userOldFile);
		userFile.getFileLines().add("");
		int lineNum = 0;
		while (myScanner.hasNextLine()){
			try{
				if (lineNum == 0){
					userFile.loadDictionary(myScanner.nextLine());
					lineNum++;
				}
				else{
					userFile.getFileLines().add(myScanner.nextLine());
				}
			}catch (Exception e){
			}
		}
		userFile.getFileLines().add("");			
	}
	
	/**
	 * 
	 * @param index is the index in the fileNames array for the file name we wish to return
	 * @return the file name (with extension .txt or .cmp depending on the index entered)
	 */
	public String getFileName(int index){
		return fileNames[index];
	}
	
	
	/**
	 * 
	 * @param index is the index associated with the file Name (for either txt or cmp file name)
	 * @param name is the name of the file
	 */
	public void setFileName(int index, String name){
		fileNames[index] = name;
	}
	
	/**
	 * We use an array of length 2 to store the file names for the two files created (the txt file and the cmp file) so that
	 * these names are organized together in one common place. Since we know that there will always be only
	 * two "types" of files for any given text file the user is editing (and therefore only 2 names), we are not interested in a dynamic 
	 * data structure, so we utilize an array. As with the other array (userFiles) for system 2, we use index 0 for the txt file 
	 * and index 1 for the cmp file.
	 */
	private String[] fileNames;
}
	
	


