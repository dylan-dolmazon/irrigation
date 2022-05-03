/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_systemes;

import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import ss2_rpi_2021.AnalogInput;
import ss2_rpi_2021.DHT11;
import ss2_rpi_2021.DHT11Result;

/**
 *
 * @author Enzo
 */
public class Capteur {

   private float donnee =4;
   private String nom;

    public Capteur(String nom) {
        this.nom = nom;
    }
   
   
   
   public float get_etat_capteur() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
       
       DHT11 dht11 = new DHT11(26);
       
       if(nom == "sol"){
           AnalogInput capt = new AnalogInput(0);
            capt.start();
           return 100*capt.getDataRead()/4096;
       }
       if(nom == "hum_aire"){

            boolean i=true;
            // use GPIO pin 15
            while (i) {
            DHT11Result result = dht11.read();

            if (result.isValid()) {
                donnee = (float) result.getHumidity();
                i=false;
            }
            
            TimeUnit.SECONDS.sleep(2);
            
        }
           
           
       }
       if(nom == "temp_aire"){
           System.out.println(nom);
            boolean i=true;
           
            while (i) {
                
            DHT11Result result = dht11.read();
                
            if (result.isValid()) {
                donnee = (float) result.getTemperature();
                
                i=false;
            }
            
            TimeUnit.SECONDS.sleep(2);
            
        }
           
       }
       
       return donnee;
   } 

 
   public void set_etat_capteur(boolean b) {
        b=b;
    }

    public String getNom() {
        return nom;
    }

   
   
   
}
