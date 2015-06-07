package com.vaquinha.dialogs;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.vaquinha.R;
import com.vaquinha.UserCheckboxView;
import com.vaquinha.common.EditDateView;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddPlusMoneyRowDialog extends MoneyRowDialog {

    private static final String NOTHING_SELECTED_ON_DROPBOX = "...";

    @Override
    protected int getLayout() {
        return R.layout.add_plus_money_row_dialog;
    }

    @Override
    protected void onConfirmationAction(List<Long> userIds, float value, String description, String formattedDate) {
        walletManager.addMoneyRow(
                userIds,
                value,
                description,
                formattedDate
        );
    }

    @Override
    protected void initializeValues() {
        ((EditDateView) moneyRowDialog.findViewById(R.id.money_date_input)).setDate(MoneyDateHelper.nowFormatted());
    }

    @Override
    protected boolean isUserSelected(long userId) {
        return true;
    }

    @Override
    protected void buildUsersList() {
        List<User> allUsers = walletManager.getAllUsers();
        String[] items = new String[allUsers.size() + 1];

        items[0] = NOTHING_SELECTED_ON_DROPBOX;

        int index = 1;
        for (User user : allUsers) {
            items[index] = user.getName();
            index++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.user_spinner_item, items);

        Spinner dropdown = (Spinner) moneyRowDialog.findViewById(R.id.user_list_container);
        dropdown.setAdapter(adapter);
    }

    @Override
    protected List<Long> getSelectedUsersIds() {
        Spinner dropdown = (Spinner) moneyRowDialog.findViewById(R.id.user_list_container);
        if(dropdown.getSelectedItemPosition() == 0) {
            return Collections.EMPTY_LIST;
        }

        // Because position 0 will serve for no position
        long userId = walletManager.getAllUsers().get(dropdown.getSelectedItemPosition() - 1).getId();
        List<Long> usersIds = new ArrayList<>(1);
        usersIds.add(userId);

        return usersIds;
    }
}
