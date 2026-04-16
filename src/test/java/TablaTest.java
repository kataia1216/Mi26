import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TablaTest {

    @Test
    void testAlapallapotEpit() {
        Tabla t = new Tabla();
        assertEquals(0, t.getUresMezoIndex());
        assertEquals(1, t.getMezo(1));
        assertEquals(2, t.getMezo(5));
    }

    @Test
    void testAllapotSzoveggeAlakitasa() {
        Tabla t = new Tabla();
        assertEquals("011112222", t.allapotSzoveggeAlakitasa());
    }

    @Test
    void testSzovegbolEpitEsMasol() {
        Tabla t = new Tabla("211112022");
        assertEquals(6, t.getUresMezoIndex());
        assertEquals(2, t.getMezo(0));
        
        Tabla masolat = new Tabla(t);
        assertEquals("211112022", masolat.allapotSzoveggeAlakitasa());
    }

    @Test
    void testSzabalyosLepesekKozeprolKifele() {
        Tabla t = new Tabla("101112222"); // 0 helyen Ember, 1 ures
        List<Integer> lep = t.getSzabalyosLepesek(1); // Ember lephet-e az ures(1) helyre?

        // Embernek mik a lepesi lehetosegei, ha az 1. mezo ures?
        // 0. mezo o, onnan lephet. 2. mezo (ember) onnan is.
        // 8. mezo gep, onnan o nem lephet, mert emberkent vizsgaljuk.
        assertTrue(lep.contains(0));
        assertTrue(lep.contains(2));
        assertFalse(lep.contains(8));
        assertEquals(2, lep.size());
    }

    @Test
    void testSzabalyosLepesekKivulrolBefele() {
        Tabla t = new Tabla(); // Kozep ures. Ember 1-4, Gep 5-8
        // Csak azok lephetnek kozepre akik gep mellett vannak: 1 es 4
        List<Integer> lep = t.getSzabalyosLepesek(1);
        assertTrue(lep.contains(1)); // 8-as gep mellett
        assertTrue(lep.contains(4)); // 5-os gep mellett
        assertFalse(lep.contains(2)); // belsok
        assertEquals(2, lep.size());
    }

    @Test
    void testLepesVegrehajtasa() {
        Tabla t = new Tabla();
        Tabla ujt = t.lepesVegrehajtasa(1); // 1-es bemegy ures kozepre
        assertEquals(1, ujt.getUresMezoIndex());
        assertEquals(1, ujt.getMezo(0));
        assertEquals(0, ujt.getMezo(1));
    }
}
