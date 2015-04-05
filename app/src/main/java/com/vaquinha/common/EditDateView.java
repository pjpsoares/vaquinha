package com.vaquinha.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaquinha.GlobalContext;
import com.vaquinha.R;
import com.vaquinha.UserListGeneralSpendingItemView;
import com.vaquinha.UserListItemView;
import com.vaquinha.dao.UserDAO;
import com.vaquinha.dao.WalletManager;
import com.vaquinha.model.User;
import com.vaquinha.utils.MoneyDateHelper;

import java.util.Calendar;
import java.util.List;

public class EditDateView extends LinearLayout {

    private Calendar calendar = Calendar.getInstance();

    public EditDateView(Context context) {
        super(context);
        initialize();
    }

    public EditDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.edit_date_view, this, true);

        ImageButton pickDateButton = (ImageButton) findViewById(R.id.button_pick_date);
        pickDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();
            }
        });
    }

    public void setDate(String date) {
        ((TextView) findViewById(R.id.money_date_label)).setText(date);
        this.calendar = MoneyDateHelper.parse(date);
    }

    public String getDate() {
        return MoneyDateHelper.format(this.calendar);
    }
}
