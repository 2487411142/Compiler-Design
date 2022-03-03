package ast;

import java.util.List;

public class StructTypeDecl implements ASTNode {

    public final StructType aStructType;
    public final List<VarDecl> aVarDecls;

    public StructTypeDecl(StructType pStructType, List<VarDecl> pVarDecls){
        aStructType = pStructType;
        aVarDecls = pVarDecls;
    }

    // to be completed

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructTypeDecl(this);
    }

}
