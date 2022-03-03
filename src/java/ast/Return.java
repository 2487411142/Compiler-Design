package ast;


public class Return extends Stmt{
    public final Expr aExp;

    public Return(Expr pExp){
        aExp = pExp;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitReturn(this);
    }
}
