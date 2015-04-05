package com.vaquinha.model;

import com.vaquinha.BalanceChangeListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Balance {

    private BigDecimal totalMoney = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;

    private final List<MoneyRow> moneyRows = new ArrayList<>();
    private BalanceChangeListener balanceChangeListener;

    public void addMoneyRows(List<MoneyRow> moneyRows) {
        for (MoneyRow moneyRow: moneyRows) {
            addMoneyRow(moneyRow);
        }
    }

    public void addMoneyRow(MoneyRow moneyRow) {
        moneyRows.add(moneyRow);

        addToTotalMoney(moneyRow.getValue());
    }

    public void addToTotalMoney(float value) {
        addToTotalMoney(new BigDecimal(value));
    }

    public void addToTotalMoney(BigDecimal value) {
        totalMoney = totalMoney.add(value).setScale(2, RoundingMode.HALF_UP);
        addToTotalBalance(value);
    }

    public void addToTotalBalance(float value) {
        addToTotalBalance(new BigDecimal(value));
    }

    public void addToTotalBalance(BigDecimal value) {
        totalBalance = totalBalance.add(value).setScale(2, RoundingMode.HALF_UP);
        notifyTotalBalanceChangeListener();
    }

    public void setOnTotalBalanceChangeListener(BalanceChangeListener balanceChangeListener) {
        this.balanceChangeListener = balanceChangeListener;
    }

    public void notifyTotalBalanceChangeListener() {
        if (this.balanceChangeListener != null) {
            this.balanceChangeListener.onBalanceChange();
        }
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public List<MoneyRow> getMoneyRows() {
        return moneyRows;
    }

    public MoneyRow findMoneyRow(long moneyRowId) {
        for (MoneyRow moneyRow: moneyRows) {
            if (moneyRow.getId() == moneyRowId) {
                return moneyRow;
            }
        }

        return null;
    }

    public float findValue(long moneyRowId) {
        MoneyRow moneyRow = findMoneyRow(moneyRowId);

        return moneyRow != null ? moneyRow.getValue(): 0;
    }

    public void removeMoneyRow(long moneyRowId) {
        MoneyRow moneyRow = findMoneyRow(moneyRowId);
        moneyRows.remove(moneyRow);

        addToTotalMoney(moneyRow.getValue() * -1);
    }

    public float updateMoneyRow(long moneyRowId, float value, String description, String formattedDate) {

        MoneyRow moneyRow = findMoneyRow(moneyRowId);

        BigDecimal difference = new BigDecimal(value).subtract(new BigDecimal(moneyRow.getValue()));

        moneyRow.setValue(value);
        moneyRow.setDescription(description);
        moneyRow.setDate(formattedDate);

        addToTotalMoney(difference);

        return difference.floatValue();
    }
}
