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
public class Vanne {
    private Boolean etatVanne;

    public Vanne(Boolean etatVanne) {
        this.etatVanne = etatVanne;
    }

    public Vanne() {
    }

    public Boolean getEtatVanne() {
        return etatVanne;
    }

    public void setEtatVanne(Boolean etatVanne) {
        this.etatVanne = etatVanne;
    }
}
