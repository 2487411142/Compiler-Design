package ast;

public class ValueAtExpr extends Expr {
    public final Expr aExp;

    public ValueAtExpr(Expr pExp){
        aExp = pExp;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitValueAtExpr(this);
    }
}
