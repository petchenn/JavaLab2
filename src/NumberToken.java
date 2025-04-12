public record NumberToken(Integer value) implements Token {
    @Override
    public  TokenType type(){
        return TokenType.NUMBER;
    }
}
