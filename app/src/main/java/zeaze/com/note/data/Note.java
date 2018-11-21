package zeaze.com.note.data;

import java.util.Date;
import java.util.List;

public class Note {
    String note;
    Date creatAt,deletedAt;
    boolean isDeleted,isRemind;

    List<Weather> weatherList;
    List<Cycle> cycleList;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Date creatAt) {
        this.creatAt = creatAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public List<Cycle> getCycleList() {
        return cycleList;
    }

    public void setCycleList(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }
}