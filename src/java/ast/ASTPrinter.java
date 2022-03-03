package ast;

import java.io.PrintWriter;

public class ASTPrinter implements ASTVisitor<Void> {

    private PrintWriter writer;

    public ASTPrinter(PrintWriter writer) {
            this.writer = writer;
    }

    @Override
    public Void visitBlock(Block b) {
        writer.print("Block(");
        String delimiter = "";
        for (VarDecl vd : b.aVarDecls){
            writer.print(delimiter);
            delimiter = ",";
            vd.accept(this);
        }
        for (Stmt s : b.aStmts){
            writer.print(delimiter);
            delimiter = ",";
            s.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl fd) {
        writer.print("FunDecl(");
        fd.type.accept(this);
        writer.print(","+fd.name+",");
        for (VarDecl vd : fd.params) {
            vd.accept(this);
            writer.print(",");
        }
        fd.block.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitProgram(Program p) {
        writer.print("Program(");
        String delimiter = "";
        for (StructTypeDecl std : p.structTypeDecls) {
            writer.print(delimiter);
            delimiter = ",";
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            writer.print(delimiter);
            delimiter = ",";
            vd.accept(this);
        }
        for (FunDecl fd : p.funDecls) {
            writer.print(delimiter);
            delimiter = ",";
            fd.accept(this);
        }
        writer.print(")");
	    writer.flush();
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd){
        writer.print("VarDecl(");
        vd.type.accept(this);
        writer.print(","+vd.varName);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        writer.print("VarExpr(");
        writer.print(v.name);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitBaseType(BaseType bt) {
        //writer.print("BaseType(");
        writer.print(bt.toString());
        //writer.print(")");
        return null;
    }

    @Override
    public Void visitStructTypeDecl(StructTypeDecl st) {
        writer.print("StructTypeDecl(");
        st.aStructType.accept(this);
        for (VarDecl vd : st.aVarDecls){
            writer.print(",");
            vd.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitPointerType(PointerType p) {
        writer.print("PointerType(");
        p.aType.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitStructType(StructType s) {
        writer.print("StructType(");
        writer.print(s.aName);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitArrayType(ArrayType a) {
        writer.print("ArrayType(");
        a.aType.accept(this);
        writer.print(",");
        writer.print(a.aNum);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral i) {
        writer.print("IntLiteral(");
        writer.print(i.aValue);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitStrLiteral(StrLiteral s) {
        writer.print("StrLiteral(");
        writer.print(s.aValue);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitChrLiteral(ChrLiteral c) {
        writer.print("CharLiteral(");
        writer.print(c.aValue);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr f) {
        writer.print("FunCallExpr(");
        writer.print(f.aName);
        for (Expr e : f.aArgs){
            writer.print(",");
            e.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitBinOp(BinOp b) {
        writer.print("BinOp(");
        b.aLhs.accept(this);
        writer.print(",");
        b.aOp.accept(this);
        writer.print(",");
        b.aRhs.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitOp(Op o) {
        //writer.print("Op(");
        writer.print(o.toString());
        //writer.print(")");
        return null;
    }

    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr a) {
        writer.print("ArrayAccessExpr(");
        a.aArray.accept(this);
        writer.print(",");
        a.aIndex.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr f) {
        writer.print("FieldAccessExpr(");
        f.aStruct.accept(this);
        writer.print(",");
        writer.print(f.aField);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitValueAtExpr(ValueAtExpr v) {
        writer.print("ValueAtExpr(");
        v.aExp.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitAddressOfExpr(AddressOfExpr a) {
        writer.print("AddressOfExpr(");
        a.aExp.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitSizeOfExpr(SizeOfExpr s) {
        writer.print("SizeOfExpr(");
        s.aType.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitTypecastExpr(TypecastExpr t) {
        writer.print("TypecastExpr(");
        t.aType.accept(this);
        writer.print(",");
        t.aExp.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt e) {
        writer.print("ExprStmt(");
        e.aExp.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitWhile(While w) {
        writer.print("While(");
        w.aExp.accept(this);
        writer.print(",");
        w.aStmt.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitIf(If i) {
        writer.print("If(");
        i.aCond.accept(this);
        writer.print(",");
        i.aStmt1.accept(this);
        if (i.aStmt2 != null){
            writer.print(",");
            i.aStmt2.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitAssign(Assign a) {
        writer.print("Assign(");
        a.aLhs.accept(this);
        writer.print(",");
        a.aRhs.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        writer.print("Return(");
        if (r.aExp != null){
            r.aExp.accept(this);
        }
        writer.print(")");
        return null;
    }



    // to complete ...
    
}
