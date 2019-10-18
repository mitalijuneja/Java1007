package assignment2;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Editor interface lists out the methods that the editing classes of each system will perform. We create an interface that the editors
 * for each system will implement, because each system's editors perform the same basic actions (run the system and perform each of the commands),
 * but each will carry out these actions in its own way.
 * 
 */
public interface Editor {

	public void readFile(String firstLine) throws Exception;

	public void replaceLine(String line);

	public void setFileToDir(String line) throws Exception;

	public void editWord(String line);

	public void run(String line) throws Exception;

}