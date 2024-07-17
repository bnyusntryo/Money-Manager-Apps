package com.example.moneymanagerapps.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moneymanagerapps.adapters.TransactionsAdapter;
import com.example.moneymanagerapps.databinding.FragmentTransactionBinding;
import com.example.moneymanagerapps.models.Transaction;
import com.example.moneymanagerapps.utils.Constants;
import com.example.moneymanagerapps.utils.Helper;
import com.example.moneymanagerapps.viewmodels.MainViewModels;
import com.example.moneymanagerapps.views.fragments.AddTransactionFragment;
import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.databinding.ActivityMainBinding;
import com.example.moneymanagerapps.views.fragments.StatsFragment;
import com.example.moneymanagerapps.views.fragments.TransactionFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        viewModels = new ViewModelProvider(this).get(MainViewModels.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Money Manager");


        Constants.setCategories();
        calendar = Calendar.getInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions) {
                    getSupportFragmentManager().popBackStack();
                } else if (item.getItemId() == R.id.stats) {
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return false;
            }
        });
    }

    public void getTransactions() {
        viewModels.getTransactions(calendar);
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}