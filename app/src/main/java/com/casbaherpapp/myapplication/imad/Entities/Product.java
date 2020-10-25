package com.casbaherpapp.myapplication.imad.Entities;

public class Product {

    private int id;
   private String nom;
   private String famille;
   private int fardeau;
    private  int palette;
    private  double prix_usine;
    private double prixVente;
    boolean isSelected;
    private  int numberDesFardeaux;
    private int numberDesPalettes;
    private int numberBouteille;
    private int quantiteUsine;
    public  Product(){}
    public Product(int id, String nom, String famille, int fardeau, int palette, double prix_usine,int quantiteUsine) {
        this.id=id;
        this.nom = nom;
        this.famille = famille;
        this.fardeau = fardeau;
        this.palette = palette;
        this.prix_usine = prix_usine;
        this.isSelected = false;
        this.prixVente = 0;
        this.quantiteUsine =quantiteUsine;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public int getFardeau() {
        return fardeau;
    }

    public void setFardeau(int fardeau) {
        this.fardeau = fardeau;
    }

    public int getPalette() {
        return palette;
    }

    public void setPalette(int palette) {
        this.palette = palette;
    }

    public double getPrix_usine() {
        return prix_usine;
    }

    public void setPrix_usine(double prix_usine) {
        this.prix_usine = prix_usine;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public int getNumberDesFardeaux() {
        return numberDesFardeaux;
    }

    public void setNumberDesFardeaux(int numberDesFardeaux) {
        this.numberDesFardeaux = numberDesFardeaux;
    }

    public int getNumberDesPalettes() {
        return numberDesPalettes;
    }

    public void setNumberDesPalettes(int numberDesPalettes) {
        this.numberDesPalettes = numberDesPalettes;
    }

    public int getNumberBouteille() {
        return numberBouteille;
    }

    public void setNumberBouteille(int numberBouteille) {
        this.numberBouteille = numberBouteille;
    }

    public int getQuantiteUsine() {
        return quantiteUsine;
    }

    public void setQuantiteUsine(int quantiteUsine) {
        this.quantiteUsine = quantiteUsine;
    }
}