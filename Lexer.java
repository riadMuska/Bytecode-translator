import java.io.*; 
//import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    private boolean commento = false;
    private boolean commento2 = false;
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        /*  
            gestione del commento
            commento = //
            commento2 = /* 
        */
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r' || commento || commento2) {
            if(peek == (char)-1){
                System.err.println("\nCommento non chiuso");
                return null;
            }
            else if (peek == '\n'){
                line++;
                commento = false;
            }else if(peek == '*'){
                readch(br);
                if(peek == '/'){
                    commento2 = false;
                }
            }
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;
            
            case ')':
                peek = ' ';
                return Token.rpt;
            
            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;
            
            case '+':
                peek = ' ';
                return Token.plus;
            
            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                readch(br);
                if(peek == '/'){
                    peek = ' ';
                    commento = true;
                    return lexical_scan(br);
                }else if(peek == '*') {
                    peek = ' ';
                    commento2 = true;
                    return lexical_scan(br);
                }
                else{
                    return Token.div;
                }
                

            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
            
            case '<':
                readch(br);
                if (peek == ' ') {
                    peek = ' ';
                    return Word.lt;
                }else if(peek == '='){
                    peek = ' ';
                    return Word.le;
                }else if(peek == '>'){
                    peek = ' ';
                    return Word.ne;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }

            case '>':
                readch(br);
                if (peek == ' ') {
                    peek = ' ';
                    return Word.gt;
                }else if(peek == '='){
                    peek = ' ';
                    return Word.ge;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after > : "  + peek );
                    return null;
                }
            
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                String s = "";
                if (Character.isLetter(peek)||peek=='_') {

                    while(Character.isLetter(peek)){
                        s +=peek;
                        readch(br);
                    }
                    if(s.equals(Word.assign.lexeme)){
                        s = "";
                        return Word.assign;
                    }else if(s.equals(Word.to.lexeme)){
                        s = "";
                        return Word.to;
                    }else if(s.equals(Word.iftok.lexeme)){
                        s = "";
                        return Word.iftok;
                    }else if(s.equals(Word.elsetok.lexeme)){
                        s = "";
                        return Word.elsetok;
                    }else if(s.equals(Word.whiletok.lexeme)){
                        s = "";
                        return Word.whiletok;
                    }else if(s.equals(Word.begin.lexeme)){
                        s = "";
                        return Word.begin;
                    }else if(s.equals(Word.end.lexeme)){
                        s = "";
                        return Word.end;
                    }else if(s.equals(Word.print.lexeme)){
                        s = "";
                        return Word.print;
                    }else if(s.equals(Word.read.lexeme)){
                        s = "";
                        return Word.read;
                    }else{ //prima ho delle lettere o un _, continuo a leggere lettere e numeri e _ -> identificatore
                        while(Character.isDigit(peek)||Character.isLetter(peek)||peek=='_'){
                            s += peek;
                            readch(br);
                        }
                        if(s.compareTo("_")==0){
                            System.err.println("L'identificatore non può essere: " + peek );
                            return null;
                        }
                        return new Word(Tag.ID, s);
                    }

                }else if (Character.isDigit(peek)) {
                    String num = new String();
                    //Se prima non ho ricevuto lettere, questo sarà un nuovo numero
                        while(Character.isDigit(peek)){
                            num += peek;
                            readch(br);
                        }
                        int n = Integer.valueOf(num);
                        return new NumberTok(n);
                    

                }else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "lexer.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}