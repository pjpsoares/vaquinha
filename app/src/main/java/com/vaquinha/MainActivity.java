package com.vaquinha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vaquinha.controller.AddUserActivity;
import com.vaquinha.dialogs.AddMinusMoneyRowDialog;
import com.vaquinha.dialogs.AddPlusMoneyRowDialog;

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
            default:
                throw new UnsupportedOperationException();
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionPlusMoneyRow(View view) {
        AddPlusMoneyRowDialog addMoneyRowDialog = new AddPlusMoneyRowDialog();
        addMoneyRowDialog.show(getFragmentManager(), "PlusMoneyRowDialog");
    }

    public void actionMinusMoneyRow(View view) {
        AddMinusMoneyRowDialog addMoneyRowDialog = new AddMinusMoneyRowDialog();
        addMoneyRowDialog.show(getFragmentManager(), "MinusMoneyRowDialog");
    }

}
