package com.beanstringnew.Model;

/**
 * Created by admin on 11/28/2016.
 */

public class Model_mycart {

    String Cartid;
    String Productid;
    String Catid;
    String name;
    String Price;
    String Beans;
    String Color;
    String Size;
    String Qty;
    String image;
    String Catname;
    String Tot_amount;
    String Tot_beans;

    public String getTot_amount() {
        return Tot_amount;
    }

    public void setTot_amount(String tot_amount) {
        Tot_amount = tot_amount;
    }

    public String getTot_beans() {
        return Tot_beans;
    }

    public void setTot_beans(String tot_beans) {
        Tot_beans = tot_beans;
    }

    public String getCatname() {
        return Catname;
    }

    public void setCatname(String catname) {
        Catname = catname;
    }

    public String getCartid() {
        return Cartid;
    }

    public void setCartid(String cartid) {
        Cartid = cartid;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getCatid() {
        return Catid;
    }

    public void setCatid(String catid) {
        Catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getBeans() {
        return Beans;
    }

    public void setBeans(String beans) {
        Beans = beans;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
