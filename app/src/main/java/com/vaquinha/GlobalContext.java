package com.vaquinha;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.vaquinha.dao.MoneyRowDAO;
import com.vaquinha.dao.MoneyRowToUserDAO;
import com.vaquinha.dao.UserDAO;
import com.vaquinha.dao.VaquinhaDbHelper;
import com.vaquinha.dao.WalletManager;

public class GlobalContext extends Application {

    private UserDAO userDAO = null;
    private MoneyRowDAO moneyRowDAO = null;
    private MoneyRowToUserDAO moneyRowToUserDAO = null;
    private WalletManager walletManager = null;

    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteDatabase dbInstance = new VaquinhaDbHelper(getApplicationContext()).getWritableDatabase();

        userDAO = new UserDAO(dbInstance);
        moneyRowDAO = new MoneyRowDAO(dbInstance);
        moneyRowToUserDAO = new MoneyRowToUserDAO(dbInstance);

        walletManager = new WalletManager(userDAO, moneyRowDAO, moneyRowToUserDAO);
    }

    public WalletManager getWalletManager() {
        return walletManager;
    }
}
