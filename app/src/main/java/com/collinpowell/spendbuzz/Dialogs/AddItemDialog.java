package com.collinpowell.spendbuzz.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.collinpowell.spendbuzz.Models.ListItemModel;
import com.collinpowell.spendbuzz.R;

public class AddItemDialog extends AppCompatDialogFragment {
    private EditText editTextItemName;
    private EditText editTextItemPrice;
    private EditText editTextQuantity;

    private DialogListener listener;

    ListItemModel itemModel;

    public AddItemDialog() {
    }

    public AddItemDialog(ListItemModel itemModel) {
        this.itemModel = itemModel;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!validateAmount() | !validateQuantity() | !validateName()) {
                            return;
                        } else {
                            String name = editTextItemName.getText().toString();
                            int amount = Integer.parseInt(editTextItemPrice.getText().toString());
                            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                            int id = itemModel.getId();

                            ListItemModel model = new ListItemModel(name,quantity,amount,id);
                            listener.applyText(model);
                        }

                    }
                });

        editTextItemName = view.findViewById(R.id.edit_name);
        editTextItemPrice = view.findViewById(R.id.edit_amount);
        editTextQuantity = view.findViewById(R.id.edit_quantity);

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
        void applyText(ListItemModel model);
    }

    private boolean validateName() {
        String val = editTextItemName.getText().toString();

        if (val.isEmpty()) {
            editTextItemName.setError("Field cannot be empty");
            return false;
        } else {
            editTextItemName.setError(null);
            return true;
        }
    }

    private boolean validateQuantity() {
        String val = editTextQuantity.getText().toString();

        if (val.isEmpty()) {
            editTextQuantity.setError("Field cannot be empty");
            return false;
        } else {
            editTextQuantity.setError(null);
            return true;
        }
    }

    private boolean validateAmount() {
        String val = editTextItemPrice.getText().toString();

        if (val.isEmpty()) {
            editTextItemPrice.setError("Field cannot be empty");
            return false;
        } else {
            editTextItemPrice.setError(null);
            return true;
        }
    }

}
