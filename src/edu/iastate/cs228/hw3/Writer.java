package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author akashsetti
 *
 */
/**
 * 
 * This class writes the corresponding message to the output text file
 *
 */
public class Writer {
	/**
	 * The infix line we are analyzing
	 */
	private String line;
	/**
	 * The message we are displaying on the output text file
	 */
	private String message;

	/**
	 * Constructor for the writer class. This takes in the file that we want to
	 * analyze, and sets both of our instance variables
	 * 
	 * @param inputFilePath
	 */
	public Writer(String inputFilePath) {

		try {
			Converter convert;
			File file = new File(inputFilePath);
			Scanner scan = new Scanner(file);

			emptyTheFile();
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				convert = new Converter(line);
				convert.conversion();
				message = convert.getPostLine();
				writeMessageToFile();

			}
			scan.close();

		} catch (FileNotFoundException e) {
			System.out.println("The file was not found. ");
		}
	}

	/**
	 * Writes the message in the file.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMessageToFile() throws FileNotFoundException {
		try {
			FileWriter writer = new FileWriter("src/output.txt", true);
			writer.write(message + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("Caught an IOException");
		}
	}

	/**
	 * Empties the file before everytime we run the program
	 */
	private void emptyTheFile() {
		try {
			FileWriter fw = new FileWriter("src/output.txt", false);
			PrintWriter pw = new PrintWriter(fw, false);
			pw.flush();

			pw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Caught an IOException");
		}
	}

}
