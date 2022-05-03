/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL_gest;
import clases_systemes.Plante;
import clases_systemes.Raie;
import clases_systemes.Saison;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author louis
 */
public class SQLiteJDBC {
    private String name="org.sqlite.JDBC";//pour créer une conexion vers une base 
    private String conection="jdbc:sqlite:data_base.db";//pour  créer la conexion, le nom du fichier est passer à la fin
    
    protected void créer_table() throws SQLException{
        Connection c = null;
        Statement stmt = null;
      
        try {
            Class.forName(name);
            c = DriverManager.getConnection(conection);//ouverture ducanal de conexion
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE PLANTES " +
                           "(NOM STRING PRIMARY KEY      NOT NULL," +
                            " QUANTITE_NUT              INT, " + 
                            " NUT           STRING, " + 
                            " IMAGE         STRING,"+ 
                            " TAUX_HUM       INT," +
                            " SEUIL_HUM      INT," +
                            " SAISON         INT," +
                            " RAIE           INT);"; 
            stmt.executeUpdate(sql);//envoi de la comande stcker dans sql comme comande 
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() +" 1");
            stmt.close();
            c.commit();
            c.close();
           //System.exit(0);
        }
        System.out.println("Table created successfully");
    }
    protected void insérer_table(Plante p) throws SQLException{
            Connection c = null;
            Statement stmt = null;

            try {
               Class.forName(name);
               c = DriverManager.getConnection(conection);
               c.setAutoCommit(false);
               System.out.println("Opened database successfully");

               stmt = c.createStatement();
               String sql = "INSERT INTO PLANTES (NOM, QUANTITE_NUT, NUT, IMAGE, TAUX_HUM, SEUIL_HUM, SAISON, RAIE) ";
                sql+="VALUES (";
                
                sql+="\""+p.getNom_plante()+"\""+",";
                sql+=p.getQuantite_de_nutriment()+",";
                sql+="\""+p.getNutriment()+"\""+",";
                sql+="\""+p.getImg_plante()+"\""+",";
                sql+=p.getTaux_humidite()+",";
                sql+=p.getSeuil_temperature()+",";
                sql+=p.getSaison()+",";
                if(p.getRaie()==Raie.ZERO)
                   sql+=0;
               else if(p.getRaie()==Raie.PREMIER)
                   sql+=1;
               else if(p.getRaie()==Raie.DEUXIEME)
                   sql+=2;
                
                sql+=");";
                              
                              
               stmt.executeUpdate(sql);

               stmt.close();
               c.commit();
               c.close();
            } catch ( Exception e ) {
               System.err.println( e.getClass().getName() + ": " + e.getMessage() +" 11");
               stmt.close();
               c.commit();
               c.close();
               //System.exit(0);
            }
            System.out.println("Records created successfully");
    }
    protected ArrayList<Plante> selectionner_table() throws SQLException{
        String nom,nut,img;
        int q_nut,t_h,s_h,s,r;
        Saison ss = null;
        Raie rr = null;
        
        ArrayList<Plante>pl=new ArrayList<Plante>();
        
        Connection c = null;
        Statement stmt = null;
        
        try {
           Class.forName(name);
           c = DriverManager.getConnection(conection);
           c.setAutoCommit(false);
           System.out.println("Opened database successfully");

           stmt = c.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM PLANTES;" );

           while ( rs.next() ) {
               nom=rs.getString("nom");
               nut=rs.getString("nut");
               img=rs.getString("image");
               q_nut=rs.getInt("quantite_nut");
               t_h=rs.getInt("taux_hum");
               s_h=rs.getInt("seuil_hum");
               s=rs.getInt("saison");
               r=rs.getInt("raie");
               if(s==0)
                   ss=Saison.HIVER;
               else if(s==1)
                   ss=Saison.AUTOMNE;
               else if(s==2)
                   ss=Saison.PRINTEMPS;
               else if(s==3)
                   ss=Saison.ETE;
               if(r==0)
                   rr=Raie.ZERO;
               else if(r==1)
                   rr=Raie.PREMIER;
               else if(r==2)
                   rr=Raie.DEUXIEME;
               
               pl.add(new Plante(nom,q_nut,nut,img,t_h,s_h,ss,rr));
               System.out.println("+1 plante");
           }
           rs.close();
           stmt.close();
           c.close();
        } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() +"111");
               stmt.close();
               c.commit();
               c.close();
           //System.exit(0);
        }       
        System.out.println("Operation done successfully");
        return pl;
    }
    protected void update_table(String colone,int valeur_p_1,String valeur_f_1, String colone_d,int valeur_p_2,String valeur_f_2, Boolean choix_v1,Boolean choix_v2) throws SQLException{
        String sql = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(name);
            c = DriverManager.getConnection(conection);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
           
           
            stmt = c.createStatement();
            if(choix_v1){
                if(choix_v2)
                    sql = "UPDATE PLANTES set "+colone+" "+valeur_p_1+" where "+colone_d+"="+valeur_p_2+";";
                else
                    sql = "UPDATE PLANTES set "+colone+" "+valeur_p_1+" where "+colone_d+"='"+valeur_f_2+"';";
            }
            else{
                if(choix_v2)
                    sql = "UPDATE PLANTES set "+colone+" '"+valeur_f_1+"' where "+colone_d+"="+valeur_p_2+";";
                else
                    sql = "UPDATE PLANTES set "+colone+" '"+valeur_f_1+"' where "+colone_d+"='"+valeur_f_2+"';";
            }
            stmt.executeUpdate(sql);
            c.commit();

           ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
           rs.close();
           stmt.close();
           c.close();
        } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() +"1111");
               stmt.close();
               c.commit();
               c.close();
           //System.exit(0);
        }
         System.out.println("Operation done successfully");
    }
    protected void supprimer_table(String nom_de_la_plante) throws SQLException{
        String nom,nut,img;
        int q_nut,t_h,s_h,s,r;
        ArrayList<Plante>pl=new ArrayList<Plante>();
        Saison ss = null;
        Raie rr = null;
        
        Connection c = null;
        Statement stmt = null;

        try {
           Class.forName(name);
           c = DriverManager.getConnection(conection);
           c.setAutoCommit(false);
           System.out.println("Opened database successfully");
           stmt = c.createStatement();
           String sql = "DELETE from PLANTES where NOM="+nom_de_la_plante+";";
           stmt.executeUpdate(sql);
           c.commit();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM PLANTES;" );           
           while ( rs.next() ) {
               nom=rs.getString("nom");
               nut=rs.getString("nut");
               img=rs.getString("image");
               q_nut=rs.getInt("quantite_nut");
               t_h=rs.getInt("taux_hum");
               s_h=rs.getInt("seuil_hum");
               s=rs.getInt("saison");
               r=rs.getInt("raie");
               if(s==0)
                   ss=Saison.HIVER;
               else if(s==1)
                   ss=Saison.AUTOMNE;
               else if(s==2)
                   ss=Saison.PRINTEMPS;
               else if(s==3)
                   ss=Saison.ETE;
               if(r==0)
                   rr=Raie.ZERO;
               else if(r==1)
                   rr=Raie.PREMIER;
               else if(r==2)
                   rr=Raie.DEUXIEME;
        }
        rs.close();
        stmt.close();
        c.close();
        } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() +"11111");
               stmt.close();
               c.commit();
               c.close();
           //System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
}
