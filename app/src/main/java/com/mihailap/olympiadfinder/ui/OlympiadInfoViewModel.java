package com.mihailap.olympiadfinder.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mihailap.olympiadfinder.data.OlympiadDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OlympiadInfoViewModel extends AndroidViewModel {
    private final OlympiadDatabase database;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> isFavNow = new MutableLiveData<>();

    public OlympiadInfoViewModel(@NonNull Application application) {
        super(application);
        database = OlympiadDatabase.newInstance(application);
    }

    public LiveData<Boolean> getIsFav() {
        return isFavNow;
    }

    public void getFavouriteStatus(int id, Boolean isFirst) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    isFavNow.postValue(database.olympiadDao().getFavouriteStatus(id));
                } else {
                    isFavNow.postValue(!database.olympiadDao().getFavouriteStatus(id));
                    updateFavouriteStatus(id, !database.olympiadDao().getFavouriteStatus(id));
                }
            }
        });
        thread.start();
    }


    public void updateFavouriteStatus(int olympiadId, Boolean isFavourite) {
        Disposable disposable = database.olympiadDao().updateFavouriteStatus(olympiadId, isFavourite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                });
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
