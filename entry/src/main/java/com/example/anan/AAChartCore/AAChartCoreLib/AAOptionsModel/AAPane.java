package com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel;

import java.util.Arrays;

public class AAPane {
    public AABackground background;
    public Object[] center;
    public Float endAngle;
    public Float size;
    public Float startAngle;

    public AAPane background(AABackground prop) {
        background = prop;
        return this;
    }

    public AAPane center(Object[] prop) {
        center = Arrays.asList(prop).toArray();
        return this;
    }

    public AAPane endAngle(Float prop) {
        endAngle = prop;
        return this;
    }

    public AAPane size(Float prop) {
        size = prop;
        return this;
    }

    public AAPane startAngle(Float prop) {
        startAngle = prop;
        return this;
    }
}
