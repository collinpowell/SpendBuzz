package com.collinpowell.spendbuzz.Models;

public class IEModel {
    String name,type;
    int amount;
    String period;
    int amountReceived;

    public IEModel(String name, String type, int amount, String period, int amountReceived) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.period = period;
        this.amountReceived = amountReceived;
    }

    public int getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(int amountReceived) {
        this.amountReceived = amountReceived;
    }

    public IEModel(String name, String type, int amount, String period) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
