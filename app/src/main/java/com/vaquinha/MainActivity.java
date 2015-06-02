package com.vaquinha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.vaquinha.controller.AddUserActivity;
import com.vaquinha.dialogs.AddMoneyRowDialog;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_user:
                Intent intent = new Intent(this, AddUserActivity.class);
                startActivity(intent);
                break;
            case R.id.action_add_money_row:
                AddMoneyRowDialog addMoneyRowDialog = new AddMoneyRowDialog();

                addMoneyRowDialog.show(getFragmentManager(), "AddMoneyRowDialog");
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return super.onOptionsItemSelected(item);
    }
}
