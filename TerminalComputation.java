package assignment3;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * The TerminalComputation enum enumerates the 4 terminal stream operations the user can perform on an output stream. For counting
 * primitives, the user can calculate the mean (MEAN_VAL) or standard deviation (SD_VAL) of the values of a particular field in the 
 * output stream. For Strings, the user can calculate the mean (MEAN_LENGTH) or standard deviation (SD_VAL) of the lengths of the Strings
 * of a particular field in the output stream.
 *
 */
public enum TerminalComputation {
	MEAN_VAL, SD_VAL, MEAN_LENGTH, SD_LENGTH;
}

