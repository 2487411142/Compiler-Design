package sem;

import ast.StructType;
import ast.VarDecl;

public class StructField {
    private StructType aTypeOfStruct;
    private String aName;
    private VarDecl aVd;

    public StructField(StructType pTypeOfStruct, String pName, VarDecl pVd){
        aName = pName;
        aTypeOfStruct = pTypeOfStruct;
        aVd = pVd;
    }

    public StructType getType(){
        return aTypeOfStruct;
    }

    public String getName() {
        return aName;
    }

    public VarDecl getVd() {
        return aVd;
    }
}
