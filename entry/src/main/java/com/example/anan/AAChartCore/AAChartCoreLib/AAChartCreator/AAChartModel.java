/**
 * //  AAChartModel.java
 * //  AAChartCore
 * //
 * //  Created by AnAn on 2017/9/5.
 * //  Copyright © 2018年 An An. All rights reserved.
 * <p>
 * ◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉ ...... SOURCE CODE ......◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉
 * ◉◉◉...................................................       ◉◉◉
 * ◉◉◉   https://github.com/AAChartModel/AAChartCore            ◉◉◉
 * ◉◉◉   https://github.com/AAChartModel/AAChartCore-Kotlin     ◉◉◉
 * ◉◉◉...................................................       ◉◉◉
 * ◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉ ...... SOURCE CODE ......◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉◉
 * <p>
 * <p>
 * -------------------------------------------------------------------------------
 * <p>
 * 🌕 🌖 🌗 🌘  ❀❀❀   WARM TIPS!!!   ❀❀❀ 🌑 🌒 🌓 🌔
 * <p>
 * Please contact me on GitHub,if there are any problems encountered in use.
 * GitHub Issues : https://github.com/AAChartModel/AAChartCore/issues
 * -------------------------------------------------------------------------------
 * And if you want to contribute for this project, please contact me as well
 * GitHub        : https://github.com/AAChartModel
 * StackOverflow : https://stackoverflow.com/users/7842508/codeforu
 * JianShu       : http://www.jianshu.com/u/f1e6753d4254
 * SegmentFault  : https://segmentfault.com/u/huanghunbieguan
 * <p>
 * -------------------------------------------------------------------------------
 */

package com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator;

import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartAnimationType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartStackingType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartSymbolStyleType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartZoomType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAOptions;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAScrollablePlotArea;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAStyle;

import java.util.Arrays;
import java.util.List;

public class AAChartModel {
    public String animationType;         // 动画类型
    public Integer animationDuration;     // 动画时间
    public String title;                 // 标题内容
    public AAStyle titleStyle;            // 标题文本风格样式
    public String subtitle;              // 副标题内容
    public String subtitleAlign;         // 副标题水平对齐方式
    public AAStyle subtitleStyle;         // 副标题文本风格样式
    public String axesTextColor;         // x 轴和 y 轴文字颜色
    public String chartType;             // 图表类型
    public String stacking;              // 堆积样式
    public String markerSymbol; // 折线曲线连接点的类型："circle", "square", "diamond", "triangle","triangle-down"，默认是"circle"
    public String markerSymbolStyle;     // 折线曲线连接点的自定义风格样式
    public String zoomType;              // 缩放类型 AAChartZoomTypeX表示可沿着 x 轴进行手势缩放
    public Boolean inverted;              // x 轴是否翻转(垂直)
    public Boolean xAxisReversed;         // x 轴翻转
    public Boolean yAxisReversed;         // y 轴翻转
    public Boolean tooltipEnabled;        // 是否显示浮动提示框(默认显示)
    public String tooltipValueSuffix;    // 浮动提示框单位后缀
    public Boolean gradientColorEnable;   // 是否要为渐变色
    public Boolean polar;                 // 是否极化图形(变为雷达图)
    public Float[] margin;                // 图表外边缘和绘图区域之间的边距
    public Boolean dataLabelsEnabled;     // 是否显示数据
    public AAStyle dataLabelsStyle;       // 数据文本风格样式
    public Boolean xAxisLabelsEnabled;    // x 轴是否显示数据
    public Integer xAxisTickInterval;     // x 轴刻度点间隔数(设置每隔几个点显示一个 X轴的内容)
    public String[] categories;            // x 轴是否显示数据
    public Float xAxisGridLineWidth;    // x 轴网格线的宽度
    public Boolean xAxisVisible;          // x 轴是否显示
    public Boolean yAxisVisible;          // y 轴是否显示
    public Boolean yAxisLabelsEnabled;    // y 轴是否显示数据
    public String yAxisTitle;            // y 轴标题
    public Float yAxisLineWidth;        // y 轴轴线的宽度
    public Float yAxisMin;              // y 轴最小值
    public Float yAxisMax;              // y 轴最大值
    public Boolean yAxisAllowDecimals;    // y 轴是否允许显示小数
    public Float yAxisGridLineWidth;    //y 轴网格线的宽度
    public Object[] colorsTheme;           // 图表主题颜色数组
    public Boolean legendEnabled;         // 是否显示图例
    public Object backgroundColor;       // 图表背景色
    public Float borderRadius;          // 柱状图长条图头部圆角半径(可用于设置头部的形状,仅对条形图,柱状图有效)
    public Float markerRadius;          // 折线连接点的半径长度
    public Object[] series;               // 图表的数据列内容
    public Boolean touchEventEnabled;     // 是否支持用户触摸事件
    public AAScrollablePlotArea scrollablePlotArea;


