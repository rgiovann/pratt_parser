package parser.prat_parser;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import parser.prat_parser.lexer.LexerFactory;

public class PrattParserTest {

    private void assertParsed(String input, String expected) {
        var lexer = LexerFactory.createDefaultLexer(input);
        var parser = new PrattParser(lexer);
        var expression = parser.parse();
        assertEquals(expected, expression.toString());
    }

    @Test
    public void testSingleNumber() {
        assertParsed("1", "1");
    }

    @Test
    public void testSimpleAddition() {
        assertParsed("1 + 2", "(+ 1 2)");
    }

    @Test
    public void testPrecedence() {
        assertParsed("1 + 2 * 3", "(+ 1 (* 2 3))");
    }

    @Test
    public void testParentheses() {
        assertParsed("(1 + 2) * 3", "(* (+ 1 2) 3)");
    }

    @Test
    public void testUnary() {
        assertParsed("-9!", "(- (! 9))");
    }

    @Test
    public void testDotAndPostfix() {
        assertParsed("f . g !", "(! (. f g))");
    }

    @Test
    public void testNestedTernary() {
        assertParsed("a ? b : c ? d : e", "(? a b (? c d e))");
    }
}
