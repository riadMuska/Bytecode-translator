/**
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono un numero
 * di matricola seguito (subito) da un cognome, dove la combinazione di matricola e cognome
 * corrisponde a studenti del corso A che hanno un numero di matricola pari oppure a studenti del corso B
 * che hanno un numero di matricola dispari. Si ricorda che gli studenti del corso A sono quelli con
 * cognomi la cui iniziale e compresa tra A e K, e gli studenti del corso B sono quelli con cognomi la cui
 * iniziale e compresa tra L e Z. Per esempio, “123456Bianchi” e “654321Rossi” sono stringhe del linguaggio, 
 * mentre “654321Bianchi” e “123456Rossi” no. Nel contesto di questo esercizio, un numero di matricola non
 * ha un numero prestabilito di cifre (ma deve essere composto di almeno una cifra). Un cognome corrisponde
 * a una sequenza di lettere, e deve essere composto di almeno una lettera. Quindi l’automa deve accettare
 * le stringhe “2Bianchi” e “122B” ma non “654322” e “Rossi”. Assicurarsi che il DFA sia minimo
 */

public class Esercizio3
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
            else if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                    state = 2; //PARI
                else
                    state = 1; //DISPARI
            else 
                state = -1; //ERRORE - NON RICONOSCIUTO DALL'ALFABETO
        break;

	    case 1: //CASO DISPARI CON L_Z
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                    state = 2; //PARI
                else
                    state = 1; //DISPARI
            else if((ch<='K' && ch>='A')||(ch<='z' && ch>='a'))
                state = -1; //ERRORE - LETTERE MINUSCOLA OPPURE COPPIA MATRICOLA/COGNOME SBAGLIATA
            else if(ch<='Z' && ch>='L')
                state = 3; //COPPIA MATRICOLA/COGNOME GIUSTA, VADO ALLO STATO FINALE
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
            else if(ch<='K' && ch>='A')
                state = 3; //COPPIA MATRICOLA/COGNOME GIUSTA, VADO ALLO STATO FINALE
            else
                state = -1;
		break;

	    case 3:
            if(ch<='z' && ch>='a')
		        state = 3;
            else
                state = -1;
		break;
	    }
	}
	return state == 3;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}