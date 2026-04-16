import java.util.List;

public class MinimaxMI {
    private int maxMelyseg;
    private int gepJatekos; 
    private int emberJatekos; 

    public MinimaxMI(int maxMelyseg, int gepJatekos) {
        this.maxMelyseg = maxMelyseg;
        this.gepJatekos = gepJatekos;
        this.emberJatekos = (gepJatekos == 1) ? 2 : 1;
    }

    public int getLegjobbLepes(Tabla tabla) {
        List<Integer> lehetsegesLepesek = tabla.getSzabalyosLepesek(gepJatekos);
        
        if (lehetsegesLepesek.isEmpty()) return -1;
        
        int legjobbErtek = Integer.MIN_VALUE;
        int legjobbLepes = lehetsegesLepesek.get(0);
        
        for (int lepes : lehetsegesLepesek) {
            Tabla ujTabla = tabla.lepesVegrehajtasa(lepes);
            int lepesErtek = minimaxKiertekeles(ujTabla, maxMelyseg - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            
            if (lepesErtek > legjobbErtek) {
                legjobbErtek = lepesErtek;
                legjobbLepes = lepes;
            }
        }
        
        return legjobbLepes;
    }

    private int minimaxKiertekeles(Tabla tabla, int melyseg, int alfa, int beta, boolean maximalizaloE) {
        int aktualisJatekos = maximalizaloE ? gepJatekos : emberJatekos;
        List<Integer> szabalyosLepesek = tabla.getSzabalyosLepesek(aktualisJatekos);
        
        if (szabalyosLepesek.isEmpty()) {
            return maximalizaloE ? -10000 - melyseg : 10000 + melyseg; 
        }
        
        if (melyseg == 0) {
            return tablaKiertekelese(tabla);
        }

        if (maximalizaloE) {
            int legjobbErtek = Integer.MIN_VALUE;
            for (int lepes : szabalyosLepesek) {
                Tabla ujTabla = tabla.lepesVegrehajtasa(lepes);
                int ertek = minimaxKiertekeles(ujTabla, melyseg - 1, alfa, beta, false);
                legjobbErtek = Math.max(legjobbErtek, ertek);
                alfa = Math.max(alfa, legjobbErtek);
                if (beta <= alfa) break; 
            }
            return legjobbErtek;
        } else {
            int legjobbErtek = Integer.MAX_VALUE;
            for (int lepes : szabalyosLepesek) {
                Tabla ujTabla = tabla.lepesVegrehajtasa(lepes);
                int ertek = minimaxKiertekeles(ujTabla, melyseg - 1, alfa, beta, true);
                legjobbErtek = Math.min(legjobbErtek, ertek);
                beta = Math.min(beta, legjobbErtek);
                if (beta <= alfa) break; 
            }
            return legjobbErtek;
        }
    }

    private int tablaKiertekelese(Tabla tabla) {
        int gepLepesSzam = tabla.getSzabalyosLepesek(gepJatekos).size();
        int emberLepesSzam = tabla.getSzabalyosLepesek(emberJatekos).size();
        return gepLepesSzam - emberLepesSzam;
    }
}
