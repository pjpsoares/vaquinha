package com.vaquinha.dao;

import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletManager {

    private Map<User, Balance> usersBalance;
    private Balance generalBalance = new Balance();

    private UserDAO userDAO;
    private MoneyRowDAO moneyRowDAO;
    private MoneyRowToUserDAO moneyRowToUserDAO;

    private Map<Long, List<Long>> userToMoneyRowsIds;

    public WalletManager(
            UserDAO userDAO, MoneyRowDAO moneyRowDAO, MoneyRowToUserDAO moneyRowToUserDAO) {

        this.userDAO = userDAO;
        this.moneyRowDAO = moneyRowDAO;
        this.moneyRowToUserDAO = moneyRowToUserDAO;

        initialize();
    }

    private void initialize() {
        List<User> users = userDAO.getAll();
        List<MoneyRow> moneyRows = moneyRowDAO.getAll();
        userToMoneyRowsIds = moneyRowToUserDAO.getAllMappedByUser();
        Map<Long, Integer> moneyRowToNumberOfUsers = getMoneyRowNumberOfUsersMap(userToMoneyRowsIds);

        initializeGeneralSpendings(moneyRows);
        initializeUsers(users, moneyRows, userToMoneyRowsIds, moneyRowToNumberOfUsers);
    }

    private Map<Long, Integer> getMoneyRowNumberOfUsersMap(Map<Long, List<Long>> userToMoneyRows) {
        Map<Long, Integer> moneyRowNumberOfUsersMap = new HashMap<>();

        for (List<Long> moneyRowsIds : userToMoneyRows.values()) {

            for (Long moneyRowId : moneyRowsIds) {
                Integer numberOfUsers = moneyRowNumberOfUsersMap.get(moneyRowId);
                numberOfUsers = numberOfUsers == null ? 1 : ++numberOfUsers;

                moneyRowNumberOfUsersMap.put(moneyRowId, numberOfUsers);
            }
        }

        return moneyRowNumberOfUsersMap;
    }

    private void initializeUsers(
            List<User> users, List<MoneyRow> moneyRows,
            Map<Long, List<Long>> userToMoneyRows, Map<Long, Integer> moneyRowToNumberOfUsers) {

        usersBalance = new HashMap<>(users.size());
        Map<Long, MoneyRow> moneyRowById = getMoneyRowById(moneyRows);

        for (User user : users) {
            Balance userBalance = new Balance();
            usersBalance.put(user, userBalance);

            userBalance.addMoneyRows(
                    getMoneyRows(userToMoneyRows.get(user.getId()), moneyRowById),
                    moneyRowToNumberOfUsers);
        }
    }

    private List<MoneyRow> getMoneyRows(List<Long> moneyRowsIds, Map<Long, MoneyRow> moneyRowById) {
        if (moneyRowsIds == null) {
            return Collections.EMPTY_LIST;
        }

        List<MoneyRow> moneyRows = new ArrayList<>(moneyRowsIds.size());

        for (Long moneyRowId : moneyRowsIds) {
            moneyRows.add(moneyRowById.get(moneyRowId));
        }

        return moneyRows;
    }

    private Map<Long, MoneyRow> getMoneyRowById(List<MoneyRow> moneyRows) {
        Map<Long, MoneyRow> moneyRowById = new HashMap<>(moneyRows.size());

        for (MoneyRow moneyRow : moneyRows) {
            moneyRowById.put(moneyRow.getId(), moneyRow);
        }

        return moneyRowById;
    }

    private void initializeGeneralSpendings(List<MoneyRow> moneyRows) {
        for (MoneyRow moneyRow : moneyRows) {
            generalBalance.addMoneyRow(moneyRow, 1);
        }
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

    private void addMoneyRowIdToUserId(Long userId, Long moneyRowId) {
        List<Long> moneyRowsIds = userToMoneyRowsIds.get(userId);
        if (moneyRowsIds == null) {
            moneyRowsIds = new ArrayList<>(1);
            userToMoneyRowsIds.put(userId, moneyRowsIds);
        }
        moneyRowsIds.add(moneyRowId);
    }

    private void removeMoneyRowIdFromUserId(Long userId, Long moneyRowId) {
        List<Long> moneyRowsIds = userToMoneyRowsIds.get(userId);
        moneyRowsIds.remove(moneyRowId);
    }

    public long addMoneyRow(
            List<Long> userIds, float value, String description, String formattedDate) {

        long moneyRowId = moneyRowDAO.insert(value, description, formattedDate);
        MoneyRow moneyRow = new MoneyRow(moneyRowId, value, description, formattedDate);

        for (long userId : userIds) {
            moneyRowToUserDAO.insert(userId, moneyRowId);
            usersBalance.get(new User(userId, "")).addMoneyRow(moneyRow, userIds.size());

            List<Long> moneyRowsIds = userToMoneyRowsIds.get(userId);
            if (moneyRowsIds == null) {
                moneyRowsIds = new ArrayList<>(1);
                userToMoneyRowsIds.put(userId, moneyRowsIds);
                addMoneyRowIdToUserId(userId, moneyRowId);
            }
            moneyRowsIds.add(moneyRowId);
        }
        generalBalance.addMoneyRow(moneyRow, 1);

        return moneyRowId;
    }

    private List<Long> difference(List<Long> list1, List<Long> list2) {
        List<Long> list1Clone = new ArrayList<>(list1);
        list1Clone.removeAll(list2);

        return list1Clone;
    }

    private List<Long> intersection(List<Long> list1, List<Long> list2) {
        List<Long> result = new ArrayList<>();

        for (Long element1 : list1) {
            if(list2.contains(element1)) {
                result.add(element1);
            }
        }

        return result;
    }

    public void updateMoneyRow(
            List<Long> userIds, long moneyRowId, float value, String description, String formattedDate) {

        this.moneyRowDAO.update(moneyRowId, value, description, formattedDate);

        List<Long> originalMoneyRows = getUsersIdsForMoneyRow(moneyRowId);

        List<Long> usersIdsToRemove = difference(originalMoneyRows, userIds);
        List<Long> usersIdsToAdd = difference(userIds, originalMoneyRows);
        List<Long> usersIdsToUpdate = intersection(originalMoneyRows, userIds);

        MoneyRow moneyRow = new MoneyRow(moneyRowId, value, description, formattedDate);

        for (Long userId : usersIdsToRemove) {
            this.moneyRowToUserDAO.delete(userId, moneyRowId);
            usersBalance.get(new User(userId, "")).removeMoneyRow(moneyRowId);
            this.userToMoneyRowsIds.get(userId).remove(moneyRowId);

            removeMoneyRowIdFromUserId(userId, moneyRowId);
        }

        for (Long userId : usersIdsToAdd) {
            this.moneyRowToUserDAO.insert(userId, moneyRowId);
            usersBalance.get(new User(userId, "")).addMoneyRow(moneyRow, userIds.size());
            List<Long> userMoneyRows = this.userToMoneyRowsIds.get(userId);
            if (userMoneyRows == null) {
                userMoneyRows = new ArrayList<>(1);
                this.userToMoneyRowsIds.put(userId, userMoneyRows);
            }
            userMoneyRows.add(moneyRowId);
            addMoneyRowIdToUserId(userId, moneyRowId);
        }

        for (Long userId : usersIdsToUpdate) {
            usersBalance.get(new User(userId, ""))
                    .updateMoneyRow(moneyRowId, value, description, formattedDate, userIds.size());
        }

        this.generalBalance.updateMoneyRow(moneyRowId, value, description, formattedDate, 1);
    }

    public void deleteMoneyRow(long moneyRowId) {
        moneyRowToUserDAO.deleteMoneyRow(moneyRowId);
        moneyRowDAO.delete(moneyRowId);

        for (Map.Entry<User, Balance> entryUserBalance : usersBalance.entrySet()) {
            if (entryUserBalance.getValue().removeMoneyRow(moneyRowId)) {
                removeMoneyRowIdFromUserId(entryUserBalance.getKey().getId(), moneyRowId);
            }
        }

        generalBalance.removeMoneyRow(moneyRowId);
    }

    public Balance getGeneralBalance() {
        return generalBalance;
    }

    public List<Long> getUsersIdsForMoneyRow(MoneyRow moneyRow) {
        return getUsersIdsForMoneyRow(moneyRow.getId());
    }

    public List<Long> getUsersIdsForMoneyRow(Long moneyRowId) {
        List<Long> usersIds = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> userToMoneyRowsIdsEntry : this.userToMoneyRowsIds.entrySet()) {
            if (userToMoneyRowsIdsEntry.getValue().contains(moneyRowId)) {
                usersIds.add(userToMoneyRowsIdsEntry.getKey());
            }
        }

        return usersIds;
    }
}
