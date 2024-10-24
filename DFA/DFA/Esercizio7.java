public class Esercizio7
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
			if(ch=='P')
				state = 1;
			else
				state = 6; //non posso cambiare ulteriori lettere
		break;

	    case 1:
			if(ch=='a')
				state = 2;
			else
				state = 7; //non posso cambiare ulteriori lettere
		break;

        case 2:
			if(ch=='o')
				state = 3;
			else
				state = 8; //non posso cambiare ulteriori lettere
		break;

	    case 3:
			if(ch=='l')
				state = 4;
			else
				state = 9; //non posso cambiare ulteriori lettere
		break;

        case 4:
            state = 5;
		break;

        case 5:
            state = -1;
        break;

        //INIZIO CASI CON UN ERRORE
        case 6:
			if(ch=='a')
				state = 7;
			else
				state = -1;
		break;

        case 7:
			if(ch=='o')
				state = 8;
			else
				state = -1;
		break;

        case 8:
			if(ch=='l')
				state = 9;
			else
				state = -1;
		break;

        case 9:
			if(ch=='o')
				state = 5;
			else
				state = -1;
		break;
        
	    }
	}
	return state == 5;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}