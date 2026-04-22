import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

public class JatekGUI extends JFrame {
    private CardLayout kartyasElrendezes;
    private JPanel foPanel;
    
    private Tabla tabla;
    private MinimaxMI mestersegesIntelligencia;
    private AdatbazisKezelo adatbazisKezelo;
    private int aktualisJatekos;
    private String jatekosNev;
    
    private JLabel allapotCimke;
    private JatekterPanel jatekterPanel;
    
    public JatekGUI() {
        setTitle("Mu Torere - Csillagjáték");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        
        adatbazisKezelo = new AdatbazisKezelo();
        mestersegesIntelligencia = new MinimaxMI(8, 2);
        
        kartyasElrendezes = new CardLayout();
        foPanel = new JPanel(kartyasElrendezes);
        
        foPanel.add(fomenuLetrehozasa(), "MENU");
        foPanel.add(jatekterLetrehozasa(), "JATEK");
        
        add(foPanel);
        kartyasElrendezes.show(foPanel, "MENU");
    }
    
    private JPanel fomenuLetrehozasa() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(30, 30, 40));
        GridBagConstraints pozicio = new GridBagConstraints();
        pozicio.insets = new Insets(15, 15, 15, 15);
        pozicio.gridx = 0; pozicio.gridy = 0;
        
        JLabel cim = new JLabel("Mu Torere (Csillagjáték)");
        cim.setFont(new Font("Arial", Font.BOLD, 36));
        cim.setForeground(Color.WHITE);
        menuPanel.add(cim, pozicio);
        
        pozicio.gridy++;
        JLabel leiras = new JLabel("<html><center>Magyar szabályok szerint: a nyolcszögletű táblán a saját bábuddal<br>az egyetlen üres mezőre léphetsz. Középre csak ellenfél mellől!<center></html>");
        leiras.setFont(new Font("Arial", Font.PLAIN, 16));
        leiras.setForeground(Color.LIGHT_GRAY);
        menuPanel.add(leiras, pozicio);
        
        pozicio.gridy++;
        JTextField nevMezo = new JTextField(15);
        nevMezo.setFont(new Font("Arial", Font.PLAIN, 24));
        nevMezo.setToolTipText("Add meg a játékosnevedet...");
        menuPanel.add(nevMezo, pozicio);
        
        pozicio.gridy++;
        JButton inditoGomb = new JButton("Játék Indítása");
        inditoGomb.setFont(new Font("Arial", Font.BOLD, 22));
        inditoGomb.setBackground(new Color(70, 130, 180));
        inditoGomb.setForeground(Color.WHITE);
        inditoGomb.setFocusPainted(false);
        inditoGomb.addActionListener(e -> {
            String nev = nevMezo.getText().trim();
            if (nev.isEmpty()) nev = "Névtelen";
            jatekInditasa(nev);
        });
        menuPanel.add(inditoGomb, pozicio);
        
        return menuPanel;
    }
    
    private JPanel jatekterLetrehozasa() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        allapotCimke = new JLabel("Várakozás...", SwingConstants.CENTER);
        allapotCimke.setFont(new Font("Arial", Font.BOLD, 26));
        allapotCimke.setBorder(new EmptyBorder(15, 0, 15, 0));
        panel.add(allapotCimke, BorderLayout.NORTH);
        
        jatekterPanel = new JatekterPanel();
        panel.add(jatekterPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void jatekInditasa(String nev) {
        this.jatekosNev = nev;
        
        AdatbazisKezelo.JatekAdat mentettJatek = adatbazisKezelo.jatekBetoltes(nev);
        if (mentettJatek != null) {
            this.tabla = new Tabla(mentettJatek.tablaAllapot);
            this.aktualisJatekos = mentettJatek.aktualisJatekos;
            JOptionPane.showMessageDialog(this, "Félbehagyott játékállás betöltve!");
        } else {
            this.tabla = new Tabla(); 
            this.aktualisJatekos = 1;
            adatbazisKezelo.jatekMentes(jatekosNev, tabla.allapotSzoveggeAlakitasa(), aktualisJatekos, false);
        }
        
        kartyasElrendezes.show(foPanel, "JATEK");
        feluletFrissitese();
    }
    
    private void feluletFrissitese() {
        jatekterPanel.repaint();
        List<Integer> lehetsegesLepesek = tabla.getSzabalyosLepesek(aktualisJatekos);
        
        if (lehetsegesLepesek.isEmpty()) {
            adatbazisKezelo.jatekMentes(jatekosNev, tabla.allapotSzoveggeAlakitasa(), aktualisJatekos, true);
            String gyoztesSzoveg = (aktualisJatekos == 1) ? "Gép" : "Ember";
            adatbazisKezelo.eredmenyMentes(jatekosNev, gyoztesSzoveg);
            
            String gyoztesUzenet = (aktualisJatekos == 1) ? "A Gép (O) nyert! Nem maradt lépésed." : "Gratulálok, nyertél (X)! A Gép (O) beszorult.";
            allapotCimke.setText("JÁTÉK VÉGE - " + (aktualisJatekos == 1 ? "Gép nyert" : "Ember nyert"));
            
            Object[] opciok = {"Újrajátszás ezzel a névvel", "Vissza a főmenübe", "Kilépés"};
            int valasztas = JOptionPane.showOptionDialog(this,
                    gyoztesUzenet + "\n\nSzeretnél egy új kört kezdeni?",
                    "Játék Vége",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciok,
                    opciok[0]);

            if (valasztas == 0) {
                jatekInditasa(jatekosNev);
            } else if (valasztas == 1) {
                kartyasElrendezes.show(foPanel, "MENU");
            } else {
                System.exit(0);
            }
            return;
        }
        
        if (aktualisJatekos == 1) {
            allapotCimke.setText("Ember (X) következik!");
            allapotCimke.setForeground(new Color(0, 120, 0));
        } else {
            allapotCimke.setText("Gép (O) gondolkodik...");
            allapotCimke.setForeground(new Color(200, 0, 0));
            
            Timer idozito = new Timer(750, e -> {
                int legjobbLepes = mestersegesIntelligencia.getLegjobbLepes(tabla);
                tabla = tabla.lepesVegrehajtasa(legjobbLepes);
                aktualisJatekos = 1;
                adatbazisKezelo.jatekMentes(jatekosNev, tabla.allapotSzoveggeAlakitasa(), aktualisJatekos, false);
                feluletFrissitese();
            });
            idozito.setRepeats(false);
            idozito.start();
        }
    }
    
    private class JatekterPanel extends JPanel {
        public JatekterPanel() {
            setBackground(new Color(40, 45, 50));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    egerkattintasKezelese(evt.getX(), evt.getY());
                }
            });
        }
        
        private int getCsomopontX(int i, int szelesseg, int magassag) {
            if (i == 0) return szelesseg / 2;
            double szog = Math.PI / 2 - (i - 1) * (Math.PI / 4.0); 
            int sugar = Math.min(szelesseg, magassag) / 3;
            return (int)(szelesseg / 2 + Math.cos(szog) * sugar);
        }
        
        private int getCsomopontY(int i, int szelesseg, int magassag) {
            if (i == 0) return magassag / 2;
            double szog = Math.PI / 2 - (i - 1) * (Math.PI / 4.0);
            int sugar = Math.min(szelesseg, magassag) / 3;
            return (int)(magassag / 2 - Math.sin(szog) * sugar); 
        }
        
        private void egerkattintasKezelese(int egerX, int egerY) {
            if (aktualisJatekos != 1) return; 
            
            int szelesseg = getWidth();
            int magassag = getHeight();
            for (int i = 0; i < 9; i++) {
                int cspX = getCsomopontX(i, szelesseg, magassag);
                int cspY = getCsomopontY(i, szelesseg, magassag);
                if (Math.hypot(egerX - cspX, egerY - cspY) < 30) { 
                    List<Integer> szabalyosLepesek = tabla.getSzabalyosLepesek(1);
                    if (szabalyosLepesek.contains(i)) {
                        tabla = tabla.lepesVegrehajtasa(i);
                        aktualisJatekos = 2;
                        adatbazisKezelo.jatekMentes(jatekosNev, tabla.allapotSzoveggeAlakitasa(), aktualisJatekos, false);
                        feluletFrissitese();
                    }
                    break;
                }
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int szelesseg = getWidth();
            int magassag = getHeight();
            
            g2.setColor(new Color(120, 120, 140));
            g2.setStroke(new BasicStroke(4));
            for (int i = 1; i <= 8; i++) {
                g2.drawLine(getCsomopontX(0, szelesseg, magassag), getCsomopontY(0, szelesseg, magassag), getCsomopontX(i, szelesseg, magassag), getCsomopontY(i, szelesseg, magassag));
                int kovetkezo = (i % 8) + 1;
                g2.drawLine(getCsomopontX(i, szelesseg, magassag), getCsomopontY(i, szelesseg, magassag), getCsomopontX(kovetkezo, szelesseg, magassag), getCsomopontY(kovetkezo, szelesseg, magassag));
            }
            
            List<Integer> lehetsegesLepesek = (tabla != null && aktualisJatekos == 1) ? tabla.getSzabalyosLepesek(1) : List.of();
            
            for (int i = 0; i < 9; i++) {
                int cspX = getCsomopontX(i, szelesseg, magassag);
                int cspY = getCsomopontY(i, szelesseg, magassag);
                int mezo = (tabla != null) ? tabla.getMezo(i) : 0;
                
                g2.setColor(Color.WHITE);
                g2.fillOval(cspX - 30, cspY - 30, 60, 60);
                
                if (mezo == 1) { 
                    g2.setColor(new Color(50, 150, 255)); 
                } else if (mezo == 2) { 
                    g2.setColor(new Color(255, 100, 100)); 
                } else { 
                    g2.setColor(new Color(40, 45, 50)); 
                }
                
                if (lehetsegesLepesek.contains(i)) {
                    g2.setColor(Color.YELLOW); 
                }
                
                g2.fillOval(cspX - 26, cspY - 26, 52, 52);
                
                if (mezo != 0) {
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 30));
                    String szoveg = (mezo == 1) ? "X" : "O";
                    FontMetrics fm = g2.getFontMetrics();
                    int szovegSzelesseg = fm.stringWidth(szoveg);
                    int szovegMagassag = fm.getAscent();
                    g2.drawString(szoveg, cspX - szovegSzelesseg/2, cspY + szovegMagassag/2 - 3);
                }
            }
        }
    }
}
