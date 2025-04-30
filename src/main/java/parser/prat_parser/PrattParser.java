package parser.prat_parser;

import java.util.List;

import parser.prat_parser.lexer.Lexer;
import parser.prat_parser.model.Atom;
import parser.prat_parser.model.BindingPower;
import parser.prat_parser.model.Cons;
import parser.prat_parser.model.Expression;
import parser.prat_parser.model.Token;
import parser.prat_parser.model.TokenType;

public class PrattParser {
    private int tokenIndex;
    private final List<Token> tokens;

    public PrattParser(Lexer lexer) {
        this.tokens = lexer.tokenize(); 
        this.tokenIndex = 0;
    }

    private Token nextToken() {
        //List<Token> tokens = lexer.tokenize();
        return tokenIndex < tokens.size() ? tokens.get(tokenIndex++) : new Token(TokenType.EOF, "");
    }

    private Token peekToken() {
        //List<Token> tokens = lexer.tokenize();
        return tokenIndex < tokens.size() ? tokens.get(tokenIndex) : new Token(TokenType.EOF, "");
    }

    public Expression parseExpression(int minBp) {
        Expression lhs;
        Token token = nextToken();

        // Handle prefix expressions
        if (token.type() == TokenType.NUMBER || token.type() == TokenType.VARIABLE) {
            lhs = new Atom(token.value());
        } else if (token.type() == TokenType.OPERATOR && token.value().matches("[+-]")) {
            int rBp = prefixBindingPower(token.value().charAt(0));
            Expression rhs = parseExpression(rBp);
            lhs = new Cons(token.value(), List.of(rhs));
        } else if (token.type() == TokenType.LPAREN) {
            lhs = parseExpression(0);
            Token next = nextToken();
            if (next.type() != TokenType.RPAREN) {
                throw new IllegalStateException("Expected ')', found: " + next);
            }
        } else {
            throw new IllegalStateException("Bad token: " + token);
        }

        // Handle infix and postfix expressions
        while (true) {
            Token opToken = peekToken();
            if (opToken.type() == TokenType.EOF) {
                break;
            }
            if (opToken.type() != TokenType.OPERATOR && opToken.type() != TokenType.LBRACKET) {
                break;
            }
            char op = opToken.value().charAt(0);

            // Postfix operators
            Integer postfixBp = postfixBindingPower(op);
            if (postfixBp != null) {
                if (postfixBp < minBp) {
                    break;
                }
                nextToken();
                if (op == '[') {
                    Expression rhs = parseExpression(0);
                    Token next = nextToken();
                    if (next.type() != TokenType.RBRACKET) {
                        throw new IllegalStateException("Expected ']', found: " + next);
                    }
                    lhs = new Cons(String.valueOf(op), List.of(lhs, rhs));
                } else {
                    lhs = new Cons(String.valueOf(op), List.of(lhs));
                }
                continue;
            }

            // Infix operators
            BindingPower bp = infixBindingPower(op);
            if (bp == null) {
                break;
            }
            int lBp = bp.left();
            int rBp = bp.right();
            if (lBp < minBp) {
                break;
            }
            nextToken();

            if (op == '?') {
                Expression mhs = parseExpression(0);
                Token next = nextToken();
                if (next.type() != TokenType.OPERATOR || !next.value().equals(":")) {
                    throw new IllegalStateException("Expected ':', found: " + next);
                }
                Expression rhs = parseExpression(rBp);
                lhs = new Cons(String.valueOf(op), List.of(lhs, mhs, rhs));
            } else {
                Expression rhs = parseExpression(rBp);
                lhs = new Cons(String.valueOf(op), List.of(lhs, rhs));
            }
        }

        return lhs;
    }

    public Expression parse() {
        return parseExpression(0);
    }

    private int prefixBindingPower(char op) {
        return switch (op) {
            case '+', '-' -> BindingPower.PREFIX_PLUS_MINUS.right();
            default -> throw new IllegalArgumentException("Bad prefix operator: " + op);
        };
    }

    private Integer postfixBindingPower(char op) {
        return switch (op) {
            case '!', '[' -> BindingPower.POSTFIX_EXCL_BRACKET.left();
            default -> null;
        };
    }

    private BindingPower infixBindingPower(char op) {
        return switch (op) {
            case '=' -> BindingPower.INFIX_ASSIGN;
            case '?' -> BindingPower.INFIX_TERNARY;
            case '+', '-' -> BindingPower.INFIX_ADD_SUB;
            case '*', '/' -> BindingPower.INFIX_MUL_DIV;
            case '.' -> BindingPower.INFIX_DOT;
            default -> null;
        };
    }
}
