package com.gigaweb.mytrucks.models;

import java.util.Date;

public class Recent {
    private String date;
    private String activity;
    private String category;
    private int amount;

    public Recent(String  date, String activity, String category, int amount) {
        this.date = date;
        this.activity = activity;
        this.category = category;
        this.amount = amount;
    }

    public String  getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

}
