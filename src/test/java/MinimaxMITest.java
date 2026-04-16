import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinimaxMITest {

    @Test
    void testAzonnaliGyozelem() {
        MinimaxMI ai = new MinimaxMI(2, 2);
        // Olyan tábla, ahol a gep konyeden egy lepessel tud nyerni
        Tabla t = new Tabla("011112222"); 
        int lepes = ai.getLegjobbLepes(t);
        // Lehetseges lepesek a 2-es szamara ures kozepnel: 5 es 8.  Ezek a valid lepesek (mivel kozep = 0).
        assertTrue(lepes == 5 || lepes == 8);
    }
    
    @Test
    void testKiertekelesVagyZeroMelyseg() {
        MinimaxMI ai = new MinimaxMI(0, 2);
        Tabla t = new Tabla("101112222"); // Kozep foglalt, de van valami logikus
        int lepes = ai.getLegjobbLepes(t);
        // A melyseg 0, szoval a legelso retegen ki is ertekel, mindegy mi kerul dontesre a legalis kozul.
        assertTrue(lepes != -1);
    }

    @Test
    void testVesztesElkeruleseUresAdattartalommal() {
         // Ha az AI-nek egyaltalan nincs lepesi lehetosege: (mert kicsinaltak vagy hamis allapot van)
         MinimaxMI ai = new MinimaxMI(3, 2);
         Tabla t = new Tabla("111110111"); // Az AI (2) sehol sincs, tehat legalMoves üres listat ad.
         int lepes = ai.getLegjobbLepes(t);
         // Ha ures, visszateressel -1 kell, ahogy definialt volt.
         assertEquals(-1, lepes);
    }

    @Test
    void testMinimaxKiertekeloAgalasok() {
        // Legalabb 3 melysegu teszt, ami megprobal eljutni gyoztes es vesztes agakra
        MinimaxMI ai = new MinimaxMI(3, 2);
        Tabla t = new Tabla("121212102"); // Ket szereplo kozel egyforma allapota, ures a 7. pozicio
        int lepes = ai.getLegjobbLepes(t);
        assertTrue(lepes > -1);
    }
}
