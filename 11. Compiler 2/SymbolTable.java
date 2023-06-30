package compiler;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String, Symbol> classTable;
	private Map<String, Symbol> subroutineTable;
	private Map<String, Integer> indices;
	
	public SymbolTable() {
		classTable = new HashMap<>();
		subroutineTable = new HashMap<>();
		indices = new HashMap<>();
		indices.put("field", 0);
		indices.put("static", 0);
		indices.put("argument", 0);
		indices.put("var", 0);
	}
	
	// empties the subroutine table
	public void reset() {
		subroutineTable.clear();
		indices.put("argument", 0);
		indices.put("var", 0);
	}
	
	// defines and adds to the table a new variable
	public void define(String name, String type, String kind) {
		int index = indices.get(kind);
		Symbol symbol = new Symbol(type, kind, index);
		
		indices.put(kind, index + 1);
		
		if(kind.equals("field") || kind.equals("static")) { // class table
			classTable.put(name, symbol);
			
		}else if(kind.equals("argument") || kind.equals("var")) { // subroutine table
			subroutineTable.put(name, symbol);
			
		}else {
			System.out.println("Invalid Type Error");
		}
	}
	
	// returns the number of variables of the given kind in the table it was allocated to
	public int varCount(String kind) {
		return indices.get(kind);
	}
	
	// returns the kind of the named identifier; returns NONE if the identifier is not found
	public String kindOf(String name) {
		String kind = "NONE";
		
		if(classTable.containsKey(name)) {
			kind = classTable.get(name).getKind();
			
		}else if(subroutineTable.containsKey(name)) {
			kind = subroutineTable.get(name).getKind();
			
		}
		
		return kind;
	}
	
	// returns the type of the named variable
	public String typeOf(String name) {
		String type = "";
		
		if(classTable.containsKey(name)) {
			type = classTable.get(name).getType();
			
		}else if(subroutineTable.containsKey(name)) {
			type = subroutineTable.get(name).getType();
			
		}
		
		return type;
	}
	
	// returns the index of the named variable
	public int indexOf(String name) {
		int index = -1; // in case the named identifier doesn't exist in either table
		
		if(classTable.containsKey(name)) {
			index = classTable.get(name).getIndex();
			
		}else if(subroutineTable.containsKey(name)) {
			index = subroutineTable.get(name).getIndex();
			
		}
		
		return index;
	}
}
