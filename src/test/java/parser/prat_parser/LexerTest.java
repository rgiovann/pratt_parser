package parser.prat_parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import parser.prat_parser.lexer.Lexer;
import parser.prat_parser.lexer.LexerFactory;
import parser.prat_parser.model.Token;
import parser.prat_parser.model.TokenType;

class LexerTest {

    private Lexer createLexer(String input) {
        return LexerFactory.createDefaultLexer(input);
    }

    @Test
    void testNumberToken() {
        Lexer lexer = createLexer("123");
        //lexer.printTokens();
        List<Token> tokens = lexer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals(new Token(TokenType.NUMBER, "123"), tokens.get(0));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(1));
    }

    @Test
    void testVariableToken() {
        Lexer lexer = createLexer(" abc ");
        List<Token> tokens = lexer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals(new Token(TokenType.VARIABLE, "abc"), tokens.get(0));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(1));
    }

    @Test
    void testOperatorTokens() {
        Lexer lexer = createLexer("+-*/  .!:=?:");
        //lexer.printTokens();
        List<Token> tokens = lexer.tokenize();
        assertEquals(11, tokens.size());
        assertEquals(new Token(TokenType.OPERATOR, "+"), tokens.get(0));
        assertEquals(new Token(TokenType.OPERATOR, "-"), tokens.get(1));
        assertEquals(new Token(TokenType.OPERATOR, "*"), tokens.get(2));
        assertEquals(new Token(TokenType.OPERATOR, "/"), tokens.get(3));
        assertEquals(new Token(TokenType.OPERATOR, "."), tokens.get(4));
        assertEquals(new Token(TokenType.OPERATOR, "!"), tokens.get(5));
        assertEquals(new Token(TokenType.OPERATOR, ":"), tokens.get(6));
        assertEquals(new Token(TokenType.OPERATOR, "="), tokens.get(7));
        assertEquals(new Token(TokenType.OPERATOR, "?"), tokens.get(8));
        assertEquals(new Token(TokenType.OPERATOR, ":"), tokens.get(9));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(10));
    }

    @Test
    void testParenthesesTokens() {
        Lexer lexer = createLexer("()");
        List<Token> tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(new Token(TokenType.LPAREN, "("), tokens.get(0));
        assertEquals(new Token(TokenType.RPAREN, ")"), tokens.get(1));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(2));
    }

    @Test
    void testBracketTokens() {
        Lexer lexer = createLexer("[ ]");
        //lexer.printTokens();
        List<Token> tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(new Token(TokenType.LBRACKET, "["), tokens.get(0));
        assertEquals(new Token(TokenType.RBRACKET, "]"), tokens.get(1));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(2));
    }

    @Test
    void testWhitespaceHandling() {
        Lexer lexer = createLexer("  1  +  2  ");
        //lexer.printTokens();
        List<Token> tokens = lexer.tokenize();
        assertEquals(4, tokens.size());
        assertEquals(new Token(TokenType.NUMBER, "1"), tokens.get(0));
        assertEquals(new Token(TokenType.OPERATOR, "+"), tokens.get(1));
        assertEquals(new Token(TokenType.NUMBER, "2"), tokens.get(2));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(3));
    }

    @Test
    void testComplexExpression() {
        Lexer lexer = createLexer("a + b * c[1]");
        List<Token> tokens = lexer.tokenize();
        assertEquals(9, tokens.size());
        assertEquals(new Token(TokenType.VARIABLE, "a"), tokens.get(0));
        assertEquals(new Token(TokenType.OPERATOR, "+"), tokens.get(1));
        assertEquals(new Token(TokenType.VARIABLE, "b"), tokens.get(2));
        assertEquals(new Token(TokenType.OPERATOR, "*"), tokens.get(3));
        assertEquals(new Token(TokenType.VARIABLE, "c"), tokens.get(4));
        assertEquals(new Token(TokenType.LBRACKET, "["), tokens.get(5));
        assertEquals(new Token(TokenType.NUMBER, "1"), tokens.get(6));
        assertEquals(new Token(TokenType.RBRACKET, "]"), tokens.get(7));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(8));
    }

    @Test
    void testTernaryOperator() {
        Lexer lexer = createLexer("a ? b : c");
        List<Token> tokens = lexer.tokenize();
        //lexer.printTokens();
        assertEquals(6, tokens.size());
        assertEquals(new Token(TokenType.VARIABLE, "a"), tokens.get(0));
        assertEquals(new Token(TokenType.OPERATOR, "?"), tokens.get(1));
        assertEquals(new Token(TokenType.VARIABLE, "b"), tokens.get(2));
        assertEquals(new Token(TokenType.OPERATOR, ":"), tokens.get(3));
        assertEquals(new Token(TokenType.VARIABLE, "c"), tokens.get(4));
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(5));
    }

    @Test
    void testEmptyInput() {
        Lexer lexer = createLexer("");
        List<Token> tokens = lexer.tokenize();
        assertEquals(1, tokens.size());
        assertEquals(new Token(TokenType.EOF, ""), tokens.get(0));
    }

    @Test
    void testInvalidCharacter() {
        Lexer lexer = createLexer("@");
        assertThrows(IllegalStateException.class, lexer::tokenize, "No rule matched for character: @");
    }
}