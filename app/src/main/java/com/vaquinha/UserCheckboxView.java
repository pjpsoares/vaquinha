package com.vaquinha;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.User;

import java.util.List;

public class UserCheckboxView extends LinearLayout {

    private User user;
    private static final boolean DEFAULT_CHECKED_STATE = true;

    public UserCheckboxView(Context context, User user) {
        this(context, user, DEFAULT_CHECKED_STATE);
    }

    public UserCheckboxView(Context context, User user, boolean checked) {
        super(context);
        this.user = user;
        initialize(checked);
    }

    public void initialize(boolean checked) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.user_checkbox, this, true);

        initializeCheckbox(checked);
    }

    private void initializeCheckbox(boolean checked) {
        final TextView userNameTextView = (TextView) this.findViewById(R.id.user_name);
        userNameTextView.setText(user.getName());

        ((CheckBox) this.findViewById(R.id.user_checkbox)).setChecked(checked);
    }

    public User getUser() {
        return user;
    }

    public boolean isSelected() {
        return ((CheckBox) this.findViewById(R.id.user_checkbox)).isChecked();
    }
}
