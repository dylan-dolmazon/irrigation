/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_systemes;

import com.pi4j.io.gpio.RaspiBcmPin;
import ss2_rpi_2021.DigitaBCMGpioListener;

/**
 *
 * @author louis
 */
public class BoutonUrgence {
    
    private Boolean etatBouton;
    DigitaBCMGpioListener bout; 

    public BoutonUrgence(Boolean etatBouton) {
        this.etatBouton = etatBouton;
    }

    public Boolean getEtatBouton() {
        
        return etatBouton;
    }

    public void setEtatBouton() {
        
        bout.start();
        
        this.etatBouton =bout.etat_bout();
        
    }

    public BoutonUrgence() {
        etatBouton = false;
        System.out.println("erreur");
        bout=new DigitaBCMGpioListener(RaspiBcmPin.GPIO_22);
        
    }
}
