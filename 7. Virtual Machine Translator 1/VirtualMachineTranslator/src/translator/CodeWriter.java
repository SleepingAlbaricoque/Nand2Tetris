package translator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* writes the assembly code that implements the parsed command */
public class CodeWriter {
	private FileWriter writer;
	private int jumpCounter; // counter to mark jump related labels; each label should have different name

	public CodeWriter(File outputFile) {
		jumpCounter = 0;
		
		try {
			writer = new FileWriter(outputFile);
			
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}
	
	// writes to the output file the assembly code that implements the arithmetic-logical command given as argument
	public void writeArithmetic(String command) throws IOException {
		writer.write("// " + command + "\n");
		
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
				writer.write(pushConstant("16", index));
				
			}else if(segment.equals("temp")) {
				writer.write(pushConstant("R5", index));
				
			}else if(segment.equals("pointer")) { // push pointer = push this/that
				if(index == 0) {
					writer.write(pushConstant("THIS", 0)); 
					
				}else { // index == 1
					writer.write(pushConstant("THAT", 0)); 
					
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
				writer.write(popConstant("16", index));
				
			}else if(segment.equals("temp")) {
				writer.write(popConstant("R5", index));
				
			}else if(segment.equals("pointer")) { // pop pointer = pop this/that
				if(index == 0) {
					writer.write(popConstant("THIS", 0)); // R3 = THIS
					
				}else { // index == 1
					writer.write(popConstant("THAT", 0)); // R4 = THAT
					
				}
			}
			
		}
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
	private String pushConstant(String segment, int index) { 
		String pushConstant = "@" + index + "\n"
				+ "D=A\n"
				+ "@" + segment + "\n";
		
		if(!segment.equals("THIS") && !segment.equals("THAT")) { // static, temp
			pushConstant += "A=A+D\n";
		}
		
		pushConstant +=
				"D=M\n"
				+ "@SP\n"
				+ "A=M\n" // move to the top most blank location
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1\n";
				
		return pushConstant;
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
	private String popConstant(String segment, int index) {
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
	
	// writes command as comment
	public void writeComment(String line) throws IOException {
		writer.write("//" + line + "\n");
	}
}
