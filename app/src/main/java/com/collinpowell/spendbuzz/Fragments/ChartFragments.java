package com.collinpowell.spendbuzz.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.collinpowell.spendbuzz.Databases.DataBaseHelper;
import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ChartFragments extends Fragment {
    View view;
    TextView income,expenses,remaining;
    PieChart pieChart;
    private DataBaseHelper myDb;
    Button refresh,refresh1;
    TableLayout tableLayout;

    TextView income1,expenses1,remaining1;
    PieChart pieChart1;
    TableLayout tableLayout1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart_fragments, container, false);
        pieChart = view.findViewById(R.id.piechart);
        refresh = view.findViewById(R.id.refresh);
        refresh1 = view.findViewById(R.id.refresh1);
        tableLayout = view.findViewById(R.id.table);
        income = view.findViewById(R.id.income);
        expenses = view.findViewById(R.id.expenses);
        remaining = view.findViewById(R.id.remaining);
        myDb = new DataBaseHelper(getContext());
        tableLayout.removeAllViews();
        pieChart.clearChart();

        pieChart1 = view.findViewById(R.id.piechart1);
        tableLayout1 = view.findViewById(R.id.table1);
        income1 = view.findViewById(R.id.income1);
        expenses1 = view.findViewById(R.id.expenses1);
        remaining1 = view.findViewById(R.id.remaining1);
        tableLayout1.removeAllViews();
        pieChart1.clearChart();
        getData();
        getData1();
        //setData();


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.clearChart();
                tableLayout.removeAllViews();
                getData();

                pieChart1.clearChart();
                tableLayout1.removeAllViews();
                getData1();
            }
        });

        refresh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.clearChart();
                tableLayout.removeAllViews();
                getData();

                pieChart1.clearChart();
                tableLayout1.removeAllViews();
                getData1();
            }
        });
        return view;
    }

    private void addView(String color,String name){
        TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.chart_row_attribute, null);

        row.findViewById(R.id.color).setBackgroundColor(Color.parseColor(color));
        ((TextView) row.findViewById(R.id.name)).setText(name);

        tableLayout.addView(row);
    }

    private void addView1(String color,String name){
        TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.chart_row_attribute, null);

        row.findViewById(R.id.color).setBackgroundColor(Color.parseColor(color));
        ((TextView) row.findViewById(R.id.name)).setText(name);

        tableLayout1.addView(row);
    }
    public void getData() {
        ArrayList<HashMap> exp = new ArrayList<>();
        float totalIncome = 0;
        float totalExpense = 0;
        float totalIncomeR = 0;
        float totalExpenseS = 0;
        float totalPerI = 0;
        float totalPerE = 0;
      ArrayList<IEModel> arrayList =  myDb.getAllContacts();
        for(int i = 0;i< arrayList.size();i++){

            String type =arrayList.get(i).getType();
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

            } else {
                totalExpense += amountI;
                HashMap<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("amount",String.valueOf(amountI));
                exp.add(map);
                totalExpenseS += amount_receivedI;
                totalPerE = (totalExpenseS / totalExpense) * 100;
            }

        }
        float tp = (float) 0.0;
        for(int i = 0;i<exp.size();i++){
            HashMap map = exp.get(i);

            float percent = (Float.parseFloat(map.get("amount").toString())/totalIncome) * 100;
             tp +=percent;
             int at = (int) Float.parseFloat((String) map.get("amount"));
            Random random = new Random();
            int color = random.nextInt(699);
            while (color < 100){
                color = random.nextInt(699);

            }
            int color1 = random.nextInt(999);
            while (color1 < 700){
                color1 = random.nextInt(999);

            }


            addView("#"+color + color1,(String) map.get("name") + " "+String.format("%.2f",percent)+"%  (" + String.format("%,d", at) + " NGN)");
            // Set the data and color to the pie chart
            pieChart.addPieSlice(
                    new PieModel(
                            (String) map.get("name"),
                            (int)percent,
                            Color.parseColor("#"+color + color1)));

            if(tp < 100 && i == exp.size()-1){
                addView("#00A" + color,(String) "Remaining to spend" + " "+String.format("%.2f",(100 - tp))+"%  (" + String.format("%,d",(int) (totalIncome-totalExpense)) + " NGN)");
                remaining.setTextColor(Color.parseColor("#0AA" + color));
                pieChart.addPieSlice(
                        new PieModel(
                                "Spending",
                                (int)100 - tp,
                                Color.parseColor("#0AA" + color)));
            }


        }

        if(tp > 100){
            remaining.setTextColor(view.getResources().getColor(R.color.red));
            Toast.makeText(getContext(), "You have Spent More Than your Income\n"+tp+"%", Toast.LENGTH_LONG).show();
        }
        income.setText(String.format("%,d",(int) (totalIncome)) + " NGN");
        expenses.setText(String.format("%,d",(int) (totalExpense)) + " NGN");
        remaining.setText( String.format("%,d",(int) (totalIncome-totalExpense)) + " NGN");


        pieChart.startAnimation();

    }

    public void getData1() {
        ArrayList<HashMap> exp = new ArrayList<>();
        float totalIncome = 0;
        float totalExpense = 0;
        float totalIncomeR = 0;
        float totalExpenseS = 0;
        float totalPerI = 0;
        float totalPerE = 0;
        ArrayList<IEModel> arrayList =  myDb.getAllContacts();
        for(int i = 1;i< arrayList.size();i++){

            String type =arrayList.get(i).getType();
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

            } else {
                totalExpense += amountI;
                HashMap<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("amount",String.valueOf(amount_receivedI));
                exp.add(map);
                totalExpenseS += amount_receivedI;
                totalPerE = (totalExpenseS / totalExpense) * 100;
            }

        }
        float tp = (float) 0.0;
        for(int i = 0;i<exp.size();i++){
            HashMap map = exp.get(i);

            float percent = (Float.parseFloat(map.get("amount").toString())/totalIncomeR) * 100;
            tp +=percent;
            int at = (int) Float.parseFloat((String) map.get("amount"));
            Random random = new Random();
            int color = random.nextInt(699);
            while (color < 100){
                color = random.nextInt(699);

            }
            int color1 = random.nextInt(999);
            while (color1 < 700){
                color1 = random.nextInt(999);

            }


            addView1("#"+color + color1,(String) map.get("name") + " "+String.format("%.2f",percent)+"%  (" + String.format("%,d", at) + " NGN)");
            // Set the data and color to the pie chart
            pieChart1.addPieSlice(
                    new PieModel(
                            (String) map.get("name"),
                            (int)percent,
                            Color.parseColor("#"+color + color1)));

            if(tp < 100 && i == exp.size()-1){
                addView1("#00A" + color1,(String) "Remaining to spend" + " "+String.format("%.2f",(100 - tp))+"%  (" + String.format("%,d",(int) (totalIncomeR-totalExpenseS)) + " NGN)");
                remaining1.setTextColor(Color.parseColor("#0AA" + color1));
                pieChart1.addPieSlice(
                        new PieModel(
                                "Spending",
                                (int)100 - tp,
                                Color.parseColor("#0AA" + color1)));
            }


        }

        if(tp > 100){
            remaining1.setTextColor(view.getResources().getColor(R.color.red));
            Toast.makeText(getContext(), "You have Spent More Than you Have received\n"+tp+"%", Toast.LENGTH_LONG).show();
        }
        income1.setText(String.format("%,d",(int) (totalIncomeR)) + " NGN");
        expenses1.setText(String.format("%,d",(int) (totalExpenseS)) + " NGN");
        remaining1.setText( String.format("%,d",(int) (totalIncomeR-totalExpenseS)) + " NGN");


        pieChart1.startAnimation();

    }

}