package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * reference: https://github.com/jahnagoldman/nand2tetris/blob/master/Project11/src/edu/uchicago/jagoldman/CompilationEngine.java
 */

public class CompilationEngine {
	private VMWriter writer;
	private JackTokenizer tokenizer;
	private SymbolTable symbolTable;
	private String className; // variable to store the current class name
	private String subroutineName; // variable to store the current subroutine name
	
	public CompilationEngine(File inputFile, File outputFile) {
		writer = new VMWriter(outputFile);
		tokenizer = new JackTokenizer(inputFile);
		symbolTable = new SymbolTable();
	}
	
	// compiles a complete class
	public void compileClass() throws IOException {
		tokenizer.advance(); // class name
		className = tokenizer.currentToken;
		
		tokenizer.advance(); // {
		
		compileClassVarDec();
		compileSubroutine();
		
		writer.close();
	}
	
	/*
	 * Each compilexxx routine should read xxx from the input, advance() the input exactly 
	 * beyond xxx, and emit to the output VM code effecting the semantics of xxx
	 */
	// compiles a static/field variable declaration
	public void compileClassVarDec() throws IOException {
		tokenizer.advance(); // first part if class variable; if not, it sets the pointer to the token right after the current one
		String token = "";
		
		if(tokenizer.tokenType().equals("KEYWORD")) {
			token = tokenizer.keyword();
			System.out.println(token);
		}
		
		while(token.equals("field") || token.equals("static")) {
			String kind = "";
			
			if(token.equals("field")) {
				kind = "field";
				
			}else {
				kind = "static";
			}
			
			tokenizer.advance(); // second part
			String type = "";
			
			if(tokenizer.tokenType().equals("IDENTIFIER")) { // class type
				token = tokenizer.identifier();
				
			}else { // primitive type
				token = tokenizer.keyword();
			}
			type = token;
			
			tokenizer.advance(); // third part
			token = tokenizer.identifier(); // the name of the variable
			System.out.println("1st define: " + token + " type: " + type);
			symbolTable.define(token, type, kind); // add to the symbol table
			
			tokenizer.advance(); // check if there are more than two variable names in the current line; if not, it's a semicolon
			while(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ',') {
				tokenizer.advance();
				token = tokenizer.identifier();
				symbolTable.define(token, type, kind);
				
				tokenizer.advance(); // semicolon
			}
		}
		
		// upon reaching a subroutine, go back to the token prior
		if(token.equals("function") || token.equals("method") || token.equals("constructor")) {
			return;
		}
		
		tokenizer.advance();
		
		/* lines to check the content of the class table
		System.out.println("hey");
		for(Map.Entry<String, Symbol> entry : symbolTable.classTable.entrySet()) {
			System.out.println("symbols" + entry.getKey() + entry.getValue().getKind() + entry.getValue().getType() + entry.getValue().getIndex());
		}
		*/
	}
	
	 // compiles a complete method, function, or a constructor
    public void compileSubroutine() throws IOException { // recursive till it processes all the subroutines in the class
    	// upon reaching the end, exit
    	if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '}') {
    		return;
    	}
    	
    	// check the type of subroutine
    	String subroutineType = "";
    	if(tokenizer.keyword().equals("function") || tokenizer.keyword().equals("method") || tokenizer.keyword().equals("constructor")) {
    		subroutineType = tokenizer.keyword();
    		
    		// reset subroutine table
    		symbolTable.reset();
    		
    		// add 'this' to the subroutine table in case of method
    		if(tokenizer.keyword().equals("method")) {
    			symbolTable.define("this", className, "argument");
    		}
    		
    		tokenizer.advance(); // pointer to the return type
    	}
    	
    	// return type - identifier or primitive type
    	String returnType = "";
    	if(tokenizer.tokenType().equals("KEYWORD")) { // primitive
    		returnType = tokenizer.keyword();
    		tokenizer.advance(); // pointer to the subroutine name
    		
    	}else { // identifier or class name
    		returnType = tokenizer.identifier();
    		tokenizer.advance(); // pointer to the subroutine name
    	}
    	
    	// name of the subroutine
    	if(tokenizer.tokenType().equals("IDENTIFIER")) {
    		subroutineName = tokenizer.identifier();
    		tokenizer.advance(); // pointer to "("
    	}
    	
