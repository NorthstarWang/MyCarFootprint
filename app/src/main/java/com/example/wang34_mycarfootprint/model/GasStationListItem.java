package com.example.wang34_mycarfootprint.model;

import java.io.Serializable;
import java.util.Date;

public class GasStationListItem  implements Serializable {

    public enum FuelType{Gasoline,Diesel}

    private String gasStationName;
    private Date gasStationVisitDate;
    private FuelType fuelType;
    private int litreAmount;
    private double pricePerLitre;
    private static final double GASOLINE_COEFFICIENT = 2.32;
    private static final double DIESEL_COEFFICIENT = 2.69;
    private static final String[] items = {"Gasoline","Diesel"};

    public GasStationListItem(String gasStationName,Date gasStationVisitDate,FuelType fuelType,int litreAmount,double pricePerLitre) {
        this.gasStationName = gasStationName;
        this.gasStationVisitDate = gasStationVisitDate;
        this.fuelType = fuelType;
        this.litreAmount = litreAmount;
        this.pricePerLitre = pricePerLitre;
    }

    public double getCalculatedPrice() {
        return this.pricePerLitre*this.litreAmount;
    }

    public double getFootprint(){
        if (this.fuelType==FuelType.Gasoline){
            return GASOLINE_COEFFICIENT*this.litreAmount;
        }else{
            return DIESEL_COEFFICIENT*this.litreAmount;
        }
    }

    public Date getGasStationVisitDate() {
        return this.gasStationVisitDate;
    }

    public double getPricePerLitre() {
        return this.pricePerLitre;
    }

    public String getFuelType() {
        return this.fuelType==FuelType.Gasoline?"Gasoline":"Diesel";
    }

    public String getGasStationName() {
        return gasStationName;
    }

    public int getLitreAmount() {
        return litreAmount;
    }

    public static String[] getItems() {
        return items;
    }
}
