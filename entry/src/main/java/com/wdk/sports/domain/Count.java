package com.wdk.sports.domain;

import java.io.Serializable;

public class Count implements Serializable {
    private int counts ;
    private float calories;
    private String time;

    public Count(int counts, float calories, String time) {
        this.counts = counts;
        this.calories = calories;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Count{" +
                "counts=" + counts +
                ", calories=" + calories +
                ", time='" + time + '\'' +
                '}';
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
