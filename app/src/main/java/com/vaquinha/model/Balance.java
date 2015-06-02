package com.vaquinha.model;

import com.vaquinha.listeners.balance.NewMoneyRowListener;
import com.vaquinha.listeners.balance.RemoveMoneyRowListener;
import com.vaquinha.listeners.balance.UpdateMoneyRowListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Balance {

    private BigDecimal totalBalance = BigDecimal.ZERO;
    private int BALANCE_SCALE = 2;

    private final Map<MoneyRow, Integer> moneyRowsToNumberOfUsers = new HashMap<>();
    private NewMoneyRowListener newMoneyRowListener;
    private RemoveMoneyRowListener removeMoneyRowListener;
    private UpdateMoneyRowListener updateMoneyRowListener;

    public void addMoneyRows(List<MoneyRow> moneyRows, Map<Long, Integer> moneyRowIdToNumberOfUsers) {
        for (MoneyRow moneyRow: moneyRows) {
            addMoneyRow(moneyRow, moneyRowIdToNumberOfUsers.get(moneyRow.getId()));
        }
    }

    public void addMoneyRow(MoneyRow moneyRow, Integer numberOfUsers) {
        moneyRowsToNumberOfUsers.put(moneyRow, numberOfUsers);
        addToTotalBalance(moneyRow, numberOfUsers);
        notifyNewMoneyRow(moneyRow);
    }

    public boolean removeMoneyRow(long moneyRowId) {
        MoneyRow moneyRow = findMoneyRow(moneyRowId);
        if (moneyRow == null) {
            return false;
        }

        addToTotalBalance(moneyRow.getValue() * -1, moneyRowsToNumberOfUsers.get(moneyRow));
        moneyRowsToNumberOfUsers.remove(moneyRow);
        notifyRemoveMoneyRow(moneyRowId);

        return true;
    }

    private void addToTotalBalance(float value, Integer numberOfUsers) {
        addToTotalBalance(calculatesValueForUser(new BigDecimal(value), numberOfUsers));
    }

    public void addToTotalBalance(MoneyRow moneyRow, Integer numberOfUsers) {
        addToTotalBalance(calculatesValueForUser(moneyRow, numberOfUsers));
    }

    private void addToTotalBalance(BigDecimal value) {
        totalBalance = totalBalance.add(value).setScale(BALANCE_SCALE, RoundingMode.HALF_UP);
    }

    public void setNewMoneyRowListener(NewMoneyRowListener newMoneyRowListener) {
        this.newMoneyRowListener = newMoneyRowListener;
    }

    public void setRemoveMoneyRowListener(RemoveMoneyRowListener removeMoneyRowListener) {
        this.removeMoneyRowListener = removeMoneyRowListener;
    }

    public void setUpdateMoneyRowListener(UpdateMoneyRowListener updateMoneyRowListener) {
        this.updateMoneyRowListener = updateMoneyRowListener;
    }

    private void notifyNewMoneyRow(MoneyRow moneyRow) {
        if (this.newMoneyRowListener != null) {
            this.newMoneyRowListener.onNewMoneyRow(moneyRow);
        }
    }

    private void notifyRemoveMoneyRow(long moneyRowId) {
        if (this.removeMoneyRowListener != null) {
            this.removeMoneyRowListener.onRemoveMoneyRow(moneyRowId);
        }
    }

    private void notifyUpdateMoneyRow(MoneyRow moneyRow) {
        if (this.updateMoneyRowListener != null) {
            this.updateMoneyRowListener.onUpdateMoneyRow(moneyRow);
        }
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public Map<MoneyRow, Integer> getMoneyRowsToNumberOfUsers() {
        return moneyRowsToNumberOfUsers;
    }

    public MoneyRow findMoneyRow(long moneyRowId) {
        for (MoneyRow moneyRow: moneyRowsToNumberOfUsers.keySet()) {
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

    private BigDecimal calculatesValueForUser(MoneyRow moneyRow) {
        return calculatesValueForUser(moneyRow, moneyRowsToNumberOfUsers.get(moneyRow));
    }

    private BigDecimal calculatesValueForUser(MoneyRow moneyRow, Integer numberOfUsers) {
        BigDecimal value = new BigDecimal(moneyRow.getValue());
        return calculatesValueForUser(value, numberOfUsers);
    }

    private BigDecimal calculatesValueForUser(BigDecimal value, Integer numberOfUsers) {
        return value.divide(new BigDecimal(numberOfUsers), BALANCE_SCALE, RoundingMode.HALF_UP);
    }

    public float updateMoneyRow(long moneyRowId, float value, String description, String formattedDate, Integer totalNumberOfUsers) {

        MoneyRow moneyRow = findMoneyRow(moneyRowId);
        BigDecimal previousValue = calculatesValueForUser(moneyRow);
        BigDecimal newValue = calculatesValueForUser(new BigDecimal(value), totalNumberOfUsers);

        BigDecimal difference = newValue.subtract(previousValue);

        moneyRow.setValue(value);
        moneyRow.setDescription(description);
        moneyRow.setDate(formattedDate);
        moneyRowsToNumberOfUsers.put(moneyRow, totalNumberOfUsers);

        addToTotalBalance(difference);

        notifyUpdateMoneyRow(moneyRow);

        return difference.floatValue();
    }
}
