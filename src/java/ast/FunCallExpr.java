package ast;

import java.util.List;

public class FunCallExpr extends Expr{

    public final String aName;
    public final List<Expr> aArgs;
    public FunDecl fd;

    public FunCallExpr(String pName, List<Expr> pArgs){
        aName = pName;
        aArgs = pArgs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFunCallExpr(this);
    }
}
