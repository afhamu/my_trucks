package com.gigaweb.mytrucks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gigaweb.mytrucks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;


public class CategoryFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.category_fragment_container_view, CategoryIncomeFragment.class, null)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigationView = view.findViewById(R.id.category_top_bottom_nav);
        adjustGravity(bottomNavigationView);
        adjustWidth(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.income:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.category_fragment_container_view, CategoryIncomeFragment.class, null)
                                .commit();
                        return true;
                    case R.id.expense:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.category_fragment_container_view, CategoryExpenseFragment.class, null)
                                .commit();
                        return true;

                }
                return false;
            }
        });
    }

    private static void adjustGravity(View v) {
        if (v.getId() == com.google.android.material.R.id.navigation_bar_item_small_label_view) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.setPadding(0, 0, 0, 0);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            parent.setLayoutParams(params);
        }

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }

    private static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        }
        catch (NoSuchFieldException e) {
            // TODO
        }
        catch (IllegalAccessException e) {
            // TODO
        }
    }
}