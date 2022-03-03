package lexer;

import lexer.Token.TokenClass;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author cdubach
 */
public class Tokeniser {

    private Scanner scanner;

    private int error = 0;
    public int getErrorCount() {
	return this.error;
    }

    public Tokeniser(Scanner scanner) {
        this.scanner = scanner;
    }

    private void error(char c, int line, int col) {
        System.out.println("Lexing error: unrecognised character ("+c+") at "+line+":"+col);
	error++;
    }


    public Token nextToken() {
        Token result;
        try {
             result = next();
        } catch (EOFException eof) {
            // end of file, nothing to worry about, just return EOF token
            return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // something went horribly wrong, abort
            System.exit(-1);
            return null;
        }
        return result;
    }

    /*
     * To be completed
     */
    private Token next() throws IOException {

        int line = scanner.getLine();
        int column = scanner.getColumn();

        // get the next character
        char c = scanner.next();

        // skip white spaces
        if (Character.isWhitespace(c))
            return next();

        // no ambiguity

        // recognises the operators
        // plus
        if (c == '+')
            return new Token(TokenClass.PLUS, line, column);

        // minus
        if (c == '-')
            return new Token(TokenClass.MINUS, line, column);

        // ASTERIX
        if (c == '*')
            return new Token(TokenClass.ASTERIX, line, column);

        // DIV and Comment
        if (c == '/') {
            StringBuilder sb = new StringBuilder();
            c = scanner.peek();
            if (c == '/'){
                scanner.next();
                c = scanner.peek();
                while(c != '\n'){
                    scanner.next();
                    c = scanner.peek();
                }
                return next();
            }
            else if (c == '*'){
                scanner.next();
                c = scanner.next();
                while(c != '*' || scanner.peek() != '/'){
                    try {
                        c = scanner.next();
                    } catch (EOFException eof) {
                        return new Token(TokenClass.INVALID, line, column);
                    }
                    //c = scanner.peek();
                }
                scanner.next();
                scanner.next();
                return next();
//                c = scanner.peek();
//                if (c == '/'){
//                    scanner.next();
//                    return next();
//                }
//                else{
//                    return new Token(TokenClass.INVALID, line, column);
//                }

            }
            else {
                return new Token(TokenClass.DIV, line, column);
            }
        }
        // REM
        if (c == '%')
            return new Token(TokenClass.REM, line, column);

        // delimiters
        // LBRA
        if (c == '{')
            return new Token(TokenClass.LBRA, line, column);

        // RBRA
        if (c == '}')
            return new Token(TokenClass.RBRA, line, column);

        // LPAR
        if (c == '(')
            return new Token(TokenClass.LPAR, line, column);

        // RPAR
        if (c == ')')
            return new Token(TokenClass.RPAR, line, column);

        // LSBR
        if (c == '[')
            return new Token(TokenClass.LSBR, line, column);

        // RSBR
        if (c == ']')
            return new Token(TokenClass.RSBR, line, column);

        // SC
        if (c == ';')
            return new Token(TokenClass.SC, line, column);

        // COMMA
        if (c == ',')
            return new Token(TokenClass.COMMA, line, column);

        // dot
        if (c == '.'){
            return new Token(TokenClass.DOT, line, column);
        }

        // include 8 digit
        if (c == '#'){
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = scanner.peek();
            // test i
            if ( c == 'i'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test n
            if ( c == 'n'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test c
            if ( c == 'c'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test l
            if ( c == 'l'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test u
            if ( c == 'u'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test d
            if ( c == 'd'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            // test e
            if ( c == 'e'){
                sb.append(c);
                scanner.next();
            }
            else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            return new Token(TokenClass.INCLUDE, line, column);
        }

        // NE 2 digit
        if (c == '!'){
            c = scanner.next();
            if (c == '='){
                return new Token(TokenClass.NE, line, column);
            }
            else{
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        // LOGOR 2 digit
        if (c == '|'){
            c = scanner.next();
            if (c == '|'){
                return new Token(TokenClass.LOGOR, line, column);
            }
            else{
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }




        // assign and equal 1-2
        if (c == '=') {
            c = scanner.peek();
            if(c == '='){
                scanner.next();
                return new Token(TokenClass.EQ, line, column);
            }
            else {
                return new Token(TokenClass.ASSIGN, line, column);
            }
        }

        // LT and LE 1-2
        if (c == '<') {
            c = scanner.peek();
            if(c == '='){
                scanner.next();
                return new Token(TokenClass.LE, line, column);
            }
            else {
                return new Token(TokenClass.LT, line, column);
            }
        }

        // GT and GE 1-2
        if (c == '>') {
            c = scanner.peek();
            if(c == '='){
                scanner.next();
                return new Token(TokenClass.GE, line, column);
            }
            else {
                return new Token(TokenClass.GT, line, column);
            }
        }

        // LOGAND 1-2
        if (c == '&') {
            c = scanner.peek();
            if(c == '&'){
                scanner.next();
                return new Token(TokenClass.LOGAND, line, column);
            }
            else {
                return new Token(TokenClass.AND, line, column);
            }
        }


        // identifier and key word
        if (Character.isLetter(c) || c == '_'){
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = scanner.peek();
            while(Character.isLetterOrDigit(c) || c == '_'){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            switch (sb.toString()){
                case "int":
                    return new Token(TokenClass.INT, line, column);
                case "void":
                    return new Token(TokenClass.VOID, line, column);
                case "char":
                    return new Token(TokenClass.CHAR, line, column);
                case "if":
                    return new Token(TokenClass.IF, line, column);
                case "else":
                    return new Token(TokenClass.ELSE, line, column);
                case "while":
                    return new Token(TokenClass.WHILE, line, column);
                case "return":
                    return new Token(TokenClass.RETURN, line, column);
                case "struct":
                    return new Token(TokenClass.STRUCT, line, column);
                case "sizeof":
                    return new Token(TokenClass.SIZEOF, line, column);
                default:
                    return new Token(TokenClass.IDENTIFIER, sb.toString(), line, column);
            }
        }

        //INT_LITERAL
        if (Character.isDigit(c)){
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = scanner.peek();
            while(Character.isDigit(c)){
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            return new Token(TokenClass.INT_LITERAL, sb.toString(), line, column);
        }

        //STRING_LITERAL
        if (c == '"'){
            StringBuilder sb = new StringBuilder();
            try {
                c = scanner.peek();
                while (c != '"') {
                    if (c == '\\') {
                        scanner.next();
                        char b = scanner.peek();
                        switch (c) {
                            case '0':
                                sb.append('\\');
                                sb.append('0');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case 't':
                                sb.append('\t');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case 'b':
                                sb.append('\b');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case 'n':
                                sb.append('\n');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case 'r':
                                sb.append('\r');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case 'f':
                                sb.append('\f');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case '\'':
                                sb.append('\'');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case '\"':
                                sb.append('\"');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            case '\\':
                                sb.append('\\');
                                scanner.next();
                                c = scanner.peek();
                                break;
                            default:
                                System.out.println("string default\n");
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                        }
                    } else {
                        sb.append(c);
                        scanner.next();
                        c = scanner.peek();
                    }
                }
                scanner.next();
                return new Token(TokenClass.STRING_LITERAL, sb.toString(), line, column);

            } catch (EOFException eof){
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        // CHAR_LITERAL
        if (c == '\''){
            StringBuilder sb = new StringBuilder();
            c = scanner.peek();
            switch (c){
                case '\\':
                    scanner.next();
                    c = scanner.peek();
                    switch (c){
                        case '0':
                            sb.append('\\');
                            sb.append('0');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case 't':
                            sb.append('\t');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case 'b':
                            sb.append('\b');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case 'n':
                            sb.append('\n');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case 'r':
                            sb.append('\r');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case 'f':
                            sb.append('\f');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case '\'':
                            sb.append('\'');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case '\"':
                            sb.append('\"');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        case '\\':
                            sb.append('\\');
                            scanner.next();
                            c = scanner.peek();
                            if (c == '\''){
                                scanner.next();
                                return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                            }
                            else{
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        default:
                            error(c, line, column);
                            return new Token(TokenClass.INVALID, line, column);
                    }
                case '\'':
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                default:
                    sb.append(c);
                    scanner.next();
                    c = scanner.peek();
                    if (c == '\''){
                        scanner.next();
                        return new Token(TokenClass.CHAR_LITERAL, sb.toString(), line, column);
                    }
                    else{
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }

            }

        }


        // if we reach this point, it means we did not recognise a valid token
        error(c, line, column);
        return new Token(TokenClass.INVALID, line, column);
    }


}
