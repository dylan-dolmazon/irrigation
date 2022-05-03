/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irigation_co;

/**
 *
 * @author louis
 */
import clases_systemes.Raie;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Fenetre extends JFrame implements ActionListener {

    //<<<<<<<<<< pano selection >>>>>>>>>>
    private JLabel Humi;
    private JLabel Lum;
    private JLabel Heure;
    private JLabel Temp;
    private JLabel Selec;
    protected ArrayList<JButton> legume = new ArrayList<JButton>();
    private ArrayList<JLabel> desc = new ArrayList<JLabel>();
    private ArrayList<ImageIcon> icone = new ArrayList<ImageIcon>();
    protected JButton Ajout1;
    protected JButton Ajout2;
    protected JButton Ajout3;
    protected JButton Retour;
    private ArrayList<JButton> select = new ArrayList<JButton>();

    //recuperer quel fruit est click
    JPanel pano;
    //<<<<<<<<<< pano selection >>>>>>>>>>

    protected Fenetre window;

    Accueil acc;
    Modif mod;
    Ajout1 ajo;
    PopUp popu;

    Systeme s;

    public Fenetre(Systeme s) {

        //personalisation de la fenetre
        this.setTitle("Accueil");
        this.setBounds(0, 0, 800, 480);
        acc = new Accueil(this);
        this.add(acc);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mod = new Modif(this);
        this.s = s;
        ajo = new Ajout1(this);
        popu = new PopUp(this);

    }

    public void ajout_plante(String n, String h, String nu, String saison, String t) throws SQLException {
        int humi = Integer.parseInt(h);
        int temp = Integer.parseInt(t);
        s.transforme_Plante(n, 0, nu, n, humi, temp);
    }

    public void affiche(int i) throws InterruptedException {

        if (i == 3) {
            this.setContentPane(mod);
            this.pack();
        }

        if (i == 1) {
            this.setContentPane(acc);
            this.pack();
        }

        if (i == 2) {
            this.initialisation(s);
           
        }

        if (i == 4) {
            this.setContentPane(ajo);
            this.pack();
        }

    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        for (int h = 0; h < legume.size(); h++) {

            //System.out.println("fruit click");
            if (evt.getSource().hashCode() == legume.get(h).hashCode()) {

                String name = JOptionPane.showInputDialog(this, "sur quelle raie?", "attention", JOptionPane.INFORMATION_MESSAGE);

                int numero = Integer.parseInt(name);
                //verifier auprès de toutes les plantes qu'il y a une raie de libre
                int cpt_rai1 = 0;
                int cpt_rai2 = 0;

                for (int i = 0; i < s.savoir_Plantes_size(); i++) {

                    if (s.savoir_Plante(i).getRaie() == Raie.PREMIER) {

                        cpt_rai1++;

                    }
                    if (s.savoir_Plante(i).getRaie() == Raie.DEUXIEME) {

                        cpt_rai2++;

                    }

                }

                if (cpt_rai1 > 0 && cpt_rai2 > 0) {

                    JOptionPane.showMessageDialog(this, "toutes les raies sont occupées", "warning", JOptionPane.WARNING_MESSAGE);

                }

                if (numero == 1 && cpt_rai1 == 0) {

                    s.savoir_Plante(h).setRaie(Raie.PREMIER);
                    JOptionPane.showMessageDialog(this, "Plante mise en raie 1", "warning", JOptionPane.WARNING_MESSAGE);
                    //name= ce qu'on recup de la pop_up, mettre à jour la raie de la plante avec si toutes les raies occupé mettre un pop_up attenbtion toutes ocuuper

                } else if (numero == 2 && cpt_rai2 == 0) {

                    JOptionPane.showMessageDialog(this, "Plante mise en raie 2", "warning", JOptionPane.WARNING_MESSAGE);
                    s.savoir_Plante(h).setRaie(Raie.DEUXIEME);
                    //name= ce qu'on recup de la pop_up, mettre à jour la raie de la plante avec si toutes les raies occupé mettre un pop_up attenbtion toutes ocuuper

                } //on essaie de voir si l'autre raie est disponible est on la met dedans
                else {

                    if (numero == 1 && cpt_rai2 == 0) {

                        s.savoir_Plante(h).setRaie(Raie.DEUXIEME);
                        JOptionPane.showMessageDialog(this, "la raie 1 est occupée, je la mets sur la raie 2", "warning", JOptionPane.WARNING_MESSAGE);

                    }
                    if (numero == 2 && cpt_rai1 == 0) {

                        JOptionPane.showMessageDialog(this, "la raie 2 est occupée, je la mets sur la raie 1", "warning", JOptionPane.WARNING_MESSAGE);
                        s.savoir_Plante(h).setRaie(Raie.PREMIER);

                    }

                }

            }

        }

    }

    public void initialisation(Systeme s) throws InterruptedException {

        for (int i = 0; i < s.savoir_Plantes_size(); i++) {

            desc.add(new JLabel());
            desc.get(i).setText(s.savoir_Plante(i).getNom_plante());
            legume.add(new JButton());

        }

        // création des composants;
        Humi = new JLabel("humidite");
        Humi.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 0)); // permet de définir des bordures vides 

        Lum = new JLabel("luminosite");
        Lum.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 0));

        Heure = new JLabel("heure");
        Heure.setBorder(BorderFactory.createEmptyBorder(10, 70, 25, 0));

        Temp = new JLabel("temperature");
        Temp.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 50));

        Selec = new JLabel("Menu selection");

        Retour = new JButton("Retour");

        Ajout1 = new JButton();
        Ajout1.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        ImageIcon add1 = new ImageIcon(getClass().getResource("img/plus.png"));
        Ajout1.setIcon(add1);
        Ajout1.setOpaque(false);
        Ajout1.setBorderPainted(false);
        Ajout1.setContentAreaFilled(false);

        Ajout2 = new JButton();
        Ajout2.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        ImageIcon add2 = new ImageIcon(getClass().getResource("img/plus.png"));
        Ajout2.setIcon(add2);
        Ajout2.setOpaque(false);
        Ajout2.setBorderPainted(false);
        Ajout2.setContentAreaFilled(false);

        Ajout3 = new JButton();
        Ajout3.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        ImageIcon add3 = new ImageIcon(getClass().getResource("img/plus.png"));
        Ajout3.setIcon(add3);
        Ajout3.setOpaque(false);
        Ajout3.setBorderPainted(false);
        Ajout3.setContentAreaFilled(false);

        //chemin d'accès des images
        String tmp;
        for (int i = 0; i < s.savoir_Plantes_size(); i++) {
            //bordure pour les descrpitions
            desc.get(i).setBorder(BorderFactory.createEmptyBorder(10, 200, 0, 0));
            //bordure pour les legumes(boutons)
            legume.get(i).setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));

            tmp = "img/" + s.savoir_Plante(i).getImg_plante() + ".png";

            icone.add(new ImageIcon(getClass().getResource(tmp)));

        }

        //boucle qui s'occuper des légumes
        for (int i = 0; i < s.savoir_Plantes_size(); i++) {

            legume.get(i).setOpaque(false);
            legume.get(i).setContentAreaFilled(false);
            legume.get(i).setBorderPainted(false);
            legume.get(i).setIcon(icone.get(i));

        }

        //création d'un panneau
        pano = new JPanel();

        // ajout du gestionnaire de placement au panneau
        pano.setLayout(new GridBagLayout());

        GridBagConstraints cont = new GridBagConstraints();

        // placement des composants
        cont.fill = GridBagConstraints.BOTH;

        // placement du taux d'humidité dans le sol
        cont.gridx = 0;//j
        cont.gridy = 0;//i
        pano.add(Humi, cont);

        // placement du taux de luminosité dans la serre
        cont.gridx = 1;
        cont.gridy = 0;
        pano.add(Lum, cont);

        // placement de l'heure
        cont.gridx = 2;
        cont.gridy = 0;
        pano.add(Heure, cont);

        // placement de la température dans la serre
        cont.gridx = 3;
        cont.gridy = 0;
        pano.add(Temp, cont);

        // placement du titre de la page
        cont.gridx = 0;
        cont.gridy = 2;
        pano.add(Selec, cont);

        //recupère les images
        int x = 0;
        // la ligne ou commence l'affichage de la liste des plantes
        int y = 4;

        for (int i = 0; i < s.savoir_Plantes_size(); i++) {

            for (int j = 0; j < 3; j++) {
                if (x < s.savoir_Plantes_size()) {
                    cont.gridx = j;
                    cont.gridy = y;
                    pano.add(desc.get(x), cont);
                    //System.out.println(desc.get(x));

                    cont.gridx = j;//j
                    cont.gridy = y;//i
                    pano.add(legume.get(x), cont);

                }
                i++;
                x++;
            }
            //pour contrer le i++ de la boucle for  
            i--;
            y++;

        }

        // si on a pas 3 plante sur la dernière ligne, la comblé par des plus
        int nb_plus = 0;

        if (s.savoir_Plantes_size() % 3 != 0) {

            nb_plus = s.savoir_Plantes_size() % 3;
            if (nb_plus == 1) {

                cont.gridx = 1;//j
                cont.gridy = y - 1;//i
                pano.add(Ajout1, cont);

                cont.gridx = 2;//j
                cont.gridy = y - 1;//i
                pano.add(Ajout2, cont);

            }
            if (nb_plus == 2) {

                cont.gridx = 2;//j
                cont.gridy = y - 1;//i
                pano.add(Ajout3, cont);
            }
        }

        cont.gridx = 0;
        cont.gridy = y;
        pano.add(Retour, cont);

        //listener exlisif pour ue le listener puisse lire tous les boutons
        Retour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    affiche(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        Ajout1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    //Action !

                    affiche(4);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        Ajout2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    //Action !

                    affiche(4);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        Ajout3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    //Action !

                    affiche(4);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        this.setContentPane(pano);
        this.pack();

        //ajoute un listener sur tous les boutons legumes
        for (int i = 0; i < legume.size(); i++) {

            legume.get(i).addActionListener(this);

        }

    }

    public ArrayList<JButton> getLegume() {
        return legume;
    }

}
