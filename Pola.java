
package businesstour;


public class Pola {

    String nazwa_pola;
    int koszt_kupna_pola;
    int koszt_1_domu;
    int koszt_2_domow;
    int koszt_3_domow;
    int koszt_hotelu;
    double czynsz_pole;
    double czynsz_1_dom;
    double czynsz_2_dom ;
    double czynsz_3_dom;
    double czynsz_hotel;
    Gracz wlasciciel;
    int stopien_rozbudowy=0;
    public Pola (){}

    Pola(String nazwa_pola, int koszt_kupna_pola, int koszt_1_domu, int koszt_2_domow, int koszt_3_domow, int koszt_hotelu, double czynsz_pole, double czynsz_1_dom, double czynsz_2_dom, double czynsz_3_dom, double czynsz_hotel) {
        this.nazwa_pola = nazwa_pola;
        this.koszt_kupna_pola = koszt_kupna_pola;
        this.koszt_1_domu = koszt_1_domu;
        this.koszt_2_domow = koszt_2_domow;
        this.koszt_3_domow = koszt_3_domow;
        this.koszt_hotelu = koszt_hotelu;
        this.czynsz_pole = czynsz_pole;
        this.czynsz_1_dom = czynsz_1_dom;
        this.czynsz_2_dom = czynsz_2_dom;
        this.czynsz_3_dom = czynsz_3_dom;
        this.czynsz_hotel = czynsz_hotel;
    }
    public String nazwa_pola(){
    return this.nazwa_pola;
}
    public int koszt_kupna_pola(){
    return this.koszt_kupna_pola;
}
     public int koszt_1_domu(){
    return this.koszt_1_domu;
}
     public int koszt_2_domow(){
    return this.koszt_2_domow;
}
     public int koszt_3_domow(){
    return this.koszt_3_domow;
}
     public int koszt_hotelu(){
    return this.koszt_hotelu;
}
     public double czynsz_pole(){
    return this.czynsz_pole;
}
     public double czynsz_1_dom(){
    return this.czynsz_1_dom;
}
     public double czynsz_2_dom (){
    return this.czynsz_2_dom;
}
     public double czynsz_3_dom(){
    return this.czynsz_3_dom;
}
     public double czynsz_hotel(){
    return this.czynsz_hotel;
}
}

