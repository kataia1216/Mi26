import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdatbazisKezelo {
    private static final String URL = "jdbc:sqlite:csillagjatek.db";

    public AdatbazisKezelo() {
        tablaLetrehozasaHaNemLetezik();
    }

    private Connection csatlakozas() {
        Connection kapcsolat = null;
        try {
            kapcsolat = DriverManager.getConnection(URL);
        } catch (SQLException e) {
        }
        return kapcsolat;
    }

    private void tablaLetrehozasaHaNemLetezik() {
        String sql1 = "CREATE TABLE IF NOT EXISTS game_states (\n"
                + "    player_name TEXT PRIMARY KEY,\n"
                + "    board_state TEXT NOT NULL,\n"
                + "    current_player INTEGER NOT NULL,\n"
                + "    is_finished BOOLEAN NOT NULL\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS eredmenyek (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    jatekos_nev TEXT NOT NULL,\n"
                + "    gyoztes TEXT NOT NULL,\n"
                + "    datum DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        try (Connection kapcsolat = csatlakozas();
             Statement parancs = (kapcsolat != null) ? kapcsolat.createStatement() : null) {
            if (parancs != null) {
                parancs.execute(sql1);
                parancs.execute(sql2);
            }
        } catch (SQLException e) {
        }
    }

    public void jatekMentes(String jatekosNev, String tablaAllapot, int aktualisJatekos, boolean fobejezveE) {
        String sql = "INSERT OR REPLACE INTO game_states(player_name, board_state, current_player, is_finished) " +
                     "VALUES(?,?,?,?)";

        try (Connection kapcsolat = csatlakozas();
             PreparedStatement elokeszitettParancs = (kapcsolat != null) ? kapcsolat.prepareStatement(sql) : null) {
            if (elokeszitettParancs != null) {
                elokeszitettParancs.setString(1, jatekosNev);
                elokeszitettParancs.setString(2, tablaAllapot);
                elokeszitettParancs.setInt(3, aktualisJatekos);
                elokeszitettParancs.setBoolean(4, fobejezveE);
                elokeszitettParancs.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    public void eredmenyMentes(String jatekosNev, String gyoztes) {
        String sql = "INSERT INTO eredmenyek(jatekos_nev, gyoztes) VALUES(?,?)";
        try (Connection kapcsolat = csatlakozas();
             PreparedStatement elokeszitettParancs = (kapcsolat != null) ? kapcsolat.prepareStatement(sql) : null) {
            if (elokeszitettParancs != null) {
                elokeszitettParancs.setString(1, jatekosNev);
                elokeszitettParancs.setString(2, gyoztes);
                elokeszitettParancs.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    public JatekAdat jatekBetoltes(String jatekosNev) {
        String sql = "SELECT player_name, board_state, current_player, is_finished FROM game_states WHERE player_name = ?";
        JatekAdat adat = null;

        try (Connection kapcsolat = csatlakozas();
             PreparedStatement elokeszitettParancs = (kapcsolat != null) ? kapcsolat.prepareStatement(sql) : null) {
            if (elokeszitettParancs != null) {
                elokeszitettParancs.setString(1, jatekosNev);
                ResultSet eredmeny = elokeszitettParancs.executeQuery();

                if (eredmeny.next()) {
                    boolean befejezveE = eredmeny.getBoolean("is_finished");
                    if (!befejezveE) {
                        adat = new JatekAdat();
                        adat.jatekosNev = eredmeny.getString("player_name");
                        adat.tablaAllapot = eredmeny.getString("board_state");
                        adat.aktualisJatekos = eredmeny.getInt("current_player");
                    }
                }
            }
        } catch (SQLException e) {
        }
        return adat; 
    }

    public static class JatekAdat {
        public String jatekosNev;
        public String tablaAllapot;
        public int aktualisJatekos;
    }
}
