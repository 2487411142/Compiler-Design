package ast;

public interface Type extends ASTNode {

    public <T> T accept(ASTVisitor<T> v);

    public String print();

    public boolean comp(Object other);


}
