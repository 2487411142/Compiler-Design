package ast;

public class ArrayAccessExpr extends Expr{
    public final Expr aArray;
    public final Expr aIndex;

    public ArrayAccessExpr(Expr pArray, Expr pIndex){
        aArray = pArray;
        aIndex = pIndex;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitArrayAccessExpr(this);
    }
}
