package ast;

public class TypecastExpr extends Expr {
    public final Type aType;
    public final Expr aExp;

    public TypecastExpr(Type pType, Expr pExp){
        aType = pType;
        aExp = pExp;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitTypecastExpr(this);
    }
}
