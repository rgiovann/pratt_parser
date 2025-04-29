package parser.prat_parser.model;

public enum TokenType {
    NUMBER,       // Números (ex.: "123").
    VARIABLE,    // Variáveis (ex.: "x", "abc").
    OPERATOR,    // Operadores (ex.: "+", "-", "*", "/", ".", "!", "=", "?", ":").
    LPAREN,      // Parêntese esquerdo "(".
    RPAREN,      // Parêntese direito ")".
    LBRACKET,    // Colchete esquerdo "[".
    RBRACKET,    // Colchete direito "]".
    EOF,         // Fim da entrada.
    UNKNOWN      // Token inválido (para erros).
}

