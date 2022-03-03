package sem;

import ast.*;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {
	private Map<String, List<Type>> funTypeMap;
	private Map<String, Integer> arrayMap;
	private Type typeOfFun;

	public TypeCheckVisitor(){
		funTypeMap = new HashMap<>();
		arrayMap = new HashMap<>();
		typeOfFun = null;
	}


	@Override
	public Type visitProgram(Program p) {
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
	public Type visitStructTypeDecl(StructTypeDecl st) {
		for (VarDecl vd : st.aVarDecls){
			vd.accept(this);
		}
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
		if (vd.type == BaseType.VOID) {
			error("wrong type of " + vd.varName);
		}
		if (vd.type instanceof ArrayType){
			Type ty = ((ArrayType) vd.type).aType;
			arrayMap.put(vd.varName, ((ArrayType) vd.type).aNum);
			if (ty == BaseType.VOID){
				error("Array of voids");
			}
		}
		return null;
	}

	@Override
	public Type visitVarExpr(VarExpr v) {
		if (v.vd != null) {
			v.type = v.vd.type;
			return v.vd.type;
		}
		else {
			return null;
		}
	}

	@Override
	public Type visitFunDecl(FunDecl p) {
		List<Type> funType = new ArrayList<>();
		for (VarDecl vd : p.params){
			funType.add(vd.type);
		}
		funType.add(p.type);
		funTypeMap.put(p.name, funType);
		for (VarDecl vd : p.params){
			vd.accept(this);
		}
		Type old = typeOfFun;
		typeOfFun = p.type;
		p.block.accept(this);
		typeOfFun = old;
		return null;
	}

	@Override
	public Type visitBlock(Block b) {
		for (VarDecl vd : b.aVarDecls){
			vd.accept(this);
		}
		for (Stmt st : b.aStmts){
			st.accept(this);
		}
		return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f) {
		List<Type> funType = funTypeMap.get(f.aName);
		List<Type> paraTypes = new ArrayList<>();
		for (Expr e : f.aArgs){
			paraTypes.add(e.accept(this));
		}
		if (funType.size() != paraTypes.size() + 1){
			error("wrong parameter number of " + f.aName);
			return null;
		}
		else {
			for (int i = 0; i < paraTypes.size(); i++ ){
				if (funType.get(i) != paraTypes.get(i)){
					error("wrong parameter type of " + f.aName);
					return null;
				}
			}
			return funType.get(funType.size() - 1);
		}

	}

	@Override
	public Type visitPointerType(PointerType p) {
		return p;
	}

	@Override
	public Type visitStructType(StructType s) {
		return s;
	}

	@Override
	public Type visitArrayType(ArrayType a) {
		return a;
	}

	@Override
	public Type visitIntLiteral(IntLiteral i) {
		return BaseType.INT;
	}

	@Override
	public Type visitStrLiteral(StrLiteral s) {
		//arrayMap.put(s.aValue, s.aValue.length() + 1);
		return new ArrayType(BaseType.CHAR, s.aValue.length()+1);
	}

	@Override
	public Type visitChrLiteral(ChrLiteral c) {
		return BaseType.CHAR;
	}


	@Override
	public Type visitBinOp(BinOp b) {
		Type tl = b.aLhs.accept(this);
		Type tr = b.aRhs.accept(this);
		if (b.aOp == Op.ADD){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.SUB){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.MUL){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.DIV){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.MOD){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.OR){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.AND){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.GT){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.GE){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.LT){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		if (b.aOp == Op.LE){
			if (tl == BaseType.INT && tr == BaseType.INT){
				b.type = BaseType.INT;
				return BaseType.INT;
			}
			else {
				error("wrong expr of BinOp");
				return null;
			}
		}
		else {
			if (tl != tr || tl instanceof StructType  || tl instanceof ArrayType || tl == BaseType.VOID){
				error("wrong expr of BinOp");
				return null;

			}
			else {
				return BaseType.INT;
			}
		}
	}

	@Override
	public Type visitOp(Op o) {
		return null;
	}

	@Override
	public Type visitArrayAccessExpr(ArrayAccessExpr a) {
		Type tl = a.aArray.accept(this);
		Type tr = a.aIndex.accept(this);
		if (tr == BaseType.INT){
			int n2 = ((IntLiteral) a.aIndex).aValue;
			if (tl instanceof ArrayType ){
				int n = ((ArrayType) tl).aNum;
				if (n2 > n){
					error("out of array index");
					return null;
				}
				a.type = ((ArrayType) tl).aType;
				return ((ArrayType) tl).aType;
			}
			else if (tl instanceof PointerType )
				if( ((PointerType)tl).aType instanceof ArrayType){
					int n = ((ArrayType) ((PointerType)tl).aType).aNum;
					if (n2 > n){
						error("out of array index");
						return null;
					}
					a.type = ((ArrayType) ((PointerType)tl).aType).aType;
					return ((ArrayType) ((PointerType)tl).aType).aType;
				}
				else {
					error("wrong array type");
					return null;
				}
			else{
				error("wrong array type");
				return null;
				}
		}
		else {
			error("wrong array type");
			return null;
		}

	}

	@Override
	public Type visitFieldAccessExpr(FieldAccessExpr f) {
		f.type = f.vd.type;
		return f.vd.type;
	}

	@Override
	public Type visitValueAtExpr(ValueAtExpr v) {
		Type t = v.aExp.accept(this);
		if (t instanceof PointerType){
			v.type = ((PointerType) t).aType;
			return ((PointerType) t).aType;
		}
		else {
			error("wrong value at type");
			return null;
		}

	}

	@Override
	public Type visitAddressOfExpr(AddressOfExpr a) {
		a.type = new PointerType(a.aExp.accept(this));
		return a.type;
	}

	@Override
	public Type visitSizeOfExpr(SizeOfExpr s) {
		s.type = BaseType.INT;
		return BaseType.INT;
	}

	@Override
	public Type visitTypecastExpr(TypecastExpr t) {
		Type ty = t.aExp.accept(this);
		if (ty == BaseType.CHAR){
			if (t.aType == BaseType.INT){
				t.type = BaseType.INT;
				return BaseType.INT;
			}
			else{
				error("wrong type cast");
				return null;
			}
		}
		else if (ty instanceof ArrayType){
			if (t.aType instanceof PointerType && ((PointerType) t.aType).aType.print().
					equals(((ArrayType) ty).aType.print())){
				t.type = t.aType;
				return t.aType;
			}
			else {
				error("wrong type cast");
				return null;
			}
		}
		else if (ty instanceof PointerType){
			if (t.aType instanceof PointerType){
				t.type = t.aType;
				return t.aType;
			}
			else {
				error("wrong type cast");
				return null;
			}
		}
		else {
			error("wrong type cast");
			return null;
		}
	}

	@Override
	public Type visitExprStmt(ExprStmt e) {
		return e.aExp.accept(this);
	}

	@Override
	public Type visitWhile(While w) {
		if (w.aExp.accept(this) == BaseType.INT) {
			w.aStmt.accept(this);
		}
		else {
			error("wrong while");

		}
		return null;
	}

	@Override
	public Type visitIf(If i) {
		if (i.aCond.accept(this) == BaseType.INT){
			if (i.aStmt2 == null){
				i.aStmt1.accept(this);
			}
			else {
				i.aStmt1.accept(this);
				i.aStmt2.accept(this);
			}
		}
		else {
			error("wrong if");

		}
		return null;
	}

	@Override
	public Type visitAssign(Assign a) {
		Type t1 = a.aLhs.accept(this);
		Type t2 = a.aRhs.accept(this);
		if (t1 == null || t2 == null){
			error("assign wrong");
			return null;
		}
		if (t1.equals(t2)){
			if (!(t1 instanceof ArrayType) && t1 != BaseType.VOID){
				return null;
			}
			else {
				error("wrong assign");
				return null;
			}
		}
		else {
			error("wrong assign");
			return null;
		}
	}

	@Override
	public Type visitReturn(Return r) {
		if (typeOfFun == BaseType.VOID){
			if (r.aExp != null){
				error("should return nothing");
			}
		}
		else {
			if (r.aExp != null){
				Type ty = r.aExp.accept(this);
				if (ty == null || !typeOfFun.print().equals(ty.print())){
					error("inconsistent return type");
				}
			}
		}
		return null;
	}

	@Override
	public Type visitBaseType(BaseType bt) {
		// To be completed...
		return bt;
	}

	// To be completed...


}
