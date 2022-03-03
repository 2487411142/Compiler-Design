package ast;

public class StrLiteral extends Expr implements ASTNode{

    public final String aValue;

    public StrLiteral(String pValue){
        aValue = pValue;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStrLiteral(this);
    }
}
