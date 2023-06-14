package translator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* drives the process */
public class Main {
	public static void main(String[] args) {
		
		String inputFilePath = "C:\\Users\\user\\Desktop\\StaticTest.vm";
		String outputFilePath = "C:\\Users\\user\\Desktop\\StaticTest.asm";
		
		try {
			// Get the assembly file under the outputFilePath in case it exists
			File outputFile = new File(outputFilePath);
			
			// Create a new assembly file if it doesn't exist
			if(!outputFile.exists()) {
				outputFile.createNewFile();
			}
			
			// Get the virtual machine language file under the inputFilePath
			File inputFile = new File(inputFilePath);
			
			// Declare and initialize Parser and CodeWriter objects
			Parser parser = new Parser(inputFile);
			CodeWriter writer = new CodeWriter(outputFile);
			
			// Start reading the input file and parse
			while(parser.hasMoreLines()) {
				parser.advance();
				
				// Translate each line into assembly language
				if(!parser.line.isBlank() && !parser.line.startsWith("/")) {
																																																								
					// determine the command type
					String commandType = parser.commandType();
					
					// process arithmetic/logical command
					if(commandType.equals("C_ARITHMETIC")) {
						writer.writeArithmetic(parser.line);
					}
					
					// process push/pop command
					if(commandType.equals("C_PUSH") || commandType.equals("C_POP")) {
						writer.writeComment(parser.line);
						writer.writePushPop(commandType, parser.arg1(), parser.arg2());
					}
					
				}
			}
			
			parser.close();
			writer.close();
			
		}catch(IOException e) {
			System.out.println("An error occured while creating File objects: " + e.getMessage());
		}
	}
}
