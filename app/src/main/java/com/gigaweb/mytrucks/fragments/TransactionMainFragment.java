package com.gigaweb.mytrucks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.adapters.TransactionsAdapter;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;


public class TransactionMainFragment extends Fragment {

    private TransactionsAdapter adapter;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TransactionsAdapter(getChildFragmentManager(), getContext());
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}