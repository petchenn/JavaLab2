import java.util.*;

public class Lexer {
    private static final String DELIMITERS = " +-*/()";
    public List<Token> getTokens(String source){
        StringTokenizer tokenizer = new StringTokenizer(source, DELIMITERS, true);
        List<Token> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            if(token.isBlank()){
                continue;
            } else if (isNunber(token)) {
                tokens.add(new NumberToken(Integer.parseInt(token)));
                continue;
            }
            tokens.add(switch (token){
                case "+" -> new BinaryOperationToken(OperationType.PLUS);
                case "-" -> new BinaryOperationToken(OperationType.MINUS);
                case "*" -> new BinaryOperationToken(OperationType.MULTIPLY);
                case "/" -> new BinaryOperationToken(OperationType.DIVIDE);
                case "(" -> new OtherToken(TokenType.OPEN_BRACKET);
                case ")" -> new OtherToken(TokenType.CLOSE_BRACKET);
                default -> throw new RuntimeException("Unexpected token: " + token);
                }
                    );
        }
        return tokens;
    }

    public List<Token> convertToPostfix(List<Token> source) {
        List<Token> postfixExpression = new ArrayList<>();
        Deque<Token> operationStack = new LinkedList<>();
        for (Token token: source){
            switch (token.type()){
                case NUMBER -> postfixExpression.add(token);
                case OPEN_BRACKET -> operationStack.push(token);
                case CLOSE_BRACKET -> {
                    while (!operationStack.isEmpty() && operationStack.peek().type() != TokenType.OPEN_BRACKET) {
                        postfixExpression.add(operationStack.pop());
                    }
                    operationStack.pop(); // открывающая скобка
                }
                case BINARY_OPERATION -> {
                    while (!operationStack.isEmpty() && getPriority(operationStack.peek()) >= getPriority(token)) {
                        postfixExpression.add(operationStack.pop());
                    }
                    operationStack.push(token);
                }
            }
        }
        while (!operationStack.isEmpty()){
            postfixExpression.add(operationStack.pop());
        }
        return postfixExpression;
    }

    private int getPriority(Token token) {
        if (token instanceof BinaryOperationToken operation) {
            return switch (operation.operationType()) {
                case PLUS, MINUS -> 1;
                case MULTIPLY, DIVIDE -> 2;
            };
        }
        return 0; // для открывающей скобки
    }

    private boolean isNunber(String token){
        for (int i = 0; i< token.length(); i++){
            if(!Character.isDigit(token.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
