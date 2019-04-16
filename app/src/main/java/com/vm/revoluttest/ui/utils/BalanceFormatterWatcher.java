package com.vm.revoluttest.ui.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;


public class BalanceFormatterWatcher implements TextWatcher {
    private boolean selfChanged = false;
    private String prefix;
    private RelativeSizeSpan sixDigitSizeSpan = new RelativeSizeSpan(0.8f);
    private RelativeSizeSpan decimalSizeSpan = new RelativeSizeSpan(0.8f);
    private boolean initialZero = false;
    private boolean prefixApplied = false;
    private boolean prefixWasApplied = false;

    public BalanceFormatterWatcher(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        initialZero = s.toString().equals("0");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (selfChanged) return;
        //prevent from self update
        selfChanged = true;
        //removing every special symbols
        String str = s.toString().replaceAll(",", ".");
        str = str.replaceAll("[^\\d.]", "");
        //check if it contains only one point
        if (str.isEmpty()) {
            selfChanged = false;
            s.append('0');
            Selection.setSelection(s, 1);
            return;
        }

        if (initialZero && str.endsWith("0")) {
            str = str.replace("0", "");
        }

        if (str.matches("^[0-9]*\\.?[0-9]*$")) {
            String digitString = format(str);
            if (str.isEmpty()) {
                selfChanged = false;
                s.append('0');
                Selection.setSelection(s, 1);
                return;
            }
            s.replace(0, s.length(), digitString);
            //if applying prefix move cursor
            if (!prefixWasApplied && prefixApplied) {
                Selection.setSelection(s, Selection.getSelectionStart(s) + 1 < s.length() ?
                        Selection.getSelectionStart(s) + 1 :
                        s.length());
            }
        } else {
            StringBuilder b = new StringBuilder(str);
            b.replace(str.lastIndexOf("."), str.lastIndexOf(".") + 1, "");
            str = b.toString();
            String digitString = format(str);
            s.replace(0, s.length(), digitString);
            if (!prefixWasApplied && prefixApplied) {
                Selection.setSelection(s, Selection.getSelectionStart(s) + 1 < s.length() ?
                        Selection.getSelectionStart(s) + 1 :
                        s.length());
            }
        }
        //changeSpan(s);
        selfChanged = false;
    }

    private void changeSpan(Editable s){
        s.removeSpan(sixDigitSizeSpan);
        if (s.length() > 6) {
            s.setSpan(sixDigitSizeSpan, 0, s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        s.removeSpan(decimalSizeSpan);
        String result = s.toString();
        if (result.contains(".")) {
            int startPos = result.lastIndexOf(".");
            s.setSpan(decimalSizeSpan, startPos, s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
    }

    private String format(String str) {
        prefixWasApplied = prefixApplied;
        prefixApplied = false;
        BigDecimal value;
        if (str.isEmpty()) {
            return "0";
        } else if (str.length() == 1 && str.contains(".")) {
            value = BigDecimal.valueOf(0.0d);
        } else if (str.startsWith(".")) {
            return str;
        } else {
            value = new BigDecimal(str);
        }
        String digitString = BalanceFormatter.formatBalance(value, "#.##");
        if (str.charAt(str.length() - 1) == '.') {
            return digitString + ".";
        }
        if (str.endsWith(".0")) {
            return digitString + ".0";
        }
        if (str.contains(".00")) {
            return digitString + ".00";
        }
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return digitString;
        }
        prefixApplied = true;
        return prefix + digitString;
    }

}
