package ast;

public class IntLiteral extends Expr {
    public final int aValue;

    public IntLiteral(int pValue){
        aValue = pValue;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitIntLiteral(this);
    }
}
