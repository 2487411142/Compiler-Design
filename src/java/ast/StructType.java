package ast;

public class StructType implements Type{
    public final String aName;

    public StructType(String pName){
        aName = pName;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructType(this);
    }

    @Override
    public String print() {
        return "StructType";
    }

    @Override
    public boolean comp(Object other) {
        return false;
    }


}
