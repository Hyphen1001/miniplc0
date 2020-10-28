package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        String tmp="";
        while (Character.isDigit(it.peekChar())){
            tmp+=it.nextChar();
        }

        return new Token(TokenType.Uint,tmp,it.previousPos(), it.currentPos());

    }

    private Token lexIdentOrKeyword() throws TokenizeError {
        String tmp="";
        while (Character.isDigit(it.peekChar())||Character.isLetter(it.peekChar())){
            tmp=tmp+it.nextChar();
        }
        switch (tmp.toLowerCase()){
            case "begin":
                return new Token(TokenType.Begin, "begin", it.previousPos(), it.currentPos());
            case "end":
                return new Token(TokenType.End, "end", it.previousPos(), it.currentPos());
            case "var":
                return new Token(TokenType.Var, "var", it.previousPos(), it.currentPos());
            case "const":
                return new Token(TokenType.Const, "const", it.previousPos(), it.currentPos());
            case "print":
                return new Token(TokenType.Print, "print", it.previousPos(), it.currentPos());
            default:
                return new Token(TokenType.Ident, tmp, it.previousPos(), it.currentPos());
        }

    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());

            case '-':

                return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());

            case '*':

                return new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());

            case '/':

                return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());

            case '=':

                return new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());

            case ';':

                return new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());

            case '(':

                return new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());

            case ')':

                return new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());


            default:

                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
