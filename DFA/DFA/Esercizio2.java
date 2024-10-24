public class Esercizio2
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
			if(Character.isLetter(ch))
				state = 2;
			else if(ch=='_')
				state = 1;
			else //SE E' UN NUMERO (O QUALSIASI ALTRA COSA) -> ERRORE
				state = -1;
		break;

	    case 1:
			if (ch == '_')
				state = 1;
			//character.isDigit restiuisce true se legge un numero
			else if(Character.isLetter(ch)||Character.isDigit(ch))
				state = 2; //SE E' UNA LETTERA O UN NUMERO VADO ALLO STATE FINALE (2)
			else
				state = -1; //POZZO
		break;

	    case 2:
			if(ch=='_'||Character.isLetter(ch)||Character.isDigit(ch))
				state = 2;
			else
				state = -1;
		break;
	    }
	}
	return state == 2;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}