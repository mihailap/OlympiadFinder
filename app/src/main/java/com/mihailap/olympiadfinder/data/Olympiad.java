package com.mihailap.olympiadfinder.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Olympiad")
public class Olympiad {
    // Variables
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    // Main
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "gradeRange")
    private String gradeRange;
    // About
    @ColumnInfo(name = "bvi")
    private Boolean bvi;
    @ColumnInfo(name = "rsoshLevel")
    private int rsoshLevel;
    @ColumnInfo(name = "url")
    private String url;

    @Ignore
    public Olympiad () {}

    public Olympiad(String name, String subject, String gradeRange, Boolean bvi, int rsoshLevel, String url) {
        this.name = name;
        this.subject = subject;
        this.gradeRange = gradeRange;
        this.bvi = bvi;
        this.rsoshLevel = rsoshLevel;
        this.url = url;
        this.id = 0;
    }

    // Getters
    public int getId() { return id; }

    public String getName() { return name; }

    public String getSubject() { return subject; }

    public String getGradeRange() { return gradeRange; }

    public Boolean getBvi() { return bvi; }

    public int getRsoshLevel() { return rsoshLevel; }

    public String getUrl() { return url; }

    // Setters

    public void setId(int id) { this.id = id; }
}
