/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_systemes;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiBcmPin;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;
import java.util.logging.Level;
import java.util.logging.Logger;
import ss2_rpi_2021.DigitaBCMGpio;
import ss2_rpi_2021.Execute;

/**
 *
 * @author louis
 */
public class Pompe implements Execute {

    final private GpioPinDigitalOutput pin;
    final private GpioController gpio;
    private Boolean etatPompe;

    public Pompe(Boolean etatPompe, Pin pinNumber) throws UnsupportedOperationException {
        this.etatPompe = etatPompe;
        // create gpio controller
        gpio = GpioFactory.getInstance();
        // provision broadcom gpio pin #24 as an output pin and turn on
        pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.HIGH);
        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);
        setEtatPompe(etatPompe);
    }

    public Boolean getEtatPompe() {
        return etatPompe;
    }

    public void setEtatPompe(Boolean etatPompe) {
        this.etatPompe = etatPompe;
        if (etatPompe) {
            //start();
            allum();
        } else {
            shut();
        }
    }
    

    public Pompe(Pin pinNumber) throws UnsupportedOperationException {
        // in order to use the Broadcom GPIO pin numbering scheme, we need to configure the
        // GPIO factory to use a custom configured Raspberry Pi GPIO provider
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));

        // create gpio controller
        gpio = GpioFactory.getInstance();

        // provision broadcom gpio pin #16 as an output pin and turn on
        pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.HIGH);

        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);

        etatPompe = false;

    }

    @Override
    public void start() {
        System.out.println("<--Pi4J--> GPIO Control LED on GPIO BCM_24 ... started.");

        System.out.println("--> GPIO state should be: ON");

        try {
            Thread.sleep(1000);

            //turn off gpio pin 
            pin.low();
            System.out.println("--> GPIO state should be: OFF");
            Thread.sleep(100000);

            //toggle the current state of gpio pin  (should turn on)
            pin.toggle();
            System.out.println("--> GPIO state should be: ON");

            Thread.sleep(10000);

            // toggle the current state of gpio pin (should turn off)
            pin.toggle();
            System.out.println("--> GPIO state should be: OFF");

            Thread.sleep(1000);

            // turn on gpio pin  for 1 second and then off
            System.out.println("--> GPIO state should be: ON for only 1 second");
            pin.pulseSync(1000);

            //stop all GPIO activity/threads by shutting down the GPIO controller
            //(this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
            gpio.shutdown();

            System.out.println("Exiting DigitaBCMGpio");

        } catch (InterruptedException ex) {
            Logger.getLogger(DigitaBCMGpio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void allum() {

        //pin.setShutdownOptions(true, PinState.HIGH);
        pin.setState(PinState.HIGH);
        
    }

    public void shut() {

        pin.low();

    }

    public void sleep() {

        gpio.shutdown();

    }

    public static void main(String[] args) throws InterruptedException {
        // test unitaire sur GPIO_16
        DigitaBCMGpio digitaBCMGpio = new DigitaBCMGpio(RaspiBcmPin.GPIO_24);
        digitaBCMGpio.start();
    }
}
