package clases_systemes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author louis
 */
public class Plante {
    private String nom_plante;
    private int quantite_de_nutriment;
    private String nutriment;
    private String img_plante;
    private int taux_humidite;
    private int seuil_temperature;
    private Saison saison;
    private Raie raie;

    //constructeur sans la saison
    public Plante(String nom_plante, int quantite_de_nutriment, String nutriment, String img_plante, int taux_humidite, int seuil_temperature, Raie raie) {
        this.nom_plante = nom_plante;
        this.quantite_de_nutriment = quantite_de_nutriment;
        this.nutriment = nutriment;
        this.img_plante = img_plante;
        this.taux_humidite = taux_humidite;
        this.seuil_temperature = seuil_temperature;
        this.raie = raie;
    }

    //constructeur avec tous les attributs
    public Plante(String nom_plante, int quantite_de_nutriment, String nutriment, String img_plante, int taux_humidite, int seuil_temperature, Saison saison, Raie raie) {
        this.nom_plante = nom_plante;
        this.quantite_de_nutriment = quantite_de_nutriment;
        this.nutriment = nutriment;
        this.img_plante = img_plante;
        this.taux_humidite= taux_humidite;
        this.seuil_temperature = seuil_temperature;
        this.saison = saison;
        this.raie = raie;
    }

    //tous les setters
    public void setNom_plante(String nom_plante) {
        this.nom_plante = nom_plante;
    }

    public void setQuantite_de_nutriment(int quantite_de_nutriment) {
        this.quantite_de_nutriment = quantite_de_nutriment;
    }

    public void setNutriment(String nutriment) {
        this.nutriment = nutriment;
    }

    public void setImg_plante(String img_plante) {
        this.img_plante = img_plante;
    }

    public void setTaux_humidite(int taux_humidite) {
        this.taux_humidite = taux_humidite;
    }

    public void setSeuil_temperature(int seuil_temperature) {
        this.seuil_temperature = seuil_temperature;
        
    }

    @Override
    public String toString() {
        return "Plante{" + "nom_plante=" + nom_plante + ", quantite_du_nutriment=" + quantite_de_nutriment + ", nutriment=" + nutriment + ", img_plante=" + img_plante + ", taux_humidit\u00e9=" + taux_humidite + ", seuil_temperature=" + seuil_temperature + '}';
    }

    public void setSaison(Saison saison) {
        this.saison = saison;
    }

    public void setRaie(Raie raie) {
        this.raie = raie;
    }

    //tous les getters
    public String getNom_plante() {
        return nom_plante;
    }

    public int getQuantite_de_nutriment() {
        return quantite_de_nutriment;
    }

    public String getNutriment() {
        return nutriment;
    }

    public String getImg_plante() {
        return img_plante;
    }

    public int getTaux_humidite() {
        return taux_humidite;
    }

    public int getSeuil_temperature() {
        return seuil_temperature;
    }

    public Saison getSaison() {
        return saison;
    }

    public Raie getRaie() {
        return raie;
    }
    
    public Plante(Plante p){
    
        p.nom_plante=nom_plante;
        p.quantite_de_nutriment=quantite_de_nutriment;
        p.nutriment=nutriment;
        p.img_plante=img_plante;
        p.taux_humidite=taux_humidite;
        p.seuil_temperature=seuil_temperature;
        p.saison=saison;
        p.raie=raie;
        
        
    
    }
    //si le capteur d'humidité renvoie une valeur < taux_humidite faut arroser jusqu'a 
    public float Temps_arrosage(float arr){
    
        float resultat=0;
        
        return resultat;
    
    }
   
}
