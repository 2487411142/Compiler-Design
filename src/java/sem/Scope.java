package sem;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private Scope aOuter;
	private Map<String, Symbol> currentTable;
	
	public Scope(Scope pOuter) {
		aOuter = pOuter;
		currentTable = new HashMap<>();
	}

	public Scope(Scope pOuter, Scope pInner) {
		aOuter = pOuter;
		currentTable = new HashMap<>();
	}
	
	public Scope() {
		aOuter = null;
		currentTable = new HashMap<>();
	}
	
	public Symbol lookup(String name) {
		if (currentTable.containsKey(name)){
			return currentTable.get(name);
		}
		else {
			if (aOuter == null){
				return null;
			}
			else {
				return aOuter.lookup(name);
			}
		}
	}
	
	public Symbol lookupCurrent(String name) {
		return currentTable.get(name);
	}


	public void put(Symbol sym) {
		currentTable.put(sym.name, sym);
	}

	public void putStructVar(String name, Symbol sym){
		currentTable.put(name, sym);
	}


}
