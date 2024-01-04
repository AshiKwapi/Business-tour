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
    
   public static String [] SelectAll() {
    List<String> nazwaPolaList = new ArrayList<>(); // Lista do przechowywania wartości "nazwa_pola"

    String sql = "SELECT * FROM Pola";
    try {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String nazwaPola = rs.getString("nazwa_pola");
            int kosztKupnaPola = rs.getInt("koszt_kupna_pola");
            
            System.out.println(nazwaPola + '\t' + kosztKupnaPola);
            
            // Dodawanie wartości "nazwa_pola" do listy
            nazwaPolaList.add(nazwaPola);
        }

        // Zamknięcie połączenia
        conn.close();

        // Przetwarzanie listy "nazwa_pola"
        String[] nazwaPolaArray = nazwaPolaList.toArray(new String[0]);
 return nazwaPolaArray;
        // Możesz teraz wykorzystać tablicę nazwaPolaArray do dalszego przetwarzania
        // np. przekazując ją do innych metod lub wykonując na niej odpowiednie operacje.
    } 
    catch (SQLException e) 
    
    {
        System.out.println(e.getMessage());
    }return null;
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
    
    public static void ruch(String NAME, String[] pola) {
        System.out.println(" ");
        System.out.print(NAME + " ");
        enter();
        
        int przechowywator = 0;
        
        czekajnaenter();
        przechowywator = rzut_kostka(przechowywator);
        int o = 0;
        for (o = 0; o < 32; o++) {
            if (o == przechowywator) {
                 boldText("------> TUTAJ JESTEŚ " + NAME + " ");
                
            }
            System.out.println(pola[o] + " ");
        }
        
    }
     public static void boldText(String text) {
        // Kod ANSI Escape do ustawienia pogrubienia
        String boldCode = "\u001b[1m";
        // Kod ANSI Escape do zresetowania ustawień pogrubienia
        String resetCode = "\u001b[0m";

        // Wydrukuj tekst z ustawieniem pogrubienia
        System.out.print(boldCode + text + resetCode);
    }
 public static Gracz [] wybor_ilosci_graczy (){
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

      
       Gracz [] gracze=wybor_ilosci_graczy ();
        String[] pola = SelectAll();
        
        for(int x=0;x<gracze.length ;x++){
        ruch( gracze[x].nazwa_gracza, pola);}
        
        
        
    }
}
class Gracz{
    String nazwa_gracza;
    int pozycja_gracza;
    double majatek;
    public Gracz(String nazwa_gracza) {
            this.nazwa_gracza = nazwa_gracza;
}
}
