package translator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* writes the assembly code that implements the parsed command */
public class CodeWriter {
	private FileWriter writer;
	private int jumpCounter; // counter to mark jump related labels; each label should have different name
	private int retAddrCounter; // counter to mark return address related labels; each label should have different name
	private String functionName; // string to store the current function's name for pushing/popping static value
	
	public CodeWriter(File outputFile) {
		jumpCounter = 0;
		functionName = "noFunction"; // initialize in case pushing/popping static value outside a function
		
		try {
			writer = new FileWriter(outputFile);
			
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}
	
	// part 8 - writes the assembly code that initializes the VM
	public void writeInit() throws IOException {
		String spInit = "@256\n"
				+ "D=A\n"
				+ "@SP\n"
				+ "M=D\n";
		
		writer.write(spInit); // Initialize SP value to 256
		writeCall("Sys.init", 0); // call Sys.init
	}
	
	// writes to the output file the assembly code that implements the arithmetic-logical command given as argument
	public void writeArithmetic(String command) throws IOException {
		if(command.equals("add")) {
			writer.write(arithmetic() + "M=M+D\n");
		}else if(command.equals("sub")) {
			writer.write(arithmetic() + "M=M-D\n");
		}else if(command.equals("neg")) {
			String neg = "@0\n"
					+ "D=A\n"
					+ "A=M-1\n"
					+ "M=D-M\n";
			
			writer.write(neg);
		}else if(command.equals("and")) {
			writer.write(arithmetic() + "M=M&D\n");
		}else if(command.equals("or")) {
			writer.write(arithmetic() + "M=M|D\n");
		}else if(command.equals("not")) {
			String not = "@SP\n"
					+ "A=M-1\n"
					+ "M=!M\n";
			
			writer.write(not);
		}else if(command.equals("eq")) {
			writer.write(comparison("JEQ"));
		}else if(command.equals("gt")) {
			writer.write(comparison("JGT"));
		}else if(command.equals("lt")) {
			writer.write(comparison("JLT"));
		} 
	}
	
	// writes to the output file the assembly code that implements the given push or pop command
	public void writePushPop(String command, String segment, int index) throws IOException {
		
		if(command.equals("C_PUSH")) { // push
			
			if(segment.equals("local")) {
				writer.write(push("LCL", index));
				
			}else if(segment.equals("argument")) {
				writer.write(push("ARG", index));
				
			}else if(segment.equals("this")) {
				writer.write(push("THIS", index));
				
			}else if(segment.equals("that")) {
				writer.write(push("THAT", index));
				
			}else if(segment.equals("constant")) {
				writer.write("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
				
			}else if(segment.equals("static")) {
				writer.write(pushStatic(this.functionName, index));
				
			}else if(segment.equals("temp")) {
				writer.write(pushDirect("R5", index));
				
			}else if(segment.equals("pointer")) { // push pointer = push this/that
				if(index == 0) {
					writer.write(pushDirect("THIS", 0)); 
					
				}else { // index == 1
					writer.write(pushDirect("THAT", 0)); 
					
				}
			}
			
		}else { // pop
			if(segment.equals("local")) {
				writer.write(pop("LCL", index));
				
			}else if(segment.equals("argument")) {
				writer.write(pop("ARG", index));
				
			}else if(segment.equals("this")) {
				writer.write(pop("THIS", index));
				
			}else if(segment.equals("that")) {
				writer.write(pop("THAT", index));
				
			}else if(segment.equals("static")) {
				writer.write(popStatic(this.functionName, index));
				
			}else if(segment.equals("temp")) {
				writer.write(popDirect("R5", index));
				
			}else if(segment.equals("pointer")) { // pop pointer = pop this/that
				if(index == 0) {
					writer.write(popDirect("THIS", 0)); // R3 = THIS
					
				}else { // index == 1
					writer.write(popDirect("THAT", 0)); // R4 = THAT
					
				}
			}
			
		}
	}
	
	// part 8 - writes assembly code that affects the label command
	public void writeLabel(String label) throws IOException {
		writer.write("(" + label + ")\n");
	}
	
	// part 8 - writes assembly code that affects the goto command
	public void writeGoto(String label) throws IOException {
		String writeGoto = "@" + label + "\n"
				+ "0;JMP\n"; // unconditional jump
		
		writer.write(writeGoto);
	}
	
	// part 8 - writes assembly code that affects the if-goto command
	public void writeIf(String label) throws IOException {
		// the convention here is -1 if true and 0 if false; thus, it should be JNE
		// after each operation, the result is temporarily stored in the D register to be stored in the stack in the next step
		String writeIf = "@" + label + "\n"
				+ "D;JNE\n";  
		
		writer.write(writeIf);
	}
	
	// part 8 - writes assembly code that affects the function command
	public void writeFunction(String functionName, int nVars) throws IOException {
		String writeFunction = "(" + functionName + ")\n"; // function's entry point
				
		for(int i = 0; i < nVars; i++) { // push 0 nVars times (=initialize the local variables to 0)
			writeFunction += "@0\n"
					+ "D=A\n"
					+ "@SP\n"
					+ "A=M\n"
					+ "M=D\n"
					+ "@SP\n"
					+ "M=M+1\n";
		}
		
		writer.write(writeFunction);
		
		this.functionName = functionName.substring(0, functionName.indexOf("."));
	}
	
	// part 8 - writes assembly code that affects the call command
	public void writeCall(String functionName, int nArgs) throws IOException {
		String writeCall = "@RETURN_ADDR" + retAddrCounter + "\n" // generates and pushes retAddrLabel label
				+ "D=A\n" // the A value will be determined by the assembler and stored in the symbol table during the first pass and it will be the line number of the respective code of the program
				+ "@SP\n"
				+ "A=M\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1\n"
				+ pushDirect("LCL", 0) // push LCL
				+ pushDirect("ARG", 0) // push ARG
				+ pushDirect("THIS", 0) // push THIS
				+ pushDirect("THAT", 0) // push THAT
				+ "@" + nArgs +"\n"
				+ "D=A\n"
				+ "@5\n"
				+ "D=D+A\n"
				+ "@SP\n"
				+ "D=M-D\n"
				+ "@ARG\n" // ARG = SP - 5 - nArgs
				+ "M=D\n"
				+ "@SP\n"
				+ "D=M\n"
				+ "@LCL\n" // LCL = SP
				+ "M=D\n"
				+ "@" + functionName + "\n" // goto functionName
				+ "0;JMP\n"
				+ "(RETURN_ADDR" + retAddrCounter + ")\n"; // (retAddrLabel)
		
		retAddrCounter++;
		
		writer.write(writeCall);
	}
	
	// part 8 - writes assembly code that affects the return command
	public void writeReturn() throws IOException {
		String writeReturn = "@LCL\n" // gets the LCL pointer value
				+ "D=M\n"
				+ "@R12\n"
				+ "M=D\n" // stores LCL value as endFrame variable in an arbitrary, free segment to start later reinstating the caller's segment pointers stored during the call process
				+ "@5\n"
				+ "D=D-A\n" // endFrame - 5
				+ "A=D\n" // go to (endFrame - 5) and get the value inside
				+ "D=M\n"
				+ "@R14\n" // stores the return address value in an arbitrary, free register
				+ "M=D\n"
				+ pop("ARG", 0) // puts the return value for the caller; no need to worry about the stack pointer value for now as it will be readjusted soon
				+ "@ARG\n" // repositions the stack pointer right after where we put the function's return value
				+ "D=M\n"
				+ "@SP\n"
				+ "M=D+1\n"
				+ restorePointer("THAT")
				+ restorePointer("THIS")
				+ restorePointer("ARG")
				+ restorePointer("LCL")
				+ "@R14\n" // jumps to the return address
				+ "A=M\n"
				+ "0;JMP\n"; 
		
		writer.write(writeReturn);
	}
	
	// closes the output file
	public void close() throws IOException {
		writer.close();
	}
	
	// template for add/sub/and/or
	private String arithmetic() {
		return "@SP\n"
				+ "AM=M-1\n" // decrement the pointer by one and move to the top most location which is not blank in the stack
				+ "D=M\n"
				+ "A=A-1\n";
	}
	
	// template for comparison
	private String comparison(String type) {
		String comparison =  arithmetic() 
				+ "D=M-D\n"
				+ "@TRUE" + jumpCounter + "\n"
				+ "D;" + type + "\n"
				+ "@SP\n"
				+ "A=M-1\n"
				+ "M=0\n"
				+ "D=0\n" // for if-goto IF_TRUE in fibonacciElement
				+ "@CONTINUE" + jumpCounter + "\n"
				+ "0;JMP\n"
				+ "(TRUE" + jumpCounter + ")\n"
				+ "@SP\n"
				+ "A=M-1\n"
				+ "M=-1\n"
				+ "(CONTINUE" + jumpCounter + ")\n";
		
		jumpCounter++;
		
		return comparison;
	}
	
	// template for push local/argument/this/that
	private String push(String segment, int index) {
		return "@" + index + "\n"
				+ "D=A\n"
				+ "@" + segment + "\n"
				+ "A=M+D\n"
				+ "D=M\n"
				+ "@SP\n"
				+ "A=M\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1\n";
	}
	
	// template for the rest of push operations: static, pointer, temp
	private String pushDirect(String segment, int index) { 
		String pushDirect = "@" + index + "\n"
				+ "D=A\n"
				+ "@" + segment + "\n";
		
		if(!segment.equals("THIS") && !segment.equals("THAT") && !segment.equals("LCL") && !segment.equals("ARG")) { // static, temp; the latter two are used in writeCall() where their values are needed as is, not as address references
			pushDirect += "A=A+D\n";
		}
		
		pushDirect +=
				"D=M\n"
				+ "@SP\n"
				+ "A=M\n" // move to the top most blank location
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1\n";
				
		return pushDirect;
	}
	
	// template for pushing static value
	private String pushStatic(String functionName, int index) {
		String pushStatic = "@" + functionName + "-" + index + "\n"
				+ "D=M\n"
				+ "@SP\n"
				+ "A=M\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1\n";
		
		return pushStatic;
	}
	
	// template for pop local/argument/this/that
	private String pop(String segment, int index) {
		return "@" + segment + "\n"
				+ "D=M\n"
				+ "@" + index + "\n"
				+ "D=D+A\n"
				+ "@R13\n" // R13 isn't occupied by any memory segment so we can store the destination address here temporarily
				+ "M=D\n"
				+ "@SP\n"
				+ "AM=M-1\n"
				+ "D=M\n"
				+ "@R13\n"
				+ "A=M\n"
				+ "M=D\n";
	}
	
	// template for the rest of pop operations: static, pointer, temp
	private String popDirect(String segment, int index) {
		return "@" + index + "\n"
				+ "D=A\n"
				+ "@" + segment + "\n"
				+ "D=A+D\n"
				+ "@R13\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "AM=M-1\n"
				+ "D=M\n"
				+ "@R13\n"
				+ "A=M\n"
				+ "M=D\n";
	}
	
	// template for popping static value
	private String popStatic(String functionName, int index) {
		return "@SP\n" 
				+ "M=M-1\n"
				+ "A=M\n"
				+ "D=M\n"
				+ "@" + functionName + "-" + index + "\n"
				+ "M=D\n";
	}
	
	// template for restoring caller's segment pointers
	public String restorePointer(String segment) {
		return "@R12\n" // endFrame value is stored in R12
				+ "D=M-1\n" // pointer values are stored in the range of (endFrame -1) to (endFrame -4) so we decrement the endFrame value one by one
				+ "AM=D\n" // store the decremented value in R12 and move to the pointer address
				+ "D=M\n" // get the value stored in the pointer address
				+ "@" + segment + "\n"
				+ "M=D\n"; // put the value in the respective segment
	}
	
	// writes command as comment
	public void writeComment(String line) throws IOException {
		writer.write("//" + line + "\n");
	}
}
