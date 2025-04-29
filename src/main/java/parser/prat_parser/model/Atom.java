package parser.prat_parser.model;

public record Atom(String value) implements Expression {
    @Override
    public String toString() {
        return value;
    }
}
