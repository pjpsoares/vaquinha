package com.vaquinha;

import android.content.Context;

import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

public class UserListItemView extends ListItemView {

    public UserListItemView(Context context, User user) {
        super(context, user);
    }

    @Override
    protected void deleteMoneyRow(long rowId) {
        walletManager.deleteMoneyRow(rowId);
    }

    @Override
    protected void updateMoneyRow(MoneyRow newMoneyRow) {
        walletManager.updateMoneyRow(
                newMoneyRow.getId(), newMoneyRow.getValue(), newMoneyRow.getDescription());
    }

    @Override
    protected long addMoneyRow(float value, String description, String dateFormatted) {
        return walletManager.addMoneyRow(
                user.getId(), value, description, dateFormatted);
    }

    @Override
    protected String getUserName() {
        return user.getName();
    }

    @Override
    protected Balance getBalance() {
        return walletManager.getBalanceForUser(user.getId());
    }

}
