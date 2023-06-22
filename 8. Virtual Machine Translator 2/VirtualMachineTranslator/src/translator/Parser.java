package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* parses each VM command into its lexical elements */
public class Parser {
	private FileReader fileReader;
	private BufferedReader reader;
	public String line; // variable to temporarily store each line's content
	public List<String> lines; // list to store variable line at each read
	private List<String> arithmetics; // list to store arithmetic/logical commands
	private Map<String, String> commands; // map to store command types

	// constructor - opens the input file and gets ready to parse it
	public Parser(File inputFile) { 
		// initialize variables to store line at each read
		line = "";
		lines = new ArrayList<>();
		
		// add arithmetic/logical commands to arithmetics list
		arithmetics = new ArrayList<>();
		arithmetics.add("add");
		arithmetics.add("sub");
		arithmetics.add("neg");
		arithmetics.add("eq");
		arithmetics.add("gt");
		arithmetics.add("lt");
		arithmetics.add("and");
		arithmetics.add("or");
		arithmetics.add("not");
		
		// add command key-value pairs to commands map
		commands = new HashMap<>();
		commands.put("label", "C_LABEL");
		commands.put("goto", "C_GOTO");
		commands.put("if-goto", "C_IF");
		commands.put("push", "C_PUSH");
		commands.put("pop", "C_POP");
		commands.put("call", "C_CALL");
		commands.put("return", "C_RETURN");
		commands.put("function", "C_FUNCTION");
		for(String arithmetic : arithmetics) {
			commands.put(arithmetic, "C_ARITHMETIC");
		}

		// read the file one line at a time
		try {
			fileReader = new FileReader(inputFile);
			reader = new BufferedReader(fileReader);
			
			/*
			while(hasMoreLines() == true) {
				line = advance();
				
				if(!line.isBlank()) { // prevent blank line from being added to the list
					lines.add(line);
				}
			}
			*/
			
		}catch(IOException e){
			System.out.println("An error occurred while reading the file: " + e.getMessage());
		}
	}
	
	// checks if there are more lines in the input file
	public boolean hasMoreLines() throws IOException{
		reader.mark(1); // Mark the current position
        int nextChar = reader.read(); // Read the next character
        
        if (nextChar == -1) {
            return false; // Reached end of stream, no next line
            
        } else {
            reader.reset(); // Reset to the marked position
            return true; // Next line exists
        }
	}
	
	// reads the next line
	public String advance() throws IOException {
		String newLine = reader.readLine();
		
		if(!newLine.startsWith("/") && newLine.contains("/")) {
			newLine = newLine.substring(0, newLine.indexOf("/"));
		}
		
		newLine = newLine.trim();
		
		if(!newLine.isBlank()) {
			line = newLine;
			lines.add(line);
			
		}else{
			line = newLine;
		}
		
		return newLine;
	}
	
	// returns a constant representing the type of the current command; call this after advance()
	public String commandType() {
		// inspect the first segment command stored in variable line 
		String firstSeg = "";
		
		if(line.contains(" ")) { // multiple word command
			firstSeg = line.split(" ")[0];
		}else { // a single word command
			firstSeg = line;
		}
		
		String commandType = commands.get(firstSeg);
		return commandType;
	}
	
	// returns the first argument of the current command
	// C_ARITHMETIC command returns itself
	// if C_RETURN, don't call this method
	public String arg1() {
		if(commandType().equals("C_ARITHMETIC")) {
			return line;
		}
		
		return line.split(" ")[1];
	}
	
	// returns the second argument of the current command
	// should be called only if the command type is push, pop, function or call
	public int arg2() {
		int arg2 = Integer.valueOf(line.split(" ")[2]);
		return arg2;
	}
	
	// closes the BufferedReader
	public void close() throws IOException {
		reader.close();
	}
}
