package sem;

import ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StructNameAnalysisVisitor extends BaseSemanticVisitor<Void>{

    private Scope aScope;
    private List<StructField> listOfFields;
    private List<StructVar> listOfVars;
    private VarDecl vd;

    public StructNameAnalysisVisitor(){
        aScope = new Scope();
        listOfFields = new ArrayList<>();
        listOfVars = new ArrayList<>();
    }

    @Override
    public Void visitProgram(Program p) {
        for(StructTypeDecl std : p.structTypeDecls){
            Scope old = aScope;
            aScope = new Scope(old);
            std.accept(this);
            aScope = old;
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
        for(VarDecl vd : st.aVarDecls){
            // add structVar to listOfVars
            if (vd.type instanceof StructType){
                listOfVars.add(new StructVar((StructType) vd.type, vd));
            }
            // check same name.
            Symbol s = aScope.lookupCurrent(vd.varName);
            if (s != null){
                error(vd.varName + " declared.");
            }
            else {
                aScope.put(new VarSymbol(vd));
                listOfFields.add(new StructField(st.aStructType, vd.varName, vd));
            }
        }
        return null;
    }

    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr f) {
        f.aStruct.accept(this);
        // new
        String svName;
        boolean found = false;
        if (f.aStruct instanceof FieldAccessExpr) {
            svName = ((FieldAccessExpr) f.aStruct).aField;
            if (vd == null){
                error("wrong field access of the first var");
                return null;
            }
            StructType st = (StructType)vd.type;
            for (StructVar sv : listOfVars){
                if (sv.getName().equals(svName)){
                    String field = f.aField;
                    // check if the field name and structType are met
                    for (StructField sf : listOfFields){
                        if (sf.getName().equals(field) && (sf.getType().aName).equals(st.aName)){
                            found = true;
                            f.vd = sv.getVd();
                            vd = sf.getVd();
                            break;
                        }
                    }
                }
            }
            if (!found){
                error("wrong field access of " + svName + "." + f.aField);
            }
        }
        else {
            if (f.aStruct instanceof VarExpr) {
                svName = ((VarExpr) f.aStruct).name;
                for (StructVar sv : listOfVars) {
                    if (sv.getName().equals(svName)) {
                        StructType st = sv.getType();
                        String field = f.aField;
                        // check if the field name and structType are met
                        for (StructField sf : listOfFields) {
                            if (sf.getName().equals(field) && (sf.getType().aName).equals(st.aName)) {
                                found = true;
                                f.vd = sf.getVd();
                                vd = sf.getVd();
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                    error("wrong field access of " + svName + "." + f.aField);
                }
            }
            else {
                error("wrong field access of the first var");
                return null;
            }
        }
        // use StructVar to find the structType
//        for (StructVar sv : listOfVars){
//            if (sv.getName().equals(svName)){
//                StructType st = sv.getType();
//                String field = f.aField;
//                // check if the field name and structType are met
//                for (StructField sf : listOfFields){
//                    if (sf.getName().equals(field) && sf.getType() == st){
//                        found = true;
//                        f.vd = sv.getVd();
//                        break;
//                    }
//                }
//            }
//        }
//        if (!found){
//            error("wrong field access");
//        }

        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd) {
        if (vd.type instanceof StructType){
            listOfVars.add(new StructVar((StructType) vd.type, vd));
        }
        Symbol s = aScope.lookupCurrent(vd.varName);
        if (s != null){
            error(vd.varName + " declared.");
        }
        else {
            aScope.put(new VarSymbol(vd));
        }
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        Symbol s = aScope.lookup(v.name);
        if (s == null || !s.isVar()){
            error(v.name + " is not declared");
        }
        else {
            v.vd = ((VarSymbol)s).aVd;
        }
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl p) {
        Symbol s = aScope.lookupCurrent(p.name);
        if (s != null){
            error(p.name + " declared.");
        }
        else {
            aScope.put(new FunSymbol(p));
        }

        Scope old = aScope;
        aScope = new Scope(old);
        for (VarDecl vd : p.params){
            vd.accept(this);
        }
        p.block.accept(this);
        aScope = old;
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        for(VarDecl vd : b.aVarDecls){
            vd.accept(this);
        }
        for(Stmt st : b.aStmts){
            st.accept(this);
        }
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr f) {
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
        b.aLhs.accept(this);
        b.aRhs.accept(this);
        return null;
    }

    @Override
    public Void visitOp(Op o) {
        return null;
    }

    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr a) {
        a.aArray.accept(this);
        return null;
    }


    @Override
    public Void visitValueAtExpr(ValueAtExpr v) {
        v.aExp.accept(this);
        return null;
    }

    @Override
    public Void visitAddressOfExpr(AddressOfExpr a) {
        a.aExp.accept(this);
        return null;
    }

    @Override
    public Void visitSizeOfExpr(SizeOfExpr s) {
        return null;
    }

    @Override
    public Void visitTypecastExpr(TypecastExpr t) {
        t.aExp.accept(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt e) {
        e.aExp.accept(this);
        return null;
    }

    @Override
    public Void visitWhile(While w) {
        w.aExp.accept(this);
        Scope old = aScope;
        aScope = new Scope(old);
        w.aStmt.accept(this);
        aScope = old;
        return null;
    }

    @Override
    public Void visitIf(If i) {
        i.aCond.accept(this);
        Scope old = aScope;
        aScope = new Scope(old);
        i.aStmt1.accept(this);
        if (i.aStmt2 != null){
            i.aStmt2.accept(this);
        }
        aScope = old;
        return null;
    }

    @Override
    public Void visitAssign(Assign a) {
        a.aLhs.accept(this);
        a.aRhs.accept(this);
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        if(r.aExp != null) {
            r.aExp.accept(this);
        }
        return null;
    }

    @Override
    public Void visitBaseType(BaseType bt) {
        return null;
    }
}
