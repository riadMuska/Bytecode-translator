public class Esercizio5
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
        if(ch<='K' && ch>='A')
            state = 1;
        else if(ch<='Z' && ch>='L')
            state = 2;
        else
            state = -1; //ERRORE - NON RICONOSCIUTO DALL'ALFABETO
        break;

	    case 1: //CASO A_K CON MATRICOLA PARI
            if(ch>='a' && ch<='z')
                state = 1;
            else if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                    state = 3; //MATRICOLA PARI A_K (STATO FINALE)
                else
                    state = 4; //MATRICOLA DISPARI A_K 
            else
                state = -1;
		break;

        case 2: //CASO L_Z CON MATRICOLA DISPARI
            if(ch>='a' && ch<='z')
            state = 2;
            else if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                    state = 5; //MATRICOLA PARI L_Z
                else
                    state = 6; //MATRICOLA DISPARI L_Z (STATO FINALE)
            else
                state = -1;
		break;

	    case 3:
        if(Character.isDigit(ch))
            if(Character.getNumericValue(ch)%2==0)
                state = 3; //MATRICOLA PARI A_K (STATO FINALE)
            else
                state = 4; //MATRICOLA DISPARI A_K 
        else
            state = -1;
		break;

        case 4:
        if(Character.isDigit(ch))
            if(Character.getNumericValue(ch)%2==0)
                state = 3; //MATRICOLA PARI A_K (STATO FINALE)
            else
                state = 4; //MATRICOLA DISPARI A_K 
        else
            state = -1;
		break;

        case 5:
        if(Character.isDigit(ch))
            if(Character.getNumericValue(ch)%2==0)
                state = 5; //MATRICOLA PARI L_Z
            else
                state = 6; //MATRICOLA DISPARI L_Z (STATO FINALE)
        else
            state = -1;
		break;

        case 6:
        if(Character.isDigit(ch))
            if(Character.getNumericValue(ch)%2==0)
                state = 5; //MATRICOLA PARI L_Z
            else
                state = 6; //MATRICOLA DISPARI L_Z (STATO FINALE)
        else
            state = -1;
		break;
	    }
	}
	return state == 3 || state == 6;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}