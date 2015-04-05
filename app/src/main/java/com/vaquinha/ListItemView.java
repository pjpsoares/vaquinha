package com.vaquinha;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.List;

public abstract class ListItemView extends LinearLayout {

    protected final User user;
    protected final LayoutInflater layoutInflater;
    protected final WalletManager walletManager;
    protected final Balance balance;

    public ListItemView(Context context, User user) {
        super(context);

        this.user = user;
        this.layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.user_list_item, this, true);

        this.walletManager = ((GlobalContext) context.getApplicationContext()).getWalletManager();
        this.balance = getBalance();

        initBalanceListener();
        initUserNameButton();
        initAddMoneyPanelInteractions();
        initMoneyList();
    }

    protected abstract Balance getBalance();

    private void setTotalBalanceLabel() {
        RelativeLayout userNameButton = (RelativeLayout) findViewById(R.id.user_name_button);
        ((TextView) userNameButton.findViewById(R.id.user_total_money_label))
                .setText(String.valueOf(balance.getTotalBalance()));
    }

    private void initBalanceListener() {
        balance.setOnTotalBalanceChangeListener(new BalanceChangeListener() {
            @Override
            public void onBalanceChange() {
                setTotalBalanceLabel();
            }
        });
    }

    protected abstract String getUserName();

    protected int getTitleColor() {
        return getResources().getColor(R.color.userTitle);
    }

    protected int getPanelColor() {
        return getResources().getColor(R.color.userPanel);
    }

    private void initUserNameButton() {
        RelativeLayout userNameButton = (RelativeLayout) findViewById(R.id.user_name_button);
        ((TextView) userNameButton.findViewById(R.id.user_name_label)).setText(getUserName());
        userNameButton.setBackgroundColor(getTitleColor());
        setTotalBalanceLabel();

        final LinearLayout panelProfile = (LinearLayout) findViewById(R.id.panelProfile);
        panelProfile.setBackgroundColor(getPanelColor());

        userNameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                panelProfile.setVisibility(
                        panelProfile.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void initMoneyList() {
        List<MoneyRow> moneyRows = balance.getMoneyRows();

        LinearLayout moneyList = (LinearLayout) findViewById(R.id.userMoneyList);
        for (MoneyRow moneyRow : moneyRows) {
            addRowToMoneyList(moneyList, moneyRow);
        }
    }

    private void addRowToMoneyList(MoneyRow moneyRow) {
        addRowToMoneyList((LinearLayout) findViewById(R.id.userMoneyList), moneyRow);
    }

    protected void editMoneyRow(LinearLayout moneyRowPanel, MoneyRow moneyRow) {

        Activity mainActivity = (Activity) this.getRootView().getContext();

        EditMoneyRowDialog dialog = new EditMoneyRowDialog()
                .onDeleteRow(new OnDeleteRow(moneyRowPanel))
                .onSaveRow(new OnSaveRow(moneyRowPanel));

        dialog.setMoneyRow(moneyRow);

        dialog.show(mainActivity.getFragmentManager(), "NoticeDialogFragment");
    }

    protected abstract void deleteMoneyRow(long rowId);

    private class OnDeleteRow implements EditMoneyRowDialog.OnDeleteListener {

        final LinearLayout moneyRowPanel;

        protected OnDeleteRow(LinearLayout moneyRowPanel) {
            this.moneyRowPanel = moneyRowPanel;
        }

        @Override
        public void onClick(long moneyRowId) {
            deleteMoneyRow(moneyRowId);

            ((LinearLayout) moneyRowPanel.getParent()).removeView(moneyRowPanel);
        }
    }

    private class OnSaveRow implements EditMoneyRowDialog.OnUpdateListener {

        private final LinearLayout moneyRowPanel;

        public OnSaveRow(LinearLayout moneyRowPanel) {
            this.moneyRowPanel = moneyRowPanel;
        }

        @Override
        public void onClick(MoneyRow newMoneyRow) {
            updateMoneyRow(newMoneyRow);

            ((TextView) moneyRowPanel.findViewById(R.id.money_value))
                    .setText(String.valueOf(newMoneyRow.getValue()));
            ((TextView) moneyRowPanel.findViewById(R.id.money_description))
                    .setText(String.valueOf(newMoneyRow.getDescription()));
        }
    }

    protected abstract void updateMoneyRow(MoneyRow newMoneyRow);

    protected abstract long addMoneyRow(float value, String description, String dateFormatted);

    private void addRowToMoneyList(LinearLayout moneyList, final MoneyRow moneyRow) {
        final LinearLayout moneyRowPanel = (LinearLayout) layoutInflater.inflate(R.layout.money_row, null);

        ((TextView) moneyRowPanel.findViewById(R.id.money_value))
                .setText(String.valueOf(moneyRow.getValue()));
        ((TextView) moneyRowPanel.findViewById(R.id.money_description))
                .setText(String.valueOf(moneyRow.getDescription()));
        ImageButton editMoneyRow = (ImageButton) moneyRowPanel.findViewById(R.id.edit_money_row);

        editMoneyRow.setOnClickListener(new EditMoneyRowClickListener(moneyRowPanel, moneyRow));

        moneyList.addView(moneyRowPanel, 0);
    }

    private class EditMoneyRowClickListener implements OnClickListener {

        final LinearLayout moneyRowPanel;
        final MoneyRow moneyRow;

        private EditMoneyRowClickListener(LinearLayout moneyRowPanel, MoneyRow moneyRow) {
            this.moneyRowPanel = moneyRowPanel;
            this.moneyRow = moneyRow;
        }

        @Override
        public void onClick(View v) {
            editMoneyRow(moneyRowPanel, moneyRow);
        }
    }

    private void initAddMoneyPanelInteractions() {

        final ImageButton plusButton = (ImageButton) findViewById(R.id.confirm_money_plus);
        final ImageButton minusButton = (ImageButton) findViewById(R.id.confirm_money_minus);
        final EditText moneyInput = (EditText) findViewById(R.id.money_input);
        final EditText moneyDescriptionInput = (EditText) findViewById(R.id.money_description_input);

        plusButton.setOnClickListener(new AddValueClickListener(moneyInput, moneyDescriptionInput));
        minusButton.setOnClickListener(new AddValueClickListener(moneyInput, moneyDescriptionInput, true));
    }

    private class AddValueClickListener implements OnClickListener {

        private final EditText moneyInput;
        private final EditText moneyDescriptionInput;
        private final boolean negative;

        private AddValueClickListener(EditText moneyInput, EditText moneyDescriptionInput) {
            this(moneyInput, moneyDescriptionInput, false);
        }

        private AddValueClickListener(EditText moneyInput, EditText moneyDescriptionInput, boolean negative) {
            this.moneyInput = moneyInput;
            this.moneyDescriptionInput = moneyDescriptionInput;
            this.negative = negative;
        }

        @Override
        public void onClick(View v) {
            String moneyDescription = moneyDescriptionInput.getText().toString();
            String moneyInputString = moneyInput.getText().toString();
            if (moneyInputString == null || "".equals(moneyInputString)) {
                return;
            }

            float value = Float.valueOf(moneyInputString);
            if (negative) {
                value = value * -1;
            }
            String dateFormatted = MoneyDateHelper.nowFormatted();
            long rowId = addMoneyRow(value, moneyDescription, dateFormatted);

            addRowToMoneyList(new MoneyRow(rowId, value, moneyDescription, dateFormatted));
            moneyInput.setText("");
            moneyDescriptionInput.setText("");
        }
    }

}
