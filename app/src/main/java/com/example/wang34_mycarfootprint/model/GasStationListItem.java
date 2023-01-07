package com.example.wang34_mycarfootprint.model;

import java.util.Date;

public class GasStationListItem {

    public enum FuelType{Gasoline,Diesel};

    private String gasStationName;
    private Date gasStationVisitDate;
    private FuelType fuelType;
    private int litreAmount;
    private double pricePerLitre;
    private static double GASOLINE_COEFFICIENT = 2.32;
    private static double DIESEL_COEFFICIENT = 2.69;

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

    public void setGasStationName(String gasStationName) {
        this.gasStationName = gasStationName;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public void setGasStationVisitDate(Date gasStationVisitDate) {
        this.gasStationVisitDate = gasStationVisitDate;
    }

    public void setLitreAmount(int litreAmount) {
        this.litreAmount = litreAmount;
    }

    public void setPricePerLitre(double pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }
}
