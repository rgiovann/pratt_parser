package parser.prat_parser.lexer;

import parser.prat_parser.model.Token;
import parser.prat_parser.model.TokenType;
import java.util.List;

public class LexerFactory {
	public static Lexer createDefaultLexer(String input) {
		var rules = List.of(new Rule(Character::isWhitespace, state -> {
			state.skipWhitespace();
			return null;
		}), new Rule(Character::isDigit, state -> {
			var number = new StringBuilder();
			while (state.hasNextChar() && Character.isDigit(state.getCurrentChar())) {
				number.append(state.getCurrentChar());
				state.advance();
			}
			return new Token(TokenType.NUMBER, number.toString());
		}), new Rule(Character::isLetter, state -> {
			var variable = new StringBuilder();
			while (state.hasNextChar() && Character.isLetter(state.getCurrentChar())) {
				variable.append(state.getCurrentChar());
				state.advance();
			}
			return new Token(TokenType.VARIABLE, variable.toString());
		}), new Rule(c -> "+-*/.!:=?:".contains(String.valueOf(c)), state -> {
			var op = String.valueOf(state.getCurrentChar());
			state.advance();
			return new Token(TokenType.OPERATOR, op);
		}), new Rule(c -> c == '(', state -> {
			state.advance();
			return new Token(TokenType.LPAREN, "(");
		}), new Rule(c -> c == ')', state -> {
			state.advance();
			return new Token(TokenType.RPAREN, ")");
		}), new Rule(c -> c == '[', state -> {
			state.advance();
			return new Token(TokenType.LBRACKET, "[");
		}), new Rule(c -> c == ']', state -> {
			state.advance();
			return new Token(TokenType.RBRACKET, "]");
		}));
// **************************************************************
// não vamos tratar uknown caracter no momento, lança exception.
// **************************************************************
//			,new Rule(c -> true, state -> {
//			var unknown = String.valueOf(state.getCurrentChar());
//			state.advance();
//			return new Token(TokenType.UNKNOWN, unknown);
//		}
		
		return new Lexer(input, rules);
	}
}