package com.vaquinha.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaquinha.R;
import com.vaquinha.dialogs.EditMoneyRowDialog;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.Calendar;

public class MoneyRowView extends LinearLayout {

    private MoneyRow moneyRow;
    private LayoutInflater layoutInflater;

    public MoneyRowView(Context context, MoneyRow moneyRow) {
        super(context);
        this.moneyRow = moneyRow;

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
        editMoneyRow.setOnClickListener(new EditMoneyRowClickListener(moneyRow));
    }

    private void initLabels() {
        ((TextView) this.findViewById(R.id.money_value))
                .setText(String.valueOf(moneyRow.getValue()));
        ((TextView) this.findViewById(R.id.money_description))
                .setText(String.valueOf(moneyRow.getDescription()));
    }

    protected void editMoneyRow(MoneyRow moneyRow) {
        Activity mainActivity = (Activity) this.getRootView().getContext();

        EditMoneyRowDialog dialog = new EditMoneyRowDialog();
        dialog.setMoneyRow(moneyRow);

        dialog.show(mainActivity.getFragmentManager(), "EditMoneyRowDialog");
    }

    public void updateMoneyRow(MoneyRow moneyRow) {
        this.moneyRow = moneyRow;
        initLabels();
    }

    private class EditMoneyRowClickListener implements OnClickListener {

        final MoneyRow moneyRow;

        private EditMoneyRowClickListener(MoneyRow moneyRow) {
            this.moneyRow = moneyRow;
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
