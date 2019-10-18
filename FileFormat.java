package assignment2;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The FileFormat interface lists out the methods that the formatting classes of each system will perform. We create an interface that the 
 * formatters for each system will implement, because each system's formatters perform the same basic actions 
 * (process and store the lines of the file, operate on the file more directly), but each will carry out these actions in its own way, and the
 * second system (step 2) will need additional functionality to accommodate the dictionary.
 */
public interface FileFormat {
	
	public void printFile();

	public void writeFile(File userFile) throws Exception;

	public boolean isInFile(String words);

	public void removeWord(String words);
	
	public ArrayList<String> getFileLines();

}