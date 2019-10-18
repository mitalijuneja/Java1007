package assignment2;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.io.IOException;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * The Compressor class is built off of the FileFormat interface. It stores the dictionary for the compressed files
 * and it also stores the lines of the file that the user is modifying. 
 * The system 1 counterpart to this class is TextFileFormat. It differs from TextFileFormat because its fileLines ArrayList
 * stores the compressed form of the lines of the file and because it also stores the corresponding dictionary.
 * 
 * Throughout this system, we choose not to delete words from the dictionary that become unnecessary (i.e. these words have
 * been recently deleted by the user from the file itself), because we feel that there is a chance the user may, while
 * further modifying the file, re-include these words. This is especially the case for more common words ("a", "and", "the",
 * etc). We feel there could be too great of a chance that these words may be re-added later on, and would rather leave
 * them in the dictionary for efficiency, that way our system is not spending time removing and adding the same word to the 
 * dictionary. Realistically, for a text editor of this caliber, files will not be too long, and we do not expect the dictionary
 * to become overwhelmingly long. We also feel that a smaller overhead of additional space (and time) needed when loading the 
 * dictionary due to a few unused words is better than possibly spending a lot of time going back and forth between deleting and
 * adding the same word to the dictionary because of an indecisive user. 
 *
 */
public class Compressor implements FileFormat {
	public Compressor(){
		fileLines = new ArrayList<String>();
		dictionary = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param dictionaryWords is the String that comprises the first line of a compressed file. It contains all
	 * of the words that should be in the dictionary for that file.
	 * We use the first line of the compressed file and "chop" out the individual words from it. These words
	 * are then added into the ArrayList that stores the dictionary. We are able to recreate the dictionary, in
	 * the correct order, from this file.
	 */
	public void loadDictionary(String dictionaryWords){
		Scanner myScanner = new Scanner(dictionaryWords);
		while (myScanner.hasNext()){
			try{
				dictionary.add(myScanner.next());
				
			}catch (Exception e){	
			}
		}	
	}
	
	/**
	 * 
	 * @param line is the String that contains a new line or set of words that a user wants to either replace (r)
	 * or add (w). We "chop" out the individual words in this line and, if they are not already in the dictionary, we
	 * add them to it. This updates the dictionary, based off of the changes the user is making.
	 */
	public void addToDictionary(String line){
		Scanner myScanner = new Scanner(line);
		while (myScanner.hasNext()){
			try{
				String word = myScanner.next();
				if (dictionary.indexOf(word) == -1){
					dictionary.add(word);
				}
			}catch (Exception e){	
			}
		}
	}
	
	/**
	 * 
	 * @param words is the String containing the word or words that we check to see are in the file
	 * @return true if the sequence of words can be found in the file; false otherwise
	 * We take the sequence of words and construct its compressed sequence, as they would be represented in 
	 * the compressed file. We then fix the formatting, so that our sequence of some numbers follows the exact
	 * format as the numbers in our file (e.g. 1 2 3 4 5, with no trailing space). Then, we iterate through
	 * the ArrayList storing the lines of the file, in order to check if this sequence is in the compressed file.
	 *  
	 */
	public boolean isInFile(String words){
		String sequence = "";
		Scanner myScanner = new Scanner (words);
		while (myScanner.hasNext()){
			try{
				int wordIndex = dictionary.indexOf(myScanner.next());
				sequence = sequence + wordIndex + " ";
			}catch (Exception e){
			}
		}
		sequence = sequence.substring(0, sequence.length() - 1);
		for (int i = 0; i < fileLines.size(); i++){
			if (fileLines.get(i).contains(sequence)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param words is the String containing the words (in regular text) that we would like to compress
	 * @return the compressed representation of the parameter
	 * We read through each word in the String that is passed in, and then add its compressed representation
	 * (index in dictionary ArrayList to our compressed sequence string). We ensure that the formatting 
	 * completely matches our formatting in the ArrayList storing the compressed file, fileLines (e.g.
	 * 1 2 3 4 5, with no trailing space).
	 *  
	 */
	public String getCompressedSequence(String words){
		String sequence = "";
		Scanner myScanner = new Scanner (words);
		while (myScanner.hasNext()){
			try{
				int wordIndex = dictionary.indexOf(myScanner.next());
				sequence = sequence + wordIndex + " ";
			}catch (Exception e){
			}
		}
		sequence = sequence.substring(0, sequence.length() - 1);
		return sequence;
	}
	
	/**
	 * 
	 * @param words is the sequence of words the user wishes to remove from the file (r)
	 * We locate the index of each word in the dictionary, giving us the compressed form of the word. Then we 
	 * iterate through the ArrayList storing the compressed lines of the file (fileLines) and replace all occurrences
	 * of this word's index with "", effectively removing the word from the file. 
	 * Of course, if the specified word is not in the file, nothing will be removed, as expected.
	 */
	public void removeWord(String words){
		Scanner myScanner = new Scanner (words);
		while (myScanner.hasNext()){
			try{
				int wordIndex = dictionary.indexOf(myScanner.next());
				for (int i = 0; i< fileLines.size(); i++){
					if (fileLines.get(i).contains(Integer.toString(wordIndex))){
						fileLines.set(i, fileLines.get(i).replaceAll(Integer.toString(wordIndex), ""));
					}
				}
			}catch (Exception e){
			}
		}	
	}
	
	
	/**
	 * We print out the lines of the file, in regular text, using the compressed file. 
	 * For each line in our fileLines ArrayList, we first check if it is "blank" (""), and if it is, we print 
	 * it out as is, with only a prepended line number (this is the case for lines 0 and n + 1).
	 * For all other lines, we piece out each number from the line (index in dictionary ArrayList) and find the
	 * corresponding word. Once we have constructed the full line, we display it with the prepended line number.
	 */
	public void printFile(){
		for (int i = 0; i < fileLines.size(); i++){
			String line = "";
			if (fileLines.get(i).equals("")){
				System.out.println(i + fileLines.get(i));
			}
			else{
				Scanner lineProcessor = new Scanner(fileLines.get(i));
				while (lineProcessor.hasNextInt()){
					int wordIndex = lineProcessor.nextInt();
					line = line + dictionary.get(wordIndex) + " ";
				}
				line = line.substring(0, line.length() - 1);
				System.out.println(i + " " + line);
			}
		}
	}
	
	/**
	 * @param userFile is the text file we will be writing to.
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs
	 * while writing to the file.
	 * We make the decision to allow the user to export a txt file every time they save a file, in addition to 
	 * the cmp file, for the user's convenience.
	 * We read through each entry in the fileLines ArrayList (corresponding to the compressed lines of the file), 
	 * from elements 1 (the first line) to n (the last line). We determine the corresponding word for each number (index)
	 * in the dictionary, and once we have constructed a line and removed its trailing space, we write it to the
	 * text file that was passed in. 
	 * We use the FileWriter and BufferedWriter classes, because those are the classes we are most familiar with 
	 * in order to accomplish this, and they seem to write to the file fairly efficiently. If we found a better
	 * method for doing this in the future, we may consider modifying our current strategy.
	 */
	public void writeFile(File userFile) throws IOException {
		FileWriter writer = new FileWriter(userFile);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		
		for (int i = 1; i < fileLines.size() - 1; i++){
			String line = "";
			Scanner myScanner = new Scanner (fileLines.get(i));
			while (myScanner.hasNextInt()){
				line = line + dictionary.get(myScanner.nextInt()) + " ";
			}
			
			line = line.substring(0, line.length() - 1);
			buffWriter.write(line);
		}
		buffWriter.close();
		writer.close();
	}
	
	/**
	 * 
	 * @param userCmpFile is the cmp file we will be writing the compressed version of the file to
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs
	 * while writing to the file.
	 * We first copy over the contents of the dictionary, in order, to the first line of the cmp file, so that
	 * we will be able to reconstruct the text of the file at a late time, if needed.
	 * We then write each element of the fileLines ArrayList (corresponding to each line of the compressed file)
	 * to the cmp file to a new line, so that we have transferred over the full contents of the compressed file.
	 */
	public void writeCompressedFile (File userCmpFile) throws IOException{
		FileWriter writer = new FileWriter(userCmpFile);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		String wordsFromDictionary = "";
		for (int i = 0; i < dictionary.size(); i++){
			wordsFromDictionary = wordsFromDictionary + dictionary.get(i) + " ";
		}
		buffWriter.write(wordsFromDictionary);
		buffWriter.newLine();
		for (int i = 1; i<fileLines.size() - 1; i++){
			buffWriter.write(fileLines.get(i));
			buffWriter.newLine();
		}
		buffWriter.close();
		writer.close();
	}
	
	/**
	 * 
	 * @return the ArrayList storing the compressed lines of the file
	 */
	public ArrayList<String> getFileLines(){
		return fileLines;
	}
	
	
	/**
	 * 
	 * @return the ArrayList storing the dictionary words for this file 
	 */
	public ArrayList<String> getDictionary(){
		return dictionary;
	}
	

	
	/**
	 * We choose an ArrayList to store the lines of our file (fileLines), as the point of the text editor is 
	 * to continually change the file (i.e. adding lines). We wanted a dynamic data structure that also indexed 
	 * our lines (for ease when printing out prepended line numbers). Finally, we are frequently retrieving 
	 * specific lines, which would be too slow with the node structure of the LinkedList. 
	 * 
	 * We choose an ArrayList to store the words in our file (dictionary), because we know that words will 
	 * be continually added, and content is stored in the file by number, so we wanted a dynamic data structure 
	 * that also indexed our words. Finally, we want the process of converting between compressed and regular text 
	 * form to be efficient, and since this involves retrieving specific words, we avoid the slowness that comes 
	 * with the node structure of the LinkedList.
	 */
	private ArrayList <String> fileLines;
	private ArrayList<String> dictionary;
	
}
