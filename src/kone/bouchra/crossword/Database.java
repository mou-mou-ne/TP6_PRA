package kone.bouchra.crossword;

import java.sql.*;
import java.util.*;

public class Database {
    private Connection connexion;

    public Database() {
        try {
            connexion = connecterBD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection connecterBD() throws SQLException {
        Connection connect;
        String jdbcUrl = "jdbc:mysql://localhost:3306/base?";
        connect = DriverManager.getConnection(jdbcUrl, "root", "");
        return connect;
    }

    public Map<Integer, String> availableGrids() {
        Map<Integer, String> map = new HashMap<>();
        Statement stmt;
        try {
            stmt = connexion.createStatement();
            String s = "select * from grid";
            ResultSet rs = stmt.executeQuery(s);
            while (rs.next()) {
                String nomGrille = rs.getString("nom_grille");
                int hauteur = rs.getInt("hauteur");
                int largeur = rs.getInt("largeur");
                int numGrille = rs.getInt("numero_grille");
                String nom = nomGrille + " (" + hauteur + "x" + largeur + ")";

                map.put(numGrille, nom);
            }
        } catch (SQLException c) {
            c.printStackTrace();
        }
        return map;
    }

    public Crossword extractGrid(int numGrille) {
        Crossword crossword = null;
        try {
            String selRequest = "SELECT * FROM crossword WHERE num_grille = ?";
            PreparedStatement pstatement = connexion.prepareStatement(selRequest);
            pstatement.setInt(1, numGrille);
            ResultSet requestAll = pstatement.executeQuery();
            if (requestAll.next()) {
                int hauteur = requestAll.getInt("hauteur");
                int largeur = requestAll.getInt("largeur");
                crossword = new Crossword(hauteur, largeur);
                populateCrosswordGrid(crossword, requestAll);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crossword;
    }

    private void populateCrosswordGrid(Crossword crossword, ResultSet rs) throws SQLException {
        
        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                boolean isBlack = rs.getBoolean("is_black_" + i + "_" + j);
                crossword.setBlackSquare(i, j, isBlack);
            }
        }
    }

    public static void main(String[] args) {
        Database cg = new Database();
        Map<Integer, String> m = cg.availableGrids();
        System.out.println(m);
        Scanner sc = new Scanner(System.in);
        int choix;
        do {
            System.out.println("Choisissez une grille svp");
            choix = sc.nextInt();
            if (m.containsKey(choix)) {
                cg.extractGrid(choix);
                break;
            } else {
                System.out.println("Grille inexistante");
            }
        } while (true);
    }
}

