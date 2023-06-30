package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JackTokenizer {
	public String currentToken;
	private List<String> keywords; // a list object to store all the possible keywords
	private List<String> symbols; // a list object to store all the possible symbols
	private String operations; // a collection of operational symbols
	public List<String> tokens; // a list object to store all the tokens in the input file
	private int currentIndex; // an int value to indicate the current index of the currentToken
	private FileReader fileReader;
	private BufferedReader reader;
	
	public JackTokenizer(File inputFile) {
		// initailize keywords list
		String[] keywordsTemp = {"class", "constructor", "function", "method", "field", "static", "var", 
				"int", "char", "boolean", "void", "true", "null", "this", "let", "do", "if", "else", "while", "return"};
		keywords = Arrays.asList(keywordsTemp);
		
		// initialize symbols list
		String[] symbolsTemp = {"{", "}", "(", ")", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"};
		symbols = Arrays.asList(symbolsTemp);
		operations = "+-*/&|<>=";
	
		// initialize tokens list
		tokens = new ArrayList<>();
		
		// initialize currentIndex to 0
		currentIndex = 0;
		
		// read the input file, identify tokens and put them in tokens list
		try {
			fileReader = new FileReader(inputFile);
			reader = new BufferedReader(fileReader);
			
			String currentLine;
			while((currentLine = reader.readLine()) != null) { // while there's lines to read
				
				// parse current line and put the tokens in tokens list
				// ignore comments and blank lines
				if(!currentLine.startsWith("/") && !currentLine.startsWith(" *") && !currentLine.isBlank()) {
					currentLine = currentLine.trim();
					String currentString = ""; // store chars until encountering a whitespace
					
					for(int i = 0; i < currentLine.length(); i++) {
						char c = currentLine.charAt(i);
						
						if(c == ' ') { // if the current character is a whitespace, store currentString as token
							if(!currentString.equals("")) { // prevents whitespace from being stored as token
								if(currentString.startsWith("\"") && !currentString.endsWith("\"")){ // if currentString is a string constant, add chars till encountering the second double quotation mark
									currentString += c;
									
								}else{
									tokens.add(currentString);
									currentString = "";
								}
							}
							
						}else {
							if(symbols.contains(String.valueOf(c))) { // if the current character qualifies as symbol
								if(!currentString.equals("")) { 
									tokens.add(currentString); // store currentString as token before adding the symbol as token
								}
								tokens.add((String.valueOf(c))); // store the current character or symbol as token
								
								currentString = "";
								
							}else {
								currentString += c; // add the current character to currentString
							}
						}
					}
				}
			}
			
		}catch(IOException e) {
			System.out.println("An error has occured while reading the file: " + e.getMessage());
		}
		
		// set the first element in tokens list as currentToken
		currentToken = tokens.get(0);
	}
	
	// checks if there's more tokens left in the input
	public boolean hasMoreTokens() {
		return currentIndex < tokens.size() - 1;
	}
	
	// gets the next token from the input and sets it as the current token
	public void advance() {
		if(hasMoreTokens()) {
			// get the next token from tokens list and sets it as the current token
			currentToken = tokens.get(currentIndex + 1);
			
			// increment currentIndex by 1;
			currentIndex++;
		}
	}
	
	// decrement currentIndex by one
	public void decrementIndex() {
		currentIndex--;
	}
	
	// returns the type of the current token
	public String tokenType() {
		if(keywords.contains(currentToken)) { // keyword
			return "KEYWORD";
			
		}else if(symbols.contains(currentToken)) { // symbol
			return "SYMBOL";
			
		}else if(currentToken.matches("/^\\d+$/")) {
			return "INT_CONST";
			
		}else if(currentToken.contains("\"")) {
			return "STRING_CONST";
			
		}else {
			return "IDENTIFIER";
		}
	}
	
	// returns the string of a keyword token (current token)
	public String keyword() {
		return currentToken;
	}
	
	// returns the character of a symbol token (current token)
	public char symbol() {
		return currentToken.charAt(0);
	}
	
	// returns the string of an identifier token (current token)
	public String identifier() {
		return currentToken;
	}
	
	// returns the integer value of an integer constant token (current token)
	public int intVal() {
		return Integer.valueOf(currentToken);
	}
	
	// returns the string of a string token (current token)
	public String stringVal() {
		currentToken = currentToken.split("\"")[1]; // get rid of the double quotation marks at both ends
		return currentToken;
	}
	
	// indicates if a symbol is an operation, i.e., =, +, -, &, |, etc.
    public boolean isOperation() {
        for (int i = 0; i < operations.length(); i++) {
            if (operations.charAt(i) == symbol()) {
                return true;
            }
        }
        return false;
    }
}
