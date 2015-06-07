package com.vaquinha.dialogs;

import com.vaquinha.R;

public class EditMinusMoneyRowDialog extends EditMoneyRowDialog {

    @Override
    protected int getLayout() {
        return R.layout.edit_minus_money_row_dialog;
    }

    @Override
    protected float getInputValue() {
        return -1 * super.getInputValue();
    }

}
