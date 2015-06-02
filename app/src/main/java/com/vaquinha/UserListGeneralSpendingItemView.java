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
