package com.gigaweb.mytrucks.models;

public class IncomeCategory {
    private int importVal;
    private int exportVal;
    private int othersVal;

    public IncomeCategory(int importVal, int exportVal, int othersVal) {
        this.importVal = importVal;
        this.exportVal = exportVal;
        this.othersVal = othersVal;
    }

    public int getImportVal() {
        return importVal;
    }

    public int getExportVal() {
        return exportVal;
    }

    public int getOthersVal() {
        return othersVal;
    }
}
