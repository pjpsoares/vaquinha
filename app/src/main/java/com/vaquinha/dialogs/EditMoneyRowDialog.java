package com.vaquinha.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vaquinha.GlobalContext;
import com.vaquinha.R;
import com.vaquinha.UserCheckboxView;
import com.vaquinha.common.EditDateView;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;

import java.util.ArrayList;
import java.util.List;

public class EditMoneyRowDialog extends MoneyRowDialog {

    private MoneyRow moneyRow;

    @Override
    protected int getLayout() {
        return R.layout.edit_money_row;
    }

    private void closeDialog() {
        getDialog().dismiss();
    }

    @Override
    protected void initializeButtons() {
        super.initializeButtons();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletManager.deleteMoneyRow(moneyRow.getId());
                closeDialog();
            }
        });
    }

    @Override
    protected void onConfirmationAction(List<Long> userIds, float value, String description, String formattedDate) {
        walletManager.updateMoneyRow(userIds, moneyRow.getId(), value, description, formattedDate);
    }

    @Override
    protected void initializeValues() {
        ((EditText) moneyRowDialog.findViewById(R.id.money_description_input)).setText(moneyRow.getDescription());
        ((EditText) moneyRowDialog.findViewById(R.id.money_value_input)).setText(String.valueOf(moneyRow.getValue()));
        ((EditDateView) moneyRowDialog.findViewById(R.id.money_date_input)).setDate(moneyRow.getDate());
    }

    @Override
    protected boolean isUserSelected(long userId) {
        return walletManager.getUsersIdsForMoneyRow(this.moneyRow).contains(userId);
    }

    public void setMoneyRow(MoneyRow moneyRow) {
        this.moneyRow = moneyRow;
    }

}
