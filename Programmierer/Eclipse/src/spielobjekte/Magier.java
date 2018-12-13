package spielobjekte;

import java.awt.Point;

import prototypen.Spiel;
import spieler.Spieler;

public final class Magier extends Figur {

    private static boolean[][] bewegungsRaster = { { true, true, true, true, true }, { true, true, true, true, true },
            { true, true, false, true, true }, { true, true, true, true, true }, { true, true, true, true, true }, };

    // TODO: FALLUNTERSCHEIDUNG JE NACH SPIELSEITE !!!
    private static boolean[][] angriffsRasterS1 = {
            { false, false, false, false, false },
            { false, false, false, true, true },
            { false, false, false, true, true },
            { false, false, false, true, true },
            { false, false, false, false, false }, };

    private static boolean[][] angriffsRasterS2 = {
            { false, false, false, false, false },
            { true, true, false, false, false },
            { true, true, false, false, false },
            { true, true, false, false, false },
            { false, false, false, false, false }, };

    private static boolean[][][] angriffsRasterArray = { Magier.angriffsRasterS1, Magier.angriffsRasterS2 };

    private static char symbol = 'M';
    private static String name = "Magier";

    private static int lebenspunkte = 1;

    public Magier(final Spieler team) {
        super(Magier.name, Magier.lebenspunkte, Magier.bewegungsRaster,
                Magier.angriffsRasterArray[team.getNummer() - 1],
                Magier.symbol, team);
    }

    @Override
    public boolean angriffMoeglich(final Point ziel) {

        if (this.angriffMoeglichRaster(ziel)) {
            if (Spiel.getSpielbrett().getFeld(ziel).istAngreifbar(this)) {
                if (Spiel.getKaempfe().size() > 0) {
                    for (final Kampf k : Spiel.getKaempfe()) {
                        if (this.equals(k.getAngreifer())
                                && (Spiel.getSpielbrett().getFeld(ziel).equals(k.getVerteidiger()))) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}