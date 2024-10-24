import java.io.*;

import javax.swing.text.StyledEditorKit.BoldAction;


public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;
    int add=0;
    int mul=0;

    public Translator(Lexer l, BufferedReader br) {
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
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void prog() {
        int lnext_prog = code.newLabel();
        switch(look.tag){
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case'{':
                statlist(lnext_prog);
                code.emit(OpCode.GOto,lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                    System.out.println("IO error\n");
                };
                break;
            default:
                error("Error in prog");
                break;
        }
    }

    public void stat(int lnext) {
        switch(look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr(lnext);
                match(Tag.TO);
                idlist(lnext);
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                code.emit(OpCode.invokestatic,0);
                idlist(lnext);
                match(')');
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(lnext,true,"");
                code.emit(OpCode.invokestatic,1);
                match(')');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int whiletrue = code.newLabel();
                int whilenext = code.newLabel();
                int whilefalse = code.newLabel();
                code.emitLabel(whilenext);
                bexpr(whiletrue, whilefalse, whilenext);
                match(')');
                code.emitLabel(whiletrue);
                stat(whilenext);
                code.emit(OpCode.GOto,whilenext);
                code.emitLabel(whilefalse);
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                int iftrue = code.newLabel();
                int iffalse = code.newLabel();
                int ifnext = lnext;
                bexpr(iftrue, iffalse, ifnext);
                match(')');
                code.emitLabel(iftrue);
                stat(ifnext);
                int endif = code.newLabel();
                code.emit(OpCode.GOto,endif);
                S(iffalse,lnext,endif);
                break;
            case '{':
                match('{');
                statlist(lnext);
                match('}');
                break;
            default: {
                error("Error in stat");
                break;
            }
        }
     }

    private void statlist(int lnext) {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
                stat(lnext);
                statlistp(lnext);
                break;
            default: {
                error("Error in statlist");
                break;
            }
        }

    }

    private void statlistp(int lnext) {
        switch (look.tag) {
            case ';': {
                match(';');
                stat(lnext);
                statlistp(lnext);
                break;
            }
            case Tag.EOF:
            case '}':
            //code.emit(OpCode.x,lnext);
                //match(Tag.EOF);
                break;
            default:
                error("Error in statlistp");
                break;
        }
    }

    private void S(int iffalse, int ifnext, int endif) {
        switch (look.tag) {
            case Tag.END: {
                code.emitLabel(iffalse);
                match(Tag.END);
                code.emitLabel(endif);
                break;
            }
            case Tag.ELSE:
                match(Tag.ELSE);
                code.emitLabel(iffalse);
                stat(ifnext);
                match(Tag.END);
                code.emitLabel(endif);
                break;
            default: {
                error("Error in S");
                break;
            }
        }
    }

    private void idlist(int lnext) {
        switch(look.tag) {
        case Tag.ID:
            Token aus = look;
            match(Tag.ID);
            /*44 = virgola*/
            if(look.tag == 44){
                /**duplico l'elemento in cima alla pila */
                code.emit(OpCode.dup);
            }
            int id_addr = st.lookupAddress(((Word)aus).lexeme);
            if (id_addr==-1) {
                id_addr=count;
                st.insert(((Word)aus).lexeme,count++);
            }
            
            code.emit(OpCode.istore,id_addr);
            lnext = code.newLabel();
            //code.emit(OpCode.GOto,lnext);
            //code.emitLabel(lnext);
            idlistp(lnext);
            code.emit(OpCode.GOto,lnext);
            code.emitLabel(lnext);
            break;
        default:
            error("Error in idlist");
                break;
      }
    }

    private void idlistp(int lnext) {
        switch (look.tag) {
            case ',':
                match(',');
                Token aus = look;
                match(Tag.ID);
                /**44 = virgola */
                if(look.tag == 44){
                    /**duplico l'elemento in cima alla pila */
                    code.emit(OpCode.dup);
                }
                int id_addr = st.lookupAddress(((Word)aus).lexeme);
                if (id_addr==-1) {
                    id_addr=count;
                    st.insert(((Word)aus).lexeme,count++);
                }
                code.emit(OpCode.istore,id_addr);
                idlistp(lnext);
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

    private void expr(int lnext) {
        
        switch(look.tag) {
            case '-':
                match('-');
                expr(lnext);
                expr(lnext);
                code.emit(OpCode.isub);
                break;
            case '/':
                match('/');
                expr(lnext);
                expr(lnext);
                code.emit(OpCode.idiv);
                break;
            case '+':
                match('+');
                add = 0;
                match('(');
                exprlist(lnext,false,"iadd");
                match(')');
                code.emit(OpCode.iadd);
                break;
            case '*':
                match('*');
                mul = 0;
                match('(');
                exprlist(lnext,false,"imul");
                match(')');
                code.emit(OpCode.imul);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc,((NumberTok)look).lexeme);
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr=count;
                    st.insert(((Word)look).lexeme,count++);
                }
                code.emit(OpCode.iload,st.lookupAddress(((Word)look).lexeme));
                match(Tag.ID);
                break;
            default:
                error("Error in expr");
                break;
        }
    }

    private void bexpr(int bexprtrue, int bexprfalse, int bexprnext) {
        if(look == Word.lt){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmplt,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if(look == Word.gt){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmpgt,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if (look == Word.ge){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmpge,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if(look == Word.le){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmple,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if(look == Word.eq){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmpeq,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if(look == Word.ne){
            match(Tag.RELOP);
            expr(bexprnext);
            expr(bexprnext);
            code.emit(OpCode.if_icmpne,bexprtrue);
            code.emit(OpCode.GOto,bexprfalse);
        }else if(look == Word.and){
            match(Tag.AND);
            int bunotrue = code.newLabel();
            int bunofalse = bexprfalse;
            int bduetrue = bexprtrue;
            int bduefalse = bexprfalse;
            bexpr(bunotrue,bexprfalse,bexprnext);
            code.emitLabel(bunotrue);
            bexpr(bexprtrue,bexprfalse,bexprnext);
        }else if(look == Word.or){
            match(Tag.OR);
            int bunotrue = bexprtrue;
            int bunofalse = code.newLabel();
            int bduetrue = bexprtrue;
            int bduefalse = bexprfalse;
            bexpr(bexprtrue,bunofalse,bexprnext);
            code.emitLabel(bunofalse);
            bexpr(bexprtrue,bexprfalse,bexprnext);
        }else if(look == Token.not){
            match('!');
            int bunotrue = bexprfalse;
            int bunofalse = bexprtrue;
            bexpr(bunotrue,bunofalse,bexprnext);
        }else{
            error("Error in bexpr");
        }
    }

    private void exprlist(int lnext, boolean print, String operazione) {
        switch (look.tag) {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.NUM: {
                    expr(lnext);            
                    exprlistp(lnext,print,operazione);
                    break;
                
            }
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr=count;
                    st.insert(((Word)look).lexeme,count++);
                }
                expr(lnext);             
                exprlistp(lnext,print,operazione);                
                break;
            default: {
                error("Error in exprlist");
                break;
            }
        }
    }

    private void exprlistp(int lnext, boolean print, String operazione) {
        switch (look.tag) {
            case ',': {
                match(',');
                if(operazione == "iadd"){
                    add++;
                    if(add > 1){
                        code.emit(OpCode.iadd);
                        add--;
                    }
                }else if(operazione == "imul"){
                    mul++;
                    if(mul > 1){
                        code.emit(OpCode.imul);
                    }
                }else if(print){
                    code.emit(OpCode.invokestatic,1);
                }
                expr(lnext);
                exprlistp(lnext,print,operazione);
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
        String path = "translator.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}