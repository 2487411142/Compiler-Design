package sem;

import ast.*;

public class LvalueCheckVisitor extends BaseSemanticVisitor {

    @Override
    public Object visitProgram(Program p) {
        for (FunDecl fd : p.funDecls){
            fd.accept(this);
        }
        return null;
    }

    @Override
    public Object visitStructTypeDecl(StructTypeDecl st) {
        return null;
    }

    @Override
    public Object visitVarDecl(VarDecl vd) {
        return null;
    }

    @Override
    public Object visitVarExpr(VarExpr v) {
        return null;
    }

    @Override
    public Object visitFunDecl(FunDecl p) {
        p.block.accept(this);
        return null;
    }

    @Override
    public Object visitFunCallExpr(FunCallExpr f) {
        return null;
    }

    @Override
    public Object visitBlock(Block b) {
        for (Stmt st : b.aStmts){
            st.accept(this);
        }
        return null;
    }

    @Override
    public Object visitPointerType(PointerType p) {
        return null;
    }

    @Override
    public Object visitStructType(StructType s) {
        return null;
    }

    @Override
    public Object visitArrayType(ArrayType a) {
        return null;
    }

    @Override
    public Object visitIntLiteral(IntLiteral i) {
        return null;
    }

    @Override
    public Object visitStrLiteral(StrLiteral s) {
        return null;
    }

    @Override
    public Object visitChrLiteral(ChrLiteral c) {
        return null;
    }

    @Override
    public Object visitBinOp(BinOp b) {
        return null;
    }

    @Override
    public Object visitOp(Op o) {
        return null;
    }

    @Override
    public Object visitArrayAccessExpr(ArrayAccessExpr a) {
        return null;
    }

    @Override
    public Object visitFieldAccessExpr(FieldAccessExpr f) {
        return null;
    }

    @Override
    public Object visitValueAtExpr(ValueAtExpr v) {
        return null;
    }

    @Override
    public Object visitAddressOfExpr(AddressOfExpr a) {
        if (!(a.aExp instanceof VarExpr) && !(a.aExp instanceof ValueAtExpr) && !(a.aExp instanceof ArrayAccessExpr)
                && !(a.aExp instanceof FieldAccessExpr)) {
            error("wrong lvalue in assign");
        }
        return null;
    }

    @Override
    public Object visitSizeOfExpr(SizeOfExpr s) {
        return null;
    }

    @Override
    public Object visitTypecastExpr(TypecastExpr t) {
        return null;
    }

    @Override
    public Object visitExprStmt(ExprStmt e) {
        e.aExp.accept(this);
        return null;
    }

    @Override
    public Object visitWhile(While w) {
        w.aStmt.accept(this);
        return null;
    }

    @Override
    public Object visitIf(If i) {
        i.aStmt1.accept(this);
        if (i.aStmt2 != null){
            i.aStmt2.accept(this);
        }
        return null;
    }

    @Override
    public Object visitAssign(Assign a) {
        if (!(a.aLhs instanceof VarExpr) && !(a.aLhs instanceof ValueAtExpr) && !(a.aLhs instanceof ArrayAccessExpr)
        && !(a.aLhs instanceof FieldAccessExpr)) {
            error("wrong lvalue in assign");
        }
        return null;

    }

    @Override
    public Object visitReturn(Return r) {
        return null;
    }

    @Override
    public Object visitBaseType(BaseType bt) {
        return null;
    }
}
