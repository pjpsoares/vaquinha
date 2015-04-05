package com.vaquinha.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.vaquinha.model.MoneyRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MoneyRowDAO {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_USER_ID + " INTEGER," +
                    FeedEntry.COLUMN_NAME_VALUE + " REAL," +
                    FeedEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    FeedEntry.COLUMN_NAME_DATE + " DATE ); ";

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME + "; ";

    private final SQLiteDatabase dbInstance;

    public MoneyRowDAO(SQLiteDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "moneyRow";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public long insert(long userId, float value, String description, String formattedDate) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_USER_ID, userId);
        values.put(FeedEntry.COLUMN_NAME_VALUE, value);
        values.put(FeedEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(FeedEntry.COLUMN_NAME_DATE, formattedDate);

        return dbInstance.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public long insert(float value, String description, String formattedDate) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_VALUE, value);
        values.put(FeedEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(FeedEntry.COLUMN_NAME_DATE, formattedDate);

        return dbInstance.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public void update(long moneyRowId, float value, String description) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_VALUE, value);
        values.put(FeedEntry.COLUMN_NAME_DESCRIPTION, description);

        dbInstance.update(FeedEntry.TABLE_NAME, values, FeedEntry._ID + " = ? ", new String[]{String.valueOf(moneyRowId)});
    }

    private MoneyRow create(Cursor cursor) {
        cursor.getColumnNames();

        return new MoneyRow(
                cursor.getLong(
                        cursor.getColumnIndexOrThrow(FeedEntry._ID)),
                cursor.getFloat(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_VALUE)),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION)),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DATE)
                )
        );
    }

    public void delete(long id) {
        dbInstance.delete(
                FeedEntry.TABLE_NAME,
                FeedEntry._ID + "= ? ",
                new String[]{String.valueOf(id)});
    }

    private List<MoneyRow> buildListFromCursor(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return Collections.EMPTY_LIST;
        };

        List<MoneyRow> moneyRows = new ArrayList<>();
        do {
            moneyRows.add(create(cursor));
        } while(cursor.moveToNext());

        return moneyRows;
    }

    private String[] getColumnsForMoneyRow() {
        return new String[]  {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_VALUE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_DATE
        };
    }

    public List<MoneyRow> getAllForUser() {
        Cursor cursor = dbInstance.query(
                FeedEntry.TABLE_NAME, getColumnsForMoneyRow(),
                FeedEntry.COLUMN_NAME_USER_ID + " IS NULL",
                null, null, null, null);


        return buildListFromCursor(cursor);
    }

    public List<MoneyRow> getAllForUser(long userId) {
        Cursor cursor = dbInstance.query(
                FeedEntry.TABLE_NAME, getColumnsForMoneyRow(),
                FeedEntry.COLUMN_NAME_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        return buildListFromCursor(cursor);
    }
}
