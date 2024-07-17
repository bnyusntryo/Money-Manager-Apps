package com.example.moneymanagerapps.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.adapters.TransactionsAdapter;
import com.example.moneymanagerapps.databinding.FragmentTransactionBinding;
import com.example.moneymanagerapps.models.Transaction;
import com.example.moneymanagerapps.utils.Constants;
import com.example.moneymanagerapps.utils.Helper;
import com.example.moneymanagerapps.viewmodels.MainViewModels;
import com.example.moneymanagerapps.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Objects;

import io.realm.RealmResults;


public class TransactionFragment extends Fragment {

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentTransactionBinding binding;

    Calendar calendar;
    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
    */

    public MainViewModels viewModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater);

        viewModels = new ViewModelProvider(requireActivity()).get(MainViewModels.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.prevDateBtn.setOnClickListener(c-> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c-> {
            new AddTransactionFragment().show(getParentFragmentManager(), null);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(Objects.equals(tab.getText(), "Monthly")) {
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }else if (Objects.equals(tab.getText(), "Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModels.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionList.setAdapter(transactionsAdapter);
                if(transactions.size() > 0) {
                    binding.emptyState.setVisibility(View.GONE);
                }else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModels.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModels.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModels.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModels.getTransactions(calendar);

        return binding.getRoot();
    }

    void updateDate() {
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            binding.date.setText(Helper.formatDate(calendar.getTime()));
        }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.date.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModels.getTransactions(calendar);
    }
}