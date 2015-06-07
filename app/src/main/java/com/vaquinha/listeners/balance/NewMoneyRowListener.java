package com.vaquinha.listeners.balance;

import com.vaquinha.model.MoneyRow;

public interface NewMoneyRowListener {

    public void onNewMoneyRow(MoneyRow moneyRow, int numberOfUsers);

}