    	// get parameters
    	if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '(') {
    		compileParameterList();
    		tokenizer.advance(); // { 
    	}
    	
    	// subroutine body
    	if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '{') {
    		tokenizer.advance(); // subroutine body content
    	}
    	
    	// look for any variable declarations
    	while(tokenizer.tokenType().equals("KEYWORD") && tokenizer.keyword().endsWith("var")) {
    		compileVarDec();
    	}
    	
    	// prepare for call function
    	String function = className + "." + subroutineName;
    	
    	// call function
    	writer.writeFunction(function, symbolTable.varCount("var")); // the number of local variables corresponds to the nVars value of writeFunction()
    	
    	// pop the address in which the current object will be stored to THIS 
    	if(subroutineType.equals("method")) {
    		writer.writePush("argument", 0); // the address in heap is stored in arg 0
    		writer.writePop("pointer", 0); // pop to THIS
    		
    	}else if(subroutineType.equals("constructor")) {
    		writer.writePush("constant", symbolTable.varCount("field")); // nArgs value for Memory.alloc call
    		writer.writeCall("Memory.alloc", 1); // this will allocate free space of size nArgs to the constructor in heap
    		writer.writePop("pointer", 0); // stores the address Memory.alloc returned in THIS
    	}
    	
    	compileStatements();
    	
    	//compileSubroutine();
    }


    // compiles a (possibly empty) parameter list including the "()"
    public void compileParameterList() {
    	String type = "";
    	String name = "";
    	
    	while(!tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() != ')') { // if there's parameters
    		if(tokenizer.tokenType().equals("KEYWORD")) { // primitive type
    			type = tokenizer.keyword();
    			
    		}else { // class type
    			type = tokenizer.identifier();
    		}
    		tokenizer.advance(); // pointer to the parameter name
    		
    		if(tokenizer.tokenType().equals("IDENTIFIER")) {
    			name = tokenizer.identifier();
    		}
    		tokenizer.advance(); // after loop, the current token will be ')'
    		
    		// store the parameter in symbol table
    		symbolTable.define(name, type, "argument");
    		
    		// check if there's more than one parameters
    		if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ',') {
    			tokenizer.advance();
    		}
    	}
    	
    	tokenizer.advance(); // '{'
    	tokenizer.advance(); // subroutine body starting point
    }

    // compiles a var declaration
    public void compileVarDec() {
    	String type = "";
    	String name = "";
    	
    	// check if there's a var declaration
    	if(tokenizer.tokenType().equals("KEYWORD") && tokenizer.keyword().equals("var")) {
    		tokenizer.advance(); // pointer to variable type
    	}
    	
    	// check the type of the variable
    	if(tokenizer.tokenType().equals("KEYWORD")) { // primitive type
    		type = tokenizer.keyword();
    		tokenizer.advance(); // pointer to variable name
    		
    	}else if(tokenizer.tokenType().equals("IDENTIFIER")) {
    		type = tokenizer.identifier();
    		tokenizer.advance(); // pointer to variable name
    		
    	}
    	
    	// check the variable name
    	if(tokenizer.tokenType().equals("IDENTIFIER")) {
    		name = tokenizer.identifier();
    		tokenizer.advance(); 
    	}
    	
    	// store the variable in subroutine table
    	symbolTable.define(name, type, "var");
    	
    	// check if there are more than one variable in the current line
    	while(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ',') {
    		tokenizer.advance(); // to variable name
    		name = tokenizer.identifier();
    		symbolTable.define(name, type, "var");
    		
    		tokenizer.advance();
    	}
    	
    	// check the current token marks the end of the current line
    	if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ';') {
    		tokenizer.advance(); // pointer to subroutine statements
    	}
    }

    // compiles a sequence of statements, not including the enclosing "{}" - do, let, if, while or return
    public void compileStatements() throws IOException {
    	if(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '}') { // end of subroutine
    		return;
    	}
    	
    	if(tokenizer.tokenType().equals("KEYWORD")) {
    		if(tokenizer.keyword().equals("do")) {
    			compileDo();
    			
    		}else if(tokenizer.keyword().equals("let")) {
    			compileLet();
    			
    		}else if(tokenizer.keyword().equals("if")) {
    			compileIf();
    			
    		}else if(tokenizer.keyword().equals("while")) {
    			compileWhile();
    			
    		}else if(tokenizer.keyword().equals("return")) {
    			compileReturn();
    			
    		}
    		
    		tokenizer.advance();
    		compileStatements(); // recurse till reaching '}'
    	}
    }
    
    // compiles a do statement
    public void compileDo() throws IOException {
    	compileCall();
    	tokenizer.advance(); // pointer to semicolon
    	writer.writePop("temp", 0); // do statement to call a function for its effect, ignoring the returned value
    }

    private void compileCall() {
    	
    }

    // compiles a let statement
    public void compileLet() {
    }

    // compiles a while statement
    public void compileWhile() {
    }

    // compiles a return statement
    public void compileReturn() {


    }

    // compiles an if statement, possibly with a trailing else clause
    public void compileIf() {

    }

    // compiles an expression
    public void compileExpression() {
    }

    // compiles a term - if current token is an identifier, must distinguish between variable, array entry, and subroutine call
    // single look ahead token which may be "{" "(" or "." to distinguish between the three possibilities
    public void compileTerm() {
    }

    // compiles (possibly empty) comma separated list of expressions
    public void compileExpressionList() {

    }
}
