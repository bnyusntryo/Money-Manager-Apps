package com.example.moneymanagerapps.views.fragments;

import static com.example.moneymanagerapps.utils.Constants.SELECTED_STATS_TYPE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.charts.Scatter;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.databinding.FragmentStatsBinding;
import com.example.moneymanagerapps.models.Transaction;
import com.example.moneymanagerapps.utils.Constants;
import com.example.moneymanagerapps.utils.Helper;
import com.example.moneymanagerapps.viewmodels.MainViewModels;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentStatsBinding binding;
    Calendar calendar;
    /*
    0 = Daily
    1 = Monthly
    */

    public MainViewModels viewModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);

        viewModels = new ViewModelProvider(requireActivity()).get(MainViewModels.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.incomeText));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.textColor));

            SELECTED_STATS_TYPE = Constants.INCOME;
            updateDate();
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.expenseText));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.textColor));

            SELECTED_STATS_TYPE = Constants.EXPENSE;
            updateDate();
        });

        binding.transferBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.transfer_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.transferText));

            SELECTED_STATS_TYPE = Constants.TRANSFER;
            updateDate();
        });

        binding.nextDateBtn.setOnClickListener(c->{
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.prevDateBtn.setOnClickListener(c-> {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB_STATS = 1;
                    updateDate();
                }else if (tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB_STATS = 0;
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
        Pie pie = AnyChart.pie();

        viewModels.categoriesTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {


                if(transactions.size() > 0) {

                    binding.emptyState.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);

                    List<DataEntry> data = new ArrayList<>();

                    Map<String, Double> categoryMap = new HashMap<>();
                    for (Transaction transaction : transactions) {
                        String category = transaction.getCategory();
                        double amount = transaction.getAmount();

                        if (categoryMap.containsKey(category)) {
                            double currentTotal = categoryMap.get(category).doubleValue();
                            currentTotal += Math.abs(amount);

                            categoryMap.put(category, currentTotal);
                        }else {
                            categoryMap.put(category, Math.abs(amount));
                        }
                    }

                    for (Map.Entry<String, Double> entry : categoryMap.entrySet()){
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                    }
                    pie.data(data);
                }else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }
            }
        });

        viewModels.getTransactions(calendar, SELECTED_STATS_TYPE);

//        pie.title("Fruits imported in 2015 (in kg)");
//
//        pie.labels().position("outside");

//        pie.legend().title().enabled(true);
//        pie.legend().title()
//                .text("Retail channels")
//                .padding(0d, 0d, 10d, 0d);

//        pie.legend()
//                .position("center-bottom")
//                .itemsLayout(LegendLayout.HORIZONTAL)
//                .align(Align.CENTER);

        binding.anyChart.setChart(pie);

        return binding.getRoot();
    }

    void updateDate() {
        if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            binding.date.setText(Helper.formatDate(calendar.getTime()));
        }else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
            binding.date.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModels.getTransactions(calendar, SELECTED_STATS_TYPE);
    }
}