public class NumberTok extends Token {
    public int lexeme = 0;
    public NumberTok(int n) {
        super(Tag.NUM);
        lexeme = n;
    }
    public String toString() {
        return "<" + tag + ", " + lexeme + ">"; 
    }
}