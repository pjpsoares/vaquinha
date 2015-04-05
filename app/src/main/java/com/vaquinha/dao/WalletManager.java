package com.vaquinha.dao;

import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletManager {

    private Map<User, Balance> usersBalance;

    private Balance generalBalance = new Balance();

    private UserDAO userDAO;
    private MoneyRowDAO moneyRowDAO;

    public WalletManager(UserDAO userDAO, MoneyRowDAO moneyRowDAO) {
        this.userDAO = userDAO;
        this.moneyRowDAO = moneyRowDAO;

        initialize();
    }

    private void initialize() {
        initializeGeneralSpendings();
        initilizeUsers();
    }

    private void initilizeUsers() {
        List<User> users = userDAO.getAll();

        usersBalance = new HashMap<>(users.size());

        for (User user : users) {
            Balance userBalance = new Balance();
            userBalance.addMoneyRows(moneyRowDAO.getAllForUser(user.getId()));

            usersBalance.put(user, userBalance);

            generalBalance.addToTotalBalance(userBalance.getTotalMoney());
        }

        addGeneralValueToAllUsersTotalBalance(generalBalance.getTotalMoney());
    }

    private void initializeGeneralSpendings() {
        generalBalance.addMoneyRows(moneyRowDAO.getAllForUser());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersBalance.keySet());
    }

    public Balance getBalanceForUser(long userId) {
        return usersBalance.get(new User(userId, ""));
    }

    public long addUser(String name) {
        long userId = userDAO.insert(name);
        usersBalance.put(new User(userId, name), new Balance());

        return userId;
    }

    public void addGeneralValueToAllUsersTotalBalance(float value) {
        addGeneralValueToAllUsersTotalBalance(new BigDecimal(value));
    }

    public void addGeneralValueToAllUsersTotalBalance(BigDecimal value) {
        if (usersBalance.size() == 0) {
            return;
        }

        BigDecimal valueForEachUser = value.divide(new BigDecimal(usersBalance.size()), 2, BigDecimal.ROUND_HALF_UP);

        for (Balance balance: usersBalance.values()) {
            balance.addToTotalBalance(valueForEachUser);
        }
    }

    public long addMoneyRowToGeneralSpending(float value, String description, String formattedDate) {
        long moneyRowId = moneyRowDAO.insert(value, description, formattedDate);
        generalBalance.addMoneyRow(new MoneyRow(moneyRowId, value, description, formattedDate));

        addGeneralValueToAllUsersTotalBalance(value);

        return moneyRowId;
    }

    public long addMoneyRow(long userId, float value, String description, String formattedDate) {
        long moneyRowId = moneyRowDAO.insert(userId, value, description, formattedDate );
        usersBalance.get(new User(userId, "")).addMoneyRow(
                new MoneyRow(moneyRowId, value, description, formattedDate));

        generalBalance.addToTotalBalance(value);

        return moneyRowId;
    }

    public void updateGeneralSpendingMoneyRow(long moneyRowId, float value, String description) {
        moneyRowDAO.update(moneyRowId, value, description);
        float difference = generalBalance.updateMoneyRow(moneyRowId, value, description);

        addGeneralValueToAllUsersTotalBalance(difference);
    }

    public void updateMoneyRow(long moneyRowId, float value, String description) {
        moneyRowDAO.update(moneyRowId, value, description);

        float difference = findBalance(moneyRowId).updateMoneyRow(moneyRowId, value, description);

        generalBalance.addToTotalBalance(difference);
    }

    public Balance findBalance(long moneyRowId) {
        for (Balance balance : usersBalance.values()) {
            if (balance.findMoneyRow(moneyRowId) != null) {
                return balance;
            }
        }

        return null;
    }

    public void deleteMoneyRow(long moneyRowId) {
        Balance userBalance = findBalance(moneyRowId);
        moneyRowDAO.delete(moneyRowId);

        float valueToBeRemoved = userBalance.findValue(moneyRowId);
        generalBalance.addToTotalBalance(valueToBeRemoved * -1);

        userBalance.removeMoneyRow(moneyRowId);
    }

    public void deleteMoneyRowFromGeneralSpending(long moneyRowId) {
        moneyRowDAO.delete(moneyRowId);

        float valueToBeRemoved = generalBalance.findValue(moneyRowId);
        addGeneralValueToAllUsersTotalBalance(valueToBeRemoved * -1);

        generalBalance.removeMoneyRow(moneyRowId);
    }

    public Balance getGeneralBalance() {
        return generalBalance;
    }
}
