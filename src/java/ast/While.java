package ast;

public class While extends Stmt{
    public final Expr aExp;
    public final Stmt aStmt;

    public While(Expr pExp, Stmt pStmt){
        aExp = pExp;
        aStmt = pStmt;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitWhile(this);
    }
}
