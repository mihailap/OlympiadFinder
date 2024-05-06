package com.mihailap.olympiadfinder.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "Olympiad")
public class Olympiad implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "gradeRange")
    private String gradeRange;
    @ColumnInfo(name = "bvi")
    private Boolean bvi;
    @ColumnInfo(name = "rsoshLevel")
    private int rsoshLevel;
    @ColumnInfo(name = "url")
    private String url;

    @Ignore
    public Olympiad() {
    }

    public Olympiad(int id, String name, String subject, String gradeRange, Boolean bvi, int rsoshLevel, String url) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.gradeRange = gradeRange;
        this.bvi = bvi;
        this.rsoshLevel = rsoshLevel;
        this.url = url;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getGradeRange() {
        return gradeRange;
    }

    public Boolean getBvi() {
        return bvi;
    }

    public int getRsoshLevel() {
        return rsoshLevel;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Olympiad olympiad = (Olympiad) o;
        return id == olympiad.id && rsoshLevel == olympiad.rsoshLevel && Objects.equals(name, olympiad.name) && Objects.equals(subject, olympiad.subject) && Objects.equals(gradeRange, olympiad.gradeRange) && Objects.equals(bvi, olympiad.bvi) && Objects.equals(url, olympiad.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subject, gradeRange, bvi, rsoshLevel, url);
    }
}
