package sem;

import ast.FunDecl;

public class FunSymbol extends Symbol{
    public final FunDecl aFd;

    public FunSymbol(FunDecl pFd) {
        super(pFd.name);
        aFd = pFd;
    }

    @Override
    public boolean isFun() {
        return true;
    }

    @Override
    public boolean isVar() {
        return false;
    }
}
