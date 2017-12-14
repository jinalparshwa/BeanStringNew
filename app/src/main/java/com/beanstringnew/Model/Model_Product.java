package com.beanstringnew.Model;

import java.util.ArrayList;

/**
 * Created by admin on 11/25/2016.
 */

public class Model_Product {

    String imgurl;
    String name;
    String Price;
    String img;
    String beans;
    String id;
    String desc;
    String catid;
    String qty;
    String tot_cart;
    String order_id;
    String date;
    String time;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    ArrayList<Model_Product> array_size;
    ArrayList<Model_Product> array_color;


    String sizeid;
    String size;
    String colorid;
    String color;




    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorid() {
        return colorid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTot_cart() {
        return tot_cart;
    }

    public void setTot_cart(String tot_cart) {
        this.tot_cart = tot_cart;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<Model_Product> getArray_size() {
        return array_size;
    }

    public void setArray_size(ArrayList<Model_Product> array_size) {
        this.array_size = array_size;
    }

    public ArrayList<Model_Product> getArray_color() {
        return array_color;
    }

    public void setArray_color(ArrayList<Model_Product> array_color) {
        this.array_color = array_color;
    }
}
