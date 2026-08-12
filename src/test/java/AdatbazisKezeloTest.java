import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdatbazisKezeloTest {

    @Test
    void testMentesEsBetoltes() {
        AdatbazisKezelo db = new AdatbazisKezelo();
        
        String tesztNev = "TesztJatekos123456";
        String allapot = "111122220";
        int jatekos = 2;
        
        db.jatekMentes(tesztNev, allapot, jatekos, false);
        
        AdatbazisKezelo.JatekAdat adat = db.jatekBetoltes(tesztNev);
        assertNotNull(adat);
        assertEquals(tesztNev, adat.jatekosNev);
        assertEquals(allapot, adat.tablaAllapot);
        assertEquals(jatekos, adat.aktualisJatekos);
        
        // Jatek lezarasa gyoztest hirdetve (true)
        db.jatekMentes(tesztNev, allapot, jatekos, true);
        AdatbazisKezelo.JatekAdat bezart = db.jatekBetoltes(tesztNev);
        // Mivel is_finished = true, a program nem kene feldobja jateknak
        assertNull(bezart);
    }

    @Test
    void testEredmenyMentes() {
        AdatbazisKezelo db = new AdatbazisKezelo();
        String tesztNev = "EredmenyTesztolo";
        String gyoztes = "Ember";
        
        db.eredmenyMentes(tesztNev, gyoztes);
        
        // Ellenőrizzük az adatbázisban JDBC-vel
        boolean megvan = false;
        try (java.sql.Connection kapcsolat = java.sql.DriverManager.getConnection("jdbc:sqlite:csillagjatek.db");
             java.sql.PreparedStatement parancs = kapcsolat.prepareStatement("SELECT gyoztes FROM eredmenyek WHERE jatekos_nev = ?")) {
            parancs.setString(1, tesztNev);
            java.sql.ResultSet res = parancs.executeQuery();
            if (res.next()) {
                assertEquals(gyoztes, res.getString("gyoztes"));
                megvan = true;
            }
        } catch (java.sql.SQLException e) {
            fail("Adatbázis hiba: " + e.getMessage());
        }
        assertTrue(megvan, "Az eredmény nem lett elmentve az adatbázisba!");
    }
    @Test
    void testHianyzoNyilvantartas() {
        AdatbazisKezelo db = new AdatbazisKezelo();
        // Olyan felhasznalo esete, aki sose tette meg be a labat az adatbazisba
        assertNull(db.jatekBetoltes("NEM_LETEZO_000000000_123"));
    }
}
