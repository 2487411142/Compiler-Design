package ast;

import java.util.List;

public class Block extends Stmt {
    public final List<VarDecl> aVarDecls;
    public final List<Stmt> aStmts;

    public Block(List<VarDecl> pVarDecls, List<Stmt> pStmts){
        aVarDecls = pVarDecls;
        aStmts = pStmts;
    }
    // to complete ...

    public <T> T accept(ASTVisitor<T> v) {
	    return v.visitBlock(this);
    }
}
