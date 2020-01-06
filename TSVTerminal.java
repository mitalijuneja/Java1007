package assignment3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TSVTerminal class is a helper class for the TSVPipeline that handles the details of the terminal operation, with methods 
 * to compute the mean and standard deviations of the lengths or values of the field that the user specifies in the filter. 
 * It also stores the outputFile that stores the output streams because we determine that this file belongs more with the terminal 
 * side of this system, as the terminal computations operate on the completed output file.
 *
 */


public class TSVTerminal {
	
	public TSVTerminal(TSVFilter filter){
		outputFile = new File (System.getProperty("user.dir") + "//" + "output" + filter.getFileName());
		try{
			fileReader = new Scanner(outputFile);
		}catch(Exception e){
		}
		
	}
	
	/**
	 * 
	 * @return the File that records the output stream
	 */
	public File getOutputFile(){
		return outputFile;
	}
	
	
	/**
	 * 
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @param comp is the terminal computation the user wishes to perform
	 * @return the mean of either the lengths or values in the output stream of the field specified by the user
	 * @throws IOException if output stream file is does not exist or cannot be opened, or if another IO error occurs
	 * while reading from the output stream file
	 * For each line in the output file that contains actual fields (not the first 2 header lines), we find the field value that corresponds to 
	 * to the field the user wishes to terminate on. If the user is performing the computation MEAN_LENGTH, then we add the length of 
	 * this field. Otherwise we add the value of this field (MEAN_VAL computation). Once we have summed all lengths or all values, then 
	 * we divide (by the number of lines - 2 to account for the header) and return this mean.
	 */
	public double performMeanTerminal(int index, TerminalComputation comp) throws IOException{
		double mean = 0;
		int numDigits = 0;
		while (fileReader.hasNextLine()){
			String line = fileReader.nextLine();
			if (numDigits > 1 && line.length() > 0){
				if (comp == TerminalComputation.MEAN_LENGTH)
					mean += getLength(line, index);
				else
					mean += getValue(line, index);
			}
			if (line.length() > 0)
				numDigits++;
		}
		return mean/(numDigits - 2);
	}
	
	
	/**
	 * 
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @param comp is the terminal computation the user wishes to perform
	 * @return the standard deviation of either the lengths or the values in the output stream of the field specified by the user
	 * @throws IOException if output stream file is does not exist or cannot be opened, or if another IO error occurs
	 * while reading from the output stream file
	 * First we perform the associated mean computation and save this result for use while computing the standard deviation. 
	 * Then, for each line in the output file that contains actual fields (not the first 2 header lines), we find the field value that
	 * corresponds to the field the user wishes to terminate on. If the user is performing the computation SD_LENGTH, then add the square
	 * of length - mean to our running sum. Otherwise, we add the square of value - mean (SD_VAL computation) to our running sum. Once we
	 * have summed with all lengths or all values, we divide (by the number of lines - 2 to account for the header), square root, and 
	 * return this standard deviation.
	 */
	public double performSDTerminal(int index, TerminalComputation comp) throws IOException{
		double mean = getMeanForSD(index, comp);
		fileReader = new Scanner(outputFile);
		double SD = 0;
		int numDigits = 0;
		while (fileReader.hasNextLine()){
			String line = fileReader.nextLine();
			if (numDigits > 1 && line.length() > 0){
				if (comp == TerminalComputation.SD_LENGTH)
					SD += Math.pow((mean - getLength(line, index)), 2);	
				else
					SD += Math.pow((mean - getValue(line, index)), 2);
			}
			if (line.length() > 0)
				numDigits++;
		}
		SD = SD/(numDigits - 2);
		return Math.sqrt(SD);
	}
	
	
	/**
	 * 
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @param comp is the terminal computation the user wishes to perform
	 * @return the mean of either the lengths or values in the output stream of the field specified by the user
	 * We compute the mean (either of the lengths or values, depending on which SD terminal computation the user is performing) and
	 * return it so it can be used to perform the corresponding SD terminal computation.
	 */
	private double getMeanForSD (int index, TerminalComputation comp){
		try{
			if (comp == TerminalComputation.SD_LENGTH){
				return performMeanTerminal(index, TerminalComputation.MEAN_LENGTH);
			}
			return performMeanTerminal(index, TerminalComputation.MEAN_VAL);
		}catch(Exception e){
			return 0;
		}
		
	}
	
	 
	/**
	 * 
	 * @param line is the line of the output file being processed
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @return the length of the field to terminate on in this line
	 */
	private double getLength(String line, int index){
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		int i = 0;
		while (splitter.hasNext()){
			String word = splitter.next();
			if (i == index)
				return word.length();
			i++;
		}
		return 0;	
	 }
	
	
	
	/**
	 *  
	 * @param line is the line of the output file being processed
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @return the value of the field to terminate on in this line
	 */
	 private double getValue(String line, int index){
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		int i = 0;
		while (splitter.hasNext()){
			String valueString = splitter.next();
			if (i == index)
				return Integer.parseInt(valueString);
			i++;
		}
		return 0;	
	 }
	 
	 
	 private File outputFile;
	 private Scanner fileReader;
}
