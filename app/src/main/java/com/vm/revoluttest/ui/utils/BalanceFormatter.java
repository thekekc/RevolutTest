package com.vm.revoluttest.ui.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BalanceFormatter {
    public static String formatBalance4Decimals(BigDecimal balance) {
        balance = balance.setScale(4, BigDecimal.ROUND_HALF_UP);
        return format(balance, 4);
    }

    public static String formatBalance2Decimals(BigDecimal balance) {
        balance = balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        return format(balance, 2);
    }

    private static String format(BigDecimal bd, int decimals) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimals);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(bd);
    }

    public static String formatBalance(BigDecimal balance, String formatString){
        if(balance==null) return "";
        DecimalFormat format = new DecimalFormat(formatString);
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator('.');
        customSymbol.setGroupingSeparator(' ');
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setDecimalFormatSymbols(customSymbol);
        return format.format(balance);
    }
}
