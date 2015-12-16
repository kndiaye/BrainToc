package fr.phoenix_entreprise.braintocapp;

import java.lang.String; /**
 * Created by valentin on 14/12/2015.
 */
public class Account {
    private String nom;
    private String prenom;
    private String photo;


    public Account(){
        nom = "Nom";
        prenom = "Prenom";
        photo = "bonome2";
    }

    public Account(String no,String pr){
        nom = no;
        prenom = pr;
    }

    public Account(String no,String pr,String ph){
        nom = no;
        prenom = pr;
        photo = ph;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
