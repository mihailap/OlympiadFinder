package com.mihailap.olympiadfinder.ui;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.data.OlympiadDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final OlympiadDatabase database;
    private final MutableLiveData<List<Olympiad>> olympiads = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean isDataLoaded = false;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = OlympiadDatabase.newInstance(application);
    }

    public LiveData<List<Olympiad>> getOlympiads() {
        return olympiads;
    }


    public void refreshList() {
        Disposable disposable = database.olympiadDao()
                .getOlympiads()
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



    /* Method to add olympiads manually
    public void saveOlympiad(Olympiad olympiad) {
        Disposable disposable = database.olympiadDao().insertOlympiad(olympiad)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d("TEST", "ID = " + olympiad.getId() + " NAME = " + olympiad.getName());
                    }
                });
        compositeDisposable.add(disposable);
    }
    */

    public void parseOlympiads() {
        if (!isDataLoaded) {
            Toast.makeText(this.getApplication(), "PARSING", Toast.LENGTH_SHORT).show();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 70; i < 80; i++) {
                        try {
                            Document document = Jsoup.connect("https://olimpiada.ru/activity/" + i).get();
                            String name = document.select("div.top_container > h1").text();
                            String subject = document.select("div.subject_tags_full > span.subject_tag").text();
                            String grade = document.select("span.classes_types_a").text();
                            // Get stages + dates
                            Elements rows = document.select("table.events_for_activity tr.grey");
                            StringBuilder stagesBuilder = new StringBuilder();
                            StringBuilder datesBuilder = new StringBuilder();
                            for (Element row : rows) {
                                stagesBuilder.append(row.select("td:eq(0) div.event_name").text()).append(",");
                                datesBuilder.append(row.select("td:eq(1) a").text()).append(",");
                            }
                            String stages = stagesBuilder.toString().replaceAll(",$", "");
                            String dates = datesBuilder.toString().replaceAll(",$", "");
                            String url = document.select("div.contacts > a.color").attr("href");
                            String description = document.select("meta[name = description]").attr("content");
                            String keywords = document.select("meta[name = keywords]").attr("content");

                            Olympiad olympiad = new Olympiad(i, name, subject, grade, stages, dates, url, description, keywords);

                            Disposable disposable = database.olympiadDao().insertOlympiad(olympiad)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action() {
                                        @Override
                                        public void run() throws Throwable {
                                            Log.d("TEST", "ID = " + olympiad.getId() + " NAME = " + olympiad.getName());
                                        }
                                    });
                            compositeDisposable.add(disposable);
                            Log.d("PARSE", "PARSED SUCCESSFULLY");
                            refreshList();
                        } catch (IOException e) {
                            Log.d("PARSE", "PARSE ERROR: " + e.getMessage());
                        }
                    }

                }
            });
            thread.start();
            Log.d("PARSE", "STOPPED PARSING");
            isDataLoaded = true;
        }
    }


    /*
    public void clearTable(int id) {
        Disposable disposable = database.olympiadDao().remove(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void clearAll() {
        Disposable disposable = database.olympiadDao().removeAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                });
        compositeDisposable.add(disposable);
    }
    */

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
