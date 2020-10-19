package com.collinpowell.spendbuzz.Actitvities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.collinpowell.spendbuzz.Adapters.CustomExpandableListAdapter;
import com.collinpowell.spendbuzz.Databases.DataBaseHelper;
import com.collinpowell.spendbuzz.Dialogs.Dialog;
import com.collinpowell.spendbuzz.Dialogs.UpdateDialog;
import com.collinpowell.spendbuzz.Fragments.ChartFragments;
import com.collinpowell.spendbuzz.Fragments.IEFragment;
import com.collinpowell.spendbuzz.Models.ExpandableListGroupModel;
import com.collinpowell.spendbuzz.Models.ExpandableListItemsModel;
import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Budget extends AppCompatActivity implements Dialog.DialogListener, UpdateDialog.DialogListener {

    // Create the object of TextView
    // and PieChart class

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<ExpandableListGroupModel> expandableListTitle;

    private static final int NUM_PAGES = 2;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    BottomNavigationView bottomNavigationView;

    private DataBaseHelper myDb;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    View header;

    static final float END_SCALE = 0.7f;
    LinearLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);


        View mView = getWindow().getDecorView();

        //mView.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;

        mView.setSystemUiVisibility(uiOptions);

        viewPager = findViewById(R.id.pager);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.contest:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.more:
                        openOptionMenu();
                        return true;
                }
                return false;
            }
        });

        //viewPager = findViewById(R.id.viewpager_container);
        contentView = findViewById(R.id.content);
        //bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        header = navigationView.inflateHeaderView(R.layout.header_home_nav);
        navigationView.inflateMenu(R.menu.home_menu);

        navigationDrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_about_us:
                        startActivity(new Intent(getApplicationContext(), CreateNewBudgetForm.class));
                        return true;
                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), ListOfLists.class));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void openOptionMenu() {
        PopupMenu popup = new PopupMenu( getApplicationContext(),findViewById(R.id.bottom_nav_view));
        popup.getMenuInflater().inflate(R.menu.home_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void applyText(String name, String amount, String type, int position, String period) {
        myDb = new DataBaseHelper(getApplicationContext());
        myDb.insertContact(name, name, type, amount, period, "" + 0);
        getData();
    }

    private void navigationDrawer() {
        // Navigation Drawer
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
        findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.background_tint));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

            }
        });
    }

    public void getData() {
        expandableListView = findViewById(R.id.expandableListView);
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
        expandableListAdapter = new CustomExpandableListAdapter(getApplicationContext(), expandableListTitle, expandableListDetail, expandableListView, getSupportFragmentManager());

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

    @Override
    public void applyText(String name, String amount, String type, String amountReceived, String period) {
        myDb = new DataBaseHelper(getApplicationContext());
        myDb.updateContact(name, name, type, amount, period, amountReceived);
        getData();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    IEFragment tab1 = new IEFragment();
                    return tab1;
                case 1:
                    ChartFragments tab2 = new ChartFragments();
                    return tab2;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

    /*private void setData() {
        // Set the percentage of language used
        tvR.setText(Integer.toString(40));
        tvPython.setText(Integer.toString(30));
        tvCPP.setText(Integer.toString(5));
        tvJava.setText(Integer.toString(25));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt(tvR.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Integer.parseInt(tvPython.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        Integer.parseInt(tvCPP.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        Integer.parseInt(tvJava.getText().toString()),view.getResources().getColor(R.color.red)));

        // To animate the pie chart
        pieChart.startAnimation();
    }*/

}