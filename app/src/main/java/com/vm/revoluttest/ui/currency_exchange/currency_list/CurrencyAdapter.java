package com.vm.revoluttest.ui.currency_exchange.currency_list;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.vm.revoluttest.R;
import com.vm.revoluttest.ui.utils.BalanceFormatter;
import com.vm.revoluttest.ui.utils.BalanceFormatterWatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {
    private List<CurrencyRatesUi> rates = new ArrayList<>();
    private AmountChangedListener amountChangedListener;
    private String prefix;

    public CurrencyAdapter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_item, parent, false);
        return new CurrencyViewHolder(v, prefix);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        CurrencyRatesUi rate = rates.get(holder.getAdapterPosition() % rates.size());
        holder.currencyName.setText(rate.getBaseCurrency().getCurrencyCode());

        if(holder.hadFocus){
            holder.currencyRate.setVisibility(View.INVISIBLE);
        } else {
            holder.currencyRate.setVisibility(View.VISIBLE);
        }

        String currencyRate = "1 " +
                rate.getExchangeCurrency().getSymbol() +
                " = " +
                BalanceFormatter.formatBalance4Decimals(rate.getRate()) +
                " " + rate.getBaseCurrency().getSymbol();

        if (rate.getRate().compareTo(BigDecimal.ZERO) == 0) {
            holder.currencyRate.setVisibility(View.INVISIBLE);
        } else {
            holder.currencyRate.setVisibility(View.VISIBLE);
        }
        holder.currencyRate.setText(currencyRate);

        String availableFunds = holder.availableFunds.getContext().getString(
                R.string.you_have_0) + rate.getBaseCurrency().getSymbol();
        holder.availableFunds.setText(availableFunds);


        if (!holder.amount.hasFocus()) {
            holder.amount.removeTextChangedListener(holder);
            holder.amount.setText(BalanceFormatter.formatBalance2Decimals(rate.getAmount()));
            holder.amount.addTextChangedListener(holder);
        }

        holder.setAmountChangedListener(amountChangedListener);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public void setRates(List<CurrencyRatesUi> rates) {
        this.rates = rates;
    }

    public void setAmountChangedListener(AmountChangedListener amountChangedListener) {
        this.amountChangedListener = amountChangedListener;
    }

    @SuppressWarnings("WeakerAccess")
    public static class CurrencyViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
        EditText amount;
        TextView currencyName;
        TextView availableFunds;
        TextView currencyRate;
        AmountChangedListener amountChangedListener;
        private boolean hadFocus = false;
        BalanceFormatterWatcher balanceFormatterWatcher;

        CurrencyViewHolder(View itemView, String prefix) {
            super(itemView);
            amount = (EditText) itemView.findViewById(R.id.amount);
            balanceFormatterWatcher = new BalanceFormatterWatcher(prefix);
            amount.addTextChangedListener(balanceFormatterWatcher);
            amount.addTextChangedListener(this);
            amount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
            currencyName = (TextView) itemView.findViewById(R.id.currencyName);
            availableFunds = (TextView) itemView.findViewById(R.id.availableFunds);
            currencyRate = (TextView) itemView.findViewById(R.id.currentRate);
            amount.setOnFocusChangeListener((v, hasFocus) -> {
                hadFocus = hasFocus;
                if(hasFocus){
                    currencyRate.setVisibility(View.INVISIBLE);
                } else {
                    currencyRate.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (amountChangedListener != null)
                amountChangedListener.onAmountChanged(s.toString(), getAdapterPosition());
        }

        public void setAmountChangedListener(AmountChangedListener amountChangedListener) {
            this.amountChangedListener = amountChangedListener;
        }
    }

    public interface AmountChangedListener {
        void onAmountChanged(String value, int position);
    }
}
