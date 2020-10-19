package com.collinpowell.spendbuzz.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.collinpowell.spendbuzz.Databases.DataBaseHelper;
import com.collinpowell.spendbuzz.Dialogs.Dialog;
import com.collinpowell.spendbuzz.Dialogs.UpdateDialog;
import com.collinpowell.spendbuzz.Models.ExpandableListGroupModel;
import com.collinpowell.spendbuzz.Models.ExpandableListItemsModel;
import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements Dialog.DialogListener {

    private Context context;
    private List<ExpandableListGroupModel> expandableListTitle;
    private List<ExpandableListGroupModel> expandableListTitle1;
    private HashMap<String, List<ExpandableListItemsModel>> expandableListDetail;
    private DataBaseHelper myDb;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    FragmentManager fragmentManager;
    /*
    public CustomExpandableListAdapter(Context context, List<ExpandableListGroupModel> expandableListTitle, HashMap<String, List<ExpandableListItemsModel>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    public CustomExpandableListAdapter(Context context, List<ExpandableListGroupModel> expandableListTitle, HashMap<String, List<ExpandableListItemsModel>> expandableListDetail, ExpandableListView expandableListView) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.expandableListView = expandableListView;
    }*/

    public CustomExpandableListAdapter(Context context, List<ExpandableListGroupModel> expandableListTitle, HashMap<String, List<ExpandableListItemsModel>> expandableListDetail, ExpandableListView expandableListView, FragmentManager fragmentManager) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.expandableListView = expandableListView;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition).getTitle())
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListItemsModel expandedListDetail = (ExpandableListItemsModel) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView period = (TextView) convertView.findViewById(R.id.period);
        TextView sr = (TextView) convertView.findViewById(R.id.sr);
        TextView typeTextView = (TextView) convertView.findViewById(R.id.type);
        ProgressBar pBAr = (ProgressBar) convertView.findViewById(R.id.pBar);
        ImageView delete = convertView.findViewById(R.id.delete);
        Button update = convertView.findViewById(R.id.edit_button);
        // Get the Drawable custom_progressbar
        Drawable draw = convertView.getResources().getDrawable(R.drawable.custom_progressbar);
// set the drawable as progress drawable
        pBAr.setProgressDrawable(draw);
        myDb = new DataBaseHelper(context);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to delete this?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    myDb.deleteContact(expandedListDetail.getTitle());
                                    getData();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                } catch (IllegalStateException e) {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                }


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog1(expandedListDetail.getTitle(), expandedListDetail.getType(), Integer.parseInt(expandedListDetail.getAmount()), Integer.parseInt(expandedListDetail.getAmountRS()), expandedListDetail.getPeriod());
                //Toast.makeText(context, "update" + expandedListDetail.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        if (expandedListDetail.getType().equals("Income")) {
            typeTextView.setTextColor(convertView.getResources().getColor(R.color.green));
        } else {
            typeTextView.setTextColor(convertView.getResources().getColor(R.color.blue));
        }
        typeTextView.setText(expandedListDetail.getType());
        amount.setText(String.format("%,d", Long.parseLong(String.valueOf(expandedListDetail.getAmount()))) + " NGN");
        name.setText(expandedListDetail.getTitle());
        period.setText(expandedListDetail.getPeriod());

        if (expandedListDetail.getAmountRS() == null) {
            if (expandedListDetail.getType().equals("Income")) {
                sr.setText("0 / 100 % Received");
            } else {
                sr.setText("0 / 100 % Spent");
            }
            pBAr.setProgress(0);
        } else {
            float amountI = Integer.parseInt(expandedListDetail.getAmount());
            float amountRSI = Integer.parseInt(expandedListDetail.getAmountRS());
            float per = (amountRSI / amountI) * 100;

            pBAr.setProgress((int) per);
            if (expandedListDetail.getType().equals("Income")) {
                sr.setText(String.format("%.2f", per) + " / 100 % Received");
            } else {
                sr.setText(String.format("%.2f", per) + " / 100 % Spent");
            }
            if (per > 100) {
                sr.setTextColor(convertView.getResources().getColor(R.color.red));
                //pBAr.setBackgroundColor(convertView.getResources().getColor(R.color.red));
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition).getTitle())
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
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

        expandableListTitle = new ArrayList<ExpandableListGroupModel>();
        expandableListTitle.add(groupModel);
        expandableListTitle.add(groupModel1);
        expandableListAdapter = new CustomExpandableListAdapter(context, expandableListTitle, expandableListDetail, expandableListView, fragmentManager);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }


        });

    }


    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final ExpandableListGroupModel listTitle = (ExpandableListGroupModel) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        final TextView total = convertView.findViewById(R.id.total);
        TextView percentage = convertView.findViewById(R.id.percentage);
        TextView addIncomeTextView = convertView.findViewById(R.id.add_income);
        ProgressBar pBar = convertView.findViewById(R.id.pBar);
        // Get the Drawable custom_progressbar
        Drawable draw = convertView.getResources().getDrawable(R.drawable.custom_progressbar);
// set the drawable as progress drawable
        pBar.setProgressDrawable(draw);
        RelativeLayout addIncome = convertView.findViewById(R.id.add_income_layout);
        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listTitle.getTitle().equals("All Income")) {
                    openDialog("Income", null, 0);
                } else {
                    openDialog("Expenses", null, 0);
                }
            }
        });
        listTitleTextView.setTypeface(null, Typeface.BOLD);

        //Toast.makeText(context, String.format("%.1f", Float.parseFloat(listTitle.getPercentage())), Toast.LENGTH_SHORT).show();
        if (listTitle.getTitle().equals("All Income")) {
            addIncomeTextView.setText("Add Income");
            total.setText("Total Income (Month): \n <-- " + String.format("%,d", (int) Float.parseFloat(listTitle.getTotal())) + "NGN -->");
            percentage.setText("Percentage Received: " + String.format("%.2f", Float.parseFloat(listTitle.getPercentage())) + "%");
        } else {
            addIncomeTextView.setText("Add Expenses");
            addIncomeTextView.setTextColor(convertView.getResources().getColor(R.color.blue));
            total.setText("Total Budgeted Expenditure (Month): \n <-- " + String.format("%,d", (int) Float.parseFloat(listTitle.getTotal())) + "NGN -->");
            percentage.setText("Percentage Spent: " + String.format("%.2f", Float.parseFloat(listTitle.getPercentage())) + "%");
        }
        listTitleTextView.setText(listTitle.getTitle());
        pBar.setProgress((int) Float.parseFloat(listTitle.getPercentage()));
        if (Float.parseFloat(listTitle.getPercentage()) > 100) {
            percentage.setTextColor(convertView.getResources().getColor(R.color.red));
            //pBar.setBackgroundColor(convertView.getResources().getColor(R.color.red));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void openDialog(String type, String title, int position) {
        Dialog dialog = new Dialog(type, title, position);
        dialog.show(fragmentManager, "Input Dialog");

    }

    public void openDialog1(String title, String type, int amount, int amountReceived, String period) {
        UpdateDialog dialog = new UpdateDialog(title, type, amount, amountReceived, period);
        dialog.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void applyText(String name, String amount, String type, int position, String period) {
        myDb.insertContact(name, name, type, amount, period, "" + 0);
        getData();
    }

}
