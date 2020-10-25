package com.casbaherpapp.myapplication.imad.Entities;

public class Order {
    private int idOrder;
    private double prixtTotal;
   private boolean isVerified;
   private String createdDate;
 public Order(){

 }

    public Order(int idOrder, double prixtTotal, boolean isVerified,String createdDate) {
        this.idOrder = idOrder;
        this.prixtTotal = prixtTotal;
        this.isVerified = isVerified;
        this.createdDate = createdDate;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public double getPrixtTotal() {
        return prixtTotal;
    }

    public void setPrixtTotal(double prixtTotal) {
        this.prixtTotal = prixtTotal;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
