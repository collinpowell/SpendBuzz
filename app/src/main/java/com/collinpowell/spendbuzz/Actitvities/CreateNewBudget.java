package com.collinpowell.spendbuzz.Actitvities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collinpowell.spendbuzz.Databases.DataBaseHelper;
import com.collinpowell.spendbuzz.Dialogs.Dialog;
import com.collinpowell.spendbuzz.Adapters.IERecyclerViewAdapter;
import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;

public class CreateNewBudget extends AppCompatActivity implements Dialog.DialogListener {

    IERecyclerViewAdapter adapter;
    IERecyclerViewAdapter adapter1;
    RecyclerView income, expenses;

    ArrayList<IEModel> incomeList;
    ArrayList<IEModel> expensesList;

    RelativeLayout addIncome, addExpenses;

    Button CreateBudget;
    private DataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_budget);

        income = findViewById(R.id.income);
        expenses = findViewById(R.id.expenses);
        addExpenses = findViewById(R.id.add_expenses_layout);
        addIncome = findViewById(R.id.add_income_layout);
        CreateBudget = findViewById(R.id.create_new_budget);
        incomeList = new ArrayList<>();
        expensesList = new ArrayList<>();
        myDb = new DataBaseHelper(this);

        income.setHasFixedSize(true);
        income.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        expenses.setHasFixedSize(true);
        expenses.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        incomeList.add(new IEModel("Salary/Wadges", "Income", 0, "Monthly"));
        incomeList.add(new IEModel("Other Incomes", "Income", 0, "Daily"));

        expensesList.add(new IEModel("Home/Rent", "Expenses", 0, "Monthly"));
        expensesList.add(new IEModel("Dues/subscriptions", "Expenses", 0, "Monthly"));
        expensesList.add(new IEModel("Health Care", "Expenses", 0, "Monthly"));
        expensesList.add(new IEModel("Financial Savings", "Expenses", 0, "Monthly"));
        expensesList.add(new IEModel("Entertainment", "Expenses", 0, "Daily"));
        expensesList.add(new IEModel("Daily Living", "Expenses", 0, "Daily"));

        adapter = new IERecyclerViewAdapter(incomeList);
        income.setAdapter(adapter);

        adapter1 = new IERecyclerViewAdapter(expensesList);
        expenses.setAdapter(adapter1);

        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Expenses", null, expensesList.size() - 1,null);
            }
        });

        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Income", null, incomeList.size() - 1,null);
            }
        });

        adapter.setOnItemClickListener(new IERecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                openDialog(incomeList.get(position).getType(), incomeList.get(position).getName(), position,String.valueOf(incomeList.get(position).getAmount()));
            }

            @Override
            public void onDeleteClick(final int position) {
                new AlertDialog.Builder(CreateNewBudget.this)
                        .setMessage("Are you sure you want to delete this?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                incomeList.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        adapter1.setOnItemClickListener(new IERecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                openDialog(expensesList.get(position).getType(), expensesList.get(position).getName(), position,String.valueOf(expensesList.get(position).getAmount()));
            }

            @Override
            public void onDeleteClick(final int position) {
                new AlertDialog.Builder(CreateNewBudget.this)
                        .setMessage("Are you sure you want to delete this?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expensesList.remove(position);
                                adapter1.notifyItemRemoved(position);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        CreateBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // record transaction
                //save pref
                savePrefsData();
                for (int i = 0; i < incomeList.size(); i++) {
                    IEModel ieModel = incomeList.get(i);
                    if (ieModel.getAmount() != 0) {
                        myDb.insertContact(ieModel.getName(),ieModel.getName(), ieModel.getType(), String.valueOf(ieModel.getAmount()), ieModel.getPeriod(), ""+0);
                    }

                }
                for (int i = 0; i < expensesList.size(); i++) {
                    IEModel ieModel = expensesList.get(i);
                    if (ieModel.getAmount() != 0) {
                        myDb.insertContact(ieModel.getName(),ieModel.getName(), ieModel.getType(), String.valueOf(ieModel.getAmount()), ieModel.getPeriod(), ""+0);

                    }
                }
                Toast.makeText(getApplicationContext(), "" + myDb.numberOfRows(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Budget.class));
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isBudgetCreated",true);
        editor.commit();
    }


    public void openDialog(String type, String title, int position,String amount) {
        Dialog dialog = new Dialog(title, type, position,amount);
        dialog.show(getSupportFragmentManager(), "Input Dialog");
    }


    @Override
    public void applyText(String name, String amount, String type, int position, String period) {
        IEModel ieModel = new IEModel(name, type, Integer.parseInt(amount), period);
        if (type.equals("Income")) {
            if (name.equals(incomeList.get(position).getName())) {
                incomeList.remove(position);
                incomeList.add(position, ieModel);
            } else {
                incomeList.add(ieModel);
            }
            adapter = new IERecyclerViewAdapter(incomeList);
            income.setAdapter(adapter);
            adapter.setOnItemClickListener(new IERecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onEditClick(int position) {
                    openDialog(incomeList.get(position).getType(), incomeList.get(position).getName(), position,String.valueOf(incomeList.get(position).getAmount()));
                }

                @Override
                public void onDeleteClick(final int position) {
                    new AlertDialog.Builder(CreateNewBudget.this)
                            .setMessage("Are you sure you want to delete this?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    incomeList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });
        } else {

            if (name.equals(expensesList.get(position).getName())) {
                expensesList.remove(position);
                expensesList.add(position, ieModel);
            } else {
                expensesList.add(ieModel);
            }
            adapter1 = new IERecyclerViewAdapter(expensesList);
            expenses.setAdapter(adapter1);
            adapter1.setOnItemClickListener(new IERecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onEditClick(int position) {
                    openDialog(expensesList.get(position).getType(), expensesList.get(position).getName(), position,String.valueOf(expensesList.get(position).getAmount()));
                }

                @Override
                public void onDeleteClick(final int position) {
                    new AlertDialog.Builder(CreateNewBudget.this)
                            .setMessage("Are you sure you want to delete this?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    expensesList.remove(position);
                                    adapter1.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });

        }
        Toast.makeText(getApplicationContext(), "Added: " + name + ", " + amount, Toast.LENGTH_SHORT).show();
    }
}