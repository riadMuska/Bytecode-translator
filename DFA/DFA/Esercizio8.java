public class Esercizio8
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
            if(ch == '+' || ch== '-')
                state = 1;
            else if(ch == '.')
                state = 2;
            else if(Character.isDigit(ch))
                state = 3;
            else
                state = -1; //ERRORE - NON RICONOSCIUTO DALL'ALFABETO
        break;

	    case 1: //Ho ricevuto + o -
            if(Character.isDigit(ch))
                state = 3;
            else if(ch == '.')
                state = 2;
            else
                state = -1;
		break;

        case 2: //Ho ricevuto .
            if(Character.isDigit(ch))
                state = 4;
            else
                state = -1;
		break;

	    case 3: //ho ricevuto un n
            if(Character.isDigit(ch))
		        state = 3;
            else if(ch == '.')
                state = 2;
            else if(ch == 'e')
                state = 5;
            else
                state = -1;
		break;

        case 4: //ho ricevuto un n da case 2
            if(Character.isDigit(ch))
                state = 4;
            else if(ch == 'e')
                state = 5;
            else
                state = -1;
        break;

        case 5: //ho ricevuto una e da case 4 o case 3
            if(Character.isDigit(ch))
                state = 7;
            else if(ch == '+' || ch == '-')
                state = 6;
            else 
                state = -1;
        break;

        case 6: //ho e con un segno + o -
            if(Character.isDigit(ch))
                state = 7;
            else
                state = -1;
        break;

        case 7:
            if(Character.isDigit(ch))
                state = 7;
            else if(ch == '.')
                state = 8;
            else
                state = -1;
        break;

        case 8:
            if(Character.isDigit(ch))
                state = 9;
            else
                state = -1;
        break;

        case 9:
            if(Character.isDigit(ch))
                state = 9;
            else 
                state = -1;
	    }
	}
	return state == 3 || state == 4 || state == 7 || state == 9;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}