/**
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono un numero di matricola
 * seguito (subito) da un cognome, dove la combinazione di matricola e cognome corrisponde a studenti del turno T2
 * o del turno T3 del laboratorio di Linguaggi Formali e Traduttori. Si ricorda le regole per suddivisione di studenti in turni:
 * Turno T1: cognomi la cui iniziale e compresa tra A e K, e la penultima cifra del numero di matricola e dispari; 
 * Turno T2: cognomi la cui iniziale e compresa tra A e K, e la penultima cifra del numero di matricola e pari;
 * Turno T3: cognomi la cui iniziale e compresa tra L e Z, e la penultima cifra del numero di matricola e dispari;
 * Turno T4: cognomi la cui iniziale e compresa tra L e Z, e la penultima cifra del numero di matricola e pari;
 * Un numero di matricola deve essere composto di almeno due cifre, ma (come in Esercizio 1.3) non ha un numero massimo
 * prestabilito di cifre. Assicurarsi che il DFA sia minimo. Esempi di stringhe accettate: “654321Bianchi”, “123456Rossi”,
 * “221B” Esempi di stringhe non accettate: “123456Bianchi”, “654321Rossi”, “5”, “654322”, “Rossi”,“2Bianchi”
 */

public class Esercizio6
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
                    state = 1; //PARI
                else
                    state = 2; //DISPARI
            else 
                state = -1; //ERRORE - NON RICONOSCIUTO DALL'ALFABETO
        break;

        //CASO PARI
	    case 1:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 3; //PARI
                    else
                        state = 4; //DISPARI
                else
                    state = -1; //MATRICOLA<2 => ERRORE
		break;

        //CASO DISPARI
        case 2:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 5; //PARI
                    else
                        state = 6; //DISPARI
                else
                    state = -1;
		break;

        //CASO PARI CON PENULTIMA CIFRA PARI
	    case 3:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 3; //se pari rimango in questo stato
                    else
                        state = 4; //se dispari vado nello stato 4
            else if(ch>='A' && ch<='K')
                state = 7; //SE PASSO DI QUI E' T2 (stato finale)
            else
                state = -1;
		break;

        //CASO DISPARI CON PENULTIMA CIFRA PARI
        case 4:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 5; //se pari vado nello stato 5
                    else
                        state = 6; //se dispari vado nello stato 6
            else if(ch>='A' && ch<='K')
                state = 7; //SE PASSO DI QUI E' T2 (stato finale)
            else
                state = -1;
		break;

        //CASO PARI CON PENULTIMA CIFRA DISPARI
        case 5:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 3; //se pari vado nello stato 3
                    else
                        state = 4; //se dispari vado nello stato 4
            else if(ch>='L' && ch<='Z')
                state = 8; //SE PASSO DI QUI E' T3 (stato finale)
            else
                state = -1;
		break;

        case 6:
            if(Character.isDigit(ch))
                if(Character.getNumericValue(ch)%2==0)
                        state = 5; //se pari vado nello stato 5
                    else
                        state = 6; //se dispari rimango qui
            else if(ch>='L' && ch<='Z')
                state = 8; //SE PASSO DI QUI E' T3 (stato finale)
            else
                state = -1;
		break;

        //T2
        case 7:
            if(ch<='z' && ch>='a')
                state = 7;
            else
                state = -1;
        break;

        //T3
        case 8:
            if(ch<='z' && ch>='a')
                state = 7;
            else
                state = -1;
        break;

	    }

    }

        
	return state == 7 || state == 8;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}