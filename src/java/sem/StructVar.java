package sem;

import ast.StructType;
import ast.VarDecl;

public class StructVar {
    private StructType aTypeOfStruct;
    private VarDecl aVd;

    public StructVar(StructType pTypeOfStruct, VarDecl pVd){
        aTypeOfStruct = pTypeOfStruct;
        aVd = pVd;
    }

    public StructType getType(){
        return aTypeOfStruct;
    }

    public String getName() {
        return aVd.varName;
    }

    public VarDecl getVd() {
        return aVd;
    }
}
