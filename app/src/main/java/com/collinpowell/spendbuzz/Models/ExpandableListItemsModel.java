package com.collinpowell.spendbuzz.Models;

public class ExpandableListItemsModel {
    String id,title,amount,period,type,amountRS;

    public ExpandableListItemsModel(String id,String title, String amount, String period, String type, String amountRS) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.period = period;
        this.type = type;
        this.amountRS = amountRS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmountRS() {
        return amountRS;
    }

    public void setAmountRS(String amountRS) {
        this.amountRS = amountRS;
    }
}
