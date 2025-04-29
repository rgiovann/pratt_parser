package parser.prat_parser;

import org.junit.jupiter.api.Test;
import parser.prat_parser.lexer.LexerFactory;
import parser.prat_parser.model.Expression;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrattParserTest_00 {

    private Expression parse(String input) {
        PrattParser parser = new PrattParser(LexerFactory.createDefaultLexer(input));
        return parser.parse();
    }

    @Test
    void testSimpleAtom() {
        assertEquals("1", parse("1").toString());
    }

    @Test
    void testAdditionAndMultiplication() {
        assertEquals("(+ 1 (* 2 3))", parse("1 + 2 * 3").toString());
    }

    @Test
    void testComplexExpression() {
        assertEquals("(+ (+ a (* (* b c) d)) e)", parse("a + b * c * d + e").toString());
    }

    @Test
    void testFunctionComposition() {
        assertEquals("(. f (. g h))", parse("f . g . h").toString());
    }

    @Test
    void testMixedExpression() {
        assertEquals("(+ (+ 1 2) (* (* (. f (. g h)) 3) 4))", parse("1 + 2 + f . g . h * 3 * 4").toString());
    }

    @Test
    void testPrefixMinus() {
        assertEquals("(* (- (- 1)) 2)", parse("--1 * 2").toString());
    }

    @Test
    void testPrefixMinusWithComposition() {
        assertEquals("(- (- (. f g)))", parse("--f . g").toString());
    }

    @Test
    void testPostfixFactorial() {
        assertEquals("(- (! 9))", parse("-9!").toString());
    }

    @Test
    void testCompositionWithFactorial() {
        assertEquals("(! (. f g))", parse("f . g !").toString());
    }

    @Test
    void testParenthesizedExpression() {
        assertEquals("0", parse("(((0)))").toString());
    }

    @Test
    void testArrayIndexing() {
        assertEquals("([ ([ x 0) 1)", parse("x[0][1]").toString());
    }

    @Test
    void testTernaryOperator() {
        assertEquals("(? a b (? c d e))", parse("a ? b : c ? d : e").toString());
    }

    @Test
    void testAssignmentAndTernary() {
        assertEquals("(= a (= (? 0 b c) d))", parse("a = 0 ? b : c = d").toString());
    }
}
