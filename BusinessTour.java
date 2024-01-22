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
import java.util.Arrays;
import java.util.LinkedList;
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
                int koszt_1_domu = rs.getInt("koszt_1_domu");
                int koszt_2_domow = rs.getInt("koszt_2_domow");
                int koszt_3_domow = rs.getInt("koszt_3_domow");
                int koszt_hotelu = rs.getInt("koszt_hotelu");
                double czynsz_pole = rs.getDouble("czynsz_pole");
                double czynsz_1_dom = rs.getDouble("czynsz_1_dom");
                double czynsz_2_dom = rs.getDouble("czynsz_2_dom");
                double czynsz_3_dom = rs.getDouble("czynsz_3_dom");
                double czynsz_hotel = rs.getDouble("czynsz_hotel");
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

    public static int rzut_kostka(int pozycja, Gracz gracz) {
        Random rand = new Random();
        int pierwsza_kostka = rand.nextInt((6 - 1) + 1) + 1;
        int druga_kostka = rand.nextInt((6 - 1) + 1) + 1;
        int o_ile_sie_przesunie = pierwsza_kostka + druga_kostka;
        pozycja += o_ile_sie_przesunie;
        System.out.println("Wylosowane liczby to: " + pierwsza_kostka + "+" + druga_kostka + "=" + o_ile_sie_przesunie);
        if (pozycja >= 32) {
            System.out.println("Gracz " + gracz.nazwa_gracza + " przechodzi przez START. W nagrodę otrzymuje 200.");
            gracz.majatek += 200;
            System.out.println("Stan konta: " + gracz.majatek);
            pozycja -= 32;
        }
        return pozycja;
    }

    public static void gdzie_jestes(Pola[] pola, Gracz gracz) {
        int o = 0;
        for (o = 0; o < 32; o++) {
            if (o == gracz.pozycja_gracza) {
                boldText("------> TUTAJ JESTEŚ " + gracz.nazwa_gracza + " ");

            }
            System.out.println(pola[o].nazwa_pola + " ");
        }
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

    public static int ruch(String NAME, Pola[] pola, Gracz gracz, Gracz[] gracze) {
        System.out.println(" ");
        System.out.print(gracz.nazwa_gracza + " ");
        enter();

        if (gracz.pozycja_gracza == 24) {
            gracz.pozycja_gracza = PODROZ(pola, gracz);
        } else {
            czekajnaenter();
            gracz.pozycja_gracza = rzut_kostka(gracz.pozycja_gracza, gracz);
        }
        gdzie_jestes(pola, gracz);
        IFpodroz(gracz);
        IFpodatek(gracz);

        if (pola[gracz.pozycja_gracza].wlasciciel != null) {
            double dostepneSrodki = gracz.majatek;
            System.out.println("To pole jest własnością: " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + ". Musisz zapłacić mu czynsz w wysokości: ");
            IFczynsz_pole(pola, gracz, gracze);
            IFczynsz_1dom(pola, gracz, gracze);
            IFczynsz_2dom(pola, gracz, gracze);
            IFczynsz_3dom(pola, gracz, gracze);
            IFczynsz_hotel(pola, gracz, gracze);

        } else if (gracz.pozycja_gracza != 0 && gracz.pozycja_gracza != 8 && gracz.pozycja_gracza != 12 && gracz.pozycja_gracza != 16 && gracz.pozycja_gracza != 20 && gracz.pozycja_gracza != 28 && gracz.pozycja_gracza
                != 30) {
            System.out.println();
            System.out.println("Czy chcesz kupić " + pola[gracz.pozycja_gracza].nazwa_pola + " za: " + pola[gracz.pozycja_gracza].koszt_kupna_pola);
            System.out.println("Wpisz w konsoli TAK lub NIE");
            String taknie = sc.nextLine();
            if ("TAK".equals(taknie) || "tak".equals(taknie) && gracz.majatek > pola[gracz.pozycja_gracza].koszt_kupna_pola && pola[gracz.pozycja_gracza].wlasciciel == null) {
                pola[gracz.pozycja_gracza].wlasciciel = gracz;
                pola[gracz.pozycja_gracza].stopien_rozbudowy = 0;
                gracz.majatek -= pola[gracz.pozycja_gracza].koszt_kupna_pola;
                gracz.posiadlosci.add(pola[gracz.pozycja_gracza]);

                plaze(gracz, pola);
                wyborkupna(gracz, pola);
                System.out.println("Stan konta: " + gracz.majatek);
                if (gracz.majatek < pola[gracz.pozycja_gracza].koszt_kupna_pola) {
                    System.out.println("Nie masz wystarczającej ilości pieniędzy");
                }

            }
        }
        return gracz.pozycja_gracza;
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

    public static int IFpodroz(Gracz gracz) {
        if (gracz.pozycja_gracza
                == 24) {
            System.out.println("Jesteś na polu podróży, w następnej rundzie będziesz mógł wybrać cel swojej podróży lub rzucić kostką");

        }
        return gracz.pozycja_gracza;
    }

    public static int PODROZ(Pola[] pola, Gracz gracz) {
        System.out.println("Czy chcesz skorzystać z opcji podróży? Płatne 50. Wpisz w konsoli TAK lub NIE");
        String taknie = sc.nextLine();
        if (taknie.equals("TAK") || taknie.equals("tak")) {
            gracz.majatek -= 50;
            System.out.println("Stan konta: " + gracz.majatek);
            System.out.println("Wybierz cel podróży: ");
            for (int i = 0; i < 32; i++) {

                if (pola[i].wlasciciel == null && i != 0 && i != 8 && i != 12 && i != 16 && i != 20 && i != 24 && i != 28 && i != 30) { //tutaj wybieram pola, na które można podróżować ( nie może to być np. start, szansa itp.)

                    System.out.print(pola[i].nazwa_pola + " ");
                    System.out.println();
                }
            }

            String cel = sc.nextLine();
            for (int i = 0; i < 32; i++) {
                if (cel.equals(pola[i].nazwa_pola)) { //sprawdzam co wpisał gracz, jakie pole.
                    if (pola[i].wlasciciel == null && i != 0 && i != 8 && i != 12 && i != 16 && i != 20 && i != 24 && i != 28 && i != 30) { //daje ponownie te same warunki co przy wypisywaniu
                        gracz.pozycja_gracza = i;
                        return gracz.pozycja_gracza;
                    } else {
                        System.out.println("Błąd. Nie możesz wejść na to pole, spróbuj ponownie.");
                        return PODROZ(pola, gracz);
                    }
                }

            }
        }
        if (taknie.equals("NIE") || taknie.equals("nie")) {
            return rzut_kostka(gracz.pozycja_gracza, gracz);
        }
        return gracz.pozycja_gracza;
    }

    public static void IFpodatek(Gracz gracz) {
        if (gracz.pozycja_gracza == 30 && gracz.posiadlosci.size() > 0) {
            System.out.println("Musisz zapłacić podatek od swoich nieruchomości, w wysokości 100");
            gracz.majatek -= 100;
            System.out.println("Zostało Ci: " + gracz.majatek);
        }
        if (gracz.pozycja_gracza == 30 && gracz.posiadlosci.size() < 1) {
            System.out.println("Nie masz żadnych posiadłości");
        }
    }

    public static void IFczynsz_pole(Pola[] pola, Gracz gracz, Gracz[] gracze) {
        double dostepneSrodki = gracz.majatek;
        if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek >= pola[gracz.pozycja_gracza].czynsz_pole) {
            System.out.println(pola[gracz.pozycja_gracza].czynsz_pole);
            gracz.majatek -= pola[gracz.pozycja_gracza].czynsz_pole;
            pola[gracz.pozycja_gracza].wlasciciel.majatek += pola[gracz.pozycja_gracza].czynsz_pole;
            System.out.println("Zostalo Ci: " + gracz.majatek + "Natomiast gracz " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + "zarobił +" + pola[gracz.pozycja_gracza].czynsz_pole + "i ma teraz: " + pola[gracz.pozycja_gracza].wlasciciel.majatek);
        } else if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek <= pola[gracz.pozycja_gracza].czynsz_pole) {
            gracz.majatek -= dostepneSrodki;
            System.out.println("Brak wystarczających środków. Pobrano dostępną kwotę: " + dostepneSrodki);
            System.out.println("Niestety Twoja gra dobiegła końca. Odpadasz.");
            ArrayList<Gracz> listaGraczy = new ArrayList<>(Arrays.asList(gracze));
            Gracz graczDoUsuniecia = gracz;
            listaGraczy.remove(graczDoUsuniecia);
            System.out.println("Lista graczy po usunięciu: " + listaGraczy.toString());
            if(listaGraczy.size()==1){
            System.out.println("Gratulacje! Wszyscy gracze zostali wyeliminowani. Wygrał gracz"+listaGraczy.toString());
            }
        }
    }

    public static void IFczynsz_1dom(Pola[] pola, Gracz gracz, Gracz[] gracze) {
        double dostepneSrodki = gracz.majatek;
        if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 1) {
            System.out.println(pola[gracz.pozycja_gracza].czynsz_1_dom);
            gracz.majatek -= pola[gracz.pozycja_gracza].czynsz_1_dom;
            pola[gracz.pozycja_gracza].wlasciciel.majatek += pola[gracz.pozycja_gracza].czynsz_1_dom;
            System.out.println("Zostalo Ci: " + gracz.majatek + "Natomiast gracz " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + "zarobił +" + pola[gracz.pozycja_gracza].czynsz_1_dom + "i ma teraz: " + pola[gracz.pozycja_gracza].wlasciciel.majatek);
        }
          else if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek <= pola[gracz.pozycja_gracza].czynsz_1_dom) {
            gracz.majatek -= dostepneSrodki;
            System.out.println("Brak wystarczających środków. Pobrano dostępną kwotę: " + dostepneSrodki);
            System.out.println("Niestety Twoja gra dobiegła końca. Odpadasz.");
            ArrayList<Gracz> listaGraczy = new ArrayList<>(Arrays.asList(gracze));
            Gracz graczDoUsuniecia = gracz;
            listaGraczy.remove(graczDoUsuniecia);
            System.out.println("Lista graczy po usunięciu: " + listaGraczy.toString());
            if(listaGraczy.size()==1){
            System.out.println("Gratulacje! Wszyscy gracze zostali wyeliminowani. Wygrał gracz"+listaGraczy.toString());
            }
        }
    }

    public static void IFczynsz_2dom(Pola[] pola, Gracz gracz, Gracz[] gracze) {
        double dostepneSrodki = gracz.majatek;
        if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 2) {
            System.out.println(pola[gracz.pozycja_gracza].czynsz_2_dom);
            gracz.majatek -= pola[gracz.pozycja_gracza].czynsz_2_dom;
            pola[gracz.pozycja_gracza].wlasciciel.majatek += pola[gracz.pozycja_gracza].czynsz_2_dom;
            System.out.println("Zostalo Ci: " + gracz.majatek + "Natomiast gracz " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + "zarobił +" + pola[gracz.pozycja_gracza].czynsz_2_dom + "i ma teraz: " + pola[gracz.pozycja_gracza].wlasciciel.majatek);
        }else if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek <= pola[gracz.pozycja_gracza].czynsz_2_dom) {
            gracz.majatek -= dostepneSrodki;
            System.out.println("Brak wystarczających środków. Pobrano dostępną kwotę: " + dostepneSrodki);
            System.out.println("Niestety Twoja gra dobiegła końca. Odpadasz.");
            ArrayList<Gracz> listaGraczy = new ArrayList<>(Arrays.asList(gracze));
            Gracz graczDoUsuniecia = gracz;
            listaGraczy.remove(graczDoUsuniecia);
            System.out.println("Lista graczy po usunięciu: " + listaGraczy.toString());
            if(listaGraczy.size()==1){
            System.out.println("Gratulacje! Wszyscy gracze zostali wyeliminowani. Wygrał gracz"+listaGraczy.toString());
            }
        }
    }

    public static void IFczynsz_3dom(Pola[] pola, Gracz gracz, Gracz[] gracze) {
        double dostepneSrodki = gracz.majatek;
        if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 3) {
            System.out.println(pola[gracz.pozycja_gracza].czynsz_3_dom);
            gracz.majatek -= pola[gracz.pozycja_gracza].czynsz_3_dom;
            pola[gracz.pozycja_gracza].wlasciciel.majatek += pola[gracz.pozycja_gracza].czynsz_3_dom;
            System.out.println("Zostalo Ci: " + gracz.majatek + "Natomiast gracz " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + "zarobił +" + pola[gracz.pozycja_gracza].czynsz_3_dom + "i ma teraz: " + pola[gracz.pozycja_gracza].wlasciciel.majatek);
        }else if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek <= pola[gracz.pozycja_gracza].czynsz_3_dom) {
            gracz.majatek -= dostepneSrodki;
            System.out.println("Brak wystarczających środków. Pobrano dostępną kwotę: " + dostepneSrodki);
            System.out.println("Niestety Twoja gra dobiegła końca. Odpadasz.");
            ArrayList<Gracz> listaGraczy = new ArrayList<>(Arrays.asList(gracze));
            Gracz graczDoUsuniecia = gracz;
            listaGraczy.remove(graczDoUsuniecia);
            System.out.println("Lista graczy po usunięciu: " + listaGraczy.toString());
            if(listaGraczy.size()==1){
            System.out.println("Gratulacje! Wszyscy gracze zostali wyeliminowani. Wygrał gracz"+listaGraczy.toString());
            }
        }
    }

    public static void IFczynsz_hotel(Pola[] pola, Gracz gracz, Gracz[] gracze) {
        double dostepneSrodki = gracz.majatek;
        if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 4) {
            System.out.println(pola[gracz.pozycja_gracza].czynsz_hotel);
            gracz.majatek -= pola[gracz.pozycja_gracza].czynsz_hotel;
            pola[gracz.pozycja_gracza].wlasciciel.majatek += pola[gracz.pozycja_gracza].czynsz_hotel;
            System.out.println("Zostalo Ci: " + gracz.majatek + ". Natomiast gracz " + pola[gracz.pozycja_gracza].wlasciciel.nazwa_gracza + " zarobił +" + pola[gracz.pozycja_gracza].czynsz_hotel + " i ma teraz: " + pola[gracz.pozycja_gracza].wlasciciel.majatek);
        }else if (pola[gracz.pozycja_gracza].stopien_rozbudowy == 0 && gracz.majatek <= pola[gracz.pozycja_gracza].czynsz_hotel) {
            gracz.majatek -= dostepneSrodki;
            System.out.println("Brak wystarczających środków. Pobrano dostępną kwotę: " + dostepneSrodki);
            System.out.println("Niestety Twoja gra dobiegła końca. Odpadasz.");
            ArrayList<Gracz> listaGraczy = new ArrayList<>(Arrays.asList(gracze));
            Gracz graczDoUsuniecia = gracz;
            listaGraczy.remove(graczDoUsuniecia);
            System.out.println("Lista graczy po usunięciu: " + listaGraczy.toString());
            if(listaGraczy.size()==1){
            System.out.println("Gratulacje! Wszyscy gracze zostali wyeliminowani. Wygrał gracz"+listaGraczy.toString());
            }
        }
    }

    public static int plaze(Gracz gracz, Pola[] pola) {
        
        if (gracz.pozycja_gracza == 4 || gracz.pozycja_gracza == 14 || gracz.pozycja_gracza == 18 || gracz.pozycja_gracza == 25) {

            pola[gracz.pozycja_gracza].stopien_rozbudowy = gracz.liczba_plaz;
            gracz.liczba_plaz += 1;
            if (gracz.liczba_plaz == 4) {
                System.out.println("Wygrał gracz " + gracz.nazwa_gracza + " Gratulacje!");
                return -1;
            }

        }
        return 0;
    }

    public static void wyborkupna(Gracz gracz, Pola[] pola) {
        double one_dd = pola[gracz.pozycja_gracza].koszt_kupna_pola + pola[gracz.pozycja_gracza].koszt_1_domu;
        double two_dd = pola[gracz.pozycja_gracza].koszt_kupna_pola + pola[gracz.pozycja_gracza].koszt_1_domu + pola[gracz.pozycja_gracza].koszt_2_domow;
        double three_dd = pola[gracz.pozycja_gracza].koszt_kupna_pola + pola[gracz.pozycja_gracza].koszt_1_domu + pola[gracz.pozycja_gracza].koszt_2_domow + pola[gracz.pozycja_gracza].koszt_3_domow;
        double hott = pola[gracz.pozycja_gracza].koszt_kupna_pola + pola[gracz.pozycja_gracza].koszt_1_domu + pola[gracz.pozycja_gracza].koszt_2_domow + pola[gracz.pozycja_gracza].koszt_3_domow + pola[gracz.pozycja_gracza].koszt_hotelu;

        double one_d = gracz.majatek - one_dd;
        double two_d = gracz.majatek - two_dd;
        double three_d = gracz.majatek - three_dd;
        double hot = gracz.majatek - hott;
        if (gracz.pozycja_gracza != 4 && gracz.pozycja_gracza != 14 && gracz.pozycja_gracza != 18 && gracz.pozycja_gracza != 25) {
            System.out.println("Wybierz co chcesz kupić: POLE(" + pola[gracz.pozycja_gracza].koszt_kupna_pola + "), 1 DOMEK (" + one_dd + "), 2 DOMKI (" + two_dd + "), 3 DOMKI (" + three_dd + "), HOTEL(" + hott + ")");
            String rozbudowa = sc.nextLine();

            if (("1 DOMEK".equals(rozbudowa) || "1 domek".equals(rozbudowa)) && gracz.majatek >= one_d) {
                gracz.majatek = one_d;
                pola[gracz.pozycja_gracza].stopien_rozbudowy = 1;
            }
            if (("2 DOMKI".equals(rozbudowa) || "2 domki".equals(rozbudowa)) && gracz.majatek >= two_d) {
                gracz.majatek = two_d;
                pola[gracz.pozycja_gracza].stopien_rozbudowy = 2;
            }
            if (("3 DOMKI".equals(rozbudowa) || "3 domki".equals(rozbudowa)) && gracz.majatek >= three_d) {
                gracz.majatek = three_d;
                pola[gracz.pozycja_gracza].stopien_rozbudowy = 3;
            }
            if (("HOTEL".equals(rozbudowa) || "hotel".equals(rozbudowa)) && gracz.majatek >= hot) {
                gracz.majatek = hot;
                pola[gracz.pozycja_gracza].stopien_rozbudowy = 4;
            }

        }
    }

    public static void main(String[] args) {

        Gracz[] gracze = wybor_ilosci_graczy();
        Pola[] pola;
        pola = SelectAll();
        sc.nextLine();
        for (int i = 0; i < 20; i++) {
            for (int x = 0; x < gracze.length; x++) {
                int pozycja_gracza = ruch(gracze[x].nazwa_gracza, pola, gracze[x], gracze);
                if (pozycja_gracza == -1) {
                    return;
                }
                gracze[x].pozycja_gracza = pozycja_gracza;
            }
        }
    }

}
