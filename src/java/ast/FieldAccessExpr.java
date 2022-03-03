package ast;

public class FieldAccessExpr extends Expr {

    public final Expr aStruct;
    public final String aField;
    public VarDecl vd;

    public FieldAccessExpr(Expr pStruct, String pField){
        aStruct = pStruct;
        aField = pField;
    }
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFieldAccessExpr(this);
    }
}
