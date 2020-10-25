package com.casbaherpapp.myapplication.imad.Entities;

public class VersementItem {
    private int id;
    private String comptableFirstName;
    private String comptableLastName;
    private String action;
    private String createdDate;
    private String updatedDate;
    private  double ancientCredit;
    private double noveauCredit;
    private double dernierVersement;
    private double versementTotal;
    public VersementItem(){}

    public VersementItem(int id, String comptableFirstName, String comptableLastName, String action, String createdDate, String updatedDate, double ancientCredit, double noveauCredit, double dernierVersement, double versementTotal) {
        this.id = id;
        this.comptableFirstName = comptableFirstName;
        this.comptableLastName = comptableLastName;
        this.action = action;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.ancientCredit = ancientCredit;
        this.noveauCredit = noveauCredit;
        this.dernierVersement = dernierVersement;
        this.versementTotal = versementTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComptableFirstName() {
        return comptableFirstName;
    }

    public void setComptableFirstName(String comptableFirstName) {
        this.comptableFirstName = comptableFirstName;
    }

    public String getComptableLastName() {
        return comptableLastName;
    }

    public void setComptableLastName(String comptableLastName) {
        this.comptableLastName = comptableLastName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public double getAncientCredit() {
        return ancientCredit;
    }

    public void setAncientCredit(double ancientCredit) {
        this.ancientCredit = ancientCredit;
    }

    public double getNoveauCredit() {
        return noveauCredit;
    }

    public void setNoveauCredit(double noveauCredit) {
        this.noveauCredit = noveauCredit;
    }

    public double getDernierVersement() {
        return dernierVersement;
    }

    public void setDernierVersement(double dernierVersement) {
        this.dernierVersement = dernierVersement;
    }

    public double getVersementTotal() {
        return versementTotal;
    }

    public void setVersementTotal(double versementTotal) {
        this.versementTotal = versementTotal;
    }
}
