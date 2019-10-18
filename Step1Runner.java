package assignment2;

import java.util.Scanner;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * Our system for step 1 includes 5 classes, the TextEditor class, the TextFileFormat class, the TextFileGenerator class, the
 * TextLineReplacer class, and the TextWordChanger class.
 * The TextEditor class implements the Editor interface and runs our text editor by processing which command a user has specified and
 * performing the appropriate actions on the file at a higher level.
 * The TextFileFormat class implements the FileFormat interface and works with the file more directly. It stores the individual lines 
 * of our file and also performs more direct actions on the lines of the file themselves.
 * The TextFileGenerator class is a helper class for carrying out the "g" command.
 * The TextLineReplacer class is a helper class for carrying out the "r" command.
 * The TextWordChanger class is a helper class for carrying out the "w" command (option 1 for step 3).
 * 
 * SUMMARY OF PLANNED TEST CASES = 
 * 1. as shown in assignment sheet for programming part (g a new file, "r 1 This is the first line", "r 2 This is the second
 * line", "r 1", "p", "s myfile". We will then retrieve the saved file both by using "g myfile" and "g myfile.txt")
 * - from this test we will ensure that line replacement and deletion is working, files are being saved, and that we are
 * handling incorrect user input for g command)
 * 
 * 2. as shown in assignment sheet for step 3 option 1, but we will save this file as "wtest"
 * - from this test we will see that all parts of the w command are working except for if a user tries to replace
 * a word not found in the file, which will be tested in test case 3 (deletion, replacement, and replacement of a word that
 * is already in the dictionary)
 * 
 * 3. (g a new file, "r -1 This is the first line", "r 1 a new line", "w the word", "q". In this test the user forgets to save
 * their file, so we will not have a saved file to retrieve, only console output)
 * - from this test we will verify that we can handle invalid line numbers, a user trying to replace a word not found in the
 * file, and can warn the user of unsaved changes when they do not save a file
 * 
 * 4. ("g wtest", "e 2", "r 1 a new line", "s". We use this file to show that a compressed file can have larger size than a text
 * file for small files)
 * - from this test we can verify that we can work straight from retrieving an existing file, can handle the user's invalid
 * commands, and can save any new changes made
 * 
 * 5. as shown in assignment sheet for step 2, but we will save this file as "yodafile" (We use this file to show that 
 * the compressed file is smaller than the text file for larger files), then we will g a new file, "s newfile", "q"
 * - from this test we can see that we can g multiple files in one run of the system
 * 
 * 
 * TEST RESULTS = 
 * 1. (myfile.txt size = 23 bytes is smaller than myfile.cmp file size of 41 bytes because this is a short, one line file for which 
 * the additional overhead of storing the dictionary outweighs any potential file size decrease that results from compressing the lines)
 * Start editing!
g
0 
1 
r 1 This is the first line
r 2 This is the second line
r 1
p
0 
1 This is the second line
2 
s myfile
myfile written


Start editing!
g myfile
p
0 
1 This is the second line
2 


Start editing!
g myfile.txt
p
0 
1 This is the second line
2 
 * 2. (wtest.txt file size = 22 bytes is smaller than wtest.cmp file size of 34 bytes because this is a short, one line file for which 
 * the additional overhead of storing the dictionary outweighs any potential file size decrease that results from compressing the lines)
 * Start editing!
g
0 
1 
r 1 This is the first line
w This That
p
0 
1 That is the first line
2 
w That
p
0 
1 is the first line
2 
w is What is
p
0 
1 What is the first line
2 
s wtest
wtest written

Start editing!
g wtest
p
0 
1 What is the first line
2 
 * 3.
 * Start editing!
g
0 
1 
r -1 a new line
Invalid line number, please try again
r 1 a new line
w the word
p
0 
1 a new line
2 
q
changes to last file unsaved
Goodbye!

 * 4. (wtest.txt file size = 10 bytes is smaller than wtest.cmp file size of 46 bytes because this is a short, one line file for which 
 * the additional overhead of storing the dictionary outweighs any potential file size decrease that results from compressing the lines) 
 * Start editing!
g wtest
e 2
Invalid command. Please try again.
r 1 a new line
p
0 
1 a new line
2 
s
wtest written

Start editing!
g wtest
p
0 
1 a new line
2 
 * 5. (yodafile.txt file size = 153 bytes is larger than yodafile.cmp file size of 119 bytes because this is a longer, repetitive file for 
 * which the additional overhead of storing the dictionary is smaller than any file size decrease that results from compressing the lines) 
 * Start editing!
g
0 
1 
r 1 This is the first one
r 2 This is the second one
r 3 This is not the second one
r 4 This the first one is not says Yoda
r 5 Yoda is the one
r 6 Yoda first Yoda second Yoda always
p
0 
1 This is the first one
2 This is the second one
3 This is not the second one
4 This the first one is not says Yoda
5 Yoda is the one
6 Yoda first Yoda second Yoda always
7 
s yodafile
yodafile written

Start editing!
g yodafile
p
0 
1 This is the first one
2 This is the second one
3 This is not the second one
4 This the first one is not says Yoda
5 Yoda is the one
6 Yoda first Yoda second Yoda always
7 

 * 
 * 
 * 
 *
 */


public class Step1Runner {

	public static void main (String[] args){
		System.out.println("Mitali Juneja (mj2944)\n");
		System.out.println("Start editing!");
		
		Editor tinyTextEditor = new TextEditor();
		
		Scanner myScanner = new Scanner(System.in);
		while (myScanner.hasNextLine()){
			try{
				String line = myScanner.nextLine();
				tinyTextEditor.run(line);
				
			}catch (Exception e){
				
			}
		}
	}
}
