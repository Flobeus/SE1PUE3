package prototypen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import spielobjekte.Figur;
import spielobjekte.Hindernis;
import spielobjekte.Spielobjekt;

public class Spielbrett {

    private static Spielbrett instance;

    private Spielobjekt[][] spielobjekte;
    private int xLaenge;
    private int yLaenge;

    private final int xFeldLaenge = 7;
    private final int yFeldLaenge = 3;

    public int getXLaenge() {
	return this.xLaenge;
    }

    public int getYLaenge() {
	return this.yLaenge;
    }

    private Spielbrett() {
	this.generiereSpielbrettSpielobjektArray(10, 10);
	this.setHindernisse();
	this.setFiguren(Spiel.getSpieler1().getHelden(), Spiel.getSpieler2().getHelden());
    }

    public Spielobjekt[][] copySpielobjekte() {
	final Spielobjekt[][] copySpielobjekte = new Spielobjekt[this.yLaenge][this.xLaenge];
	for (int y = 0; y < this.yLaenge; y++) {
	    for (int x = 0; x < this.xLaenge; x++) {
		copySpielobjekte[y][x] = this.spielobjekte[y][x];
	    }
	}
	return copySpielobjekte;
    }

    private void generiereSpielbrettSpielobjektArray(final int xLaenge, final int yLaenge) {

	this.spielobjekte = new Spielobjekt[yLaenge][xLaenge];
	this.setDimensionen();
    }

    private void setFiguren(Figur[] heldenSpieler1, Figur[] heldenSpieler2) {

	ArrayList<Figur> gesetzteFiguren = new ArrayList<>();

	Random randomGenerator = new Random();
	int index;
	final int maxFiguren = heldenSpieler1.length + heldenSpieler2.length;

	for (int y = 0; y < this.yLaenge; y++) {
	    if (y % 2 == 0) {
		do {
		    index = randomGenerator.nextInt(heldenSpieler1.length);
		} while (gesetzteFiguren.contains(heldenSpieler1[index]) && (gesetzteFiguren.size() <= maxFiguren));
		this.setFeld(new Point(0, y), heldenSpieler1[index]);
		gesetzteFiguren.add(heldenSpieler1[index]);
	    } else if (y % 2 == 1) {
		do {
		    index = randomGenerator.nextInt(heldenSpieler2.length);
		} while (gesetzteFiguren.contains(heldenSpieler2[index]) && (gesetzteFiguren.size() <= maxFiguren));
		this.setFeld(new Point(9, y), heldenSpieler2[index]);
		gesetzteFiguren.add(heldenSpieler2[index]);
	    }
	}
    }

    private void setHindernisse() {

	final Random randomGenerator = new Random();
	int y;
	int x;

	int anzahlGrosseHindernisse = 2;
	int anzahlKleineHindernisse = 3;

	while (anzahlGrosseHindernisse > 0) {
	    y = randomGenerator.nextInt(this.yLaenge);
	    x = 1 + randomGenerator.nextInt(this.xLaenge - 3);

	    if ((this.spielobjekte[y][x] == null || this.spielobjekte[y][x].isEmpty())
		    && (this.spielobjekte[y][x + 1] == null || this.spielobjekte[y][x + 1].isEmpty())) {
		this.spielobjekte[y][x] = new Hindernis();
		this.spielobjekte[y][x].setPosition(new Point(x, y));
		this.spielobjekte[y][x + 1] = new Hindernis();
		this.spielobjekte[y][x + 1].setPosition(new Point(x + 1, y));
		anzahlGrosseHindernisse--;
	    }
	}

	while (anzahlKleineHindernisse > 0) {
	    y = randomGenerator.nextInt(this.yLaenge);
	    x = 1 + randomGenerator.nextInt(this.xLaenge - 2);

	    if (this.spielobjekte[y][x] == null || this.spielobjekte[y][x].isEmpty()) {
		this.spielobjekte[y][x] = new Hindernis();
		this.spielobjekte[y][x].setPosition(new Point(x, y));
		anzahlKleineHindernisse--;
	    }
	}
    }

    public static Spielbrett getInstance() {
	if (Spielbrett.instance == null) {
	    Spielbrett.instance = new Spielbrett();
	}
	return Spielbrett.instance;
    }

    public void reset() {
	Spielbrett.instance = null;
    }

