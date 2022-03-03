package sem;

import ast.VarDecl;

public class VarSymbol extends Symbol{
    public final VarDecl aVd;

    public VarSymbol(VarDecl pVd){
        super(pVd.varName);
        aVd = pVd;
    }

    @Override
    public boolean isFun() {
        return false;
    }

    @Override
    public boolean isVar() {
        return true;
    }
}
