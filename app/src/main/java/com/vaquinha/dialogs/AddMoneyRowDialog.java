package com.vaquinha.dialogs;

import com.vaquinha.R;
import com.vaquinha.common.EditDateView;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.List;

public class AddMoneyRowDialog extends MoneyRowDialog {

    @Override
    protected int getLayout() {
        return R.layout.add_money_row_dialog;
    }

    @Override
    protected void onConfirmationAction(List<Long> userIds, float value, String description, String formattedDate) {
        walletManager.addMoneyRow(
                userIds,
                value,
                description,
                formattedDate
        );
    }

    @Override
    protected void initializeValues() {
        ((EditDateView) moneyRowDialog.findViewById(R.id.money_date_input)).setDate(MoneyDateHelper.nowFormatted());
    }

    @Override
    protected boolean isUserSelected(long userId) {
        return true;
    }

}
