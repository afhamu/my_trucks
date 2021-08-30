package com.gigaweb.mytrucks.models;

public class Month {
    private String monthName;
    private String monthNumber;
    private String year;
    private String expenses;
    private String income;
    private boolean expanded;

    public Month(String monthName, String monthNumber, String year, String expenses, String income) {
        this.monthName = monthName;
        this.monthNumber = monthNumber;
        this.year = year;
        this.expenses = expenses;
        this.income = income;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean b) {
        this.expanded = b;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public String getYear() {
        return year;
    }
}
