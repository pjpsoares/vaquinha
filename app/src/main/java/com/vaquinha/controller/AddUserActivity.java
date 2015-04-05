package com.vaquinha.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.vaquinha.GlobalContext;
import com.vaquinha.MainActivity;
import com.vaquinha.R;
import com.vaquinha.dao.UserDAO;
import com.vaquinha.dao.WalletManager;


public class AddUserActivity extends ActionBarActivity {

    public static final String USER_SHARED_PREFERENCES = "userSharedPreferences";
    public static final String USERS_SET = "usersSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
    }

    private void backToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        backToMainScreen();
    }

    public void ok(View view) {
        EditText userNameText = (EditText) findViewById(R.id.user_name);

        WalletManager walletManager = ((GlobalContext) getApplication()).getWalletManager();
        walletManager.addUser(userNameText.getText().toString());

        backToMainScreen();
    }

}
