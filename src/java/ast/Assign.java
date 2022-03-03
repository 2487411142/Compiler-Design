package ast;

public class Assign extends Stmt{
    public final Expr aLhs;
    public final Expr aRhs;

    public Assign(Expr pLhs, Expr pRhs){
        aLhs = pLhs;
        aRhs = pRhs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitAssign(this);
    }
}
