package com.wdk.sports.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CountSportData implements Serializable {

    private List<Count> count ;
    private List<SportDataAddId> list;

    @Override
    public String toString() {
        return "CountSportData{" +
                "count=" + count +
                ", list=" + list +
                '}';
    }

    public List<Count> getCount() {
        return count;
    }

    public void setCount(List<Count> count) {
        this.count = count;
    }

    public List<SportDataAddId> getList() {
        return list;
    }

    public void setList(List<SportDataAddId> list) {
        this.list = list;
    }

    public CountSportData(List<Count> count, List<SportDataAddId> list) {
        this.count = count;
        this.list = list;
    }

    public CountSportData() {
    }
}
