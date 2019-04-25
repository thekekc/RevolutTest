package com.vm.revoluttest.ui.currency_exchange.currency_list_data;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vm.revoluttest.databinding.CurrencyExchangeItemBinding;
import com.vm.revoluttest.ui.utils.BalanceFormatterWatcher;
import com.vm.revoluttest.ui.utils.KeyboardUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.CurrencyHolder> {

    private List<CurrencyViewModel> items;

    public CurrencyListAdapter(List<CurrencyViewModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CurrencyExchangeItemBinding binding = CurrencyExchangeItemBinding
                .inflate(inflater, parent, false);
        return new CurrencyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        holder.bind(items.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (items.get(position).getClickListener() != null) {
                items.get(position).getClickListener().onClick(items.get(position), position);
            }
        });
        if (position == 0 && !holder.binding.amount.hasFocus()) {
            holder.binding.amount.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CurrencyHolder extends RecyclerView.ViewHolder {
        private CurrencyExchangeItemBinding binding;

        CurrencyHolder(CurrencyExchangeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.amount.addTextChangedListener(new BalanceFormatterWatcher(""));
            binding.amount.setOnClickListener(v -> {
                ((View) v.getParent()).performClick();
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                KeyboardUtils.showKeyboard(binding.amount.getContext(), binding.amount);
            });
            binding.amount.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    binding.amount.setSelection(binding.amount.getText().length());
                } else {
                    KeyboardUtils.hideKeyboard(binding.amount.getContext(), binding.amount);
                }
            });
            binding.amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = CurrencyHolder.this.getAdapterPosition();
                    if (position == 0 && items.get(position).getAmountChangeListener() != null &&
                            CurrencyHolder.this.binding.amount.hasFocus()) {
                        items.get(position).getAmountChangeListener()
                                .onAmountChanged(items.get(position));
                    }
                }
            });

        }

        void bind(CurrencyViewModel viewModel) {
            binding.setCurrency(viewModel);
            binding.executePendingBindings();
        }
    }
}
