package assignment2;

import java.util.Scanner;

/**
 * 
 * @author Mitali Juneja (mj2944)
 * Our system for step 2 includes five classes, the CompressedEditor class, the Compressor class, the CompressedFileGenerator
 * class, the CompressedLineReplacer class, and the CompressedWordChanger class.
 * The CompressedEditor class implements the Editor interface and runs the text editor system by processing which command a user has entered
 * and performs the appropriate actions (modifications on compressed file only, output of both text and compressed file for user convenience).
 * The Compressor class implements the FileFormat interface and works with compressed file more directly. It stores the dictionary associated
 * with our file as well as the files compressed lines. It also performs more direct and specific actions either on or with the lines of the 
 * files themselves.
 * The CompressedFileGenerator class is a helper class for carrying out the "g" command.
 * The CompressedLineReplacer class is a helper class for carrying out the "r" command.
 * The CompressedWordChanger class is a helper class for carrying out the "w" command (option 1 for step 3).
 * 
 * In the 2 arrays used for system 2, we use index 0 for the txt file and index 1 for the cmp file.
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
 * TEST RESULTS = 
 * 1. (myfile.cmp file size = 41 bytes is larger than myfile.txt file size of 23 bytes, because this is a short, one line file for which 
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

myfile.cmp has the following contents = 
This is the first line second 
0 1 2 5 4

 * 2. (wtest.cmp file size = 34 bytes is larger than wtest.txt file size of 22 bytes, because this is a short, one line file for which 
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

wtest.cmp has the following contents = 
This is the first line That What 
 6 1 2 3 4
 
 

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

 * 4. (wtest.cmp file size = 46 bytes is larger than wtest.txt file size of 10 bytes, because this is a short, one line file for which 
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

wtest.cmp has the following contents = 
This is the first line That What a new 
7 8 4


 * 5. (yodafile.cmp file size = 119 bytes is smaller than wtest.txt file size of 153 bytes, because this is a longer, repetitive file for 
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

yodafile.cmp has the following contents = 
This is the first one second not says Yoda always 
0 1 2 3 4
0 1 2 5 4
0 1 6 2 5 4
0 2 3 4 1 6 7 8
8 1 2 4
8 3 8 5 8 9
 * 
 *
 */
public class Step2Runner {

	public static void main (String[] args){
		System.out.println("Mitali Juneja (mj2944)\n");
		System.out.println("Start editing!");
		
		CompressedEditor tinyTextEditor = new CompressedEditor();
		
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
