package parser.prat_parser;

import parser.prat_parser.lexer.Lexer;
import parser.prat_parser.lexer.LexerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
	    Lexer lexer = LexerFactory.createDefaultLexer("45]034+-*/.!:=?:");
	    lexer.printTokens();
	}
}
