package com.example.moneymanagerapps.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.adapters.AccountsAdapter;
import com.example.moneymanagerapps.adapters.CategoryAdapter;
import com.example.moneymanagerapps.databinding.FragmentAddTransactionBinding;
import com.example.moneymanagerapps.databinding.ListCategoryBinding;
import com.example.moneymanagerapps.models.Account;
import com.example.moneymanagerapps.models.Category;
import com.example.moneymanagerapps.models.Transaction;
import com.example.moneymanagerapps.utils.Constants;
import com.example.moneymanagerapps.utils.Helper;
import com.example.moneymanagerapps.viewmodels.MainViewModels;
import com.example.moneymanagerapps.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.incomeText));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.textColor));

            transaction.setType(Constants.INCOME);
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.expenseText));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.textColor));

            transaction.setType(Constants.EXPENSE);
        });

        binding.transferBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.transferBtn.setBackground(getContext().getDrawable(R.drawable.transfer_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.transferBtn.setTextColor(getContext().getColor(R.color.transferText));

            transaction.setType(Constants.TRANSFER);
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(c-> {
            ListCategoryBinding dialogBinding = ListCategoryBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c->{
            ListCategoryBinding dialogBinding = ListCategoryBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Accounts"));
            accounts.add(new Account(0, "Card"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(c->{
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();

            if(transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount*-1);
            }else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModels.addTransactions(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });

        return binding.getRoot();
    }
}