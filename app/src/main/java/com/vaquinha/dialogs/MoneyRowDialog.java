package com.vaquinha.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vaquinha.GlobalContext;
import com.vaquinha.R;
import com.vaquinha.UserCheckboxView;
import com.vaquinha.common.EditDateView;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class MoneyRowDialog extends DialogFragment {

    protected View moneyRowDialog;
    protected WalletManager walletManager;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.moneyRowDialog = inflater.inflate(getLayout(), null);
        this.walletManager = ((GlobalContext) getActivity().getApplicationContext()).getWalletManager();

        initializeValues();
        initializeButtons();

        builder.setView(moneyRowDialog);

        buildUsersList();

        return builder.create();
    }

    protected abstract int getLayout();

    private void closeDialog() {
        getDialog().dismiss();
    }

    protected void initializeButtons() {
        initializeCloseButton();
        initializeConfirmButton();
    }

    private float getInputValue() {
        EditText inputValue = (EditText) moneyRowDialog.findViewById(R.id.money_value_input);
        return Float.valueOf(inputValue.getText().toString());
    }

    private String getInputDescription() {
        EditText inputValue = (EditText) moneyRowDialog.findViewById(R.id.money_description_input);
        return inputValue.getText().toString();
    }

    private String getInputDate() {
        EditDateView inputValue = (EditDateView) moneyRowDialog.findViewById(R.id.money_date_input);
        return inputValue.getDate();
    }

    protected abstract void onConfirmationAction(List<Long> userIds, float value, String description, String formattedDate);

    private void initializeConfirmButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Long> selectedUsersIds = getSelectedUsersIds();

                if (selectedUsersIds.size() < 1) {
                    return;
                }

                onConfirmationAction(
                        selectedUsersIds,
                        getInputValue(),
                        getInputDescription(),
                        getInputDate()
                );

                closeDialog();
            }
        });
    }

    private void initializeCloseButton() {
        ((ImageButton) moneyRowDialog.findViewById(R.id.button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
    }

    protected abstract void initializeValues();

    private void buildUsersList() {
        List<User> allUsers = walletManager.getAllUsers();
        LinearLayout userListView = (LinearLayout) this.moneyRowDialog.findViewById(R.id.user_list_container);

        for (User user : allUsers) {
            userListView.addView(new UserCheckboxView(getActivity().getApplicationContext(), user, isUserSelected(user.getId())));
        }
    }

    protected abstract boolean isUserSelected(long userId);

    private List<Long> getSelectedUsersIds() {
        LinearLayout userListView = (LinearLayout) this.moneyRowDialog.findViewById(R.id.user_list_container);
        List<Long> selectedUsers = new ArrayList<>();

        UserCheckboxView userCheckboxView;
        for (int i = 0; i < userListView.getChildCount(); i++) {
            userCheckboxView = (UserCheckboxView) userListView.getChildAt(i);
            if (userCheckboxView.isSelected()) {
                selectedUsers.add(userCheckboxView.getUser().getId());
            }
        }

        return selectedUsers;
    }
}
