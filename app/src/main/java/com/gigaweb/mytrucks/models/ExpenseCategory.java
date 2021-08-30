package com.gigaweb.mytrucks.models;

public class ExpenseCategory {
    private int dieselValue;
    private int maintenanceValue;
    private int tripAllowanceValue;
    private int othersValue;

    public ExpenseCategory(int dieselValue, int maintenanceValue, int tripAllowanceValue, int othersValue) {
        this.dieselValue = dieselValue;
        this.maintenanceValue = maintenanceValue;
        this.tripAllowanceValue = tripAllowanceValue;
        this.othersValue = othersValue;
    }

    public int getDieselValue() {
        return dieselValue;
    }

    public int getMaintenanceValue() {
        return maintenanceValue;
    }

    public int getTripAllowanceValue() {
        return tripAllowanceValue;
    }

    public int getOthersValue() {
        return othersValue;
    }
}
