package ast;

public class ArrayType implements Type {
    public final int aNum;
    public final Type aType;

    public ArrayType(Type pType, int pNum){
        aType = pType;
        aNum = pNum;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitArrayType(this);
    }

    @Override
    public String print() {
        return "ArrayType";
    }

    @Override
    public boolean comp(Object other) {
        return false;
    }
}
