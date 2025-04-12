import java.awt.*;
import java.util.List;

/**
 * Класс для вычисления математических выражений, поддерживающий переменные и функции.
 */
public class Calculator {
    private final Lexer lexer = new Lexer();

    private final StackMachine stackMachine = new StackMachine();

    public int calculate(String expression) {
        List<Token> tokens = lexer.getTokens(expression);
        List<Token> postfixExpression = lexer.convertToPostfix(tokens);
        int result = stackMachine.evaluate(postfixExpression);
        System.out.println(result);
        return result;
}}