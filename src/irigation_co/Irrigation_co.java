package irigation_co;
import clases_systemes.Capteur;
import clases_systemes.Plante;
import clases_systemes.Pompe;
import clases_systemes.Raie;
import clases_systemes.Saison;
import com.pi4j.io.gpio.RaspiBcmPin;
import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_24;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static jdk.internal.org.jline.utils.Colors.s;
import ss2_rpi_2021.AnalogInput;
import ss2_rpi_2021.DHT;
import ss2_rpi_2021.DHT11;
import ss2_rpi_2021.DHT11Result;
import ss2_rpi_2021.DHTMock;
import ss2_rpi_2021.DigitaBCMGpio;
//import ss2_rpi_2021.DigitaBCMGpio;
import ss2_rpi_2021.DigitaBCMGpioListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author louis
 */
public class Irrigation_co {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException, SQLException {
        
        
        //création du système
        Systeme s = new Systeme();
        
        //création des plantes de base
        s.transforme_Plante("radis", 0, "", "radis", 25, 0,Raie.PREMIER);
        s.transforme_Plante("tomate", 0, "", "tomate", 30, 0);
        s.transforme_Plante("carotte", 0, "", "carotte", 40, 0);
        s.transforme_Plante("patate", 0, "", "patate", 10, 0);
        
        //création des ports de lecture de la carte
        DigitaBCMGpio led_raie1 = new DigitaBCMGpio(RaspiBcmPin.GPIO_16);
        DigitaBCMGpio led_raie2 = new DigitaBCMGpio(RaspiBcmPin.GPIO_05);
        
        //création des capteurs
        Capteur air = new Capteur("temp_aire");
        Capteur hum_air = new Capteur("hum_aire");
        Capteur hum_sol = new Capteur("sol");
        
        //on ajouute les capteurs au système
        s.ajouter_capteur(hum_sol);
        s.ajouter_capteur(hum_air);
        s.ajouter_capteur(air);
        
        //fenetre du programme
        Fenetre window = new Fenetre(s);
        window.setVisible(true);

        
        s.implémente_Capteurs();
        
//        DigitaBCMGpioListener bouton = new DigitaBCMGpioListener(RaspiBcmPin.GPIO_05);
        //bouton.start();
//        int BCM_26 = 25; // wiringPi number
//        //DHT dht22 = new DHT(BCM_26);
//        final DHT dht = new DHT(BCM_26);
//        dht.getTemperature();
//        for (int i = 0; i < 50; i++) {
//            Thread.sleep(100);
//            dht.getTemperature();
//        }

//capteur hum air/ hum sol 
//        DHT11 dht11 = new DHT11(26); // use GPIO pin 15
//        while (true) {
//            DHT11Result result = dht11.read();
//            
//            if (result.isValid()) {
//                System.out.println("Last valid input: " + new Date());
//                System.out.printf("Temperature: %.1f C\n" , result.getTemperature());
//                System.out.printf("Humidity:    %.1f %%\n", result.getHumidity());
//            }
//            
//            
//        
//        }

//        Plante p1=new Plante("Tomate",4,"","tomate",20,23,Saison.AUTOMNE,Raie.PREMIER);

       


       
       
//       System.out.println(air.get_etat_capteur()+" Degré de l'air");

//        
       
//        System.out.println(hum_air.get_etat_capteur()+" % d'humidité de l'air");
//        
        
        //System.out.println(hum_sol.get_etat_capteur()+" % d'humidité du sol");
//        

       
        
//        
//        
       // s1.transforme_Plante(p1);
       
        
        boolean led_raie1_allum=false;
        boolean led_raie2_allum=false;
        boolean fin_prog=false;
        boolean jaiarros=false;
        
        //boucle principale
        while(!fin_prog){
            s.implémente_BoutonUrgence();
            //on parcours toutes les plantes
            for(int j=0;j<s.savoir_Plantes_size();j++){
                //juste par ce qu'on a une plante c'est un test
                //j=0;
                //si les plantes sont plantés
                
                if(jaiarros==true){
                
                    j--;
                    
                    
                }
                
                if(s.savoir_Plante(j).getRaie()==Raie.PREMIER){//dans la premiere raie
                   //si l'humidité de la terre est inférieur à l'humidité nécessaire ça arrose
                   //capteur humidité du sol= 0
                   
                    if(s.savoir_Capteur(0)<s.savoir_Plante(j).getTaux_humidite()){
                        //si ça arrose allumer la led 
                        System.out.println(s.savoir_Capteur(0));
                        System.out.println(s.savoir_Plante(j).getNom_plante());
                        System.out.println(s.savoir_Plante(j).getTaux_humidite());
                        jaiarros=true;
                        if(led_raie1_allum==false){
                            led_raie1.allum(s);
                            System.out.println("j'allume la led/pompe");
                            
                            led_raie1_allum=true;
                        }else if(led_raie1_allum!=false){
                            System.out.println("raie 1 deja allume");
                            
                        }
                    }
                    else{
                        System.out.println(s.savoir_Capteur(0));
                        System.out.println("eteint led 1");
                        led_raie1.shut(s);
                        led_raie1_allum=false;
                        jaiarros=false;
                        //fin_prog=true;
                    }
                }
                
                
                
                if(s.savoir_Plante(j).getRaie()==Raie.DEUXIEME){//dans la seconde raie
                    if(s.savoir_Capteur(0)<s.savoir_Plante(j).getTaux_humidite()){
                        //si ça arrose allumer la led 
                        if(led_raie2_allum==false){
                            led_raie2.allum(s);
                            led_raie2_allum=true;
                        }else{
                            System.out.println("raie 2 deja allume");
                        }
                    }else{
                        System.out.println(s.savoir_Capteur(0));
                        System.out.println("eteint led 2");
                        led_raie2.shut(s);
                        led_raie2_allum=false;
                        //fin_prog=true;
                    }
                }
                System.out.println("toutes les plantes sont arroser");
                
            }
            fin_prog=true;
        }
        //arreter la lecture des capteurs
        led_raie1.sleep();
        led_raie2.sleep();
//        Capteur sol = new Capteur("temp_aire");
//      System.out.println(sol.get_etat_capteur()+" Degré de l'air");
//        Capteur sol1 = new Capteur("hum_aire");
//        System.out.println(sol1.get_etat_capteur()+" % d'humidité de l'air");
//        Capteur sol2 = new Capteur("sol");
//        System.out.println(sol2.get_etat_capteur()+" % d'humidité du sol");

//pompe sa mere
//    DigitaBCMGpio digitaBCMGpio = new DigitaBCMGpio(RaspiBcmPin.GPIO_24);
//        digitaBCMGpio.start();
       // Pompe p=new Pompe(RaspiBcmPin.GPIO_24);
    //DigitaBCMGpio led = new DigitaBCMGpio(RaspiBcmPin.GPIO_24);
    
//        int i=0;
//        Thread.sleep(1000);
//        p.allum();
//        while(1==1){
//            //p.start();
//            
//           // System.out.println(i);
//            i+=9;
//        }
//    
//    }
    }
}
