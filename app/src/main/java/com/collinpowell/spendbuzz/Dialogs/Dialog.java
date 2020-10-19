package com.collinpowell.spendbuzz.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;
import java.util.List;

public class Dialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextAmount;
    private Spinner spinner;

    private DialogListener listener;

    String title;
    String type;
    int position;
    String amount;

    public Dialog(String type, String title, int position) {
        this.title = title;
        this.type = type;
        this.position = position;
    }

    public Dialog(String title, String type, int position, String amount) {
        this.title = title;
        this.type = type;
        this.position = position;
        this.amount = amount;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.input_dialog, null);

        builder.setView(view)
                .setTitle("Add "+type +" Field")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!validateAmount() | !validatePeriod() | !validateTitle()) {
                            return;
                        } else {
                            String title = editTextName.getText().toString();
                            String amount = editTextAmount.getText().toString();
                            String period = spinner.getSelectedItem().toString();
                            listener.applyText(title, amount, type, position, period);
                        }

                    }
                });

        editTextName = view.findViewById(R.id.edit_title);
        editTextAmount = view.findViewById(R.id.edit_amount);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> period = new ArrayList<String>();
        period.add("Monthly");
        period.add("Daily");
        period.add("Weekly");
        period.add("Yearly");
        period.add("One Time");
        spinner.setEnabled(true);
        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, period);
        //Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (title != null) {
            editTextName.setText(title);
            editTextAmount.setText(amount);
            editTextName.setEnabled(false);
        }


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface DialogListener {
        void applyText(String name, String amount, String type, int position, String period);
    }

    private boolean validateTitle() {
        String val = editTextName.getText().toString();

        if (val.isEmpty()) {
            editTextName.setError("Field cannot be empty");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }

    private boolean validateAmount() {
        String val = editTextAmount.getText().toString();

        if (val.isEmpty()) {
            editTextAmount.setError("Field cannot be empty");
            return false;
        } else {
            editTextAmount.setError(null);
            return true;
        }
    }

    private boolean validatePeriod() {
        String val = spinner.toString();

        if (val.isEmpty() || val.equals("None")) {
            spinner.setPrompt("Select Something");
            return false;
        } else {
            return true;
        }
    }
}
