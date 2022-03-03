package ast;

public class ChrLiteral extends Expr implements ASTNode{
    public final String aValue;

    public ChrLiteral(String pValue){
        aValue = pValue;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitChrLiteral(this);
    }
}
