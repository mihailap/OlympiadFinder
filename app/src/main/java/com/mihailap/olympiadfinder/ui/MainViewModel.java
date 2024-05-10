package com.mihailap.olympiadfinder.ui;

import android.app.Application;
import android.util.Log;

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
    private final MutableLiveData<Integer> maxProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentProgressLiveData = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = OlympiadDatabase.newInstance(application);
    }

    public LiveData<List<Olympiad>> getOlympiads() {
        return olympiads;
    }

    public LiveData<Integer> getMaxProgressLiveData() {
        return maxProgressLiveData;
    }

    public LiveData<Integer> getCurrentProgressLiveData() {
        return currentProgressLiveData;
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
            Log.d("PARSE", "STARTED PARSING");
            int firstOlympId = 40 ;
            int lastOlympId = 100;
            maxProgressLiveData.setValue(lastOlympId - firstOlympId + 1);
            Log.d("PROGRESS", "MAX PROGRESS:" + (lastOlympId - firstOlympId + 1));
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = firstOlympId; i <= lastOlympId; i++) {
                        try {
                            // Loading html page
                            Document document = Jsoup.connect("https://olimpiada.ru/activity/" + i).get();
                            // Get name
                            String name = document.select("div.top_container > h1").text();
                            // Get subject + replace space with ","
                            String subject = document.select("div.subject_tags_full > span.subject_tag").text();
                            String[] subjectsArray = subject.split(" ");
                            StringBuilder resultBuilder = new StringBuilder();
                            resultBuilder.append(subjectsArray[0]);

                            for (int j = 1; j < subjectsArray.length; j++) {
                                if (Character.isLowerCase(subjectsArray[j].charAt(0))) {
                                    resultBuilder.append(" ");
                                } else {
                                    resultBuilder.append(", ");
                                }
                                resultBuilder.append(subjectsArray[j]);
                            }
                            subject = resultBuilder.toString();
                            // Get grade
                            String grade = document.select("span.classes_types_a").text();
                            // Transform range to numbers for search
                            StringBuilder gradesList = new StringBuilder();
                            if (grade.contains("–")) {
                                int dashIndex = grade.indexOf("–");
                                int start = Integer.parseInt(grade.substring(0, dashIndex));
                                int end = Integer.parseInt(grade.substring(dashIndex + 1, grade.indexOf(' ', dashIndex)));
                                for (int j = start; j <= end; j++) {
                                    gradesList.append(j).append(" ");
                                }
                            } else if (!grade.isEmpty()) {
                                gradesList = new StringBuilder(grade.substring(0, grade.indexOf(" ")));
                            } else {
                                gradesList = new StringBuilder();
                            }
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
                            // Get olympiad url
                            String url = document.select("div.contacts > a.color").attr("href");
                            // Get description
                            String description = document.select("meta[name = description]").attr("content");
                            // Get keywords
                            String keywords = document.select("meta[name = keywords]").attr("content");
                            // Creating olympiad + adding it to DB
                            Olympiad olympiad = new Olympiad(i, name, subject, grade, stages, dates, url, description, keywords, gradesList.toString());

                            Disposable disposable = database.olympiadDao().insertOlympiad(olympiad)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action() {
                                        @Override
                                        public void run() throws Throwable {
                                        }
                                    });
                            compositeDisposable.add(disposable);
                            Log.d("PARSE", "PARSED SUCCESSFULLY " + "ID = " + olympiad.getId() + " NAME = " + olympiad.getName());
                            refreshList();
                        } catch (Exception e) {
                            Log.d("PARSE", "PARSE ERROR: " + e.getMessage());
                        }
                        currentProgressLiveData.postValue(i - firstOlympId + 1);
                        Log.d("PROGRESS", String.valueOf(i - firstOlympId + 1));
                    }

                }
            });
            thread.start();
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
