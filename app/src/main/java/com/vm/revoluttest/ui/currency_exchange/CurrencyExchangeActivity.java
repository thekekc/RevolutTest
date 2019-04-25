package com.vm.revoluttest.ui.currency_exchange;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.vm.revoluttest.AppContainer;
import com.vm.revoluttest.R;
import com.vm.revoluttest.databinding.CurrencyExchangeActBinding;
import com.vm.revoluttest.ui.base.ViewModelHolder;
import com.vm.revoluttest.ui.currency_exchange.currency_list_data.CurrencyListAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class CurrencyExchangeActivity extends Activity {

    public static final String VIEW_MODEL_HOLDER = "VIEW_MODEL_HOLDER";
    private CurrencyExchangeActBinding binding;
    private Observable.OnPropertyChangedCallback currenciesUpdateCallback;
    private Observable.OnPropertyChangedCallback baseCurrencyChangeCallback;
    private Observable.OnPropertyChangedCallback snackbarCallback;
    private CurrencyListViewModel viewModel;
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.currency_exchange_act);
        viewModel = findOrCreateViewModel();
        binding.setCurrencyList(viewModel);
        setupCurrencyList();
        setupSnackbar();
    }

    private void setupCurrencyList() {

        CurrencyListAdapter adapter = new CurrencyListAdapter(viewModel.getCurrencyViewModelList());
        final LinearLayoutManager manager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        binding.currencyList.setLayoutManager(manager);
        binding.currencyList.setAdapter(adapter);

        //turning off item change animation
        SimpleItemAnimator animator = (SimpleItemAnimator) binding.currencyList.getItemAnimator();
        if (animator != null) animator.setSupportsChangeAnimations(false);
        adapter.notifyDataSetChanged();

        currenciesUpdateCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (binding.currencyList.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                        !binding.currencyList.isComputingLayout()) {
                    adapter.notifyItemRangeChanged(1,
                            viewModel.getCurrencyViewModelList().size() - 1);
                }
            }
        };
        viewModel.addOnPropertyChangedCallback(currenciesUpdateCallback);

        baseCurrencyChangeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.notifyItemMoved(viewModel.getClickPosition(), 0);
                binding.currencyList.scrollToPosition(0);
                adapter.notifyItemRangeChanged(0, viewModel.getClickPosition() + 1);
            }
        };
        viewModel.baseCurrencyViewModel.addOnPropertyChangedCallback(baseCurrencyChangeCallback);
    }

    private void setupSnackbar() {
        snackbar = Snackbar.make(binding.getRoot(),
                getString(R.string.reloading), Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackView.addView(new ProgressBar(CurrencyExchangeActivity.this));
        snackbar.show();
        snackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Boolean showLoading = viewModel.showLoading.get();
                if (showLoading != null && showLoading) {
                    snackbar.show();
                } else {
                    if (snackbar != null) snackbar.dismiss();
                }
            }
        };
        viewModel.showLoading.addOnPropertyChangedCallback(snackbarCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeOnPropertyChangedCallback(currenciesUpdateCallback);
        viewModel.baseCurrencyViewModel.removeOnPropertyChangedCallback(baseCurrencyChangeCallback);
        viewModel.showLoading.removeOnPropertyChangedCallback(snackbarCallback);
    }

    private CurrencyListViewModel findOrCreateViewModel() {
        FragmentManager fm = getFragmentManager();

        //returning new vm if something goes wrong
        if (fm == null) return new CurrencyListViewModel(
                new CurrencyInteractorImpl(
                        ((AppContainer) getApplication()).getCurrencyRepository()),
                ((AppContainer) getApplication()).getResourceRepository());

        //adding only view model holder of current model to fragment manager
        @SuppressWarnings("unchecked")
        ViewModelHolder<CurrencyListViewModel> retainedViewModel =
                (ViewModelHolder<CurrencyListViewModel>) fm.findFragmentByTag(VIEW_MODEL_HOLDER);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            return retainedViewModel.getViewmodel();
        } else {
            CurrencyListViewModel viewModel = new CurrencyListViewModel(
                    new CurrencyInteractorImpl(
                            ((AppContainer) getApplication()).getCurrencyRepository()),
                    ((AppContainer) getApplication()).getResourceRepository());
            fm.beginTransaction()
                    .add(ViewModelHolder.createContainer(viewModel), VIEW_MODEL_HOLDER).commit();
            return viewModel;
        }
    }

}
