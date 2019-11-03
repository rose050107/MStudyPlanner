package com.gmail.mstudyplanner;

public class Dday {

    String title;
    String ddaydate;

    public Dday(String title, String ddaydate) {
        this.title = title;
        this.ddaydate = ddaydate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDdaydate() {
        return ddaydate;
    }

    public void setDdaydate(String ddaydate) {
        this.ddaydate = ddaydate;
    }
}
