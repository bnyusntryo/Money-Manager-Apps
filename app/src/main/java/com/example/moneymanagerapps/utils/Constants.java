package com.example.moneymanagerapps.utils;

import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";
    public static String TRANSFER = "TRANSFER";

    public static ArrayList<Category> categories;

    public static int DAILY = 0;
    public static int MONTHLY = 0;
    public static int CALENDAR = 0;
    public static int SUMMARY = 0;
    public static int NOTES = 0;

    public static int SELECTED_TAB = 0;
    public static int SELECTED_TAB_STATS = 0;
    public static String  SELECTED_STATS_TYPE = Constants.INCOME;


    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Allowance", R.drawable.ic_allowance, R.color.category1));
        categories.add(new Category("Sallary", R.drawable.ic_sallary, R.color.category2));
        categories.add(new Category("Petty Cash", R.drawable.ic_petty_cash, R.color.category3));
        categories.add(new Category("Bonus", R.drawable.ic_bonus, R.color.category4));
        categories.add(new Category("Other", R.drawable.ic_other, R.color.category5));
    }

    public static Category getCategoryDetails(String categoryName) {
        for (Category cat :
                categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public static int getAccountsColor(String accountName) {
        switch (accountName){
            case "Card":
                return R.color.card_color;
            case "Cash":
                return R.color.cash_color;
            case "Accounts":
                return R.color.accounts_color;
            default:
                return R.color.default_color;
        }
    }

}