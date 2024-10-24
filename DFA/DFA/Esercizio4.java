

public class Esercizio4
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
                state = -1; //ERRORE SE INIZIA CON UNA LETTERA
            else if(Character.isDigit(ch)){
                if(Character.getNumericValue(ch)%2==0)
                    state = 2; //PARI
                else
                    state = 1; //DISPARI
            }
            else if(ch==' ')
                state = 0; //SE METTO LO SPAZIO RIMANGO NELLO STESSO STATO
            else
                state = -1; //ERRORE - NON RICONOSCIUTO DALL'ALFABETO
        break;

	    case 1: //CASO DISPARI CON L_Z
            if(Character.isDigit(ch)){
                if(Character.getNumericValue(ch)%2==0)
                    state = 2; //PARI
                else
                    state = 1; //DISPARI
            }
            else if((ch<='K' && ch>='A')||(ch<='z' && ch>='a'))
                state = -1; //ERRORE - LETTERE MINUSCOLA OPPURE COPPIA MATRICOLA/COGNOME SBAGLIATA
            else if((ch<='Z' && ch>='L'))
                state = 5; //COPPIA MATRICOLA/COGNOME GIUSTA
            else if(ch==' ')
                state = 3; //SPAZIO
            else
                state = -1;
		break;

        case 2: //CASO PARI CON A_K
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                    state = 2; //PARI
                else
                    state = 1; //DISPARI
            else if((ch<='Z' && ch>='K')||(ch<='z' && ch>='a'))
                state = -1; //ERRORE - LETTERE MINUSCOLA OPPURE COPPIA MATRICOLA/COGNOME SBAGLIATA
            else if((ch<='K' && ch>='A'))
                state = 5; //COPPIA MATRICOLA/COGNOME GIUSTA
            else if(ch==' ')
                state = 4; //SPAZIO
            else
                state = -1;
		break;

        case 3: //STATO SPAZIO DOPO MATRICOLA DISPARI
        if((ch<='Z' && ch>='L'))
            state = 5; //COPPIA MATRICOLA/COGNOME GIUSTA
        else if(ch==' ')
            state = 3; //SPAZIO
        else
            state = -1;
        break;

        case 4: //STATO SPAZIO DOPO MATRICOLA PARI
        if((ch<='K' && ch>='A'))
                state = 5; //COPPIA MATRICOLA/COGNOME GIUSTA
            else if(ch==' ')
                state = 4; //SPAZIO
            else
                state = -1;
        break;

	    case 5: //STATO FINALE
            if(ch<='z' && ch>='a')
		        state = 5;
            else if(ch == ' ')
                state = 6; //SPAZIO
            else
                state = -1;
		break;

        case 6: //STATO FINALE
            if(ch<='Z' && ch>='A')
		        state = 5;
            else if(ch == ' ')
                state = 6;
            else
                state = -1;
		break;
	    }
	}
	return state == 5 || state == 6;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}