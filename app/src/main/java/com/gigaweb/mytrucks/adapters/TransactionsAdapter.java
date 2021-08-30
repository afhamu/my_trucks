package com.gigaweb.mytrucks.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.fragments.CategoryFragment;
import com.gigaweb.mytrucks.fragments.MonthlyFragment;

import org.jetbrains.annotations.NotNull;

public class TransactionsAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public TransactionsAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MonthlyFragment tab1 = new MonthlyFragment();
                return tab1;
            case 1:
                CategoryFragment tab2 = new CategoryFragment();
                return tab2;

        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
