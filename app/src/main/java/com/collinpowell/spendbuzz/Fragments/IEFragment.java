package com.collinpowell.spendbuzz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.collinpowell.spendbuzz.Adapters.CustomExpandableListAdapter;
import com.collinpowell.spendbuzz.Databases.DataBaseHelper;
import com.collinpowell.spendbuzz.Models.ExpandableListGroupModel;
import com.collinpowell.spendbuzz.Models.ExpandableListItemsModel;
import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IEFragment extends Fragment {

    public static ExpandableListView expandableListView;
    public static FragmentManager fragmentManager;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<ExpandableListGroupModel> expandableListTitle;

    private DataBaseHelper myDb;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_i_e, container, false);
        // Inflate the layout for this fragment

        expandableListView = view.findViewById(R.id.expandableListView);
        fragmentManager = getFragmentManager();

        myDb = new DataBaseHelper(getContext());
        getData();
        return view;
    }

    @Override
    public void onResume() {
        expandableListView = view.findViewById(R.id.expandableListView);
        fragmentManager = getFragmentManager();

        myDb = new DataBaseHelper(getContext());
        getData();
        super.onResume();

    }

    public void getData() {
        HashMap<String, List<ExpandableListItemsModel>> expandableListDetail = new HashMap<String, List<ExpandableListItemsModel>>();

        expandableListDetail.clear();
        expandableListView.clearTextFilter();
        List<ExpandableListItemsModel> subCategoryIncome = new ArrayList<>();
        List<ExpandableListItemsModel> subCategoryExpenses = new ArrayList<>();
        String text = "";
        ExpandableListGroupModel groupModel = null;
        ExpandableListGroupModel groupModel1 = null;
        // Toast.makeText(getApplicationContext(), "" + myDb.numberOfRows(), Toast.LENGTH_SHORT).show();
        float totalIncome = 0;
        float totalExpense = 0;
        float totalIncomeR = 0;
        float totalExpenseS = 0;
        float totalPerI = 0;
        float totalPerE = 0;
        groupModel = new ExpandableListGroupModel("All Income", String.valueOf(totalIncome), String.valueOf(totalPerI));
        groupModel1 = new ExpandableListGroupModel("All Expenses", String.valueOf(totalExpense), String.valueOf(totalPerE));

        ArrayList<IEModel> arrayList = myDb.getAllContacts();
        for (int i = 0; i < arrayList.size(); i++) {

            String type = arrayList.get(i).getType();
            String amount = String.valueOf(arrayList.get(i).getAmount());
            String name = String.valueOf(arrayList.get(i).getName());
            String period = String.valueOf(arrayList.get(i).getPeriod());
            String amount_received = String.valueOf(arrayList.get(i).getAmountReceived());

            float amountI = Float.parseFloat(amount);
            float amount_receivedI = Float.parseFloat(amount_received);

            if (type.equals("Income")) {
                totalIncome += amountI;
                totalIncomeR += amount_receivedI;
                totalPerI = (totalIncomeR / totalIncome) * 100;
                groupModel = new ExpandableListGroupModel("All Income", String.format("%.2f", totalIncome), String.format("%.2f", totalPerI));
                ExpandableListItemsModel model = new ExpandableListItemsModel(String.valueOf(i), name, amount, period, "Income", amount_received);
                subCategoryIncome.add(model);
            } else {
                totalExpense += amountI;
                totalExpenseS += amount_receivedI;
                totalPerE = (totalExpenseS / totalExpense) * 100;
                groupModel1 = new ExpandableListGroupModel("All Expenses", String.format("%.2f", totalExpense), String.format("%.2f", totalPerE));
                ExpandableListItemsModel model = new ExpandableListItemsModel(String.valueOf(i), name, amount, period, "Expenses", amount_received);
                subCategoryExpenses.add(model);
            }

        }

        expandableListDetail.put("All Income", subCategoryIncome);
        expandableListDetail.put("All Expenses", subCategoryExpenses);

        expandableListTitle = new ArrayList<>();
        expandableListTitle.add(groupModel);
        expandableListTitle.add(groupModel1);
        expandableListAdapter = new CustomExpandableListAdapter(this.getContext(), expandableListTitle, expandableListDetail, expandableListView, this.getFragmentManager());

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                /*Intent intent = null;
                if (as.equals("salesAgent")) {

                    intent = new Intent(getApplicationContext(), EditProducts.class);
                    intent.putExtra("as", "salesAgent");

                } else {
                    intent = new Intent(getApplicationContext(), ViewProducts.class);
                    intent.putExtra("as", "buyer");
                }
                intent.putExtra("Category", expandableListTitle.get(groupPosition));
                intent.putExtra("subCategory", expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));

                startActivity(intent);*/
                return true;
            }


        });

    }

    private float getTotalPrice(float price, String period) {
        float monthly = 0;
        float daily = 0;
        float weekly = 0;
        float yearly = 0;
        float oneTime = 0;
        switch (period) {
            case "Monthly":
                monthly += price;
                break;
            case "Daily":
                daily += price;
                break;
            case "Weekly":
                weekly += price;
                break;
            case "Yearly":
                yearly += price;
                break;
            case "One Time":
                oneTime += price;
                break;
            default:
                break;
        }
        return monthly;
    }

}