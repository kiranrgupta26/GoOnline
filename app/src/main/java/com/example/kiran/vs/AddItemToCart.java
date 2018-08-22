package com.example.kiran.vs;

import android.graphics.Bitmap;

/**
 * Created by Kiran on 1/20/2018.
 */

public class AddItemToCart {

    private String itemName="default";
    private int price=1;
    private Bitmap image_path;
    private int discount=0;
    private int quantity=1;
    private String category;
    private String subcat1;
    private String subcat2;
    //private String description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcat1() {
        return subcat1;
    }

    public void setSubcat1(String subcat1) {
        this.subcat1 = subcat1;
    }

    public String getSubcat2() {
        return subcat2;
    }

    public void setSubcat2(String subcat2) {
        this.subcat2 = subcat2;
    }


    //description
    //binded /non binded

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public Bitmap getImage_path() {
        return image_path;
    }

    public int getDiscount() {
        return discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    public void setPrice(int itemprice)
    {
        this.price = itemprice;
    }
    public void setDiscount(int itemdiscount)
    {
        this.discount = itemdiscount;
    }
    public void setQuantity(int itemquantity)
    {
        this.quantity = itemquantity;
    }
    public void setImage_path(Bitmap itempath)
    {
        this.image_path = itempath;
    }

}
