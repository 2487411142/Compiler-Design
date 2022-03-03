package sem;

import ast.*;

import java.util.HashMap;
import java.util.Map;

public class StructTypeCheckVisitor extends BaseSemanticVisitor<Void> {
    private Map<String, StructType> m;

    public StructTypeCheckVisitor(){
        m = new HashMap<>();
    }


    @Override
    public Void visitProgram(Program p) {
        for (StructTypeDecl std : p.structTypeDecls){
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls){
            vd.accept(this);
        }
        for (FunDecl fd : p.funDecls){
            fd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitStructTypeDecl(StructTypeDecl st) {
        StructType sType = m.get(st.aStructType.aName);
        if (sType != null){
            error(st.aStructType.aName + " has been declared");
        }
        else{
            m.put(st.aStructType.aName, st.aStructType);
        }
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd) {
        if (vd.type instanceof StructType){
            if (!m.containsKey(((StructType) vd.type).aName)){
                error(((StructType) vd.type).aName + " not declared");
            }
        }
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl p) {
        for (VarDecl vd : p.params){
            vd.accept(this);
        }
        p.block.accept(this);
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr f) {
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        for (VarDecl vd : b.aVarDecls){
            vd.accept(this);
        }
        for (Stmt st : b.aStmts){
            st.accept(this);
        }
        return null;
    }

    @Override
    public Void visitPointerType(PointerType p) {
        return null;
    }

    @Override
    public Void visitStructType(StructType s) {
        return null;
    }

    @Override
    public Void visitArrayType(ArrayType a) {
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral i) {
        return null;
    }

    @Override
    public Void visitStrLiteral(StrLiteral s) {
        return null;
    }

    @Override
    public Void visitChrLiteral(ChrLiteral c) {
        return null;
    }

    @Override
    public Void visitBinOp(BinOp b) {
        return null;
    }

    @Override
    public Void visitOp(Op o) {
        return null;
    }

    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr a) {
        return null;
    }

    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr f) {
        return null;
    }

    @Override
    public Void visitValueAtExpr(ValueAtExpr v) {
        return null;
    }

    @Override
    public Void visitAddressOfExpr(AddressOfExpr a) {
        return null;
    }

    @Override
    public Void visitSizeOfExpr(SizeOfExpr s) {
        return null;
    }

    @Override
    public Void visitTypecastExpr(TypecastExpr t) {
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt e) {
        return null;
    }

    @Override
    public Void visitWhile(While w) {
        w.aStmt.accept(this);
        return null;
    }

    @Override
    public Void visitIf(If i) {
        i.aStmt1.accept(this);
        if (i.aStmt2 != null){
            i.aStmt2.accept(this);
        }
        return null;
    }

    @Override
    public Void visitAssign(Assign a) {
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        return null;
    }

    @Override
    public Void visitBaseType(BaseType bt) {
        return null;
    }
}