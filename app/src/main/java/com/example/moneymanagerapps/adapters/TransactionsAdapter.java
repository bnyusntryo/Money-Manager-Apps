package com.example.moneymanagerapps.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanagerapps.R;
import com.example.moneymanagerapps.databinding.RowTransactionBinding;
import com.example.moneymanagerapps.models.Category;
import com.example.moneymanagerapps.models.Transaction;
import com.example.moneymanagerapps.utils.Constants;
import com.example.moneymanagerapps.utils.Helper;
import com.example.moneymanagerapps.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context;
    RealmResults<Transaction> transactions;

    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.account.setText(transaction.getAccount());

        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.account.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if (transaction.getType().equals(Constants.INCOME)) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.incomeText));
        }
         else if (transaction.getType().equals(Constants.EXPENSE)) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.expenseText));
        }
        else if (transaction.getType().equals(Constants.TRANSFER)) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.transferText));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete this transaction?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                    ((MainActivity)context).viewModels.deleteTransaction(transaction);
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> {
                    deleteDialog.dismiss();
                });
                deleteDialog.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }

}