    void printBoard() {

	// Geraden (Benennungsschema: Uhrzeigersinn, Start bei Oben)
	final char OBEN_UNTEN = '│';
	final char RECHTS_LINKS = '─';

	// Ecken
	final char UNTEN_LINKS = '┐';

	// Kreuzungen
	final char OBEN_RECHTS_UNTEN_LINKS = '┼';
	final char RECHTS_UNTEN_LINKS = '┬';
	final char OBEN_UNTEN_LINKS = '┤';

	final StringBuilder output = new StringBuilder("");

	// TOP LINE
	for (int j = 0; j < this.xFeldLaenge; j++) {
	    output.append(RECHTS_LINKS);
	}

	for (int x = 0; x < this.xLaenge; x++) {
	    output.append(RECHTS_UNTEN_LINKS);
	    for (int j = 0; j < this.xFeldLaenge; j++) {
		output.append(RECHTS_LINKS);
	    }
	}

	output.append(UNTEN_LINKS);
	output.append(String.format("%n"));

	// ABC-INDEX + SPIELFELDINHALT + TRENNLINIE
	for (int y = 0; y < this.yLaenge; y++) {
	    for (int i = 0; i < this.yFeldLaenge; i++) {
		// ABC- INDEX
		for (int j = 0; j < this.xFeldLaenge; j++) {

		    if ((i % this.yFeldLaenge) == (this.yFeldLaenge / 2)
			    && j % this.xFeldLaenge == (this.xFeldLaenge / 2)) {
			output.append((char) ('A' + y));
		    } else {
			output.append(' ');
		    }

		}
		output.append(OBEN_UNTEN);

		// SPIELFELDINHALT
		for (int x = 0; x < this.xLaenge; x++) {
		    for (int j = 0; j < this.xFeldLaenge; j++) {
			output.append(this.generiereSpielobjektSymbolCharArray(this.spielobjekte[y][x],
				this.xFeldLaenge, this.yFeldLaenge)[i][j]);
		    }
		    output.append(OBEN_UNTEN);
		}
		output.append(String.format("%n"));
	    }

	    for (int j = 0; j < this.xFeldLaenge; j++) {
		output.append(RECHTS_LINKS);
	    }
	    output.append(OBEN_RECHTS_UNTEN_LINKS);

	    // TRENNLINIE
	    for (int x = 0; x < this.xLaenge - 1; x++) {
		for (int i = 0; i < this.xFeldLaenge; i++) {
		    output.append(RECHTS_LINKS);
		}
		output.append(OBEN_RECHTS_UNTEN_LINKS);

	    }

	    for (int j = 0; j < this.xFeldLaenge; j++) {
		output.append(RECHTS_LINKS);
	    }
	    output.append(OBEN_UNTEN_LINKS);
	    output.append(String.format("%n"));
	}

	for (int i = 0; i < this.yFeldLaenge; i++) {

	    for (int j = 0; j < this.xFeldLaenge; j++) {
		output.append(' ');
	    }
	    output.append(OBEN_UNTEN);

	    for (int x = 0; x < this.xLaenge; x++) {

		int sizeOfIndex;

		// Zentrierte Indexnummern

		for (int j = 0; j < this.xFeldLaenge; j++) {

		    if ((i % this.yFeldLaenge) == (this.yFeldLaenge / 2)) {
			sizeOfIndex = String.valueOf(x + 1).length();
			if (j == ((this.xFeldLaenge - sizeOfIndex) / 2) + ((this.xFeldLaenge - sizeOfIndex) % 2)) {
			    output.append(x + 1);
			    j += sizeOfIndex - 1;
			} else {
			    output.append(' ');
			}

		    } else {
			output.append(' ');

		    }
		}
		output.append(OBEN_UNTEN);
	    }
	    output.append(String.format("%n"));
	}

	// Geteiltes Printen aufgrund von Fehlern beim printen von großen
	// Strings. TODO: Bessere Lösung.
	System.out.print(output.substring(0, (this.yLaenge * this.xLaenge * this.yFeldLaenge * this.xFeldLaenge)));
	System.out.println((output.substring((this.yLaenge * this.xLaenge * this.yFeldLaenge * this.xFeldLaenge),
		output.length())));
    }

