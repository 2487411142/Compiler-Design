package ast;

public interface ASTVisitor<T> {

    class ShouldNotReach extends Error {
        public ShouldNotReach() {
            super("Current visitor should never reach this node");
        }
    }

    public T visitProgram(Program p);
    public T visitStructTypeDecl(StructTypeDecl st);
    public T visitVarDecl(VarDecl vd);
    public T visitVarExpr(VarExpr v);
    public T visitFunDecl(FunDecl p);
    public T visitFunCallExpr(FunCallExpr f);
    public T visitBlock(Block b);
    public T visitPointerType(PointerType p);
    public T visitStructType(StructType s);
    public T visitArrayType(ArrayType a);
    public T visitIntLiteral(IntLiteral i);
    public T visitStrLiteral(StrLiteral s);
    public T visitChrLiteral(ChrLiteral c);
    public T visitBinOp(BinOp b);
    public T visitOp(Op o);
    public T visitArrayAccessExpr(ArrayAccessExpr a);
    public T visitFieldAccessExpr(FieldAccessExpr f);
    public T visitValueAtExpr(ValueAtExpr v);
    public T visitAddressOfExpr(AddressOfExpr a);
    public T visitSizeOfExpr(SizeOfExpr s);
    public T visitTypecastExpr(TypecastExpr t);
    public T visitExprStmt(ExprStmt e);
    public T visitWhile(While w);
    public T visitIf(If i);
    public T visitAssign(Assign a);
    public T visitReturn(Return r);
    public T visitBaseType(BaseType bt);


    // to complete ... (should have one visit method for each concrete AST node class)
}
