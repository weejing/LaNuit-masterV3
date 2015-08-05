package com.example.asus.lanuit;

import java.util.ArrayList;

public class Supper {

    private String name;
    private String address;
    private String postal;
    private String latitude;
    private String longitude;
    private String openHours;
    private String price;
    private String foodType;

    public static int arrayIndicator;


    public static ArrayList<Supper> listOfSupper = new ArrayList<Supper> ();
    public static ArrayList<String>supperName = new ArrayList<String>();


    public Supper(String name, String address, String postal, String latitude,
                  String longitude, String openHours, String price, String foodType) {
        super();
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openHours = openHours;
        this.foodType= foodType;
        this.price= price;
    }

    //shihui start
    private int supID;
    private String cuisine;

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public String getcuisine() {
        return cuisine;
    }

    public void setcuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Supper(int supID, String name, String cuisine, String foodType, String price,String latitude, String longitude) {
        this.supID = supID;
        this.name = name;
        this.cuisine = cuisine;
        this.foodType = foodType;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    //shihui end


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPostal() {
        return postal;
    }
    public void setPostal(String postal) {
        this.postal = postal;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getOpenHours() {
        return openHours;
    }
    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getFoodType() {
        return foodType;
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public static void clearSupperList()
    {
        if(listOfSupper.size() >0)
            listOfSupper.clear();

        if(supperName.size()>0)
            supperName.clear();
    }
}