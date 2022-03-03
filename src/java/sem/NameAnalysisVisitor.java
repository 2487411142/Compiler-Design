package sem;

import ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {

	private Scope aScope;
	private Scope structScope;
	private Map<String, StructType> v;


	public NameAnalysisVisitor(){
		aScope = new Scope();
		v = new HashMap<>();
	}

	private void addBuiltInFun(){
		// add print_s
		List<VarDecl> paras = new ArrayList<VarDecl>();
		paras.add(new VarDecl(new PointerType(BaseType.CHAR), "s"));

		Block b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl ps = new FunDecl(BaseType.VOID, "print_s", paras, b);
		aScope.put(new FunSymbol(ps));

		// add print_i
		paras = new ArrayList<VarDecl>();
		paras.add(new VarDecl(BaseType.INT, "i"));

		b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl pi = new FunDecl(BaseType.VOID, "print_i", paras, b);
		aScope.put(new FunSymbol(pi));

		// add print_c
		paras = new ArrayList<VarDecl>();
		paras.add(new VarDecl(BaseType.CHAR, "c"));

		b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl pc = new FunDecl(BaseType.VOID, "print_c", paras, b);
		aScope.put(new FunSymbol(pc));

		// add read_c
		paras = new ArrayList<VarDecl>();

		b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl rc = new FunDecl(BaseType.CHAR, "read_c", paras, b);
		aScope.put(new FunSymbol(rc));

		// add read_i
		paras = new ArrayList<VarDecl>();

		b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl ri = new FunDecl(BaseType.INT, "read_i", paras, b);
		aScope.put(new FunSymbol(ri));

		// add malloc
		paras = new ArrayList<VarDecl>();
		paras.add(new VarDecl(BaseType.INT, "size"));

		b = new Block(new ArrayList<VarDecl>(), new ArrayList<Stmt>());

		FunDecl malloc = new FunDecl(new PointerType(BaseType.VOID), "mcmalloc", paras, b);
		aScope.put(new FunSymbol(malloc));
	}

	@Override
	public Void visitBaseType(BaseType bt) {
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
		addBuiltInFun();
		//Scope old = aScope;
		//aScope = new Scope();
//		for (StructTypeDecl std : p.structTypeDecls){
//			std.accept(this);
//		}
		//structScope = aScope;
		//aScope = old;
		for (VarDecl vd : p.varDecls){
			vd.accept(this);
		}
		for (FunDecl fd : p.funDecls){
			fd.accept(this);
		}
		return null;
	}

	@Override
	public Void visitStructTypeDecl(StructTypeDecl std) {
		std.aStructType.accept(this);
		for (VarDecl vd : std.aVarDecls){
			//vd.accept(this);
			if (vd.type instanceof StructType){
				v.put(vd.varName, (StructType) vd.type);
			}
			else {
				v.put(vd.varName, std.aStructType);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(std.aStructType.aName);
			sb.append(vd.varName);
			String name = sb.toString();
			Symbol s = aScope.lookupCurrent(name);
			if (s != null){
				error(vd.varName + " declared.");
			}
			else {
				aScope.putStructVar(name, new VarSymbol(vd));
			}
		}
		return null;
	}

//	private void structVar(VarDecl vd){
//		Symbol s = structScope.lookupCurrent(vd.varName);
//		if (s != null){
//			error(vd.varName + " declared.");
//		}
//		else {
//			structScope.put(new VarSymbol(vd));
//		}
//	}

	@Override
	public Void visitBlock(Block b) {
//		Scope old = aScope;
//		aScope = new Scope(old);
		for(VarDecl vd : b.aVarDecls){
			vd.accept(this);
		}
		for(Stmt st : b.aStmts){
			st.accept(this);
		}
//		aScope = old;
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
	public Void visitVarDecl(VarDecl vd) {
		if (vd.type instanceof StructType){
			v.put(vd.varName, (StructType) vd.type);
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
	public Void visitPointerType(PointerType p) {
		p.aType.accept(this);
		return null;
	}

	@Override
	public Void visitStructType(StructType st) {
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
	public Void visitFunCallExpr(FunCallExpr f) {
		Symbol s = aScope.lookup(f.aName);
		if(s == null || !s.isFun()){
			error("function " + f.aName + " is not declared");
		}
		else {
			for (Expr e : f.aArgs){
				e.accept(this);
			}
			f.fd = ((FunSymbol)s).aFd;
		}
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
	public Void visitFieldAccessExpr(FieldAccessExpr f) {
//		f.aStruct.accept(this);
//		// new
//		StringBuilder sb = new StringBuilder();
//		if (f.aStruct instanceof FieldAccessExpr) {
//			String prefix = ((FieldAccessExpr)f.aStruct).aField;
//			if (v.containsKey(prefix)){
//				String type = v.get(prefix).aName;
//				sb.append(type);
//				sb.append(f.aField);
//				String name = sb.toString();
//				Symbol s = aScope.lookup(name);
//				if (s == null || !s.isVar()){
//					error(f.aField + " is not declared");
//				}
//			}
//			else{
//				error("wrong field access!");
//			}
//
//		}
//		else {
//			String prefix = ((VarExpr)f.aStruct).name;
//			if (v.containsKey(prefix)){
//				String type = v.get(prefix).aName;
//				sb.append(type);
//				sb.append(f.aField);
//				String name = sb.toString();
//				Symbol s = aScope.lookup(name);
//				if (s == null || !s.isVar()){
//					error(f.aField + " is not declared");
//				}
//			}
//			else{
//				error("wrong field access!");
//			}
//		}

// normal
//		Symbol s = structScope.lookup(f.aField);
//		if (s == null || !s.isVar()){
//			error(f.aField + " is not declared");
//		}
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
		aScope = old;
		if (i.aStmt2 != null){
			Scope old2 = aScope;
			aScope = new Scope(old2);
			i.aStmt2.accept(this);
			aScope = old2;
		}

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

	// To be completed...


}
