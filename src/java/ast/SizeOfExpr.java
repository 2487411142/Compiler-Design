package ast;

public class SizeOfExpr extends Expr {
    public final Type aType;

    public SizeOfExpr(Type pType){
        aType = pType;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitSizeOfExpr(this);
    }
}
