package parser.prat_parser;

import parser.prat_parser.lexer.Lexer;
import parser.prat_parser.lexer.LexerFactory;

// exemplo de uso
public class App {
	public static void main(String[] args) {
		String input ="4+3*5 + 4/6 - (3+4)*7";
		PrattParser parser = new PrattParser(LexerFactory.createDefaultLexer(input));
		Expression parsed = parser.parse();
		System.out.println("INPUT : " + input);
		System.out.println("OUTPUT : " + parsed.toString());
	}
}

