package assignment3;



/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TSVFilter class is the outer class used to construct TSVFilter objects according to the Builder pattern. It requires the file name
 * parameter. Optionally, the user can set a field name and value for a select operation (if not using the null case), as well as the
 * field and TerminalOperation for a terminal stream operation. The TSVFilter object created is then used by the TSVPipeline in order
 * to perform the select and terminal operations.
 *
 */
public class TSVFilter {
	
	public TSVFilter(Request userRequest){
		userFilter = new String[2];
		userFilter[0] = userRequest.userFilter[0];
		userFilter[1] = userRequest.userFilter[1];
		fileName = userRequest.fileName;
		terminalField = userRequest.terminalField;
		comp = userRequest.comp;
		
	}
	
	/**
	 * 
	 * @return the name of the field the user is selecting on (wants a filter for)
	 */
	public String getFieldName(){
		return userFilter[0];
	}
	
	/**
	 * 
	 * @return the value for the field the user is selecting on (wants a filter for)
	 */
	public String getFieldValue(){
		return userFilter[1];
	}
	
	/**
	 * 
	 * @return the name of the file the user is streaming from
	 */
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * 
	 * @return the name of the field the user is terminating on (wants a terminal computation for)
	 */
	public String getTerminalField(){
		return terminalField;
	}
	
	/**
	 * 
	 * @return the computation the user is terminating on (the TerminalComputation the user wants to perform)
	 */
	public TerminalComputation getTermComp(){
		return comp;
	}
	
	@Override
	/**
	 * @return the String representation of the filter, which prints the file name, the select field and value, and terminate 
	 * field and operation
	 */
	public String toString(){
		return "file name = " + fileName + "\n"
				+ "field name = " + userFilter[0] + "\n"
				+ "field value = " + userFilter[1] + "\n"
				+ "terminal operation = " + comp + " for " + terminalField;
	}
	
	
	/**
	 * 
	 * @author Mitali Juneja (mj2944)
	 * The Request class is the static inner class used to create a TSVFilter in accordance with the Builder pattern. It requires the
	 * file name in order to create a TSVFilter for the user, and optionally can take parameters for the select operation and 
	 * terminal operation.
	 *
	 */
	public static class Request{
		
		/**
		 * 
		 * @param name is the name of the file the user is streaming from (without .tsv extension)
		 */
		public Request(String name){
			fileName = name + ".tsv";
			userFilter = new String[2];
			terminalField = null;
			comp = null;
		}
		
		
		/**
		 * 
		 * @param fieldName is the name of the field the user is selecting on (wants a filter for)
		 * @param fieldValue is the value for the field the user is selecting (wants a filter for)
		 * @return the Request object that handles the select filter when a field name and value are explicitly specified
		 */
		public Request select(String fieldName, String fieldValue){
			userFilter[0] = fieldName;
			userFilter[1] = fieldValue;
			return this;
		}
		
		
		/**
		 * 
		 * @param terminalFieldName is the name of the field the user is terminating on (wants a terminal computation for)
		 * @param terminalComp is the computation the user is terminating on (the TerminalComputation the user wants to perform) 
		 * @return the Request object that handles the terminal stream operation
		 */
		public Request terminate(String terminalFieldName, TerminalComputation terminalComp){
			terminalField = terminalFieldName;
			comp = terminalComp;
			return this;
		}
		
		
		/**
		 * 
		 * @return the final TSVFilter the user has built, based on the file name, select, and terminal operation the user has specified
		 */
		public TSVFilter buildRequest(){
			return new TSVFilter(this);
		}
		
		
		/**
		 * We use an array of size 2 to store the information about the filter the user creates because the two pieces of information
		 * for the select operation (field name and field value) will always come together in a pair (although both of these values
		 * could be null for the null case), and we find it easier to store these values in a way that reflects this.
		 */
		private String[] userFilter;
		private String fileName;
		private String terminalField;
		private TerminalComputation comp;
		
	}
	
	private String[] userFilter;
	private String fileName;
	private String terminalField;
	private TerminalComputation comp;

}
