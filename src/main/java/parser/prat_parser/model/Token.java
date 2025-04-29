package parser.prat_parser.model;

public record Token(TokenType type, String value) {
    @Override
    public String toString() {
        return type == TokenType.EOF ? "EOF" : String.format("%s(%s)", type, value);
    }
}