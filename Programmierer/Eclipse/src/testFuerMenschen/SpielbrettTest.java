package testFuerMenschen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import spieler.Mensch;
import spieler.Spieler;

public class SpielbrettTest {

    public static void main(final String[] args) throws IOException {

	Spieler spieler1 = new Mensch(1);
	Spieler spieler2 = new Mensch(2);

//	final Spielbrett a = Spielbrett.getInstance("DEBUG");

	final InputStreamReader in = new InputStreamReader(System.in);
	final BufferedReader br = new BufferedReader(in);

	// Test Bewegungseingabe
	while (true) {
	    System.out.print("Befehl eingeben: ");
//	    a.setNachrichtTemporaerKurz("");
	    final String eingabe = br.readLine();
	    if (eingabe.toUpperCase().equals("EXIT")) {
		break;
	    }
//	    a.bewegungBefehleInterpretieren(eingabe);
	}
    }
}