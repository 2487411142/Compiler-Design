package ast;

public class AddressOfExpr extends Expr {
    public final Expr aExp;

    public AddressOfExpr(Expr pExp){
        aExp = pExp;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitAddressOfExpr(this);
    }
}
