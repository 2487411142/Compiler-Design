package ast;

public class BinOp extends Expr {

    public final Op aOp;
    public final Expr aLhs;
    public final Expr aRhs;

    public BinOp(Op pOp, Expr pLhs, Expr pRhs){
        aOp = pOp;
        aLhs = pLhs;
        aRhs = pRhs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBinOp(this);
    }
}
