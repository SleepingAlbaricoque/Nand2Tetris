package compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JackAnalyzer {
	public static void main(String[] args) {
		
		String inputFilesDir = "C:\\Users\\user\\Desktop\\nand2tetris\\nand2tetris\\projects\\10\\ArrayTest";
		String inputFileExt = ".jack";
		
		try {
			// Get the virtual machine language files under the inputFilesDir directory
			File directory = new File(inputFilesDir);
			List<File> files = searchFiles(directory, inputFileExt);
			
			for(File file : files) {
				String outputFilePath = "C:\\Users\\user\\Desktop\\" + file.getName().substring(0, file.getName().indexOf(".")) + ".xml";
				
				// Get the assembly file under the outputFilePath in case it exists
				File outputFile = new File(outputFilePath);
				
				// Create a new assembly file if it doesn't exist
				if(!outputFile.exists()) {
					outputFile.createNewFile();
				}
				
				CompilationEngine engine = new CompilationEngine(file, outputFile);
				engine.compileClass();
				
			}
			
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
					
				}
			}
		}
		
		return vmFiles;
	}
}
