/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irigation_co;

import SQL_gest.SQLiteJDBC;
import clases_systemes.BoutonUrgence;
import clases_systemes.Capteur;
import clases_systemes.Plante;
import clases_systemes.Pompe;
import clases_systemes.Raie;
import clases_systemes.Réservoir;
import clases_systemes.Vanne;
import com.pi4j.io.gpio.RaspiBcmPin;
import com.pi4j.io.i2c.I2CFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ss2_rpi_2021.DigitaBCMGpioListener;
import java.lang.Object;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import sun.tools.jconsole.Messages;


/**
 *
 * @author louis
 */
public class Systeme extends SQLiteJDBC{
    private Réservoir reservoir1;
    private Réservoir reservoir2;    
    private Vanne vanne1;
    private Vanne vanne2;    
    private Pompe pompe1;    
    private BoutonUrgence bouton;    
    private ArrayList<Plante> plantes;
    private ArrayList<Capteur> capteurs;//information à l'initalisation 0 et 1 sont de l'humidité et 3 la température
   
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Run>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void play(){//fonction a rappeler pour soccuper de toutes le instances en taches de fonds 
        System.out.println("Playing ;)");
        
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<initialisations>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public Systeme(Réservoir reservoir1, Réservoir reservoir2, Vanne vanne1, Vanne vanne2, Pompe pompe1, BoutonUrgence bouton) throws SQLException {
        this.reservoir1 = reservoir1;
        this.reservoir2 = reservoir2;
        this.vanne1 = vanne1;
        this.vanne2 = vanne2;
        this.pompe1 = pompe1;
        this.bouton = bouton;
        plantes=selectionner_table();
      
    }
    
    public Systeme() throws SQLException {

        
        reservoir1 = new Réservoir();
        reservoir2 = new Réservoir();
        
        vanne1 = new Vanne();
        vanne2=new Vanne();
        
        pompe1=new Pompe(RaspiBcmPin.GPIO_24);
        bouton=new BoutonUrgence();

        
        plantes=new ArrayList<>();
        capteurs= new ArrayList<>();
        
        //renvoie et donne toutes les plantes en mémoire
        plantes=selectionner_table();
        System.out.println("on a récupérer "+plantes.size()+" plantes de la base de donné");
//        for(Plante p:plantes)
//            System.out.println(p);
    }

    //intégration SQL okay

    //methodes interne à la classe:  
    private Boolean lect(){
        /*
        * 
        ***récupérations des données d'un fichier pour les plantes
        * 
        */
        String file = "my-file.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {//ouvre le flux du fichier pour la lecture
            String line;//variable pour stocker succesivement chaque ligne du fichier
            while ((line = br.readLine()) != null) {//continu tant que l'on attint pas la fin du fichier
                System.out.println(line);//affiche la ligne du fichier
                calc_line(line);//envoi la ligne du fichier se faire traiter et ajouter 
            }
        }
        catch (IOException e) {//recherche d'erreures à l'ouverture du flux sur le fichier
            System.out.println("ERROR");
            e.printStackTrace();
            return false;//pour signaler l'erreur au systeme
        }
        return true;//pour signaler le bon fonctionement au systeme
    }
    private Boolean write(){//écrit dans le fichier my-file.txt en écrasant tout ce qui est déjà ecrit si il existe sinon on le crée
        /*
        * 
        ***ecriture des données des plantes dans un fichier
        * 
        */
        String fileName = "my-file.txt";//pour definir le nom du fichier choisit
        try{
            List<String> lines = Arrays.asList("","");//on comence lines par deux chaine vide soit par deux saut de ligne(lignes vides)
            for(Plante p:plantes)//on met les valeurs des plantes dans lines
                lines.add(p.toString());
            Path file = Paths.get(fileName);//ouverture du flux
            Files.write(file, lines, StandardCharsets.UTF_8);//ecrire la liste dans le dossier
        }
        catch (IOException e){//recherche d'une erreure d'ouverture du flux
            System.out.println("ERROR");
            e.printStackTrace();
            return false;//pour signaler l'erreur au systeme
        }
        return true;//pour signaler le bon fonctionement au systeme
    }
    private void calc_line(String input){
        /*
        * 
        ***récupérations des du fichier pour les metre dans a jour dans le tableau de plante
        * 
        */
        String nom="", nut="", img="";
        int t_h=0, s_tp=0, q_nut=0,seuil_temp=0;
        /*
        //écriture générique d'une plante en une ligne pour ne pas géner la lecture!!!
            "Plante{" +
            "nom_plante=" + nom_plante +
            ", quantite_du_nutriment=" + quantite_de_nutriment + 
            ", nutriment=" + nutriment +
            ", img_plante=" + img_plante +
            ", taux_humidit\u00e9=" + taux_humidite +
            ", seuil_temperature=" + seuil_temperature + '}';
            }
        */
        //methode pour récupérer les données du string
        Scanner s = new Scanner(input);
        //s.findInLine("(\\d+) fish (\\d+) fish (\\w+) fish (\\w+)");
        s.findInLine("Plante{nom_plante=(\\w+), quantite_du_nutriment=(\\d+), nutriment=(\\w+), img_plante=(\\w+), taux_humidit\\u00e9=(\\d+), seuil_temperature=(\\d+)}");
        MatchResult result = s.match();
        for (int i=1; i<=result.groupCount(); i++){
            if(i==1)
                nom=result.group(i);
            else if(i==2)
                q_nut=Integer.parseInt(result.group(i));
            else if(i==3)
                nut=result.group(i);
            else if(i==4)
                img=result.group(i);
            else if(i==5)
                t_h=Integer.parseInt(result.group(i));
            else if(i==6)
                seuil_temp=Integer.parseInt(result.group(i));
        }
        s.close();
        plantes.add(new Plante(nom,q_nut,nut,img,t_h,seuil_temp,Raie.ZERO)); // appeler et mod pour recupérer les donneé des plantes sur les lignes
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<retours bruts (get) pour affichage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public float savoir_Réservoir(Boolean r1){//rappel pour le type int de la quanti?
        if(r1)
            return reservoir1.getQuantite();
        else
            return reservoir2.getQuantite();
    }
    public ArrayList<Plante> savoir_Plantes(){
        return plantes;
    }
    public Plante savoir_Plante(int ind){
        return plantes.get(ind);
    }
    public float savoir_Capteur(int capt) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException{
        return capteurs.get(capt).get_etat_capteur();
    }
    public Boolean savoir_Vannes(Boolean v1){
        if(v1)
            return vanne1.getEtatVanne();
        else
            return vanne2.getEtatVanne();
    }
    public Boolean savoir_Pompe(){
        return pompe1.getEtatPompe();
    }
    public int savoir_Plantes_size(){
        return plantes.size();
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<transformations (set) pour affichage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //les capteurs, le bouton d'urgence et les réservoirs ne son pas appeler car on ne veut pas les modifier ou il le font tout seuls
    public void transforme_Réservoir(Boolean r1, String s){
        if(r1)
            reservoir1.setContenu(s);
        else
            reservoir2.setContenu(s);
    }
    public void transforme_Plante(Plante p) throws SQLException{
        Boolean vérif_nom_unique=true;
        for(Plante i:plantes){
            if(i.getNom_plante()==p.getNom_plante()){
                vérif_nom_unique=false;
            }
        }
        if(vérif_nom_unique){
            plantes.add(p);
            insérer_table(p);
        }
    }//intégration SQL okay
    public void transforme_Plante(String nom_plante, int quantite_du_nutriment, String nutriment, String img_plante, int taux_humidité, int seuil_temperature) throws SQLException{
        Boolean vérif_nom_unique=true;
        Plante p=new Plante(nom_plante,quantite_du_nutriment,nutriment,nom_plante,taux_humidité,seuil_temperature,Raie.ZERO);
        for(Plante i:plantes){
            if(i.getNom_plante().equals(p.getNom_plante())){
                vérif_nom_unique=false;
            }
        }
        if(vérif_nom_unique){
            plantes.add(p);
            insérer_table(p);
        }
    }//intégration SQL okay
    
     public void transforme_Plante(String nom_plante, int quantite_du_nutriment, String nutriment, String img_plante, int taux_humidité, int seuil_temperature,Raie rai) throws SQLException{
        Boolean vérif_nom_unique=true;
        Plante p=new Plante(nom_plante,quantite_du_nutriment,nutriment,nom_plante,taux_humidité,seuil_temperature,rai);
        for(Plante i:plantes){
            if(i.getNom_plante().equals(p.getNom_plante())){
                vérif_nom_unique=false;
            }
        }
        if(vérif_nom_unique){
            plantes.add(p);
            insérer_table(p);
        }
    }//intégration SQL okay
    
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<implémentation des classe pour rafraichissement (set) pour affichage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //les classe supportan une implémentation sont les Capteurs, Réservoir, bouton d'urgence
    public void implémente_Capteurs() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException{
          
        for(int i=0;i<capteurs.size();i++){
            
            System.out.println(capteurs.get(i).getNom());
             System.out.print(savoir_Capteur(i));
            if(capteurs.get(i).getNom()=="sol"){
            
                System.out.println("% d'humidité dans le sol");
            
            }
            if(capteurs.get(i).getNom()=="hum_aire"){
            
                System.out.println("% d'humidité dans l'air");
            
            }
            if(capteurs.get(i).getNom()=="temp_aire"){
            
                System.out.println(" degré de l'air");
                
            }
           
        
        }
            
        
        
    }
    public void implémente_BoutonUrgence(){
        
        bouton.setEtatBouton();
        
        if(bouton.getEtatBouton()){
        
            System.exit(0);
            
        
        }
        
    }
    public void implémente_Réservoir(){
        reservoir1.setQuantite(0);
        reservoir2.setQuantite(0);
    }
    public void transforme_Vanne(Boolean v1, Boolean val){
        if(v1)
            vanne1.setEtatVanne(val);
        else
            vanne2.setEtatVanne(val);
    }//utilise une implémentation (hybride)
    public void transforme_Pompe(Boolean v){
        pompe1.setEtatPompe(v);
    }
    
    public void ajouter_capteur(Capteur capt){
        
        
        capteurs.add(capt);
    
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<retours améliorés (geting) pour affichage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public String Savoir_Réservoir(Boolean r1){//rappel pour le type int de la quanti?
        if(r1)
            return "La quantité restante dans le réservoir n°1 est"+reservoir1.getQuantite()+"%";
        else
            return "La quantité restante dans le réservoir n°1 est"+reservoir2.getQuantite()+"%";
    }
    public String Savoir_Plantes(){
        String s="";
        for(Plante p:plantes)
            s+=p;
        return s;
    }
    public String Savoir_Plante(int ind){
        return ""+plantes.get(ind).getNom_plante();
    }
    public String Savoir_Capteur(int capt) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException{
        capteurs.get(capt).get_etat_capteur();
        if(capt==0){
            return "Le capteur n°"+capt+" indique "+capteurs.get(capt).get_etat_capteur()+" d'humidité";
        }else if(capt==1){
            return "Le capteur n°"+capt+" indique "+capteurs.get(capt).get_etat_capteur()+" d'humidité";
        }else{
            return "Le capteur n°"+capt+" indique "+capteurs.get(capt).get_etat_capteur()+"°C";
        }
    }
    public String Savoir_Vannes(Boolean v1){
        if(v1){
            if(vanne1.getEtatVanne())
                return "La vanne n°1 est ouverte";
            return "La vanne n°1 n'est pas ouverte";
        }
        else{
            if(vanne2.getEtatVanne())
               return "La vanne n°2 est ouverte";
            return "La vanne n°2 n'est pas ouverte"; 
        }
    }
    public String Savoir_Pompe(){
        if(pompe1.getEtatPompe())
            return "La pompe est en fonctionement";
        return "La pompe n'est pas ne fonctionement";
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<suppréstion pour loge>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void supprimer_Plante(Plante p) throws SQLException {
        for(Plante i:plantes){
            if(i.getNom_plante()==p.getNom_plante()){
                plantes.remove(i);
                supprimer_table(i.getNom_plante());
            }
        }
    }//intégration SQL okay
    public void supprimer_Plante(String nom_de_la_plante_à_supprimer) throws SQLException {
        for(Plante i:plantes){
            if(i.getNom_plante()==nom_de_la_plante_à_supprimer){
                plantes.remove(i);
                supprimer_table(i.getNom_plante());
            }
        }
    }//intégration SQL okay
}
