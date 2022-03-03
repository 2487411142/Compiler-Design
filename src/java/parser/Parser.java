package parser;

import ast.*;
import lexer.Token;
import lexer.Token.TokenClass;
import lexer.Tokeniser;

import javax.swing.plaf.basic.BasicComboPopup;
import java.rmi.server.RMIClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author cdubach
 */
public class Parser {

    private Token token;

    // use for backtracking (useful for distinguishing decls from procs when parsing a program for instance)
    private Queue<Token> buffer = new LinkedList<>();

    private final Tokeniser tokeniser;



    public Parser(Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public Program parse() {
        // get the first token
        nextToken();

        return parseProgram();
    }

    public int getErrorCount() {
        return error;
    }

    private int error = 0;
    private Token lastErrorToken;

    private void error(TokenClass... expected) {

        if (lastErrorToken == token) {
            // skip this error, same token causing trouble
            return;
        }

        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (TokenClass e : expected) {
            sb.append(sep);
            sb.append(e);
            sep = "|";
        }
        System.out.println("Parsing error: expected ("+sb+") found ("+token+") at "+token.position);

        error++;
        lastErrorToken = token;
    }

    /*
     * Look ahead the i^th element from the stream of token.
     * i should be >= 1
     */
    private Token lookAhead(int i) {
        // ensures the buffer has the element we want to look ahead
        while (buffer.size() < i)
            buffer.add(tokeniser.nextToken());
        assert buffer.size() >= i;

        int cnt=1;
        for (Token t : buffer) {
            if (cnt == i)
                return t;
            cnt++;
        }

        assert false; // should never reach this
        return null;
    }


    /*
     * Consumes the next token from the tokeniser or the buffer if not empty.
     */
    private void nextToken() {
        if (!buffer.isEmpty())
            token = buffer.remove();
        else
            token = tokeniser.nextToken();
    }

    /*
     * If the current token is equals to the expected one, then skip it, otherwise report an error.
     * Returns the expected token or null if an error occurred.
     */
    private Token expect(TokenClass... expected) {
        for (TokenClass e : expected) {
            if (e == token.tokenClass) {
                Token cur = token;
                nextToken();
                return cur;
            }
        }

        error(expected);
        return null;
    }

    /*
    * Returns true if the current token is equals to any of the expected ones.
    */
    private boolean accept(TokenClass... expected) {
        boolean result = false;
        for (TokenClass e : expected)
            result |= (e == token.tokenClass);
        return result;
    }


    private Program parseProgram() {
        parseIncludes();
        List<StructTypeDecl> stds = parseStructDecls();
        List<VarDecl> vds = parseVarDecls();
        List<FunDecl> fds = parseFunDecls();
        expect(TokenClass.EOF);
        return new Program(stds, vds, fds);
    }

    // includes are ignored, so does not need to return an AST node
    private void parseIncludes() {
        if (accept(TokenClass.INCLUDE)) {
            nextToken();
            expect(TokenClass.STRING_LITERAL);
            parseIncludes();
        }
    }

    private List<StructTypeDecl> parseStructDecls() {
        List<StructTypeDecl> stds = new ArrayList<StructTypeDecl>();
        while (accept(TokenClass.STRUCT) && lookAhead(2).tokenClass == TokenClass.LBRA){
            StructType st = parseStructType();
            expect(TokenClass.LBRA);
            List<VarDecl> vds = parseVarDecls();
            expect(TokenClass.RBRA);
            expect(TokenClass.SC);
            StructTypeDecl std = new StructTypeDecl(st, vds);
            stds.add(std);
        }
        return stds;

    }

    private TokenClass helper(){
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)){ // only 1 token
            if (lookAhead(1).tokenClass == TokenClass.ASTERIX){  // there is a *
                return lookAhead(3).tokenClass;
            }
            else{
                return lookAhead(2).tokenClass;
            }
        }
        else if (accept(TokenClass.STRUCT)){      // the type is structType which contains 2 token
            if (lookAhead(2).tokenClass == TokenClass.ASTERIX){ // there is a *
                return lookAhead(4).tokenClass;
            }
            else{
                return lookAhead(3).tokenClass;
            }
        }
        else {
            return null;
        }
    }

    private List<VarDecl> parseVarDecls() {
        List<VarDecl> vds = new ArrayList<VarDecl>();
        while (helper() == TokenClass.SC || helper() == TokenClass.LSBR){  // check if the third part belongs to VarDecl
            Type t = parseType();
            String name = expect(TokenClass.IDENTIFIER).data;
            if (accept(TokenClass.SC)){
                expect(TokenClass.SC);
                vds.add(new VarDecl(t, name));
            }
            else {
                expect(TokenClass.LSBR);
                Token tk = expect(TokenClass.INT_LITERAL);
                int i = Integer.parseInt(tk.data);
                expect(TokenClass.RSBR);
                expect(TokenClass.SC);
                vds.add(new VarDecl(new ArrayType(t, i), name));
            }

        }
        return vds;
    }


    private List<FunDecl> parseFunDecls() {
        List<FunDecl> fds = new ArrayList<FunDecl>();
        while(helper() == TokenClass.LPAR){
            Type t = parseType();
            String name = expect(TokenClass.IDENTIFIER).data;
            expect(TokenClass.LPAR);
            List<VarDecl> vds = parseParams();
            expect(TokenClass.RPAR);
            Block b =parseBlock();
            fds.add(new FunDecl(t, name, vds, b));
        }
        return fds;
    }

    private StructType parseStructType() {
        if (accept(TokenClass.STRUCT)){
            nextToken();
            String name = expect(TokenClass.IDENTIFIER).data;
            return new StructType(name);
        }
        else{
            error(TokenClass.STRUCT);
            return null;
        }
    }

    private Type parseType(){
        if (accept(TokenClass.INT)){
            nextToken();
            if (accept(TokenClass.ASTERIX)){
                nextToken();
                return new PointerType(BaseType.INT);
            }
            else {
                return BaseType.INT;
            }
        }
        else if (accept(TokenClass.CHAR)){
            nextToken();
            if (accept(TokenClass.ASTERIX)){
                nextToken();
                return new PointerType(BaseType.CHAR);
            }
            else {
                return BaseType.CHAR;
            }
        }
        else if(accept(TokenClass.VOID)){
            nextToken();
            if (accept(TokenClass.ASTERIX)){
                nextToken();
                return new PointerType(BaseType.VOID);
            }
            else {
                return BaseType.VOID;
            }
        }
        else if (accept(TokenClass.STRUCT)){
            nextToken();
            String name = expect(TokenClass.IDENTIFIER).data;
            StructType st = new StructType(name);
            if(accept(TokenClass.ASTERIX)) {
                nextToken();
                return new PointerType(st);
            }
            else {
                return st;
            }
        }
        else{
            error(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT);
            return null;
        }
    }

    private Block parseBlock(){
        if (accept(TokenClass.LBRA)){
            nextToken();
            List<VarDecl> vds = parseVarDecls();

            List<Stmt> stmts = new ArrayList<Stmt>();
            while(!accept(TokenClass.RBRA)){
                stmts.add(parseStmt());
            }

            expect(TokenClass.RBRA);
            return new Block(vds, stmts);
        }
        else {
            error(TokenClass.LBRA);
            return null;
        }
    }

    private List<VarDecl> parseParams(){
        List<VarDecl> vds = new ArrayList<VarDecl>();
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)){
            Type t = parseType();
            String name = expect(TokenClass.IDENTIFIER).data;
            vds.add(new VarDecl(t, name));
            while(accept(TokenClass.COMMA)){
                nextToken();
                Type t1 = parseType();
                String name1 = expect(TokenClass.IDENTIFIER).data;
                vds.add(new VarDecl(t1, name1));
            }

        }
        return vds;
    }


    private Stmt parseStmt(){
        if (accept(TokenClass.LBRA)){
            return parseBlock();
        }
        else if (accept(TokenClass.WHILE)){
            nextToken();
            expect(TokenClass.LPAR);
            Expr e = parseExp();
            expect(TokenClass.RPAR);
            Stmt s = parseStmt();
            return new While(e, s);
        }
        else if (accept(TokenClass.IF)){
            nextToken();
            expect(TokenClass.LPAR);
            Expr e = parseExp();
            expect(TokenClass.RPAR);
            Stmt s1 = parseStmt();
            if (accept(TokenClass.ELSE)){
                nextToken();
                Stmt s2 = parseStmt();
                return new If(e, s1, s2);
            }
            return new If(e, s1, null);
        }
        else if (accept(TokenClass.RETURN)){
            nextToken();;
            if(!accept(TokenClass.SC)){
                Expr e = parseExp();
                expect(TokenClass.SC);
                return new Return(e);
            }
            expect(TokenClass.SC);
            return new Return(null);
        }
        else {
            Expr e1 = parseExp();
            if (accept(TokenClass.ASSIGN)){
                nextToken();
                Expr e2 = parseExp();
                expect(TokenClass.SC);
                return new Assign(e1, e2);
            }
            else{
                expect(TokenClass.SC);
                return new ExprStmt(e1);
            }
        }
    }



    private Expr parseExp(){
        Expr lhs = parseExp7();
        while(accept(TokenClass.LOGOR)){
            Op op = Op.OR;
            nextToken();
            Expr rhs = parseExp7();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp7(){
        Expr lhs = parseExp6();
        while(accept(TokenClass.LOGAND)){
            Op op = Op.AND;
            nextToken();
            Expr rhs = parseExp6();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp6(){
        Expr lhs = parseExp5();
        while(accept(TokenClass.EQ, TokenClass.NE)){
            Op op;
            if(token.tokenClass == TokenClass.EQ){
                op = Op.EQ;
            }
            else {
                op = Op.NE;
            }
            nextToken();
            Expr rhs = parseExp5();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp5(){
        Expr lhs = parseExp4();
        while(accept(TokenClass.LT, TokenClass.LE, TokenClass.GE, TokenClass.GT)){
            Op op;
            if(token.tokenClass == TokenClass.LT){
                op = Op.LT;
            }
            else if(token.tokenClass == TokenClass.LE) {
                op = Op.LE;
            }
            else if(token.tokenClass == TokenClass.GE) {
                op = Op.GE;
            }
            else {
                op = Op.GT;
            }
            nextToken();
            Expr rhs = parseExp4();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp4(){
        Expr lhs = parseExp3();
        while(accept(TokenClass.PLUS, TokenClass.MINUS)){
            Op op;
            if(token.tokenClass == TokenClass.PLUS){
                op = Op.ADD;
            }
            else {
                op = Op.SUB;
            }
            nextToken();
            Expr rhs = parseExp3();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp3(){
        Expr lhs = parseExp2();
        while(accept(TokenClass.ASTERIX, TokenClass.DIV, TokenClass.REM)){
            Op op;
            if(token.tokenClass == TokenClass.ASTERIX){
                op = Op.MUL;
            }
            else if (token.tokenClass == TokenClass.DIV){
                op = Op.DIV;
            }
            else {
                op = Op.MOD;
            }
            nextToken();
            Expr rhs = parseExp2();
            lhs = new BinOp(op, lhs, rhs);
        }
        return lhs;
    }

    private Expr parseExp2(){
        if (accept(TokenClass.MINUS)){
            Op op = Op.SUB;
            nextToken();
            Expr rhs = parseExp();
            return new BinOp(op, new IntLiteral(0), rhs);
        }
        else if (accept(TokenClass.PLUS)){
            Op op = Op.ADD;
            nextToken();
            Expr rhs = parseExp();
            return new BinOp(op, new IntLiteral(0), rhs);
        }
        else if (accept(TokenClass.ASTERIX)){
            return parseValueAt();
        }
        else if (accept(TokenClass.AND)){
            return parseAddressOf();
        }
        else if (accept(TokenClass.LPAR)&& (lookAhead(1).tokenClass == TokenClass.INT ||
                lookAhead(1).tokenClass == TokenClass.CHAR || lookAhead(1).tokenClass == TokenClass.VOID ||
                lookAhead(1).tokenClass == TokenClass.STRUCT)){
            return parseTypeCast();
        }
        else {
            return parseExp1();
        }
    }

    private Expr parseExp1(){
        if (accept(TokenClass.IDENTIFIER) && lookAhead(1).tokenClass == TokenClass.LPAR){
            return parseFunCall();
        }
        else{
            Expr lhs = parseExp0();
            while(accept(TokenClass.DOT,TokenClass.LSBR)){
                if (token.tokenClass == TokenClass.DOT){
                    nextToken();
                    String name = expect(TokenClass.IDENTIFIER).data;
                    lhs = new FieldAccessExpr(lhs, name);
                }
                else {
                    nextToken();
                    Expr rhs = parseExp();
                    expect(TokenClass.RSBR);
                    lhs = new ArrayAccessExpr(lhs, rhs);
                }
            }
            return lhs;
        }
    }


    private Expr parseExp0(){
        if (accept(TokenClass.LPAR)){
            nextToken();
            Expr e = parseExp();
            expect(TokenClass.RPAR);
            return e;
        }
        else if (accept(TokenClass.IDENTIFIER)){
            String name = expect(TokenClass.IDENTIFIER).data;
            return new VarExpr(name);
        }
        else if (accept(TokenClass.INT_LITERAL)){
            Token t = expect(TokenClass.INT_LITERAL);
            int i = Integer.parseInt(t.data);
            return new IntLiteral(i);
        }
        else if (accept(TokenClass.CHAR_LITERAL)){
            String c = expect(TokenClass.CHAR_LITERAL).data;
            return new ChrLiteral(c);
        }
        else if (accept(TokenClass.STRING_LITERAL)){
            String s = expect(TokenClass.STRING_LITERAL).data;
            return new StrLiteral(s);
        }
        else if (accept(TokenClass.SIZEOF)){
            nextToken();
            expect(TokenClass.LPAR);
            Type t = parseType();
            expect(TokenClass.RPAR);
            return new SizeOfExpr(t);
        }
        else {
            error(TokenClass.LPAR,TokenClass.IDENTIFIER,TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,
                    TokenClass.STRING_LITERAL, TokenClass.SIZEOF);
            return null;
        }
    }


    private FunCallExpr parseFunCall(){
        String name = expect(TokenClass.IDENTIFIER).data;
        expect(TokenClass.LPAR);
        List<Expr> args = new ArrayList<Expr>();
        if (!accept(TokenClass.RPAR)){
            args.add(parseExp());
            while(accept(TokenClass.COMMA)){
                nextToken();
                args.add(parseExp());
            }
        }
        expect(TokenClass.RPAR);
        return new FunCallExpr(name, args);
    }

    private ArrayAccessExpr parseArrayAccess(){
        if (accept(TokenClass.LSBR)){
            nextToken();
            parseExp();
            expect(TokenClass.RSBR);
        }
        else {
            error(TokenClass.LSBR);
        }

        return null;

    }

    private FieldAccessExpr parseFieldAccess(){
        if (accept(TokenClass.DOT)){
            nextToken();
            expect(TokenClass.IDENTIFIER);
        }
        else {
            error(TokenClass.DOT);
        }

        return null;

    }

    private ValueAtExpr parseValueAt(){
        expect(TokenClass.ASTERIX);
        Expr rhs = parseExp();
        return new ValueAtExpr(rhs);
    }

    private AddressOfExpr parseAddressOf(){
        expect(TokenClass.AND);
        Expr rhs = parseExp();
        return new AddressOfExpr(rhs);
    }

    private SizeOfExpr parseSizeOf(){
        if (accept(TokenClass.SIZEOF)){
            nextToken();
            expect(TokenClass.LPAR);
            parseType();
            expect(TokenClass.RPAR);
        }
        else {
            error(TokenClass.SIZEOF);
        }
        return null;
    }

    private TypecastExpr parseTypeCast(){
        expect(TokenClass.LPAR);
        Type t = parseType();
        expect(TokenClass.RPAR);
        Expr rhs = parseExp();
        return new TypecastExpr(t, rhs);
    }

}
