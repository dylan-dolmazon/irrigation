/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_systemes;

/**
 *
 * @author louis
 */
public class Réservoir {
    private float quantite;
    private String contenu;
    private Vanne reserv;

    public Réservoir(float quantite, String contenu) {
        this.quantite = quantite;
        this.contenu = contenu;
    }
    
    
    public Réservoir() {
        
        quantite = 0;
        contenu = null;
        
    }

    public float getQuantite() {
        return quantite;
    }

    public String getContenu() {
        return contenu;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public int nouvelleVerif(){
        
        return 0;
    }
}
