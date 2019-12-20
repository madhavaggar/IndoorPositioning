package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferItem {

    @Expose
    @SerializedName("rpname")
    String rpname;
    @Expose
    @SerializedName("offers")
    ArrayList offers;

    public OfferItem(){}
    public OfferItem(String rpname,ArrayList offers){
        this.rpname=rpname;
        this.offers=offers;
    }
    public void setOffers(ArrayList offers) {
        this.offers = offers;
    }

    public void setRpname(String rpname) {
        this.rpname = rpname;
    }

    public ArrayList getOffers() {
        return offers;
    }

    public String getRpname() {
        return rpname;
    }
}
