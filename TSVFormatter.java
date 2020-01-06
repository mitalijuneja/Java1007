package assignment3;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TSVFormatter class is a helper class for the TSV Pipeline class. It contains the methods and fields that involve the format and
 * information of the file the user is streaming from. A note to make here is that this class has 10 methods, greater than the typical 
 * limit of 7, because 2 of the methods are simple getters for the two ArrayLists that store the file header information. An additional
 * method is also essentially a simple getter, that only returns an index for the field that the user is requesting to operate on. There are
 * actually only 7 methods that perform actual functions for the class. 
 *
 */

public class TSVFormatter {
	
	public TSVFormatter(){
		fields = new ArrayList<String>();
		types = new ArrayList<String>();
	}
	
	
	/**
	 * 
	 * @param line is the first line of the header for the file being processed
	 * We first fix the tabs between fields, so that there is only one tab in between fields. Then, we split the line at these tabs and take
	 * each String in between these tabs as a field name, adding it to the array list that stores the names of the fields (fields).
	 */
	public void setFieldNames(String line){
		line = fixTabCount(line);
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		while (splitter.hasNext()){
			fields.add(splitter.next());
		}
	}
	
	
	/**
	 * @param line is the second line of the header for the file being processed
	 * We first fix the tabs between field types, so that there is only one tab in between types. Then, we split the line at these tabs 
	 * and take each String in between these tabs as a field type, adding it to the array list that stores the types of the fields (types).
	 */
	public void setFieldTypes(String line){
		line = fixTabCount(line);
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		while (splitter.hasNext()){
			types.add(splitter.next());
		}
	}
	
	/**
	 * @param line is the line of the file being processed
	 * @return true if the line is formatted properly, false otherwise
	 * Here, formatted properly has the following definition =
	 * Any fields that should be longs according to the file header can actually be parsed into longs (we don't check this for
	 * Strings, because any sequence of characters can be a String, even one with numbers, so there would not be a way to determine if
	 * a String "1" was incorrectly assigned as the value of a String field or if it was meant to be this way) and the file is not 
	 * missing the header that lists the names and types of each field
	 */
	public boolean isFormattedProperly(String line){
		line = fixTabCount(line);
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		int index = 0;
		while (splitter.hasNext()){
			String dataType = types.get(index).toLowerCase();
			String data = splitter.next();
			if (dataType.equals("long")){
				try{
					Long.parseLong(data);
				}catch (Exception e){
					return false;
				}
			}
			else if (!hasFieldTypesLine(line)){
				return false;
			}
			index++;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param line is the line of the file being processed
	 * @return true if the file contains the header that lists the names and types of each field, false otherwise
	 * We examine the types array list to see if the contents of the field types array list (types) have anything other String or long 
	 * (in any case-type). If they do, then we assume that the header is missing (since we are guaranteed that the user will only assign 
	 * String and long types in the file header if formatted correctly).
	 */
	public boolean hasFieldTypesLine(String line){
		line = fixTabCount(line);
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		int index = 0;
		while (splitter.hasNext()){
			String dataType = types.get(index).toLowerCase();
			String data = splitter.next();
			if (!dataType.equals("string") && !dataType.equals("long")){
				return false;
			}
			index++;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param line is the line of the file being processed
	 * @return the line, with any instances of multiple tabs removed and replaced with one single tab
	 */
	public String fixTabCount (String line){
		while (line.contains("\t" + "\t")){
			line = line.replaceAll("\t" + "\t", "\t");
		}
		return line;
	}
	
	
	
	/**
	 * 
	 * @param line is the line of the file being processed
	 * @param filter is the TSVFilter instance associated with the user's select and terminate requests
	 * @return true if the line has the specified value for the field the user is selecting on, false otherwise
	 * After fixing the tabs in the line, so that every field is only separated by 1 tab, we split the line at these tabs. We then
	 * determine if the nth element in the line (where n is the index of the field name from the filter in the field array list) has the
	 * same value as in the filter. 
	 */
	public boolean hasRequestedField(String line, TSVFilter filter){
		line = fixTabCount(line);
		ArrayList<String> lineValues = new ArrayList<String>();
		Scanner splitter = new Scanner(line);
		splitter.useDelimiter("\t");
		while (splitter.hasNext()){
			lineValues.add(splitter.next());
		}
		if (getRequestedIndex(filter) != -1){
			if (lineValues.get(getRequestedIndex(filter)).equals(filter.getFieldValue())){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @param filter is the TSVFilter instance associated with the user's select and terminate requests
	 * @return true if the field the user wants to select on is in the file (and therefore in the fields array list), false otherwise
	 */
	public boolean hasFilterField(TSVFilter filter){
		if (fields.indexOf(filter.getFieldName())  == -1 && filter.getFieldName() != null){
			return false;
		}
		return true;
	}
	
	/**
	 * @param filter is the TSVFilter instance associated with the user's select and terminate requests
	 * @return the index of the field the user is selecting on in the ArrayList storing the fields listed in the file header (fields)
	 */
	private int getRequestedIndex(TSVFilter filter){
		int requestedIndex = fields.indexOf(filter.getFieldName());
		return requestedIndex;
	}
	
	
	/**
	 * 
	 * @return the ArrayList that stores the field names from the file header
	 */
	public ArrayList<String> getFields(){
		return fields;
	}
	
	
	/**
	 * 
	 * @return the ArrayList that stores the field types from the file header
	 */
	public ArrayList<String> getTypes(){
		return types;
	}
	
	
	private ArrayList<String> fields;
	private ArrayList<String> types;
}
