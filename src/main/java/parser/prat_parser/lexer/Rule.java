package parser.prat_parser.lexer;

import parser.prat_parser.model.Token;
import java.util.function.Predicate;
import java.util.function.Function;

public record Rule(Predicate<Character> condition, Function<LexerState, Token> action) {
	public boolean appliesTo(char c) {
		return condition.test(c);
	}

	public Token apply(LexerState state) {
		return action.apply(state);
	}
}