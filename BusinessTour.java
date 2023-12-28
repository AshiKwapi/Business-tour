package businesstour;

import java.util.Random;
import java.util.Scanner;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BusinessTour {
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:Baza_Danych_pola.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Połączono z bazą danych SQLite");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BusinessTour.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return conn;
    }

    public static void SelectAll() {
        Pola pola;
        List<Pola> polaList = new ArrayList<>();
        String sql = "SELECT * FROM Pola";
        try {
                Connection conn = connect(); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println(rs.getString("nazwa_pola") + '\t' + rs.getInt("koszt_kupna_pola"));
                }
                    conn.close();
            }
        catch (SQLException e){
                        System.out.println(e.getMessage());
                        } 
    }
    

    public static int rzut_kostka(int pozycja) {
        Random rand = new Random();
        int pierwsza_kostka = rand.nextInt((6 - 1) + 1) + 1;
        int druga_kostka = rand.nextInt((6 - 1) + 1) + 1;
        int o_ile_sie_przesunie = pierwsza_kostka + druga_kostka;
        pozycja += o_ile_sie_przesunie;
        System.out.println("Wylosowane liczby to: " + pierwsza_kostka + "+" + druga_kostka + "=" + o_ile_sie_przesunie);
        if (pozycja >= 32) {
            pozycja -= 32;
        }
        return pozycja;
    }

    public static void main(String[] args) {
        SelectAll();
        System.out.println("Proszę wprowadzić nazwę pierwszego gracza");
        Scanner sc = new Scanner(System.in);
        String NAME1 = sc.nextLine();
        System.out.println("Proszę wprowadzić nazwę drugiego gracza");
        String NAME2 = sc.nextLine();
        LocalTime startTime = java.time.LocalTime.now();
        LocalTime endTime = java.time.LocalTime.now();

        System.out.println("press enter to roll the dice");
        String name = sc.nextLine();
        if ("".equals(name)) {
            String[] pola = {"START", "Granada", "Seville", "Madrid", "Bali", "Hong Kong",
                "Beijing", "Shanghai", "LOST ISLAND", "Venice", "Milan", "Rome",
                "CHANCE", "Hamburg", "Cyprus", "Berlin", "WORLD CHAMPIONSHIPS",
                "London", "Dubai", "Sydney", "CHANCE", "Chicago", "Las Vegas",
                "New York", "World Tour", "Nice", "Lyon", "Paris", "CHANCE",
                "Osaka", "TAX", "Tokyo"};
            int przechowywator = 0;
            while (startTime.until(endTime, java.time.temporal.ChronoUnit.SECONDS) < 15) {
                przechowywator = rzut_kostka(przechowywator);
                int o = 0;
                for (o = 0; o < 32; o++) {
                    if (o == przechowywator) {
                        System.out.print("TUTAJ JESTEŚ " + NAME1 + " ");
                    }
                    System.out.println(pola[o] + " ");
                }
                System.out.println("press enter to roll the dice");
                String name1 = sc.nextLine();
                if ("".equals(name1)) {
                }
                int przechowywator2 = 0;
                przechowywator2 = rzut_kostka(przechowywator2);
                for (int y = 0; y < 32; y++) {
                    if (y == przechowywator2) {
                        System.out.print("TUTAJ JESTEŚ " + NAME2 + " ");
                    }
                    if (y == przechowywator) {
                        System.out.print("TUTAJ JESTEŚ " + NAME1 + " ");
                    }
                    System.out.println(pola[y] + " ");
                }
                System.out.println("press enter to roll the dice");
                String name2 = sc.nextLine();
                if ("".equals(name2)) {
                }
                endTime = java.time.LocalTime.now();
            }
        }
    }
}
