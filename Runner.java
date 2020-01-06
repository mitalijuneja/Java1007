package assignment3;


/**
 * 
 * @author Mitali Juneja (mj2944)
 * Our system contains 4 classes, the TSVFilter class, the TSVPipeline class, the TSVTerminal class, and the TSVFormatter class, as well as
 * the enum TerminalComputation.
 * The TSVFilter class uses the builder pattern to construct the filter that is used to construct the output stream and perform the terminal
 * computation. The filter has the required field of a file name and optional fields for the select (field name, field value) and terminate
 * (field name, field value) operations.
 * The TSVPipeline class uses the information in the TSVFilter instance constructed based on the user's request in order to kick start the 
 * actions needed for the select and terminate operations.
 * The TSVTerminal class is a helper class for TSVPipeline that handles the specific operations needed to perform the terminal computation.
 * It performs the actual calculations of the TerminalComputation selected.
 * The TSVFormatter class is a helper class for the TSVPipeline that handles issues related to the formatting of the user's file and 
 * the information in it. It helps perform the specific operations needed in order to select.
 * The TerminalComputation enum lists out the 4 possible terminal computations the user can perform (option 2 for step 3). These are MEAN_VAL 
 * and SD_VAL for long fields and MEAN_LENGTH and SD_LENGTH for String fields.
 * 
 * For our system, all data is passed in and used physically as Strings, but we hold the header storing field types and cross check
 * with this information to make sure that any operations being performed are valid (and we parse Strings to longs where needed in the 
 * process).
 * 
 * Note that we initialize the fields for select and terminate with default values when the user is not selecting on or terminating on
 * any fields. In these cases, it is not necessary for the user to say in the Runner .select() or .terminate(). The default values
 * will be loaded without this, so in these cases the user can leave out the select or terminate methods (so if the user is not selecting
 * or terminating, initializing the filter only requires giving the file name and then calling buildRequest()).
 * 
 * SUMMARY OF PLANNED TEST CASES = 
 * All test cases will be performed on the following file, named data.tsv, unless otherwise specified (note that this file
 * contains two formatting errors that will be handled by the system in all test cases = two tabs are used in one spot in the 4th line, 
 * a String is used in place of a long in the 5th line)
 * 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22		2121115432	10027
 * Tony	eighteen	2010001123	99876
 * Ann	19	9171118421	43210
 *
 *
 * 1. select and terminate on the file named file.txt, which does not exist
 * 
 * 2. no select and terminate("Name", TerminalComputation.MEAN_LENGTH) (terminate on mean length for name)
 * 
 * 3. select("Zip code", "10027") and terminate("Age", TerminalComputation.SD_VAL) (terminate on standard deviation value for age)
 * 
 * 4. no select and no terminate
 * 
 * 5. select("Name", "Tony") and no terminate
 * 
 * 6. select and terminate on the file as above, except without headers
 * 
 * 7. select("Birthday", "November"), a field that does not exist, and no terminate
 * 
 * 8. no select and terminate("Birthday", TerminalComputation.SD_LENGTH), a field that does not exist
 * 
 * 9. no select and terminate ("Name", TerminalComputation.MEAN_VAL), an invalid terminal computation
 * 
 * 
 * TEST RESULTS = 
 * (we will show both console outputs and the output file)
 * 
 * 1. (no output file created)
 * file name = file.tsv
 * field name = Name
 * field value = Tony
 * terminal operation = SD_VAL for Age
 * File not found
 * 
 * 2. 
 * file name = data.tsv
 * field name = null
 * field value = null
 * terminal operation = MEAN_LENGTH for Name
 * 4.333333333333333
 * 
 * output file = 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22	2121115432	10027
 * Ann	19	9171118421	43210
 * 
 * 3. 
 * file name = data.tsv
 * field name = Zip Code
 * field value = 10027
 * terminal operation = SD_VAL for Age
 * 1.0

 * output file = 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22	2121115432	10027
 *
 * 4. 
 * file name = data.tsv
 * field name = null
 * field value = null
 * terminal operation = null for null
 * 
 * output file = 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22	2121115432	10027
 * Ann	19	9171118421	43210
 * 
 * 5. 
 * file name = data.tsv
 * field name = Name
 * field value = Tony
 * terminal operation = null for null
 * 
 * output file = 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * 
 * 6. (output file is blank)
 * file name = data.tsv
 * field name = Name
 * field value = Tony
 * terminal operation = null for null
 * missing field types line
 * 
 * 7.(no output file created)
 * file name = data.tsv
 * field name = Birthday
 * field value = November
 * terminal operation = null for null
 * select field not found in file
 * 
 * 8.
 * file name = data.tsv
 * field name = null
 * field value = null
 * terminal operation = SD_LENGTH for Birthday
 * Field not found
 * 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22	2121115432	10027
 * Ann	19	9171118421	43210
 * 
 * 9. file name = data.tsv
 * field name = null
 * field value = null
 * terminal operation = MEAN_VAL for Name
 * Invalid terminal computation
 * 
 * Name	Age	Cell Phone	Zip Code
 * String	long	long	long
 * Frank	20	2121117777	10027
 * Molly	22	2121115432	10027
 * Ann	19	9171118421	43210
 * 
 * 
 *
 */
public class Runner {
	
	public static void main (String[] args){
		System.out.println("Mitali Juneja (mj2944)\n");
		TSVFilter myFilter = new TSVFilter
				.Request("data")
				.select("Zip Code", "10027")
				.terminate("Name", TerminalComputation.MEAN_VAL)
				.buildRequest();
		System.out.println(myFilter);
		TSVPipeline myPipeline = new TSVPipeline(myFilter);
		myPipeline.performOutputRequest();
		
		
	}

}
