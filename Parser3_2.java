import java.io.*;

public class Parser3_2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser3_2(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void prog() {
         switch(look.tag)
         {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case'{':
                statlist();
                match(Tag.EOF);
                break;
            default:
            {
                error("Error in prog");
                break;
            }
          }

    }

    private void statlist() {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
                stat();
                statlistp();
                break;
            default: {
                error("Error in statlist");
                break;
            }
        }

    }

    private void statlistp() {
        switch (look.tag) {
            case ';': {
                match(';');
                stat();
                statlistp();
                break;
            }
            case Tag.EOF:
            case '}':
                //match(Tag.EOF);
                break;
            /*case '}': {
                match('}');
                break;
            }*/
            default:
                error("Error in statlistp");
                break;
        }
    }

    private void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                S();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default: {
                error("Error in stat");
                break;
            }
        }

    }

    private void S() {
        switch (look.tag) {
            case Tag.END: {
                match(Tag.END);
                break;
            }
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;
            default: {
                error("Error in S");
                break;
            }
        }
    }

    private void idlist() {
        if (look.tag == Tag.ID) {
            match(Tag.ID);
            idlistp();
        } else {
            error("Error in idList");
        }
    }

    private void idlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            case Tag.END:
            case ')':
            case ';':
            case Tag.ELSE:
            case Tag.EOF:
            case '}':
                break;
            default:
                error("Error in idlistp");
                break;
        }
    }

    private void bexpr() {
        if (look.tag == Tag.RELOP) {
            match(Tag.RELOP);
            expr();
            expr();
        } else {
            error("Error in bexpr");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+': {
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            }
            case '*': {
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            }
            case '-': {
                match('-');
                expr();
                expr();
                break;
            }
            case '/': {
                match('/');
                expr();
                expr();
                break;
            }
            case Tag.ID: {
                match(Tag.ID);
                break;
            }
            case Tag.NUM: {
                match(Tag.NUM);
                break;
            }
            default: {
                error("Error in expr");
                break;
            }
        }
    }

    private void exprlist() {
        switch (look.tag) {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.ID:
            case Tag.NUM: {
                expr();               
                exprlistp();
                break;
            }
            default: {
                error("Error in exprlist");
                break;
            }
        }
    }

    private void exprlistp() {
        switch (look.tag) {
            case ',': {
                match(',');
                expr();
                exprlistp();
                break;
            }
            case ')':
                break;
            default:
                error("Error in exprlistp");
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser3_2 parser = new Parser3_2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}