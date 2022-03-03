package ast;

public enum BaseType implements Type {
    INT, CHAR, VOID;

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBaseType(this);
    }

    @Override
    public String print() {
        return this.toString();
    }

    @Override
    public boolean comp(Object other) {
        return false;
    }
}
