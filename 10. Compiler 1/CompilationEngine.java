package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  reference: https://github.com/jahnagoldman/nand2tetris/blob/master/Project10/src/edu/uchicago/jagoldman/CompilationEngine.java
 *
 */

public class CompilationEngine {
	private FileWriter writer;
	private JackTokenizer tokenizer;
	private boolean bFirstRoutine;
	
	public CompilationEngine(File inputFile, File outputFile) {
		try {
			writer = new FileWriter(outputFile);
			tokenizer = new JackTokenizer(inputFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bFirstRoutine = true;
	}
	
	// compiles a complete class
	public void compileClass() throws IOException {
		//tokenizer.advance();
		writer.write("<class>\n");
		writer.write("<keyword> class </keyword>\n");
		tokenizer.advance();
		writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
		tokenizer.advance();
		writer.write("<symbol> { </symbol>\n");
		compileClassVarDec();
		compileSubroutine();
		writer.write("<symbol> } </symbol>\n");
		writer.write("</class>\n");
		writer.close();
	}
	
	// compiles a static/field variable declaration
	public void compileClassVarDec() throws IOException {
		tokenizer.advance();
		while(tokenizer.keyword().equals("static") || tokenizer.keyword().equals("field")) {
			writer.write("<classVarDec>\n");
			writer.write("<keyword> " + tokenizer.keyword() + "</keyword>\n");
			tokenizer.advance();
			
			// determine if it's a class name or primitive type
			if(tokenizer.tokenType().equals("IDENTIFIER")) { // class name
				writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
				
			}else { // primitive type
				writer.write("<keyword> " + tokenizer.keyword() + "</keyword>\n");
			}
			
			tokenizer.advance();
			writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n"); // variable name
			
			tokenizer.advance();
			if(tokenizer.symbol() == ',') { // in case of multiple variable names in one line ex. field int x, y
				writer.write("<symbol> , </symbol>\n");
				tokenizer.advance();
				writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
				tokenizer.advance();
			}
			
			writer.write("<symbol> ; </symbol>\n"); // semicolon
			tokenizer.advance();
			writer.write("</classVarDec>\n");
		}
		
		// upon reaching a subroutine, go back to the token right before currentToken
		if(tokenizer.keyword().equals("function") || tokenizer.keyword().equals("method") || tokenizer.keyword().equals("constructor")) {
			tokenizer.decrementIndex();
			return;
		}
	}
	
	 // compiles a complete method, function, or a constructor
    public void compileSubroutine() {
        boolean hasSubRoutines = false;

        tokenizer.advance();
        try {
            // once reach the end, return  - no more subroutines - base case for the recursive call
            if (tokenizer.symbol() == '}' && tokenizer.tokenType().equals("SYMBOL")) {
                return;
            }
            // subroutinedec tag
            if ((bFirstRoutine) && (tokenizer.keyword().equals("function") || tokenizer.keyword().equals("method") || tokenizer.keyword().equals("constructor"))) {
                bFirstRoutine = false;
                writer.write("<subroutineDec>\n");
                hasSubRoutines = true;
            }
            // function ,e
            if (tokenizer.keyword().equals("function") || tokenizer.keyword().equals("method") || tokenizer.keyword().equals("constructor")) {
                hasSubRoutines = true;
                writer.write("<keyword> " + tokenizer.keyword() + " </keyword>\n");
                tokenizer.advance();
            }
            // if there is an identifier in the subroutine statement position 2 e.g. function Square getX()
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
            }
            // if keyword instead for subroutine statement position 2 e.g. function int getX()
            else if (tokenizer.tokenType().equals("KEYWORD")) {
                writer.write("<keyword> " + tokenizer.keyword() + "</keyword>\n");
                tokenizer.advance();
            }
            // name of the subroutine
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
            }
            // get parameters, or lack there of
            if (tokenizer.symbol() == '(') {
                writer.write("<symbol> ( </symbol>\n");
                writer.write("<parameterList>\n");

                compileParameterList();
                writer.write("</parameterList>\n");
                writer.write("<symbol> ) </symbol>\n");

            }
            tokenizer.advance();
            // start subroutine body
            if (tokenizer.symbol() == '{') {
                writer.write("<subroutineBody>\n");
                writer.write("<symbol> { </symbol>\n");
                tokenizer.advance();
            }
            // get all var declarations in the subroutine
            while (tokenizer.keyword().equals("var") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<varDec>\n ");
                tokenizer.decrementIndex();
                compileVarDec();
                writer.write(" </varDec>\n");
            }
            writer.write("<statements>\n");
            compileStatements();
            writer.write("</statements>\n");
            writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
            if (hasSubRoutines) {
                writer.write("</subroutineBody>\n");
                writer.write("</subroutineDec>\n");
                bFirstRoutine = true;
            }

            // recursive call
            compileSubroutine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // compiles a (possibly empty) parameter list including the "()"
    public void compileParameterList() {
        tokenizer.advance();
        try {
            // until reach the end - )
            while (!(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ')')) {
                if (tokenizer.tokenType().equals("IDENTIFIER")) {
                    writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                    tokenizer.advance();
                } else if (tokenizer.tokenType().equals("KEYWORD")) {
                    writer.write("<keyword> " + tokenizer.keyword() + "</keyword>\n");
                    tokenizer.advance();
                }
                // commas separate the list, if there are multiple
                else if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ',')) {
                    writer.write("<symbol> , </symbol>\n");
                    tokenizer.advance();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a var declaration
    public void compileVarDec() {
        tokenizer.advance();
        try {

            if (tokenizer.keyword().equals("var") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<keyword> var </keyword>\n");
                tokenizer.advance();
            }
            // type of var, if identifier, e.g. Square or Array
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                tokenizer.advance();
            }
            // type of var, if keyword, e.g. int or boolean
            else if (tokenizer.tokenType().equals("KEYWORD")) {
                writer.write("<keyword> " + tokenizer.keyword() + " </keyword>\n");
                tokenizer.advance();
            }
            // name of var
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                tokenizer.advance();
            }
            // if there are mutliple in 1 line
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ',')) {
                writer.write("<symbol> , </symbol>\n");
                tokenizer.advance();
                writer.write(("<identifier> " + tokenizer.identifier() + "</identifier>\n"));
                tokenizer.advance();
            }
            // end of var line
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ';')) {
                writer.write("<symbol> ; </symbol>\n");
                tokenizer.advance();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a sequence of statements, not including the enclosing "{}" - do, let, if, while or return
    public void compileStatements() {
        try {
            if (tokenizer.symbol() == '}' && (tokenizer.tokenType().equals("SYMBOL"))) {
                return;
            } else if (tokenizer.keyword().equals("do") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<doStatement>\n ");
                compileDo();
                writer.write((" </doStatement>\n"));

            } else if (tokenizer.keyword().equals("let") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<letStatement>\n ");
                compileLet();
                writer.write((" </letStatement>\n"));
            } else if (tokenizer.keyword().equals("if") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<ifStatement>\n ");
                compileIf();
                writer.write((" </ifStatement>\n"));
            } else if (tokenizer.keyword().equals("while") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<whileStatement>\n ");
                compileWhile();
                writer.write((" </whileStatement>\n"));
            } else if (tokenizer.keyword().equals("return") && (tokenizer.tokenType().equals("KEYWORD"))) {
                writer.write("<returnStatement>\n ");
                compileReturn();
                writer.write((" </returnStatement>\n"));
            }
            tokenizer.advance();
            compileStatements();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a do statement
    public void compileDo() {
        try {
            if (tokenizer.keyword().equals("do")) {
                writer.write("<keyword> do </keyword>\n");
            }
            // function call
            compileCall();
            // semi colon
            tokenizer.advance();
            writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void compileCall() {
        tokenizer.advance();
        try {
            // first part
            writer.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
            tokenizer.advance();
            // if . - then is something like Screen.erase()
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '.')) {
                writer.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                tokenizer.advance();
                writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
                writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                // parameters in the parentheses
                writer.write("<expressionList>\n");
                compileExpressionList();
                writer.write("</expressionList>\n");
                tokenizer.advance();
                writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


            }
            // if ( then is something like erase()
            else if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '(')) {
                writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                writer.write("<expressionList>\n");
                compileExpressionList();
                writer.write("</expressionList>\n");
                // parentheses )
                tokenizer.advance();
                writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a let statement
    public void compileLet() {
        try {
            writer.write("<keyword>" + tokenizer.keyword() + "</keyword>\n");
            tokenizer.advance();
            writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
            tokenizer.advance();
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '[')) {
                // there is an expression -- because we have x[5] for example
                writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                compileExpression();
                tokenizer.advance();
                if ((tokenizer.tokenType().equals("SYMBOL")) && ((tokenizer.symbol() == ']'))) {
                    writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                }
                // only advance if there is an expression
                tokenizer.advance();

            }

            // = sign
            writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");

            compileExpression();
            // semi colon
            writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
            tokenizer.advance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a while statement
    public void compileWhile() {
        try {
            // while
            writer.write("<keyword>" + tokenizer.keyword() + "</keyword>\n");
            tokenizer.advance();
            // (
            writer.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            // compile inside of () - expression
            compileExpression();
            // )
            tokenizer.advance();
            writer.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            tokenizer.advance();
            // {
            writer.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            // inside of while statement
            writer.write("<statements>\n");
            compileStatements();
            writer.write("</statements>\n");
            // }
            writer.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a return statement
    public void compileReturn() {
        try {
            writer.write("<keyword> return </keyword>\n");
            tokenizer.advance();
            if (!((tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ';'))) {
                tokenizer.decrementIndex();
                compileExpression();
            }
            if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ';') {
                writer.write("<symbol> ; </symbol>\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // compiles an if statement, possibly with a trailing else clause
    public void compileIf() {
        try {
            writer.write("<keyword> if </keyword>\n");
            tokenizer.advance();
            writer.write("<symbol> ( </symbol>\n");
            // expression within if () condition
            compileExpression();
            writer.write("<symbol> ) </symbol>\n");
            tokenizer.advance();
            writer.write("<symbol> { </symbol>\n");
            tokenizer.advance();
            writer.write("<statements>\n");
            // compile statements within if clause { }
            compileStatements();
            writer.write("</statements>\n");
            writer.write("<symbol> } </symbol>\n");
            tokenizer.advance();
            // if there is an else clause of the if statement
            if (tokenizer.tokenType().equals("keyword") && tokenizer.keyword().equals("else")) {
                writer.write("<keyword> else </keyword>\n");
                tokenizer.advance();
                writer.write("<symbol> { </symbol>\n");
                tokenizer.advance();
                writer.write("<statements>\n");
                // compile statements within else clause
                compileStatements();
                writer.write("</statements>\n");
                writer.write("<symbol> } </symbol>\n");
            } else {
                // keep placeholder correct
                tokenizer.decrementIndex();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // compiles an expression
    public void compileExpression() {
        try {
            writer.write("<expression>\n");
            compileTerm();
            while (true) {
                tokenizer.advance();
                if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.isOperation()) {
                    // < > & = have different xml code
                    if (tokenizer.symbol() == '<') {
                        writer.write("<symbol> &lt; </symbol>\n");
                    } else if (tokenizer.symbol() == '>') {
                        writer.write("<symbol> &gt; </symbol>\n");
                    } else if (tokenizer.symbol() == '&') {
                        writer.write("<symbol> &amp; </symbol>\n");
                    } else {
                        writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                    }
                    compileTerm();
                } else {
                    tokenizer.decrementIndex();
                    break;
                }
            }
            writer.write("</expression>\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a term - if current token is an identifier, must distinguish between variable, array entry, and subroutine call
    // single look ahead token which may be "{" "(" or "." to distinguish between the three possibilities
    public void compileTerm() {
        try {
            writer.write("<term>\n");
            tokenizer.advance();
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                String prevIdentifier = tokenizer.identifier();
                tokenizer.advance();
                // for [] terms
                if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '[') {
                    writer.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    writer.write("<symbol> [ </symbol>\n");
                    compileExpression();
                    tokenizer.advance();
                    writer.write("<symbol> ] </symbol>\n");
                }
                // for ( or . - subroutine calls
                else if (tokenizer.tokenType().equals("SYMBOL") && (tokenizer.symbol() == '(' || tokenizer.symbol() == '.')) {
                    tokenizer.decrementIndex();
                    tokenizer.decrementIndex();
                    compileCall();

                } else {
                    writer.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    tokenizer.decrementIndex();
                }
            } else {
                // integer
                if (tokenizer.tokenType().equals("INT_CONST")) {
                    writer.write("<integerConstant> " + tokenizer.intVal() + " </integerConstant>\n");

                }
                // strings
                else if (tokenizer.tokenType().equals("STRING_CONST")) {
                    writer.write("<stringConstant> " + tokenizer.stringVal() + " </stringConstant>\n");
                }
                // this true null or false
                else if (tokenizer.tokenType().equals("keyword") && (tokenizer.keyword().equals("this") || tokenizer.keyword().equals("null")
                        || tokenizer.keyword().equals("false") || tokenizer.keyword().equals("true"))) {
                    writer.write("<keyword> " + tokenizer.keyword() + " </keyword>\n");
                }
                // parenthetical separation
                else if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '(') {
                    writer.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
                    compileExpression();
                    tokenizer.advance();
                    writer.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                }
                // unary operators
                else if (tokenizer.tokenType().equals("SYMBOL") && (tokenizer.symbol() == '-' || tokenizer.symbol() == '~')) {
                    writer.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                    // recursive call
                    compileTerm();
                }
            }
            writer.write("</term>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles (possibly empty) comma separated list of expressions
    public void compileExpressionList() {
        tokenizer.advance();
        // end of list
        if (tokenizer.symbol() == ')' && tokenizer.tokenType().equals("SYMBOL")) {
            tokenizer.decrementIndex();
        } else {
            tokenizer.decrementIndex();
            compileExpression();
        }
        while (true) {
            tokenizer.advance();
            if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ',') {
                try {
                    writer.write("<symbol> , </symbol>\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compileExpression();
            } else {
                tokenizer.decrementIndex();
                break;
            }
        }

    }
}
