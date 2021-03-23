package com.openclassrooms.connexion;

public class Telechargement {
    private String mDescrip;
    private String mImageUrl;
    private String mNom;

    public Telechargement() {
    }
    public Telechargement(String description, String nom, String imageUrl) {
        if (description.trim().equals("")) {
            description = " Pas de description";
        }

        if (nom.trim().equals("")) {
            nom= "Anonyme";
        }
        mDescrip = description;
        mImageUrl = imageUrl;
        mNom = nom;
    }
    public String getDescription() {
        return mDescrip;
    }
    public void setDescirption(String description) {
        mDescrip = description;
    }

    public String getNom() {
        return mNom;
    }
    public void setNom(String nom) {
        mNom = nom;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
