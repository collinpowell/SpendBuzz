package com.collinpowell.spendbuzz.Actitvities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.collinpowell.spendbuzz.Dialogs.AddItemDialog;
import com.collinpowell.spendbuzz.Models.ListItemModel;
import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;

public class MarketList extends AppCompatActivity implements AddItemDialog.DialogListener{
    TableLayout table;
    ArrayList<TableRow> rows;
    Button editTable,addItem;
    TextView totalTextView;
    RelativeLayout tpl;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_list);

        table = findViewById(R.id.table);
        totalTextView = findViewById(R.id.total);
        editTable = findViewById(R.id.edit_table);
        addItem = findViewById(R.id.add_item);
        tpl = findViewById(R.id.total_price_layout);
        tpl.setVisibility(View.GONE);
        rows = new ArrayList<>();

        editTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               table.removeView(rows.get(2));
                rows.remove(2);
                totalPrice = 0;
                for(int i = 0;i<rows.size();i++){
                    TableRow row = rows.get(i);
                    int quantity= Integer.parseInt(((TextView) row.findViewById(R.id.product_count)).getText().toString());
                    int price= Integer.parseInt(((TextView) row.findViewById(R.id.price)).getText().toString());
                    int tp = quantity*price;
                    totalPrice +=tp;
                    totalTextView.setText(""+String.format("%,d",totalPrice)+"NGN");
                    ((TextView) row.findViewById(R.id.product_id)).setText(""+(i+1));

                }

            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpl.setVisibility(View.VISIBLE);
                AddItemDialog itemDialog = new AddItemDialog(new ListItemModel(null,0,0,0));
                itemDialog.show(getSupportFragmentManager(), "Add Item Dialog");
            }
        });

    }

    @Override
    public void applyText(ListItemModel model) {
        totalPrice+=(model.getQuantity()*model.getPrice());
        totalTextView.setText(""+String.format("%,d",totalPrice)+"NGN");
        TableRow row = (TableRow) LayoutInflater.from(MarketList.this).inflate(R.layout.row_attribute, null);
        rows.add(row);
        ((TextView) row.findViewById(R.id.product_name)).setText(model.getName());
        ((TextView) row.findViewById(R.id.product_id)).setText(""+rows.size());
        ((TextView) row.findViewById(R.id.product_count)).setText(""+model.getQuantity());
        ((TextView) row.findViewById(R.id.price)).setText(""+model.getPrice());

        table.addView(row);
        tpl.setVisibility(View.VISIBLE);
    }
}