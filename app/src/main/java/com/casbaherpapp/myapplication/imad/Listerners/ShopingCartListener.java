package com.casbaherpapp.myapplication.imad.Listerners;

import com.casbaherpapp.myapplication.imad.Entities.Product;

public interface ShopingCartListener {
    public void addProductToCart(Product product);
    public void removeProductFromCart(int id,int position);
    public void removeAllProductFromCart();
    public void editerProduct(int nbreFardeaux,int nbrePalettes,int id);
}
