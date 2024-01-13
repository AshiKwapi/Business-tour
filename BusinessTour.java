package businesstour;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
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

    static Scanner sc = new Scanner(System.in);

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

    public static Pola[] SelectAll() {
        Pola[] pola = new Pola[32]; // Lista do przechowywania wartości "nazwa_pola"

        String sql = "SELECT * FROM Pola";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
rs.next();
            for (int i = 0; i < 32; i++) {
                String nazwa_pola = rs.getString("nazwa_pola");
                int koszt_kupna_pola = rs.getInt("koszt_kupna_pola");
                int koszt_1_domu = 0;
                int koszt_2_domow =0;
                int koszt_3_domow = 0;
                int koszt_hotelu = 0;
                double czynsz_pole = 0;
                double czynsz_1_dom= 0;
                double czynsz_2_dom= 0;
                double czynsz_3_dom= 0;
                double czynsz_hotel= 0;
                pola[i] = new Pola(nazwa_pola, koszt_kupna_pola, koszt_1_domu, koszt_2_domow, koszt_3_domow, koszt_hotelu, czynsz_pole, czynsz_1_dom, czynsz_2_dom, czynsz_3_dom, czynsz_hotel);

               // System.out.println(nazwa_pola + '\t' + koszt_kupna_pola);
                rs.next();
               

            }

            // Zamknięcie połączenia
            conn.close();

            // Przetwarzanie listy "nazwa_pola"
            return pola;
            // Możesz teraz wykorzystać tablicę nazwaPolaArray do dalszego przetwarzania
            // np. przekazując ją do innych metod lub wykonując na niej odpowiednie operacje.
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
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

    public static String nazwa() {

        String NAME;
        System.out.println("Proszę wprowadzić nazwę pierwszego gracza");
        NAME = sc.nextLine();
        return NAME;
    }

    public static void enter() {
        System.out.println("naciśnij enter by rzucić kostką");
    }

    public static void czekajnaenter() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        long startTime = System.currentTimeMillis();
        for (int x = 15; x > 0; x--) {
            try {

                System.out.println(x);
                if (in.ready()) {
                    System.out.println(in.readLine());
                    break;
                } else {
                    Thread.sleep(1000);

                }
            } catch (IOException ex) {
                Logger.getLogger(BusinessTour.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(BusinessTour.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static int ruch(String NAME, Pola[] pola, Gracz gracz) {
        System.out.println(" ");
        System.out.print(NAME + " ");
        enter();

        int przechowywator = gracz.pozycja_gracza;

        czekajnaenter();
        przechowywator = rzut_kostka(przechowywator);
        int o = 0;
        for (o = 0; o < 32; o++) {
            if (o == przechowywator) {
                boldText("------> TUTAJ JESTEŚ " + NAME + " ");

            }
            System.out.println(pola[o].nazwa_pola + " ");
           
           
        }  System.out.println();
        System.out.println("Czy chcesz kupić " + pola[przechowywator].nazwa_pola + " za: "+pola[przechowywator].koszt_kupna_pola);
        System.out.println("Wpisz w konsoli TAK lub NIE");
        return przechowywator;
    }

    public static void boldText(String text) {
        // Kod ANSI Escape do ustawienia pogrubienia
        String boldCode = "\u001b[1m";
        // Kod ANSI Escape do zresetowania ustawień pogrubienia
        String resetCode = "\u001b[0m";

        // Wydrukuj tekst z ustawieniem pogrubienia
        System.out.print(boldCode + text + resetCode);
    }

    public static Gracz[] wybor_ilosci_graczy() {
        System.out.println("Wpisz ile graczy chcesz wprowadzić:  2, 3 lub 4");
        int ilegraczy = sc.nextInt();
        while (ilegraczy < 2 || ilegraczy > 4) {
            System.out.println("Nieprawidłowa liczba graczy. Wpisz ponownie: 2, 3 lub 4");
            ilegraczy = sc.nextInt();

        }

        Gracz[] gracze = new Gracz[ilegraczy];

        for (int i = 0; i < ilegraczy; i++) {
            System.out.println("Podaj nazwę gracza " + (i + 1) + ":");
            String nazwa_gracza = sc.next();
            gracze[i] = new Gracz(nazwa_gracza);

        }

        return gracze;
    }

    public static void main(String[] args) {

        Gracz[] gracze = wybor_ilosci_graczy();
        Pola[] pola;
       pola = SelectAll();
        for (int i = 0; i < 2; i++) {
            for (int x = 0; x < gracze.length; x++) {
                int pozycja_gracza = ruch(gracze[x].nazwa_gracza, pola, gracze[x]);
                gracze[x].pozycja_gracza = pozycja_gracza;
            }
        }
    }

}

class Gracz {

    String nazwa_gracza;
    int pozycja_gracza = 0;
    double majatek;

    public Gracz(String nazwa_gracza) {
        this.nazwa_gracza = nazwa_gracza;
    }
}
