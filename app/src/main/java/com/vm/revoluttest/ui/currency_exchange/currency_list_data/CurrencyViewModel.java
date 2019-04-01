package com.vm.revoluttest.ui.currency_exchange.currency_list_data;

import com.vm.revoluttest.ui.utils.BalanceFormatter;

import java.math.BigDecimal;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

public class CurrencyViewModel extends BaseObservable {
    public final ObservableField<String> shortCurrencyName = new ObservableField<>();
    public final ObservableField<String> fullCurrencyName = new ObservableField<>();
    public final ObservableField<String> amountString = new ObservableField<>();
    public final ObservableField<Integer> resourceId = new ObservableField<>();
    public final ObservableField<Boolean> isEditable = new ObservableField<>();
    private BigDecimal rate;
    private CurrencyClickListener clickListener;
    private AmountChangeListener amountChangeListener;


    public void setClickListener(CurrencyClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @SuppressWarnings("WeakerAccess")
    public CurrencyClickListener getClickListener() {
        return clickListener;
    }

    @SuppressWarnings("WeakerAccess")
    public AmountChangeListener getAmountChangeListener() {
        return amountChangeListener;
    }

    public void setAmountChangeListener(AmountChangeListener amountChangeListener) {
        this.amountChangeListener = amountChangeListener;
    }

    public void setAmount(BigDecimal amount) {
        amountString.set(BalanceFormatter.formatBalance2Decimals(amount));
    }

    public interface CurrencyClickListener {
        void onClick(CurrencyViewModel model, int position);
    }

    public interface AmountChangeListener{
        void onAmountChanged(CurrencyViewModel model);
    }
}
