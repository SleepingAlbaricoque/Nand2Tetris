package assembler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Code {

	/* translates each field into its corresponding binary value */
	public SymbolTable table;
	public int symbolTableAddressCounter;
	
	public Code() {
		table = new SymbolTable();
		symbolTableAddressCounter = 16; // Register 0 - 15 have been allocated to the predefined symbols 
	}
	
	// Translates an A instruction into binary
	private String AtoBinary(String line) {
		// Get rid of "@"
		line = line.substring(1);
		
		// Determine if it's a symbol or decimal value
		String integerRegex = "^[0-9]*$";
		if(Pattern.matches(integerRegex, line)) { // decimal
			// convert to binary
			line = Integer.toBinaryString(Integer.valueOf(line));
			
			// add 0s to the binary code to make it 16-bit
			int length = line.length();
			String zeros ="";
			
			for(int i = 0; i < 16 - length -1; i++) {
				zeros += "0";
			}
			
			line = "0" + zeros + line;
			
		}else { // symbol
			// get the address from the table
			int symbolAddress = table.getAddress(line);
			
			// convert to binary
			String biAddress = Integer.toBinaryString(symbolAddress);
			
			// add 0s to the binary code to make it 16-bit
			int length = biAddress.length();
			String zeros = "";
			
			for(int i = 0; i < 16 - length -1; i++) {
				zeros += "0";
			}
			
			line = "0" + zeros + biAddress;
		}
		
		return line;
	}
	
	// Translates a label into binary
	private String labelToBinary(String line) {
		// remove the parenthesis at both ends
		line = line.substring(1, line.indexOf(")"));
		
		// get the address value for the symbol
		int symbolAddress = table.getAddress(line);
		
		String biAddress = Integer.toBinaryString(symbolAddress);
		
		// add 0s to the binary code to make it 16-bit
		int length = biAddress.length();
		String zeros = "";
		
		for(int i = 0; i < 16 - length -1; i++) {
			zeros += "0";
		}
		
		line = "0" + zeros + biAddress;
		
		return line;
	}
	
	// Returns the binary code of the dest mnemonic
	private String dest(String seg) {
		String d1 = "0";
		String d2 = "0";
		String d3 = "0";
		
		if(seg.contains("M")) {
			d3 = "1";
		}
		
		if(seg.contains("D")) {
			d2 = "1";
		}
		
		if(seg.contains("A")) {
			d1 = "1";
		}
		
		String dest = d1 + d2 + d3;
		
		return dest;
	}
	
	// Returns the binary code of the comp mnemonic
	private String comp(String seg) {
		Map<String, String> compMap = new HashMap<>();
		compMap.put("0", "0101010");
		compMap.put("1", "0111111");
		compMap.put("-1", "0111010");
		compMap.put("D", "0001100");
		compMap.put("A", "0110000");
		compMap.put("M", "1110000");
		compMap.put("!D", "0001101");
		compMap.put("!A", "0110001");
		compMap.put("!M", "1110001");
		compMap.put("-D", "0001111");
		compMap.put("-A", "0110011");
		compMap.put("-M", "1110011");
		compMap.put("D+1", "0011111");
		compMap.put("A+1", "0110111");
		compMap.put("M+1", "1110111");
		compMap.put("D-1", "0001110");
		compMap.put("A-1", "0110010");
		compMap.put("M-1", "1110010");
		compMap.put("D+A", "0000010");
		compMap.put("D+M", "1000010");
		compMap.put("D-A", "0010011");
		compMap.put("D-M", "1010011");
		compMap.put("A-D", "0000111");
		compMap.put("M-D", "1000111");
		compMap.put("D&A", "0000000");
		compMap.put("D&M", "1000000");
		compMap.put("D|A", "0010101");
		compMap.put("D|M", "1010101");
		
		return compMap.get(seg);
	}
	
	// Returns the binary code of the jump mnemonic
	private String jump(String seg) {
		String jump = "000";
		
		if(seg.equals("JGT")) {
			jump = "001";
		}else if(seg.equals("JEQ")) {
			jump = "010";
		}else if(seg.equals("JGE")) {
			jump = "011";
		}else if(seg.equals("JLT")) {
			jump = "100";
		}else if(seg.equals("JNE")) {
			jump = "101";
		}else if(seg.equals("JLE")) {
			jump = "110";
		}else if(seg.equals("JMP")) {
			jump = "111";
		}
		
		return jump;
	}
	
	// Translates each field into its corresponding binary value
	public String convertToBinary(String line) {
		String binaryCode = null;
		
		int instructionType = Parser.instructionType(line);
		
		switch(instructionType) {
			case 0: // A instruction
				binaryCode = AtoBinary(line);
				break;
				
			case 1: // C instruction
				binaryCode = "111";
				
				String dest = Parser.dest(line);
				String comp = Parser.comp(line);
				String jump = Parser.jump(line);
				
				if(dest != null) {
					dest = dest(dest);
				}else {
					dest = "000";
				}
				
				comp = comp(comp);
				
				if(jump != null) {
					jump = jump(jump);
				}else {
					jump = "000";
				}
				
				binaryCode += comp + dest + jump ;
				break;
				
			case 2: // whitespace
				break;
				
			case 3: // label 
				binaryCode = labelToBinary(line);
				break;
		}
		
		return binaryCode;
	}
}