    /**
     * animationType
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel animationType(String prop) {
        animationType = prop;
        return this;
    }

    /**
     * animationDuration
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel animationDuration(Integer prop) {
        animationDuration = prop;
        return this;
    }

    /**
     * title
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel title(String prop) {
        title = prop;
        return this;
    }

    /**
     * titleStyle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel titleStyle(AAStyle prop) {
        titleStyle = prop;
        return this;
    }

    /**
     * subtitle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel subtitle(String prop) {
        subtitle = prop;
        return this;
    }

    /**
     * subtitleAlign
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel subtitleAlign(String prop) {
        subtitleAlign = prop;
        return this;
    }

    /**
     * subtitleStyle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel subtitleStyle(AAStyle prop) {
        subtitleStyle = prop;
        return this;
    }

    /**
     * axesTextColor
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel axesTextColor(String prop) {
        axesTextColor = prop;
        return this;
    }

    /**
     * chartType
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel chartType(String prop) {
        chartType = prop;
        return this;
    }

    /**
     * stacking
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel stacking(String prop) {
        stacking = prop;
        return this;
    }

    /**
     * markerSymbol
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel markerSymbol(String prop) {
        markerSymbol = prop;
        return this;
    }

    /**
     * markerSymbolStyle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel markerSymbolStyle(String prop) {
        markerSymbolStyle = prop;
        return this;
    }

    /**
     * zoomType
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel zoomType(String prop) {
        zoomType = prop;
        return this;
    }

    /**
     * inverted
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel inverted(Boolean prop) {
        inverted = prop;
        return this;
    }

    /**
     * xAxisReversed
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel xAxisReversed(Boolean prop) {
        xAxisReversed = prop;
        return this;
    }

    /**
     * yAxisReversed
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisReversed(Boolean prop) {
        yAxisReversed = prop;
        return this;
    }

    /**
     * tooltipEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel tooltipEnabled(Boolean prop) {
        tooltipEnabled = prop;
        return this;
    }

    /**
     * tooltipValueSuffix
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel tooltipValueSuffix(String prop) {
        tooltipValueSuffix = prop;
        return this;
    }

    /**
     * gradientColorEnable
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel gradientColorEnable(Boolean prop) {
        gradientColorEnable = prop;
        return this;
    }

    /**
     * polar
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel polar(Boolean prop) {
        polar = prop;
        return this;
    }

    /**
     * margin
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel margin(Float[] prop) {
        margin = prop != null ? prop.clone() : null;
        return this;
    }

    /**
     * dataLabelsEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel dataLabelsEnabled(Boolean prop) {
        dataLabelsEnabled = prop;
        return this;
    }

    /**
     * dataLabelsStyle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel dataLabelsStyle(AAStyle prop) {
        dataLabelsStyle = prop;
        return this;
    }

    /**
     * xAxisLabelsEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel xAxisLabelsEnabled(Boolean prop) {
        xAxisLabelsEnabled = prop;
        return this;
    }

    /**
     * xAxisTickInterval
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel xAxisTickInterval(Integer prop) {
        xAxisTickInterval = prop;
        return this;
    }

    /**
     * categories
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel categories(String[] prop) {
        categories = prop != null ? prop.clone() : null;
        return this;
    }

    /**
     * xAxisGridLineWidth
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel xAxisGridLineWidth(Float prop) {
        xAxisGridLineWidth = prop;
        return this;
    }

    /**
     * yAxisGridLineWidth
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisGridLineWidth(Float prop) {
        yAxisGridLineWidth = prop;
        return this;
    }

    /**
     * xAxisVisible
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel xAxisVisible(Boolean prop) {
        xAxisVisible = prop;
        return this;
    }

    /**
     * yAxisVisible
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisVisible(Boolean prop) {
        yAxisVisible = prop;
        return this;
    }

    /**
     * yAxisLabelsEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisLabelsEnabled(Boolean prop) {
        yAxisLabelsEnabled = prop;
        return this;
    }

    /**
     * yAxisTitle
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisTitle(String prop) {
        yAxisTitle = prop;
        return this;
    }

    /**
     * yAxisLineWidth
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisLineWidth(Float prop) {
        yAxisLineWidth = prop;
        return this;
    }

    /**
     * yAxisMin
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisMin(Float prop) {
        yAxisMin = prop;
        return this;
    }

    /**
     * yAxisMax
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisMax(Float prop) {
        yAxisMax = prop;
        return this;
    }

    /**
     * yAxisAllowDecimals
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel yAxisAllowDecimals(Boolean prop) {
        yAxisAllowDecimals = prop;
        return this;
    }

    /**
     * colorsTheme
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel colorsTheme(Object[] prop) {
        colorsTheme = Arrays.asList(prop).toArray();
        return this;
    }

    /**
     * legendEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel legendEnabled(Boolean prop) {
        legendEnabled = prop;
        return this;
    }

    /**
     * backgroundColor
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel backgroundColor(Object prop) {
        backgroundColor = prop;
        return this;
    }

    /**
     * borderRadius
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel borderRadius(Float prop) {
        borderRadius = prop;
        return this;
    }

    /**
     * markerRadius
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel markerRadius(Float prop) {
        markerRadius = prop;
        return this;
    }

    /**
     * series
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel series(Object[] prop) {
        series = Arrays.asList(prop).toArray();
        return this;
    }

    /**
     * touchEventEnabled
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel touchEventEnabled(Boolean prop) {
        touchEventEnabled = prop;
        return this;
    }

    /**
     * scrollablePlotArea
     *
     * @param prop prop
     * @return AAChartModel
     */
    public AAChartModel scrollablePlotArea(AAScrollablePlotArea prop) {
        scrollablePlotArea = prop;
        return this;
    }

