package com.vaquinha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vaquinha.common.MoneyRowView;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.listeners.balance.NewMoneyRowListener;
import com.vaquinha.listeners.balance.RemoveMoneyRowListener;
import com.vaquinha.listeners.balance.UpdateMoneyRowListener;
import com.vaquinha.model.Balance;
import com.vaquinha.model.MoneyRow;
import com.vaquinha.model.User;

import java.util.Map;

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
        initMoneyList();
    }

    protected abstract Balance getBalance();

    private void setTotalBalanceLabel() {
        RelativeLayout userNameButton = (RelativeLayout) findViewById(R.id.user_name_button);
        ((TextView) userNameButton.findViewById(R.id.user_total_money_label))
                .setText(String.valueOf(balance.getTotalBalance()));
    }

    private void initBalanceListener() {
        balance.setNewMoneyRowListener(new NewMoneyRowListener() {
            @Override
            public void onNewMoneyRow(MoneyRow moneyRow, int numberOfUsers) {
                addRowToMoneyList(moneyRow, numberOfUsers);
                setTotalBalanceLabel();
            }
        });

        balance.setRemoveMoneyRowListener(new RemoveMoneyRowListener() {
            @Override
            public void onRemoveMoneyRow(long moneyRowId) {
                removeMoneyRow(moneyRowId);
                setTotalBalanceLabel();
            }
        });

        balance.setUpdateMoneyRowListener(new UpdateMoneyRowListener() {
            @Override
            public void onUpdateMoneyRow(MoneyRow moneyRow, int numberOfUsers) {
                updateMoneyRow(moneyRow, numberOfUsers);
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
        LinearLayout moneyList = (LinearLayout) findViewById(R.id.userMoneyList);
        for (Map.Entry<MoneyRow, Integer> moneyRowEntry : balance.getMoneyRowsToNumberOfUsers().entrySet()) {
            addRowToMoneyList(moneyList, moneyRowEntry.getKey(), moneyRowEntry.getValue());
        }
    }

    private void removeMoneyRow(final long moneyRowId) {
        LinearLayout moneyList = (LinearLayout) findViewById(R.id.userMoneyList);
        for (int i = 0; i < moneyList.getChildCount(); i++) {
            MoneyRowView moneyRowView = (MoneyRowView) moneyList.getChildAt(i);
            if (moneyRowView.isMoneyRow(moneyRowId)) {
                moneyList.removeView(moneyRowView);
                return;
            }
        }
    }

    private void updateMoneyRow(MoneyRow moneyRow, int numberOfUsers) {
        LinearLayout moneyList = (LinearLayout) findViewById(R.id.userMoneyList);
        for (int i = 0; i < moneyList.getChildCount(); i++) {
            MoneyRowView moneyRowView = (MoneyRowView) moneyList.getChildAt(i);
            if (moneyRowView.isMoneyRow(moneyRow.getId())) {
                moneyRowView.updateMoneyRow(moneyRow, numberOfUsers);
                return;
            }
        }
    }

    private void addRowToMoneyList(final MoneyRow moneyRow, int numberOfUsers) {
        LinearLayout moneyList = (LinearLayout) findViewById(R.id.userMoneyList);
        addRowToMoneyList(moneyList, moneyRow, numberOfUsers);
    }

    private void addRowToMoneyList(LinearLayout moneyList, final MoneyRow moneyRow, int numberOfUsers) {
        moneyList.addView(new MoneyRowView(getContext(), moneyRow, numberOfUsers), 0);
    }

}
