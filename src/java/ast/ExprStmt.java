package ast;

public class ExprStmt extends Stmt{
    public final Expr aExp;

    public ExprStmt(Expr pExp){
        aExp = pExp;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitExprStmt(this);
    }
}
