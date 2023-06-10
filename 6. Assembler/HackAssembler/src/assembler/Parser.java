package assembler;

public class Parser {
	
	/* unpacks each instruction into its underlying fields */
	
	// Reads each line and determines if it's an A instruction or C instruction or whitespace
	public static int instructionType(String line) {
		if(line.startsWith("/") || line.isBlank()) { // whitespace
			return 2;
			
		}else if(line.startsWith("@")) { // A instruction
			return 0;
			
		}else if(line.startsWith("(")) { // a label 
			return 3;
			
		}else { // C instruction
			return 1;
		}
	}
	
	// Breaks down a C instruction into its underlying fields
	// Looks for the destination if it exists
	public static String dest(String line) {
		String dest = null;
		
		if(line.contains("=")) {
			dest = line.substring(0, line.indexOf("="));
		}
		
		return dest;
	}
	
	// Looks for the computation segment
	public static String comp(String line) {
		String comp = line;
		
		if(line.contains("=")) {
			comp = comp.substring(line.indexOf("=")+1);
		}
		
		if(line.contains(";")) {
			comp = comp.substring(0, line.indexOf(";"));
		}
		
		return comp;
	}
	
	// Looks for the jump segment if it exists
	public static String jump(String line) {
		String jump = null;
		
		if(line.contains(";")) {
			jump = line.substring(line.indexOf(";")+1);
		}
		
		return jump;
	}
}
