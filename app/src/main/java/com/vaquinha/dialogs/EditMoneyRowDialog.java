package com.vaquinha.dialogs;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vaquinha.R;
import com.vaquinha.common.EditDateView;
import com.vaquinha.model.MoneyRow;

import java.util.List;

public abstract class EditMoneyRowDialog extends MoneyRowDialog {

    protected MoneyRow moneyRow;

    public void setMoneyRow(MoneyRow moneyRow) {
        this.moneyRow = moneyRow;
    }

    @Override
    protected boolean isUserSelected(long userId) {
        return walletManager.getUsersIdsForMoneyRow(this.moneyRow).contains(userId);
    }

    @Override
    protected void initializeValues() {
        float value = moneyRow.getValue();
        //We only want positive values
        value = value < 0 ? -1 * value : value;

        ((EditText) moneyRowDialog.findViewById(R.id.money_description_input)).setText(moneyRow.getDescription());
        ((EditText) moneyRowDialog.findViewById(R.id.money_value_input)).setText(String.valueOf(value));
        ((EditDateView) moneyRowDialog.findViewById(R.id.money_date_input)).setDate(moneyRow.getDate());
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

}