    private char[][] generiereSpielobjektSymbolCharArray(Spielobjekt spielobjekt, final int xFeldLaenge,
	    final int yFeldLaenge) {

	final char[][] spielfeld = new char[yFeldLaenge][xFeldLaenge];

	if (spielobjekt == null) {
	    spielobjekt = new Spielobjekt(" ");
	} else {
	    if (spielobjekt.getSymbol() == null) {
		spielobjekt = new Spielobjekt(" ");
	    }
	}

	final int ySpielobjektLaenge = spielobjekt.getSymbol().length;
	int ySpielobjekt = 0;

	for (int y = 0; y < yFeldLaenge; y++) {
	    if (y >= ((yFeldLaenge / 2) - (ySpielobjektLaenge / 2))
		    && y <= ((yFeldLaenge / 2) + (ySpielobjektLaenge / 2) - (1 - (ySpielobjektLaenge % 2)))) {

		int xSpielobjektLaenge = 0;

		if (spielobjekt.getSymbol()[ySpielobjekt] != null) {
		    xSpielobjektLaenge = spielobjekt.getSymbol()[ySpielobjekt].length;
		}

		int xSpielobjekt = 0;

		for (int x = 0; x < xFeldLaenge; x++) {

		    if (x >= ((xFeldLaenge / 2) - (xSpielobjektLaenge / 2))
			    && x <= ((xFeldLaenge / 2) + (xSpielobjektLaenge / 2) - (1 - (xSpielobjektLaenge % 2)))) {
			spielfeld[y][x] = spielobjekt.getSymbol()[ySpielobjekt][xSpielobjekt];
			xSpielobjekt++;
		    } else {
			spielfeld[y][x] = ' ';
		    }

		}
		ySpielobjekt++;
	    } else {
		for (int x = 0; x < xFeldLaenge; x++) {
		    spielfeld[y][x] = ' ';
		}
	    }

	}

	return spielfeld;

    }

    private void setDimensionen() {

	this.yLaenge = this.spielobjekte.length;

	for (final Spielobjekt[] spielobjekteX : this.spielobjekte) {
	    if (spielobjekteX.length > this.xLaenge) {
		this.xLaenge = spielobjekteX.length;
	    }
	}
    }

    private boolean isInBounds(Point p) {
	if (p.x >= 0 && p.x < xLaenge && p.y >= 0 && p.y < yLaenge) {
	    return true;
	} else {
	    return false;
	}
    }

    public void printBewegen(Point start) {

	final char[][] rahmenChar = { { '┌', '─', '─', '─', '┐' }, { '│', ' ', ' ', ' ', '│' },
		{ '└', '─', '─', '─', '┘' } };
	final Spielobjekt rahmen = new Spielobjekt(rahmenChar);

	final Spielobjekt[][] originalSpielbrett = this.copySpielobjekte();

	Point ziel = new Point();

	for (int yZiel = 0; yZiel < this.yLaenge; yZiel++) {

	    ziel.y = yZiel;

	    for (int xZiel = 0; xZiel < this.xLaenge; xZiel++) {

		ziel.x = xZiel;

		if (this.getFeld(start).bewegungMoeglich(ziel)) {
		    this.setFeld(ziel, rahmen);
		}
	    }
	}

	Spiel.setNachrichtTemporaerKurz("");

	if (!(this.getFeld(start) instanceof Figur)) {
	    Spiel.setNachrichtTemporaerKurz(
		    "Bewegungsanzeige nicht möglich, da ausgewähltes Objekt keine bewegbare Spielfigur ist.");
	}

	Spiel.updateConsole();
	this.spielobjekte = originalSpielbrett;
    }

    public boolean bewegungMoeglichBelegt(Point ziel) {

	final boolean moeglich = (this.getFeld(ziel) == null || this.getFeld(ziel).isEmpty());
	if (!moeglich) {
	    Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Bereits belegt.");
	}
	return moeglich;
    }

    public boolean bewegungMoeglichSpielfeld(Point start, Point ziel) {

	final boolean moeglich = (this.isInBounds(start) && this.isInBounds(ziel) && !start.equals(ziel));
	if (!moeglich) {
	    Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Außerhalb des Spielfeldes.");
	}
	return moeglich;
    }

    public Spielobjekt getFeld(Point ziel) {

	if (this.isInBounds(ziel) && this.spielobjekte[ziel.y][ziel.x] != null
		&& !this.spielobjekte[ziel.y][ziel.x].isEmpty()) {
	    return this.spielobjekte[ziel.y][ziel.x];
	} else {
	    Spielobjekt leeresObjekt = new Spielobjekt(" ");
	    leeresObjekt.setPosition(new Point(-1, -1));
	    Spiel.setNachrichtTemporaerKurz(
		    "Zug nicht möglich, da ausgewähltes Objekt keine bewegbare Spielfigur ist.");
	    return leeresObjekt;
	}
    }

    public void setFeld(Point ziel, Spielobjekt spielobjekt) {
	if (this.isInBounds(ziel)) {
	    this.spielobjekte[ziel.y][ziel.x] = spielobjekt;
	    this.spielobjekte[ziel.y][ziel.x].setPosition(ziel);
	}
    }

    public void bewege(Point start, Point ziel) {
	// Bewegendes Objekt auf neue Position setzten.
	this.setFeld(ziel, this.getFeld(start));
	// Bewegtes Objekt von alter Position entfernen.
	this.setFeld(start, new Spielobjekt(" "));
    }
}
