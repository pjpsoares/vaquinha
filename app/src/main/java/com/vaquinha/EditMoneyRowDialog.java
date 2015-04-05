package com.vaquinha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vaquinha.common.EditDateView;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.MoneyRow;

public class EditMoneyRowDialog extends DialogFragment {

    private MoneyRow moneyRow;
    private View moneyRowDialog;
    private OnUpdateListener onSaveRowClickListener;
    private OnDeleteListener onDeleteRowClickListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.moneyRowDialog = inflater.inflate(R.layout.edit_money_row, null);
        initializeValues();
        initializeButtons();

        builder.setView(moneyRowDialog);

        return builder.create();
    }

    private void closeDialog() {
        getDialog().dismiss();
    }

    private void initializeButtons() {
        initializeCloseButton();
        initializeConfirmButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteRowClickListener.onClick(moneyRow.getId());
                closeDialog();
            }
        });
    }

    private float getInputValue() {
        EditText inputValue = (EditText) moneyRowDialog.findViewById(R.id.money_value_input);
        return Float.valueOf(inputValue.getText().toString());
    }

    private String getInputDescription() {
        EditText inputValue = (EditText) moneyRowDialog.findViewById(R.id.money_description_input);
        return inputValue.getText().toString();
    }

    private String getInputDate() {
        EditDateView inputValue = (EditDateView) moneyRowDialog.findViewById(R.id.money_date_input);
        return inputValue.getText().toString();
    }

    private void initializeConfirmButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveRowClickListener.onClick(
                        new MoneyRow(moneyRow.getId(), getInputValue(), getInputDescription(), getInputDate()));
                closeDialog();
            }
        });
    }

    private void initializeCloseButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
    }

    public void initializeValues() {
        ((EditText) moneyRowDialog.findViewById(R.id.money_description_input)).setText(moneyRow.getDescription());
        ((EditText) moneyRowDialog.findViewById(R.id.money_value_input)).setText(String.valueOf(moneyRow.getValue()));
        ((EditDateView) moneyRowDialog.findViewById(R.id.money_date_input)).setDate(moneyRow.getDate());
    }

    public void setMoneyRow(MoneyRow moneyRow) {
        this.moneyRow = moneyRow;
    }

    public EditMoneyRowDialog onDeleteRow(OnDeleteListener onDeleteRowClickListener) {
        this.onDeleteRowClickListener = onDeleteRowClickListener;
        return this;
    }

    public EditMoneyRowDialog onSaveRow(OnUpdateListener onSaveRowClickListener) {
        this.onSaveRowClickListener = onSaveRowClickListener;
        return this;
    }

    public abstract interface OnDeleteListener {
        void onClick(long moneyRowId);
    }

    public abstract interface OnUpdateListener {
        void onClick(MoneyRow newMoneyRow);
    }
}
