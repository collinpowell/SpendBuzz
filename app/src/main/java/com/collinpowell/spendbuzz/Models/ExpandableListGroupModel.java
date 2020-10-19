package com.collinpowell.spendbuzz.Models;

public class ExpandableListGroupModel {
    String title,total,percentage;

    public ExpandableListGroupModel(String title, String total, String percentage) {
        this.title = title;
        this.total = total;
        this.percentage = percentage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
