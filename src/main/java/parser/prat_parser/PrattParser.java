package parser.prat_parser;

import java.util.List;

import parser.prat_parser.lexer.Lexer;
import parser.prat_parser.model.Atom;
import parser.prat_parser.model.Cons;
import parser.prat_parser.model.Expression;
import parser.prat_parser.model.Token;
import parser.prat_parser.model.TokenType;

public class PrattParser {
    private final Lexer lexer;
    private int tokenIndex;
    private final List<Token> tokens;

    public PrattParser(Lexer lexer) {
        this.tokens = lexer.tokenize(); // gera uma vez só
        this.lexer = lexer;
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
            int[] infixBp = infixBindingPower(op);
            if (infixBp == null) {
                break;
            }
            int lBp = infixBp[0];
            int rBp = infixBp[1];
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
            case '+', '-' -> 9;
            default -> throw new IllegalArgumentException("Bad operator: " + op);
        };
    }

    private Integer postfixBindingPower(char op) {
        return switch (op) {
            case '!', '[' -> 11;
            default -> null;
        };
    }

    private int[] infixBindingPower(char op) {
        return switch (op) {
            case '=' -> new int[]{2, 1};
            case '?' -> new int[]{4, 3};
            case '+', '-' -> new int[]{5, 6};
            case '*', '/' -> new int[]{7, 8};
            case '.' -> new int[]{14, 13};
            default -> null;
        };
    }
}
