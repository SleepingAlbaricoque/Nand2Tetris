package translator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* drives the process */
public class Main {
	public static void main(String[] args) {
		
		String inputFilesDir = "C:\\Users\\user\\Desktop\\nand2tetris\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest";
		String outputFilePath = "C:\\Users\\user\\Desktop\\StaticsTest.asm";
		String inputFileExt = ".vm";
		
		try {
			// Get the assembly file under the outputFilePath in case it exists
			File outputFile = new File(outputFilePath);
			
			// Create a new assembly file if it doesn't exist
			if(!outputFile.exists()) {
				outputFile.createNewFile();
			}
			
			// Get the virtual machine language files under the inputFilesDir directory
			File directory = new File(inputFilesDir);
			List<File> vmFiles = searchFiles(directory, inputFileExt);
			
			// Declare and initialize CodeWriter objects
			CodeWriter writer = new CodeWriter(outputFile);
			
			// Call Sys.init
			writer.writeInit();
			
			// Start reading the input file and parse
			for(File file: vmFiles) {
				Parser parser = new Parser(file);
				
				while(parser.hasMoreLines()) {
					
					parser.advance();
					
					// Translate each line into assembly language
					if(!parser.line.isBlank() && !parser.line.startsWith("/")) {
						
						// determine the command type
						String commandType = parser.commandType();
						
						// process arithmetic/logical command
						if(commandType.equals("C_ARITHMETIC")) {
							writer.writeComment(parser.line);
							writer.writeArithmetic(parser.line);
						}
						
						// process push/pop command
						if(commandType.equals("C_PUSH") || commandType.equals("C_POP")) {
							writer.writeComment(parser.line);
							writer.writePushPop(commandType, parser.arg1(), parser.arg2());
						}
						
						// process label command
						if(commandType.equals("C_LABEL")) {
							writer.writeComment(parser.line);
							writer.writeLabel(parser.arg1());
						}
						
						// process goto command
						if(commandType.equals("C_GOTO")) {
							writer.writeComment(parser.line);
							writer.writeGoto(parser.arg1());
						}
						
						// process if-goto command
						if(commandType.equals("C_IF")) {
							writer.writeComment(parser.line);
							writer.writeIf(parser.arg1());
						}
						
						// process call command
						if(commandType.equals("C_CALL")) {
							writer.writeComment(parser.line);
							writer.writeCall(parser.arg1(), parser.arg2());
						}
						
						// process function command
						if(commandType.equals("C_FUNCTION")) {
							writer.writeComment(parser.line);
							writer.writeFunction(parser.arg1(), parser.arg2());
						}
						
						// process return
						if(commandType.equals("C_RETURN")) {
							writer.writeComment(parser.line);
							writer.writeReturn();
						}
					}
				}
				
				parser.close();
			}
			
			writer.close();
			
		}catch(IOException e) {
			System.out.println("An error occured while creating File objects: " + e.getMessage());
		}
	}
	
	// searches for files with a specific filename extension in a given directory
	private static List<File> searchFiles(File directory, String fileExt) {
		File[] files = directory.listFiles();
		List<File> vmFiles = new ArrayList<>(); // list to store files with the target filename extension
		
		if(files != null) {
			
			for(File file: files) {
				if(file.isDirectory()) { // the given input is a directory
					searchFiles(file, fileExt); // recursively search subdirectories
					
				}else if(file.getName().endsWith(fileExt)) { // this file has the target filename extension
					vmFiles.add(file);
					
					/*
					if(file.getName().equals("Sys.vm")) {
						// get the Sys.vm file first in the list so that Sys.Init can be called before any execution
						vmFiles.remove(vmFiles.indexOf(file)); // remove the file first
						vmFiles.add(0, file); // add the file to the first index
					}
					*/
				}
			}
		}
		
		
		
		return vmFiles;
	}
}
