package com.collinpowell.spendbuzz.Actitvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.collinpowell.spendbuzz.Fragments.DatePickerFragment;
import com.collinpowell.spendbuzz.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateNewBudgetForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    TextInputEditText Dt;
    TextInputEditText Do;
    TextInputEditText At;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_budget_form);

        Dt = findViewById(R.id.dt);
        At = findViewById(R.id.at);
        Do = findViewById(R.id.doo);




        Do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Do.setText(currentDate);
    }
}