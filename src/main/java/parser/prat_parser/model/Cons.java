package parser.prat_parser.model;

import java.util.List;
import java.util.stream.Collectors;

public record Cons(String operator, List<Expression> operands) implements Expression {

    /*
    bloco de codigo 1


    
    */


    @Override
    public String toString() {
        if (operands.isEmpty()) {
            return operator;
        }
        return "(" + operator + " " +
        operands.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" ")) +
        ")";
    }

    /*
    bloco de codigo 2
    eww
    we we
    ewwe we werweewr we rwer ew
     we werwer wweer wer wer

    
    */
    
}
