package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.BR;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.resources.ResourceRepository;
import com.vm.revoluttest.ui.base.BaseCallback;
import com.vm.revoluttest.ui.base.BaseInteractor;
import com.vm.revoluttest.ui.currency_exchange.currency_list_data.CurrencyViewModel;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

@SuppressWarnings("WeakerAccess")
public class CurrencyListViewModel extends BaseObservable {
    public static final String ESCAPE = "\\";
    private List<CurrencyViewModel> currencyViewModelList = new ArrayList<>();
    private CurrencyInteractor interactor;
    private int clickPosition = 0;
    public ObservableField<Boolean> showLoading = new ObservableField<>();
    public CurrencyViewModel baseCurrencyViewModel;

    private BigDecimal baseAmount = new BigDecimal(100);
    private ResourceRepository resourceRepository;

    public CurrencyListViewModel(CurrencyInteractor interactor, ResourceRepository resourceRepository) {
        this.interactor = interactor;
        this.resourceRepository = resourceRepository;
        baseCurrencyViewModel = makeCurrencyViewModel("EUR", baseAmount, BigDecimal.ONE);
        baseCurrencyViewModel.isEditable.set(true);
        baseCurrencyViewModel.isEnabled.set(true);
        baseCurrencyViewModel.setAmountChangeListener(model -> {
                    String amountString = model.amountString.get() != null ? model.amountString.get() : "0.0";
                    //checked in amount string
                    //noinspection ConstantConditions
                    char separator =
                            DecimalFormatSymbols.getInstance().getDecimalSeparator();
                    baseAmount = new BigDecimal(amountString
                            .replaceAll(
                                    "[^\\d" + separator + "]",
                                    "")
                            .replaceAll(ESCAPE + separator + "", "."));

                    for (int i = 1; i < currencyViewModelList.size(); i++) {
                        currencyViewModelList
                                .get(i)
                                .setAmount(baseAmount
                                        .multiply(currencyViewModelList.get(i).getRate()));
                    }
                    notifyPropertyChanged(BR.currencyViewModelList);
                }
        );
        showLoading.set(true);
    }

    private CurrencyViewModel makeCurrencyViewModel(String currencyName) {
        CurrencyViewModel currencyViewModel = new CurrencyViewModel();
        currencyViewModel.shortCurrencyName.set(currencyName);
        currencyViewModel.fullCurrencyName.set(resourceRepository.getStringByName(currencyName));
        currencyViewModel.resourceId.set(resourceRepository.getDrawableIdByName(
                currencyName.toLowerCase()));
        return currencyViewModel;
    }

    private CurrencyViewModel makeCurrencyViewModel(String currencyName,
                                                    BigDecimal amount, BigDecimal rate) {
        CurrencyViewModel currencyViewModel = makeCurrencyViewModel(currencyName);
        currencyViewModel.setAmount(amount);
        currencyViewModel.setRate(rate);
        return currencyViewModel;
    }

    @Bindable
    public List<CurrencyViewModel> getCurrencyViewModelList() {
        return currencyViewModelList;
    }

    public int getClickPosition() {
        return clickPosition;
    }


    void onStart() {
        ((BaseInteractor) interactor).onStart();
        showLoading.set(true);
        interactor.getRatesPeriodically(new BaseCallback<CurrencyRates>() {
            @Override
            public void onNext(CurrencyRates currencyRates) {
                showLoading.set(false);
                currencyViewModelList.clear();
                currencyViewModelList.add(baseCurrencyViewModel);
                currencyRates.getRates().forEach((currencyName, value) -> {
                    CurrencyViewModel c = makeCurrencyViewModel(currencyName,
                            value.multiply(baseAmount), value);

                    c.setClickListener((model, position) -> {
                        clickPosition = position;
                        showLoading.set(true);
                        baseCurrencyViewModel.fullCurrencyName.set(model.fullCurrencyName.get());
                        baseCurrencyViewModel.shortCurrencyName.set(model.shortCurrencyName.get());
                        baseCurrencyViewModel.amountString.set(model.amountString.get());
                        baseCurrencyViewModel.resourceId.set(model.resourceId.get());
                        baseCurrencyViewModel.notifyChange();
                        String baseAmountString = model.amountString.get();
                        char separator =
                                DecimalFormatSymbols.getInstance().getDecimalSeparator();
                        if (baseAmountString == null) {
                            baseAmount = BigDecimal.ZERO;
                        } else {
                            baseAmount = new BigDecimal(
                                    baseAmountString.replaceAll(
                                            "[^\\d" + separator + "]",
                                            "")
                                            .replaceAll(ESCAPE + separator + "", "."));
                        }
                        currencyViewModelList.forEach(s -> {
                            s.setClickListener(null);
                            if (!s.equals(baseCurrencyViewModel)) {
                                s.isEnabled.set(false);
                            }
                        });
                        interactor.changeBaseCurrency(model.shortCurrencyName.get());

                    });
                    c.isEditable.set(false);
                    c.isEnabled.set(true);
                    currencyViewModelList.add(c);
                });
                notifyPropertyChanged(BR.currencyViewModelList);
            }

            @Override
            public void onError(Throwable t) {
                showLoading.set(true);
                interactor.changeBaseCurrency(baseCurrencyViewModel.shortCurrencyName.get());
                currencyViewModelList.forEach(s -> {
                    s.setClickListener(null);
                    if (!s.equals(baseCurrencyViewModel)) {
                        s.isEnabled.set(false);
                    }
                });
            }
        }, baseCurrencyViewModel.shortCurrencyName.get());
    }

    void onStop() {
        ((BaseInteractor) interactor).onStop();
    }


}
