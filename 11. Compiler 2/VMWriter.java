package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
	private FileWriter writer;
	
	public VMWriter(File outputFile) {
		try {
			writer = new FileWriter(outputFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// writes a VM push command
	public void writePush(String segment, int index) throws IOException {
		writer.write("push " + segment + " " + index + "\n");
	}
	
	// writes a VM pop command
	public void writePop(String segment, int index) throws IOException {
		writer.write("pop " + segment + " " + index + "\n");
	}
	
	// writes a VM arithmetic/logical command
	public void writeArithmetic(String command) throws IOException {
		writer.write(command + "\n");
	}
	
	// writes a VM label command
	public void writeLabel(String label) throws IOException {
		writer.write("(" + label + ")\n");
	}
	
	// writes a VM goto command
	public void writeGoto(String label) throws IOException {
		writer.write("goto " + label + "\n");
	}
	
	// writes a VM if-goto command
	public void writeIf(String label) throws IOException {
		writer.write("if-goto " + label + "\n");
	}
	
	// writes a VM call command
	public void writeCall(String name, int nArgs) throws IOException {
		writer.write("call " + name + " " + nArgs + "\n");
	}
	
	// writes a VM function command
	public void writeFunction(String name, int nVars) throws IOException {
		writer.write("function " + name + " " + nVars + "\n");
	}
	
	// writes a VM return command
	public void writeReturn() throws IOException {
		writer.write("return");
	}
	
	// closes the output file
	public void close() throws IOException {
		writer.close();
	}
}
