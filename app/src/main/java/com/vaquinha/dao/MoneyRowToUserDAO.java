package com.vaquinha.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyRowToUserDAO {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_USER_ID + " INTEGER," +
                    FeedEntry.COLUMN_NAME_MONEY_ROW_ID + " INTEGER ); ";

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME + "; ";

    private final SQLiteDatabase dbInstance;

    public MoneyRowToUserDAO(SQLiteDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "moneyRowToUser";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_MONEY_ROW_ID = "value";
    }

    public long insert(long userId, long moneyRowId) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_USER_ID, userId);
        values.put(FeedEntry.COLUMN_NAME_MONEY_ROW_ID, moneyRowId);

        return dbInstance.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public void delete(long userId, long moneyRowId) {
        dbInstance.delete(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_MONEY_ROW_ID + "= ? AND " + FeedEntry.COLUMN_NAME_USER_ID  + "= ? ",
                new String[]{String.valueOf(moneyRowId), String.valueOf(userId)});
    }

    public void deleteMoneyRow(long moneyRowId) {
        dbInstance.delete(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_MONEY_ROW_ID + "= ?",
                new String[]{String.valueOf(moneyRowId)});
    }

    private String[] getColumns() {
        return new String[]  {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_USER_ID,
                FeedEntry.COLUMN_NAME_MONEY_ROW_ID
        };
    }

    private Map<Long, List<Long>> buildMapFromUserToMoneyRow(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return new HashMap<>(0);
        };

        Map<Long, List<Long>> userToMoneyRow = new HashMap<>();

        do {
            Long userId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_USER_ID));
            Long moneyRowId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_MONEY_ROW_ID));

            List<Long> moneyRowsIds = userToMoneyRow.get(userId);

            if (moneyRowsIds == null) {
                moneyRowsIds = new ArrayList<>(1);
                userToMoneyRow.put(userId, moneyRowsIds);
            }

            moneyRowsIds.add(moneyRowId);

        } while(cursor.moveToNext());

        return userToMoneyRow;
    }

    public Map<Long, List<Long>> getAllMappedByUser() {
        Cursor cursor = dbInstance.query(
                FeedEntry.TABLE_NAME, getColumns(),
                null, null, null, null, null);

        return buildMapFromUserToMoneyRow(cursor);
    }
}
