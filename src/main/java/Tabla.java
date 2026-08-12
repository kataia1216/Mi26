import java.util.ArrayList;
import java.util.List;

public class Tabla {
    // 0: putahi (kozep)
    // 1-8: kewai (kulso kor)
    // 1: ember, 2: gep
    private int[] mezok;


    public Tabla() {
        mezok = new int[9];
        mezok[0] = 0; 
        mezok[1] = 1; mezok[2] = 1; mezok[3] = 1; mezok[4] = 1;
        mezok[5] = 2; mezok[6] = 2; mezok[7] = 2; mezok[8] = 2;
    }

    public Tabla(Tabla masik) {
        mezok = masik.mezok.clone();
    }

    public Tabla(String allapot) {
        mezok = new int[9];
        for (int i = 0; i < 9; i++) {
            mezok[i] = Character.getNumericValue(allapot.charAt(i));
        }
    }

    public String allapotSzoveggeAlakitasa() {
        StringBuilder szovegepito = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            szovegepito.append(mezok[i]);
        }
        return szovegepito.toString();
    }


    public int getMezo(int pozicio) {
        return mezok[pozicio];
    }

    public int getUresMezoIndex() {
        for (int i = 0; i < 9; i++) {
            if (mezok[i] == 0) return i;
        }
        return -1;
    }

    public List<Integer> getSzabalyosLepesek(int jatekos) {
        List<Integer> lepesek = new ArrayList<>();
        int uresMezo = getUresMezoIndex();
        if (uresMezo == 0) {
            for (int i = 1; i <= 8; i++) {
                if (mezok[i] == jatekos) {
                    int balSzomszed = (i == 1) ? 8 : i - 1;
                    int jobbSzomszed = (i == 8) ? 1 : i + 1;
                    int ellenfel = (jatekos == 1) ? 2 : 1;
                    if (mezok[balSzomszed] == ellenfel || mezok[jobbSzomszed] == ellenfel) {
                        lepesek.add(i);
                    }
                }
            }
        } else {

            if (mezok[0] == jatekos) {
                lepesek.add(0);
            }
            int balSzomszed = (uresMezo == 1) ? 8 : uresMezo - 1;   //ki az üres mező bal és jobb szomszédja.
            int jobbSzomszed = (uresMezo == 8) ? 1 : uresMezo + 1;
            
            if (mezok[balSzomszed] == jatekos) {
                lepesek.add(balSzomszed);
            }
            if (mezok[jobbSzomszed] == jatekos) {
                lepesek.add(jobbSzomszed);
            }
        }
        return lepesek;
    }

    public Tabla lepesVegrehajtasa(int honnan) {
        Tabla ujTabla = new Tabla(this);
        int uresMezo = this.getUresMezoIndex();
        ujTabla.mezok[uresMezo] = this.mezok[honnan];
        ujTabla.mezok[honnan] = 0;
        return ujTabla;
    }
}
