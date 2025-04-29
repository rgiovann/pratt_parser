package parser.prat_parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import parser.prat_parser.lexer.LexerFactory;
import parser.prat_parser.model.Atom;
import parser.prat_parser.model.Cons;
import parser.prat_parser.model.Expression;

public class PrattParserTest {

    private Expression parse(String input) {
        PrattParser parser = new PrattParser(LexerFactory.createDefaultLexer(input));
        return parser.parse();
    }

    private static Atom a(String value) {
        return new Atom(value);
    }

    private static Cons c(String op, Expression... operands) {
        return new Cons(op, List.of(operands));
    }

    @Nested
    class ArithmeticTests {

        @Test
        void testSingleNumber() {
            assertEquals(a("1"), parse("1"));
        }

        @Test
        void testSimpleAddition() {
            assertEquals(c("+", a("1"), a("2")), parse("1 + 2"));
        }

        @Test
        void testAdditionAndMultiplication() {
            assertEquals(c("+", a("1"), c("*", a("2"), a("3"))), parse("1 + 2 * 3"));
        }

        @Test
        void testComplexExpression() {
            assertEquals(
                c("+",
                    c("+", a("a"), c("*", c("*", a("b"), a("c")), a("d"))),
                    a("e")
                ),
                parse("a + b * c * d + e")
            );
        }

        @Test
        void testParentheses() {
            assertEquals(
                c("*", c("+", a("1"), a("2")), a("3")),
                parse("(1 + 2) * 3")
            );
        }

        @Test
        void testParenthesizedExpression() {
            assertEquals(a("0"), parse("(((0)))"));
        }
    }

    @Nested
    class OperatorTests {

        @Test
        void testFunctionComposition() {
            assertEquals(
                c(".", a("f"), c(".", a("g"), a("h"))),
                parse("f . g . h")
            );
        }

        @Test
        void testMixedExpression() {
            assertEquals(
                c("+",
                    c("+", a("1"), a("2")),
                    c("*",
                        c("*", c(".", a("f"), c(".", a("g"), a("h"))), a("3")),
                        a("4")
                    )
                ),
                parse("1 + 2 + f . g . h * 3 * 4")
            );
        }

        @Test
        void testPrefixMinus() {
            assertEquals(
                c("*",
                    c("-", c("-", a("1"))),
                    a("2")
                ),
                parse("--1 * 2")
            );
        }

        @Test
        void testPrefixMinusWithComposition() {
            assertEquals(
                c("-", c("-", c(".", a("f"), a("g")))),
                parse("--f . g")
            );
        }

        @Test
        void testPostfixFactorial() {
            assertEquals(
                c("-", c("!", a("9"))),
                parse("-9!")
            );
        }

        @Test
        void testCompositionWithFactorial() {
            assertEquals(
                c("!", c(".", a("f"), a("g"))),
                parse("f . g !")
            );
        }

        @Test
        void testDotAndPostfix() {
            assertEquals(
                c("!", c(".", a("f"), a("g"))),
                parse("f . g !")
            );
        }
    }

    @Nested
    class AdvancedTests {

        @Test
        void testArrayIndexingStructure() {
            assertEquals(
                c("[",
                    c("[", a("x"), a("0")),
                    a("1")
                ),
                parse("x[0][1]")
            );
        }

        @Test
        void testTernaryOperator() {
            assertEquals(
                c("?",
                    a("a"),
                    a("b"),
                    c("?", a("c"), a("d"), a("e"))
                ),
                parse("a ? b : c ? d : e")
            );
        }

        @Test
        void testAssignmentAndTernary() {
            assertEquals(
                c("=",
                    a("a"),
                    c("=", c("?", a("0"), a("b"), a("c")), a("d"))
                ),
                parse("a = 0 ? b : c = d")
            );
        }
    }
}