    /**
     * aa_toAAOptions
     *
     * @return AAChartModel
     */
    public AAOptions aa_toAAOptions() {
        return AAOptionsConstructor.configureChartOptions(this);
    }

    public AAChartModel() {
        chartType = AAChartType.Line;
        animationDuration = 500;// 以毫秒为单位
        animationType = AAChartAnimationType.Linear;
        inverted = false;
        stacking = AAChartStackingType.False;
        xAxisReversed = false;
        yAxisReversed = false;
        zoomType = AAChartZoomType.None;
        dataLabelsEnabled = false;
        markerSymbolStyle = AAChartSymbolStyleType.Normal;
        colorsTheme = new String[]{"#fe117c", "#ffc069", "#06caf4", "#7dffc0"};// 默认的颜色数组(必须要添加默认数组,否则就会出错)
        gradientColorEnable = false;
        polar = false;
        xAxisLabelsEnabled = true;
        xAxisGridLineWidth = 0f;
        yAxisLabelsEnabled = true;
        yAxisGridLineWidth = 1f;
        legendEnabled = true;
        backgroundColor = "#ffffff";
        borderRadius = 0f; // 柱状图长条图头部圆角半径(可用于设置头部的形状,仅对条形图,柱状图有效,设置为1000时,柱形图或者条形图头部为楔形)
        markerRadius = 6f; // 折线连接点的半径长度,如果值设置为0,这样就相当于不显示了
    }
}