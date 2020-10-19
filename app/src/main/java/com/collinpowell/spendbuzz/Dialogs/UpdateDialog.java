package com.collinpowell.spendbuzz.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.collinpowell.spendbuzz.R;

public class UpdateDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextAmount;
    private EditText editTextAmountReceived;
    private TextView text;

    private DialogListener listener;

    String title;
    String type;
    int amount;
    int amountReceived;
    String period;

    public UpdateDialog(String title, String type, int amount, int amountReceived, String period) {
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.amountReceived = amountReceived;
        this.period = period;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_input_dialog, null);

        builder.setView(view)
                .setTitle("Update " + type + " Field")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!validateAmount() | !validateTitle()) {
                            return;
                        } else {
                            String title = editTextName.getText().toString();
                            String amount = editTextAmount.getText().toString();
                            String amountR = editTextAmountReceived.getText().toString();
                            listener.applyText(title, amount, type, amountR, period);
                        }

                    }
                });

        editTextName = view.findViewById(R.id.edit_title);
        text = view.findViewById(R.id.ar);
        editTextAmount = view.findViewById(R.id.edit_amount);
        editTextAmountReceived = view.findViewById(R.id.edit_amount_received);
        final TextView sendReceive = view.findViewById(R.id.sr);
        final ProgressBar progressBar = view.findViewById(R.id.pBar);
        // Get the Drawable custom_progressbar
        Drawable draw=view.getResources().getDrawable(R.drawable.custom_progressbar);
// set the drawable as progress drawable
        progressBar.setProgressDrawable(draw);

        editTextName.setText(title);
        editTextName.setEnabled(false);
        editTextAmountReceived.setText("" + amountReceived);
        editTextAmount.setText("" + amount);

        final float[] a = {amount};
        final float[] ar = {amountReceived};
        final float[] percent = {(ar[0] / a[0]) * 100};
        progressBar.setProgress((int) percent[0]);
        if(type.equals("Income")){
            text.setText("Amount Received");
            sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Received");
        }else{
            text.setText("Amount Spent");
            sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Spent");

        }

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String n = String.valueOf(s);
                try {
                    a[0] = Integer.parseInt(n);
                } catch (NumberFormatException e) {
                }
                percent[0] = (ar[0] / a[0]) * 100;
                if(type.equals("Income")){
                    sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Received");
                }else{
                    sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Spent");

                }progressBar.setProgress((int) percent[0]);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextAmountReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String n = String.valueOf(s);
                try {
                    ar[0] = Integer.parseInt(n);

                } catch (NumberFormatException e) {
                }

                percent[0] = (ar[0] / a[0]) * 100;
                if(type.equals("Income")){
                    sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Received");
                }else{
                    sendReceive.setText(String.format("%.2f",percent[0] ) + "% / 100 % Spent");

                } progressBar.setProgress((int) percent[0]);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
        void applyText(String name, String amount, String type, String amountReceived, String period);
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


}
