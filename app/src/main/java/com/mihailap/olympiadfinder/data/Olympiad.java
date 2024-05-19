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
    @ColumnInfo(name = "stages")
    private String stages;
    @ColumnInfo(name = "dates")
    private String dates;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "keywords")
    private String keywords;
    @ColumnInfo(name = "gradesList")
    private String gradesList;
    @ColumnInfo(name = "isTech")
    private Boolean isTech;
    @ColumnInfo(name = "isNature")
    private Boolean isNature;
    @ColumnInfo(name = "isHuman")
    private Boolean isHuman;


    @Ignore
    public Olympiad() {
    }

    public Olympiad(int id,
                    String name,
                    String subject,
                    String gradeRange,
                    String stages,
                    String dates,
                    String url,
                    String description,
                    String keywords,
                    String gradesList,
                    Boolean isTech,
                    Boolean isNature,
                    Boolean isHuman) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.gradeRange = gradeRange;
        this.stages = stages;
        this.dates = dates;
        this.url = url;
        this.description = description;
        this.keywords = keywords;
        this.gradesList = gradesList;
        this.isTech = isTech;
        this.isNature = isNature;
        this.isHuman = isHuman;
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

    public String getUrl() {
        return url;
    }

    public String getStages() {
        return stages;
    }

    public String getDates() {
        return dates;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getGradesList() {
        return gradesList;
    }

    public Boolean getTech() {
        return isTech;
    }

    public Boolean getNature() {
        return isNature;
    }

    public Boolean getHuman() {
        return isHuman;
    }

    @Override
    public String toString() {
        return "Olympiad{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", gradeRange='" + gradeRange + '\'' +
                ", stages='" + stages + '\'' +
                ", dates='" + dates + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", keywords='" + keywords + '\'' +
                ", gradesList='" + gradesList + '\'' +
                ", isTech=" + isTech +
                ", isNature=" + isNature +
                ", isHuman=" + isHuman +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Olympiad olympiad = (Olympiad) o;
        return id == olympiad.id && Objects.equals(name, olympiad.name) && Objects.equals(subject, olympiad.subject) && Objects.equals(gradeRange, olympiad.gradeRange) && Objects.equals(stages, olympiad.stages) && Objects.equals(dates, olympiad.dates) && Objects.equals(url, olympiad.url) && Objects.equals(description, olympiad.description) && Objects.equals(keywords, olympiad.keywords) && Objects.equals(gradesList, olympiad.gradesList) && Objects.equals(isTech, olympiad.isTech) && Objects.equals(isNature, olympiad.isNature) && Objects.equals(isHuman, olympiad.isHuman);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subject, gradeRange, stages, dates, url, description, keywords, gradesList, isTech, isNature, isHuman);
    }
}
