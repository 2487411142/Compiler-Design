package ast;

public class If extends Stmt{
    public final Expr aCond;
    public final Stmt aStmt1;
    public final Stmt aStmt2;

    public If(Expr pCond, Stmt pStmt1, Stmt pStmt2){
        aCond = pCond;
        aStmt1 = pStmt1;
        aStmt2 = pStmt2;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitIf(this);
    }
}
