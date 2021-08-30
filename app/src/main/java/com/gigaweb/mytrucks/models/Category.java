package com.gigaweb.mytrucks.models;

public class Category {
    private String categoryName;
    private int amount;
    private String percentageVal;

    public Category(String categoryName, int amount, String percentageVal) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.percentageVal = percentageVal;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getAmount() {
        return amount;
    }

    public String getPercentageVal() {
        return percentageVal;
    }
}
