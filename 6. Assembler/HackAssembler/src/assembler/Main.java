package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	/* initializes the I/O files and drives the process */
	public static void main(String[] args) {
		Code code = new Code();
		int lineCounter = 0;
		
		// file to store the newly created binary code
		String filePath = "C:\\Users\\user\\Desktop\\Pong.hack";
		
		try {
			// Get the hack file under the file path and store it in a File object
			File file = new File(filePath);
			
			// Create a new hack file if it doesn't exist
			if(!file.exists()) {
				file.createNewFile();
			}
			
			// Initialize a FileWriter instance to write into the hack file
			FileWriter writer = new FileWriter(file);
			
			// Read the file one line at a time, convert it to binary code and write the binary code to the file
			try {
				// Original asm file to read from
				String oriFilePath = "C:\\Users\\user\\Desktop\\Pong.asm";
				
				File oriFile = new File(oriFilePath);
				FileReader fileReader = new FileReader(oriFile);
				BufferedReader reader = new BufferedReader(fileReader);
				
				// First pass - search for symbols to store in the symbol table
				//String symbolStoringLine = "";
				//int addressStoringLine = 0;
				String oriLine;
				List<String> lines = new ArrayList<>(); // an array list to store each line to read at the first and second pass
				
				while((oriLine = (reader.readLine())) != null) {
					// store the line in the array list
					if(!oriLine.startsWith("/") && oriLine.contains("/")) {
						oriLine = oriLine.substring(0, oriLine.indexOf("/"));
					}
					
					oriLine = oriLine.trim();
					
					lines.add(oriLine);
				}
				
				for(String isSymbol : lines) {
					// look for address symbols
					checkAins: if(isSymbol.startsWith("@") && Character.isLetter(isSymbol.charAt(1))) {
						// get rid of "@"
						isSymbol = isSymbol.substring(1);
						
						System.out.println("isSymbol - @ : " + isSymbol);
						
						// check if there's a label under the same name; if so, skip the storing process as the label related method will take care of it
						if(lines.contains("(" + isSymbol + ")")) {
							break checkAins;
						}
						
						// if the symbol doesn't exist in the symbol table, put it there
						if(!code.table.contains(isSymbol)) {
							System.out.println("isSymbol to add: " + isSymbol);
							System.out.println("addrCounter: " + code.symbolTableAddressCounter);
							code.table.addEntry(isSymbol, code.symbolTableAddressCounter);
							code.symbolTableAddressCounter++;
						}
					}
					
					// if it is a label, put it in the symbol table
					if(isSymbol.startsWith("(")) {
						// get rid of the parenthesis at both ends
						isSymbol = isSymbol.substring(1, isSymbol.indexOf(")"));
						
						// store the label name in a variable
						code.table.addEntry(isSymbol, lineCounter);

					}else {
						// increment the line counter by one
						if(!isSymbol.startsWith("/") && !isSymbol.isBlank()) {
							lineCounter++;
						}
					}
				}
				
				for (String key : code.table.keySet()) {
		            System.out.println("key: " + key + " value: " + code.table.getAddress(key));
		        }
				
				
				// Second pass - convert from decimal/symbol to binary
				for(String line : lines) {
					System.out.println(line);
					
					// if it's a label name, don't translate it into binary
					if(!line.startsWith("(")) {
						
						// Convert Assembly to Binary w/ Parser and Code
						String binaryCode = code.convertToBinary(line);
						System.out.println("binaryCode: " + binaryCode);
						
						// Write to the file if the return value isn't null
						if(binaryCode != null) {
							writer.write(binaryCode + "\n");
						}
					}
				}
				
				// Close the writer and reader
				writer.close();
				reader.close();
				
			}catch(IOException e) {
				System.out.println("An error occurred while reading the file: " + e.getMessage());
			}
			
			System.out.println("Successfully wrote to the file: " + filePath);
			
		}catch(IOException e) {
			System.out.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}
}
