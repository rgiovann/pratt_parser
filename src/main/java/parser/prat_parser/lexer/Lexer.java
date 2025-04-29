package parser.prat_parser.lexer;

import parser.prat_parser.model.Token;
import parser.prat_parser.model.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Lexer implements LexerState {
	private final String input;
	private final List<Rule> rules;
	private int position;
	private char currentChar;

	public Lexer(String input, List<Rule> rules) {
		this.input = input;
		this.rules = new ArrayList<>(rules); // Cópia defensiva
		this.position = 0;
		this.currentChar = input.isEmpty() ? '\0' : input.charAt(0);
	}

	public List<Token> tokenize() {
		var tokens = new ArrayList<Token>();
		while (currentChar != '\0') {
			boolean matched = false;
			for (var rule : rules) {
				if (rule.appliesTo(currentChar)) {
					var token = rule.apply(this); // Passa o Lexer como LexerState
					if (token != null) {
						tokens.add(token);
					}
					//advance();
					matched = true;
					break;
				}
			}
			if (!matched) {
				throw new IllegalStateException("No rule matched for character: " + currentChar);
			}
		}
		tokens.add(new Token(TokenType.EOF, ""));
		return tokens;
	}

	@Override
	public char getCurrentChar() {
		return currentChar;
	}

	@Override
	public void advance() {
		position++;
		currentChar = position < input.length() ? input.charAt(position) : '\0';
	}

	@Override
	public boolean hasNextChar() {
		return currentChar != '\0';
	}

	@Override
	public void skipWhitespace() {
		while (hasNextChar() && Character.isWhitespace(getCurrentChar())) {
			advance();
		}
	}

	public String getInput() {
		return input;
	}

	public int getPosition() {
		return position;
	}
	
	// metodo temporario
	public void printTokens() {
		List<Token> tokens = tokenize();
		tokens.forEach(System.out::println);
	}
}