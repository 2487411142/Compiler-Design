package ast;

public class PointerType implements Type {
    public final Type aType;

    public PointerType(Type pType){
        aType = pType;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitPointerType(this);
    }

    @Override
    public String print() {
        return "PointerType";
    }

    @Override
    public boolean comp(Object other) {
        return false;
    }
}
