package com.vaquinha;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaquinha.dao.UserDAO;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.User;

import java.util.List;

public class UserListView extends LinearLayout {

    private WalletManager walletManager;

    public UserListView(Context context) {
        super(context);
        initialize();
    }

    public UserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void initialize() {
        walletManager = ((GlobalContext) getContext().getApplicationContext()).getWalletManager();
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.user_list_view, this, true);

        buildUserList();
    }

    public void buildUserList() {
        List<User> allUsers = walletManager.getAllUsers();

        LinearLayout userListView = (LinearLayout) findViewById(R.id.user_list_main_container);
        userListView.addView(buildGeneralSpendingsPanel());

        for (User user : allUsers) {
            userListView.addView(new UserListItemView(getContext(), user));
        }
    }

    private View buildGeneralSpendingsPanel() {
        return new UserListGeneralSpendingItemView(getContext());
    }

}
