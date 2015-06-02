package com.vaquinha;

import android.content.Context;

import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;

public class UserListItemView extends ListItemView {

    public UserListItemView(Context context, User user) {
        super(context, user);
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
