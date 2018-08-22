package com.example.kiran.vs;

import java.io.Serializable;

/**
 * Created by Kiran on 1/17/2018.
 */

public class ImageItem implements Serializable{

    private String image;
    private String title;
    private int discount;
    private int price;
    private String category;
    private String subcat1;
    private String subcat2;

    public ImageItem(String image, String title,int price,int discount,String category,String subcat1,String subcat2) {
        super();
        this.image = image;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.category = category;
        this.subcat1 = subcat1;
        this.subcat2 = subcat2;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcat1() {
        return subcat1;
    }

    public String getSubcat2() {
        return subcat2;
    }
    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice(){
        return price;
    }

    public  int getDiscount(){
        return discount;
    }
}
