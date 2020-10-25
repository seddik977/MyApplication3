package com.casbaherpapp.myapplication.imad.Entities;

public class OrderItem {
    private int idOrderItem;
    private double prixVente;
    private int nombreFardeaux;
    private int nombrePalettes;
    private int idOrder;
    public OrderItem(){}

    public OrderItem(int idOrderItem, double prixVente, int nombreFardeaux, int nombrePalettes, int idOrder) {
        this.idOrderItem = idOrderItem;
        this.prixVente = prixVente;
        this.nombreFardeaux = nombreFardeaux;
        this.nombrePalettes = nombrePalettes;
        this.idOrder = idOrder;
    }

    public int getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(int idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public int getNombreFardeaux() {
        return nombreFardeaux;
    }

    public void setNombreFardeaux(int nombreFardeaux) {
        this.nombreFardeaux = nombreFardeaux;
    }

    public int getNombrePalettes() {
        return nombrePalettes;
    }

    public void setNombrePalettes(int nombrePalettes) {
        this.nombrePalettes = nombrePalettes;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "idOrderItem=" + idOrderItem +
                ", prixVente=" + prixVente +
                ", nombreFardeaux=" + nombreFardeaux +
                ", nombrePalettes=" + nombrePalettes +
                ", idOrder=" + idOrder +
                '}';
    }
}
