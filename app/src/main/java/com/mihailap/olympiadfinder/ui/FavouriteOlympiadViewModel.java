package com.mihailap.olympiadfinder.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.data.OlympiadDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteOlympiadViewModel extends AndroidViewModel {
    private final OlympiadDatabase database;
    private final MutableLiveData<List<Olympiad>> olympiads = new MutableLiveData<>();
    private final MutableLiveData<List<Olympiad>> filteredOlympiads = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public FavouriteOlympiadViewModel(@NonNull Application application) {
        super(application);
        database = OlympiadDatabase.newInstance(application);
    }

    public LiveData<List<Olympiad>> getOlympiads() {
        return olympiads;
    }

    public LiveData<List<Olympiad>> getFilteredOlympiads() {
        return filteredOlympiads;
    }


    public void refreshList() {
        Disposable disposable = database.olympiadDao()
                .getFavouriteOlympiads()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Olympiad>>() {
                    @Override
                    public void accept(List<Olympiad> olympiadDB) throws Throwable {
                        olympiads.setValue(olympiadDB);
                    }
                });
        compositeDisposable.add(disposable);
    }

    // Methods for filtering
    public void filter(String text, List<Integer> selectedGrades) {
        List<Olympiad> filteredList = new ArrayList<>();
        List<Olympiad> tempList = getOlympiads().getValue();
        if (tempList != null) {
            for (Olympiad item : tempList) {
                if ((item.getKeywords().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getSubject().toLowerCase().contains(text.toLowerCase()))
                        && containsAllGrades(item.getGradesList(), selectedGrades)) {
                    filteredList.add(item);
                }
            }
        }
        // updating LiveData with filtered list of olympiads
        filteredOlympiads.setValue(filteredList);
    }

    private boolean containsAllGrades(String gradesList, List<Integer> selectedGrades) {
        // Splitting grades string to array
        String[] gradesArray = gradesList.split(" ");
        for (int grade : selectedGrades) {
            String gradeString = String.valueOf(grade);
            boolean found = false;
            for (String word : gradesArray) {
                if (word.equals(gradeString)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }


}
