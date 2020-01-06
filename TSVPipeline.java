package assignment3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TSVPipeline class handles the select operation in its entirety (other than format issues, which are handled by the TSVFormatter
 * helper class) and the terminal operation at a high level (leaving more specific details for the TSVTerminal helper class). It determines
 * based on the user's filter which lines to write to the file containing the output streams and determines which methods the
 * TSVTerminal class uses to perform the terminal stream computation. For this system, the output file is always named according to the 
 * following convention = "output" + original file name (for instance, if the user wants to select on a field in the file myFile.txt, the
 * output file would be named outputmyFile.txt).
 * A note to make here is that this class has 9 methods, greater than the typical maximum of 7, because of 2 simple methods that essentially
 * check simple conditions (whether the select request is null and whether the file exists). Rather than cluttering the code by inserting
 * these checks directly in the code or by moving them to a helper class where they don't logically belong, I chose to have 2 additional 
 * methods in this class.
 *
 */
public class TSVPipeline {
	
	public TSVPipeline(TSVFilter userFilter){
		filter = userFilter;
		formatter = new TSVFormatter();
		terminal = new TSVTerminal(filter);
	}

	/**
	 * creates the output stream based on the filter specified by the user and performs the terminal stream operation specified by the user
	 * if the file specified by the user cannot be found (or does not exist), then we print this message to the console.
	 */
	public void performOutputRequest(){
		try{
			processRequest();
			terminalComp();
		}catch(Exception e){
		}
	}
	
	
	/**
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs while writing to the file.
	 * processes the select request at a high level (creates the output stream file and passes through the original file line by line)
	 * 
	 * First, we find the user's file and then create the associated output file (following the naming convention for this system of
	 * "output" + original file name. Then, for each line in the system, we first fix the tab formatting (ensuring that all information
	 * is only separated by 1 tab) and then attempt to extract all relevant information from the line (e.g. if this is one of the first two
	 * lines, we get the field name and type information from the header, otherwise we determine if the line should be added to the output 
	 * file. If the file specified by the user doesn't exist we print this message to the console and terminate the system.
	 */
	private void processRequest() throws IOException{
		if (isValidFile()){
			File userFile = new File(System.getProperty("user.dir") + "//" + filter.getFileName());
			Scanner fileReader = new Scanner(userFile);
			terminal.getOutputFile().createNewFile();
				int lineNum = 0;
				try{
					while (fileReader.hasNextLine()){
						String line = formatter.fixTabCount(fileReader.nextLine());
						processLine(line, lineNum, terminal.getOutputFile());
						lineNum++;
					}
				}catch(Exception e){
				}	
		}
		else{
			System.out.println("File not found");	
			System.exit(0);
		}	
	}
	
	
	/**
	 * @param line is the line of the original file to be processed
	 * @param lineNum is the line number in the original file of the line to be processed
	 * @param outputFile is the file that records the output stream
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs while writing to the file.
	 * process a line in the original file by determining whether it is a header line or potentially belongs in the output stream
	 * 
	 * If we are processing the first line in the file, then we extract the field names and store them in the array list storing 
	 * the names of the fields (fields). Then we write this line to the output file. If we are processing the second line, we extract the 
	 * field types and store them in the array list storing the field types (types). Otherwise, we determine if this line could potentially
	 * belong in the output stream.
	 * In the event that the header lines do not agree (the number of field names does not equal the number of field types), then we output
	 * a blank file, print this message to the console and terminate the system.
	 */
	private void processLine(String line, int lineNum, File outputFile) throws IOException{
		if (lineNum == 0){
			formatter.setFieldNames(line);
			writeLinesToFile(line, lineNum, outputFile);
		}
		else if (lineNum == 1){
			formatter.setFieldTypes(line);
			writeLinesToFile(line, lineNum, outputFile);
			if (formatter.getFields().size() != formatter.getTypes().size()){
				System.out.println("Header lines do not agree");
				writeLinesToFile("", 0, outputFile);
				System.exit(0);
			}
		}
		else
			createOutputStream(line, lineNum, outputFile);	
	}
	
	
	/**
	 * @param line is the line of the original file to be written to outputFile
	 * @param lineNum is the line number in the original file of the line to be written
	 * @param outputFile is the file that records the output stream
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs while writing to the file.
	 * determines if a line meets the criteria established by the filter and belongs in the output stream, if the file specified by
	 * the user is missing headers (in which case it alerts the user of this and terminates the program), or if the user
	 * is selecting on a field that is not in the file
	 * 
	 * If the user is selecting on a field that does not exist in the file, then we print this message to the file, output a blank file, 
	 * and terminate the system.
	 * If the user is not selecting on any fields (null request) or if the line contains the field the user wishes to select on, then
	 * we write this line to the output file, as long as we can see that the file and the line are formatted properly (all fields
	 * that should  be longs are actually longs, and then file contains the header in the first two lines). 
	 * This is where we would be able to perform data cleansing (if a field that should be a long cannot be parsed into a long, 
	 * we skip over the line) and where we would detect if the user's file does not contain the header 
	 * (in which case we print this message onto the console, write over anything we may have previously output in the file so 
	 * that we output a blank file, and terminate the system).
	 * If the user is not selecting on any field, we simply perform data cleansing and reflect this in the console, by displaying both
	 * the field name and field value as null.
	 */
	private void createOutputStream(String line, int lineNum, File outputFile) throws IOException{
		if ((isNullRequest() || formatter.hasRequestedField(line, filter)) && formatter.isFormattedProperly(line))
			writeLinesToFile(line, lineNum, outputFile);	
		else if (!formatter.hasFieldTypesLine(line)){
			System.out.println("missing field types line");
			writeLinesToFile("", 0, outputFile);
			System.exit(0);
		}
		if (!formatter.hasFilterField(filter)){
			System.out.println("select field not found in file");
			writeLinesToFile("", 0, outputFile);
			System.exit(0);
		}
	}
	
	
	
	
	/**
	 * @param outputFile is the file that records the output stream
	 * @param line is the line of the file to write to the output stream
	 * @param lineNum is the number of this line in the original file
	 * @throws IOException if file is does not exist or cannot be opened, or if another IO error occurs while writing to the file
	 * writes a line of the original file to outputFile
	 * 
	 * If this is the first line we are writing to the file, then we write over any existing contents (this is needed in case the user 
	 * runs the system on the same file twice, in this case, we don't keep adding our output streams to the same file, we "reset" the 
	 * file after each run). Otherwise, we simply append to the existing file (so that at the end of the output stream we record the headers
	 * and all lines in the output stream, rather than writing over every line and only ending up with the last line of the output stream
	 * in out file). 
	 * 
	 */
	private void writeLinesToFile(String line, int lineNum, File outputFile) throws IOException{
		FileWriter writer;
		if (lineNum == 0)
			writer = new FileWriter(outputFile);
		else
			writer = new FileWriter(outputFile, true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		buffWriter.write(line);
		buffWriter.newLine();
		buffWriter.close();
		writer.close();
	}
	
	
	
	/**
	 * @throws IOException if output stream file is does not exist or cannot be opened, or if another IO error occurs while reading from 
	 * the output stream file
	 * performs the terminal stream computation specified by the user in the TSVFilter
	 * If the user wishes to terminate a field that does not exist in the file, then we print this message to the console. Otherwise,
	 * we verify that the terminal computation is valid for the field specified by the user (length computations are performed on String fields,
	 * value computations are performed on long fields). If the terminal computation is valid for the specified field, then we perform it
	 * and print out its value. Otherwise, we print an error message to the console.
	 * If the user does not wish to terminate on any fields, then no computation will occur, and the console output will reflect this (by 
	 * displaying "null for null").
	 */
	private void terminalComp() throws IOException{
		int index = formatter.getFields().indexOf(filter.getTerminalField());
		if (index == -1 && filter.getTerminalField() != null)
			System.out.println("Field not found");
		if (isValidTerminal(index, filter.getTermComp())){
			if (filter.getTermComp() == TerminalComputation.MEAN_LENGTH || filter.getTermComp() == TerminalComputation.MEAN_VAL)
				System.out.println(terminal.performMeanTerminal(index, filter.getTermComp()));
			else
				System.out.println(terminal.performSDTerminal(index, filter.getTermComp()));
		}
		else
			System.out.println("Invalid terminal computation");
	}
	
	
	
	/**
	 * 
	 * @param index is the index of the terminal field in the ArrayList containing the types of all the fields in the file header
	 * @param comp is the terminal computation the user wishes to perform
	 * @return true if the specified terminal computation can be performed on the type of the field specified by the user, false otherwise
	 * If a length terminal is performed on a String field or a value terminal is performed on a long field, then we consider the terminal
	 * computation be valid, otherwise it is considered invalid.
	 */
	private boolean isValidTerminal(int index, TerminalComputation comp){
		String terminalType = formatter.getTypes().get(index).toLowerCase();
		if (terminalType.equals("string")){
			if (comp == TerminalComputation.MEAN_LENGTH || comp == TerminalComputation.SD_LENGTH)
				return true;
		}
		else if (terminalType.equals("long")){
			if (comp == TerminalComputation.MEAN_VAL || comp == TerminalComputation.SD_VAL)
				return true;
		}
		return false;
	}
	
	
	/**
	 * @return true if the user has chosen to select, without specifying the field name and value (null case), false otherwise
	 */
	private boolean isNullRequest(){
		return filter.getFieldName() == null || filter.getFieldValue() == null;
	}
	
	
	/**
	 * 
	 * @return true if the file requested by the user exists, false otherwise
	 */
	private boolean isValidFile(){
		try{
			File userFile = new File(System.getProperty("user.dir") + "//" + filter.getFileName());
			Scanner fileReader = new Scanner(userFile);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	private TSVFilter filter;
	private TSVFormatter formatter;
	private TSVTerminal terminal;

}
