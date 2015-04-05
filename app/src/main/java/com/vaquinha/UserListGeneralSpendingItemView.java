package com.vaquinha;

import android.content.Context;

import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.utils.MoneyDateHelper;

public class UserListGeneralSpendingItemView extends ListItemView {

    public UserListGeneralSpendingItemView(Context context) {
        super(context, null);
    }

    @Override
    protected Balance getBalance() {
        return walletManager.getGeneralBalance();
    }

    @Override
    protected void deleteMoneyRow(long rowId) {
        walletManager.deleteMoneyRowFromGeneralSpending(rowId);
    }

    @Override
    protected void updateMoneyRow(MoneyRow newMoneyRow) {
        walletManager.updateGeneralSpendingMoneyRow(
                newMoneyRow.getId(), newMoneyRow.getValue(), newMoneyRow.getDescription(), newMoneyRow.getDate());
    }

    @Override
    protected long addMoneyRow(float value, String description, String dateFormatted) {
        return walletManager.addMoneyRowToGeneralSpending(
                value, description, dateFormatted);
    }

    @Override
    protected String getUserName() {
        return getContext().getResources().getText(R.string.generalSpendings).toString();
    }

    @Override
    protected int getTitleColor() {
        return getResources().getColor(R.color.generalSpendingTitle);
    }

    @Override
    protected int getPanelColor() {
        return getResources().getColor(R.color.generalSpendingPanel);
    }
}
