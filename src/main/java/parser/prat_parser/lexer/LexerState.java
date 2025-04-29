package parser.prat_parser.lexer;

public interface LexerState {
	char getCurrentChar();

	void advance();

	boolean hasNextChar();

	void skipWhitespace();
}
