package com.vaquinha.dialogs;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vaquinha.R;
import com.vaquinha.model.User;

import java.util.ArrayList;
import java.util.List;

public class EditPlusMoneyRowDialog extends EditMoneyRowDialog {

    @Override
    protected int getLayout() {
        return R.layout.edit_plus_money_row_dialog;
    }

    @Override
    protected void buildUsersList() {
        List<User> allUsers = walletManager.getAllUsers();
        String[] items = new String[allUsers.size()];

        int index = 0;
        int selectedUserIndex = 0;
        for (User user : allUsers) {
            items[index] = user.getName();
            if (isUserSelected(user.getId())) {
                selectedUserIndex = index;
            };

            index++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, items);

        Spinner dropdown = (Spinner) moneyRowDialog.findViewById(R.id.user_list_container);
        dropdown.setAdapter(adapter);

        dropdown.setSelection(selectedUserIndex);
    }

    @Override
    protected List<Long> getSelectedUsersIds() {
        Spinner dropdown = (Spinner) moneyRowDialog.findViewById(R.id.user_list_container);

        long userId = walletManager.getAllUsers().get(dropdown.getSelectedItemPosition()).getId();
        List<Long> usersIds = new ArrayList<>(1);
        usersIds.add(userId);

        return usersIds;
    }

}
