package com.vaquinha.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.vaquinha.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDAO {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_USER_NAME + " TEXT ); ";

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME + "; ";

    private final SQLiteDatabase dbInstance;

    public UserDAO(SQLiteDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_NAME = "name";
    }

    public long insert(String name) {

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_USER_NAME, name);

        return dbInstance.insert(FeedEntry.TABLE_NAME, null, values);
    }

    private User create(Cursor cursor) {
        return new User(
                cursor.getLong(
                        cursor.getColumnIndexOrThrow(FeedEntry._ID)),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_USER_NAME))
        );
    }

    public List<User> getAll() {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_USER_NAME
        };

        Cursor cursor = dbInstance.query(FeedEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return Collections.EMPTY_LIST;
        };

        List<User> allUsers = new ArrayList<>();
        do {
            allUsers.add(create(cursor));
        } while(cursor.moveToNext());

        return allUsers;
    }
}
