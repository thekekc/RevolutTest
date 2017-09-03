package com.vm.revoluttest.ui.currency_exchange;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vm.revoluttest.AppContainer;
import com.vm.revoluttest.R;
import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.ui.currency_exchange.currency_list.CurrencyAdapter;
import com.vm.revoluttest.ui.currency_exchange.currency_list.CurrencyRatesUi;
import com.vm.revoluttest.ui.utils.DotsIndicator;
import com.vm.revoluttest.ui.utils.SingleItemScrollSnapHelper;

import java.util.List;

public class CurrencyExchangeActivity extends AppCompatActivity implements CurrencyExchangeView {

    private RecyclerView baseRates;
    private CurrencyAdapter baseRatesAdapter;
    private CurrencyAdapter exchangeRatesAdapter;
    private RecyclerView exchangeRates;
    private TextView toolbarRates;
    private Snackbar snackbar;

    private CurrencyExchangePresenter currencyExchangePresenter;
    private int lastBasePosition = 0;
    private int lastExchangePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_exchange);
        init();
        AppContainer appContainer = (AppContainer) getApplication();
        CurrencyInteractor interactor = new CurrencyInteractor(appContainer.getCurrencyRepository());
        currencyExchangePresenter = new CurrencyExchangePresenterImpl(interactor, this);
    }

    private void init() {
        toolbarRates = (TextView) findViewById(R.id.toolbarRate);

        baseRates = (RecyclerView) findViewById(R.id.baseRates);
        exchangeRates = (RecyclerView) findViewById(R.id.exchangeRates);

        baseRatesAdapter = new CurrencyAdapter("-");
        baseRates.setAdapter(baseRatesAdapter);
        baseRates.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper baseSnapHelper = new SingleItemScrollSnapHelper();
        baseSnapHelper.attachToRecyclerView(baseRates);
        baseRates.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    int position = layoutManager.findFirstVisibleItemPosition();
                    if(position!=lastBasePosition) {
                        currencyExchangePresenter.onBaseCurrencyScroll(position);
                        lastBasePosition = position;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        baseRatesAdapter.setAmountChangedListener((value, position) ->
                currencyExchangePresenter.onBaseAmountEntered(value, position));
        baseRates.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);
        lastBasePosition = Integer.MAX_VALUE / 2;
        DotsIndicator dotsBase = (DotsIndicator) findViewById(R.id.dotsBase);
        dotsBase.attach(baseRates);
        dotsBase.init(Currency.values().length);


        exchangeRatesAdapter = new CurrencyAdapter("+");
        exchangeRates.setAdapter(exchangeRatesAdapter);
        exchangeRates.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        exchangeRates.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    int position = layoutManager.findFirstVisibleItemPosition();
                    if(position!=lastExchangePosition) {
                        currencyExchangePresenter.onExchangeScroll(position);
                        lastExchangePosition = position;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        exchangeRatesAdapter.setAmountChangedListener((value, position) ->
                currencyExchangePresenter.onExchangeAmountEntered(value, position));
        SnapHelper exchangeSnapHelper = new SingleItemScrollSnapHelper();
        exchangeSnapHelper.attachToRecyclerView(exchangeRates);
        exchangeRates.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);
        lastExchangePosition = Integer.MAX_VALUE / 2;

        DotsIndicator dotsExchange = (DotsIndicator) findViewById(R.id.dotsExchange);
        dotsExchange.attach(exchangeRates);
        dotsExchange.init(Currency.values().length);

        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        currencyExchangePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        currencyExchangePresenter.onStop();
    }


    @Override
    public void setToolbarRate(String string) {
        toolbarRates.setText(string);
    }

    @Override
    public void setBaseRates(List<CurrencyRatesUi> list) {
        baseRatesAdapter.setRates(list);
        baseRatesAdapter.notifyDataSetChanged();
    }

    @Override
    public void setExchangeRates(List<CurrencyRatesUi> list) {
        exchangeRatesAdapter.setRates(list);
        exchangeRatesAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateBaseRates(List<CurrencyRatesUi> list) {
        baseRatesAdapter.setRates(list);
        int pos = ((LinearLayoutManager) baseRates.getLayoutManager()).findFirstVisibleItemPosition();
        baseRatesAdapter.notifyItemRangeChanged(pos - list.size()*3, list.size()*6);
    }

    @Override
    public void updateExchangeRates(List<CurrencyRatesUi> list) {
        exchangeRatesAdapter.setRates(list);
        int pos = ((LinearLayoutManager) exchangeRates.getLayoutManager()).findFirstVisibleItemPosition();
        exchangeRatesAdapter.notifyItemRangeChanged(pos - list.size()*3, list.size()*6);
    }

    @Override
    public void showReconnect() {
        if (snackbar == null) {
            snackbar = Snackbar.make(findViewById(android.R.id.content), "Reconnecting", Snackbar.LENGTH_INDEFINITE);
            Snackbar.SnackbarLayout snackView = (Snackbar.SnackbarLayout) snackbar.getView();
            snackView.addView(new ProgressBar(this));
        }
        snackbar.show();
    }

    @Override
    public void hideReconnect() {
        if (snackbar != null) snackbar.dismiss();
    }
}
