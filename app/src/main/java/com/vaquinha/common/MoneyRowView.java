package com.vaquinha.common;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaquinha.R;
import com.vaquinha.dialogs.EditMinusMoneyRowDialog;
import com.vaquinha.dialogs.EditMoneyRowDialog;
import com.vaquinha.dialogs.EditPlusMoneyRowDialog;
import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;

import java.math.BigDecimal;

public class MoneyRowView extends LinearLayout {

    private MoneyRow moneyRow;
    private int numberOfUsers;
    private LayoutInflater layoutInflater;

    public MoneyRowView(Context context, MoneyRow moneyRow, int numberOfUsers) {
        super(context);
        this.moneyRow = moneyRow;
        this.numberOfUsers = numberOfUsers;

        initialize();
    }

    private void initialize() {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.money_row, this, true);

        initLabels();
        initButtons();
    }

    private void initButtons() {
        ImageButton editMoneyRow = (ImageButton) this.findViewById(R.id.edit_money_row);
        editMoneyRow.setOnClickListener(new EditMoneyRowClickListener());
    }

    private float getValueForUser() {
        return new BigDecimal(moneyRow.getValue())
                .divide(new BigDecimal(numberOfUsers), Balance.BALANCE_SCALE, BigDecimal.ROUND_HALF_UP)
                .floatValue();
    }

    private void initLabels() {
        ((TextView) this.findViewById(R.id.money_value))
                .setText(String.valueOf(getValueForUser()));
        ((TextView) this.findViewById(R.id.money_description))
                .setText(String.valueOf(moneyRow.getDescription()));
    }

    protected void editMoneyRow(MoneyRow moneyRow) {
        Activity mainActivity = (Activity) this.getRootView().getContext();
        EditMoneyRowDialog dialog;

        if (moneyRow.getValue() < 0) {
            dialog = new EditMinusMoneyRowDialog();
        } else {
            dialog = new EditPlusMoneyRowDialog();
        }

        dialog.setMoneyRow(moneyRow);

        dialog.show(mainActivity.getFragmentManager(), "EditMoneyRowDialog");
    }

    public void updateMoneyRow(MoneyRow moneyRow, int numberOfUsers) {
        this.moneyRow = moneyRow;
        this.numberOfUsers = numberOfUsers;
        initLabels();
    }

    private class EditMoneyRowClickListener implements OnClickListener {

        private EditMoneyRowClickListener() {
        }

        @Override
        public void onClick(View v) {
            editMoneyRow(moneyRow);
        }
    }

    public boolean isMoneyRow(long moneyRowId) {
        return this.moneyRow.getId() == moneyRowId;
    }
}
