/**
 * //  AAChartModel.java
 * //  AAChartCore
 * //
 * //  Created by AnAn on 2017/9/8..
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

import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAOptions;
import com.example.anan.AAChartCore.AAChartCoreLib.AATools.AAJSStringPurer;
import com.example.anan.AAChartCore.utils.Log;
import com.example.anan.AAChartCore.utils.SystemUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.components.webengine.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class AAChartView extends WebView {
    private static final String TAG = "AAChartView";

    public interface AAChartViewCallBack {
        void chartViewDidFinishLoad(AAChartView aaChartView);

        void chartViewMoveOverEventMessage(
                AAChartView aaChartView,
                AAMoveOverEventMessageModel messageModel
        );
    }

    public Float contentWidth;
    public Float contentHeight;
    public Boolean chartSeriesHidden;
    public Boolean isClearBackgroundColor;
    public AAChartViewCallBack callBack;

    /**
     * setContentWidth
     *
     * @param contentWidth contentWidth
     */
    public void setContentWidth(Float contentWidth) {
        this.contentWidth = contentWidth;
        String jsStr = "setTheChartViewContentWidth('"
                + this.contentWidth + "')";
        safeEvaluateJavaScriptString(jsStr);
    }

    /**
     * setContentHeight
     *
     * @param contentHeight contentHeight
     */
    public void setContentHeight(Float contentHeight) {
        this.contentHeight = contentHeight;
        String jsStr = "setTheChartViewContentHeight('"
                + this.contentHeight + "')";
        safeEvaluateJavaScriptString(jsStr);
    }

    /**
     * setChartSeriesHidden
     *
     * @param chartSeriesHidden chartSeriesHidden
     */
    public void setChartSeriesHidden(Boolean chartSeriesHidden) {
        this.chartSeriesHidden = chartSeriesHidden;
        String jsStr = "setChartSeriesHidden('"
                + this.chartSeriesHidden + "')";
        safeEvaluateJavaScriptString(jsStr);
    }

    /**
     * setIsClearBackgroundColor
     *
     * @param isClearBackgroundColor isClearBackgroundColor
     */
    public void setIsClearBackgroundColor(Boolean isClearBackgroundColor) {
        this.isClearBackgroundColor = isClearBackgroundColor;
        if (this.isClearBackgroundColor) {
            RgbColor rgbColor = new RgbColor(0, 0, 0, 0);
            this.setWebViewBackground(rgbColor);
        } else {
            RgbColor rgbColor = new RgbColor(255, 255, 255, 255);
            this.setWebViewBackground(rgbColor);
        }
    }

    private String optionsJson;

    public AAChartView(Context context) {
        this(context, null);
    }

    public AAChartView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        setupBasicContent();
    }

    private void setupBasicContent() {
        // Do some initialize work.
        this.contentWidth = 420f;
        this.contentHeight = 580f;
        this.isClearBackgroundColor = false;
        this.getWebConfig().setJavaScriptPermit(true);
        addJsCallback("ohosObject", s -> ohosMethod(s));
    }

    private String ohosMethod(String message) {
        Gson gson = new Gson();
        Map messageBody = new HashMap<String, Object>();
        messageBody = gson.fromJson(message, messageBody.getClass());
        AAMoveOverEventMessageModel eventMessageModel = getEventMessageModel(messageBody);
        if (callBack != null) {
            callBack.chartViewMoveOverEventMessage(this, eventMessageModel);
        }
        return "";
    }

    /**
     * aa_drawChartWithChartModel
     *
     * @param chartModel chartModel
     */
    public void aa_drawChartWithChartModel(final AAChartModel chartModel) {
        AAOptions aaOptions = chartModel.aa_toAAOptions();
        this.aa_drawChartWithChartOptions(aaOptions);
    }

    /**
     * aa_refreshChartWithChartModel
     *
     * @param chartModel chartModel
     */
    public void aa_refreshChartWithChartModel(AAChartModel chartModel) {
        AAOptions aaOptions = chartModel.aa_toAAOptions();
        this.aa_refreshChartWithChartOptions(aaOptions);
    }

    /**
     * aa_drawChartWithChartOptions
     *
     * @param chartOptions chartOptions
     */
    public void aa_drawChartWithChartOptions(final AAOptions chartOptions) {
        if (this.optionsJson != null) {
            this.aa_refreshChartWithChartOptions(chartOptions);
        } else {
            this.loadLocalFilesAndDrawChart(chartOptions);
            this.showJavaScriptAlertView();
        }
    }

    /**
     * aa_refreshChartWithChartOptions
     *
     * @param chartOptions chartModel
     */
    public void aa_refreshChartWithChartOptions(AAOptions chartOptions) {
        configureChartOptionsAndDrawChart(chartOptions);
    }

    /**
     * aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray
     *
     * @param seriesElementsArr seriesElementsArr
     */
    public void aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
            AASeriesElement[] seriesElementsArr
    ) {
        aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(seriesElementsArr, true);
    }

    /**
     * aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray
     *
     * @param seriesElementsArr seriesElementsArr
     * @param animation         animation
     */
    public void aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
            AASeriesElement[] seriesElementsArr,
            Boolean animation
    ) {
        String seriesArr = new Gson().toJson(seriesElementsArr);
        String javaScriptStr = "onlyRefreshTheChartDataWithSeries('"
                + seriesArr + "','" + animation + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_updateChartWithOptions
     *
     * @param options options
     * @param redraw  redraw
     */
    public void aa_updateChartWithOptions(
            Object options,
            Boolean redraw
    ) {
        String classNameStr = options.getClass().getSimpleName();
        classNameStr = classNameStr.replace("AA", "");
        //convert fist character to be lowercase string
        String firstChar = classNameStr.substring(0, 1);
        String lowercaseFirstStr = firstChar.toLowerCase();
        classNameStr = classNameStr.substring(1);
        String finalClassName = lowercaseFirstStr + classNameStr;
        Map finalOptionsMap = new HashMap();
        finalOptionsMap.put(finalClassName, options);
        String optionsStr = new Gson().toJson(finalOptionsMap);
        String javaScriptStr = "updateChart('" + optionsStr + "','" + redraw + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_addPointToChartSeriesElement
     *
     * @param elementIndex elementIndex
     * @param options      options
     */
    public void aa_addPointToChartSeriesElement(
            Integer elementIndex,
            Object options
    ) {
        aa_addPointToChartSeriesElement(
                elementIndex,
                options,
                true);
    }

    /**
     * aa_addPointToChartSeriesElement
     *
     * @param elementIndex elementIndex
     * @param options      options
     * @param shift        shift
     */
    public void aa_addPointToChartSeriesElement(
            Integer elementIndex,
            Object options,
            Boolean shift
    ) {
        aa_addPointToChartSeriesElement(
                elementIndex,
                options,
                true,
                shift,
                true);
    }

    /**
     * aa_addPointToChartSeriesElement
     *
     * @param elementIndex elementIndex
     * @param options      options
     * @param redraw       redraw
     * @param shift        shift
     * @param animation    animation
     */
    public void aa_addPointToChartSeriesElement(
            Integer elementIndex,
            Object options,
            Boolean redraw,
            Boolean shift,
            Boolean animation
    ) {
        String optionsStr;
        if (options instanceof Integer
                || options instanceof Float
                || options instanceof Double) {
            optionsStr = String.valueOf(options);
        } else {
            optionsStr = new Gson().toJson(options);
        }

        String javaScriptStr = "addPointToChartSeries('"
                + elementIndex + "','"
                + optionsStr + "','"
                + redraw + "','"
                + shift + "','"
                + animation + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_showTheSeriesElementContent
     *
     * @param elementIndex elementIndex
     */
    public void aa_showTheSeriesElementContent(Integer elementIndex) {
        String javaScriptStr = "showTheSeriesElementContentWithIndex('"
                + elementIndex + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_hideTheSeriesElementContent
     *
     * @param elementIndex elementIndex
     */
    public void aa_hideTheSeriesElementContent(Integer elementIndex) {
        String javaScriptStr = "hideTheSeriesElementContentWithIndex('"
                + elementIndex + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_addElementToChartSeries
     *
     * @param aaSeriesElement aaSeriesElement
     */
    public void aa_addElementToChartSeries(AASeriesElement aaSeriesElement) {
        String pureElementJsonStr = new Gson().toJson(aaSeriesElement);
        String javaScriptStr = "addElementToChartSeriesWithElement('"
                + pureElementJsonStr + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_removeElementFromChartSeries
     *
     * @param elementIndex elementIndex
     */
    public void aa_removeElementFromChartSeries(Integer elementIndex) {
        String javaScriptStr = "removeElementFromChartSeriesWithElementIndex('"
                + elementIndex + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    /**
     * aa_evaluateTheJavaScriptStringFunction
     *
     * @param jsFunctionStr jsFunctionStr
     */
    public void aa_evaluateTheJavaScriptStringFunction(String jsFunctionStr) {
        String pureJSFunctionStr = AAJSStringPurer.pureJavaScriptFunctionString(jsFunctionStr);
        String jsFunctionNameStr = "evaluateTheJavaScriptStringFunction('"
                + pureJSFunctionStr + "')";
        safeEvaluateJavaScriptString(jsFunctionNameStr);
    }

    private String copyString(String fileName) {
        String filePath = String.format("resources/rawfile/%s", fileName);
        ResourceManager resManager = mContext.getResourceManager();
        RawFileEntry rawFileEntry = resManager.getRawFileEntry(filePath);
        Resource resource = null;
        try {
            resource = rawFileEntry.openRawFile();
            if (resource == null) {
                return "";
            }
            byte[] bytes = new byte[resource.available()];
            resource.read(bytes);
            String str = new String(bytes);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    private String base64Html(String string) {
        byte[] encode = Base64.getEncoder().encode(string.getBytes());
        try {
            String s = new String(encode, "utf-8");
            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loadLocalFilesAndDrawChart(final AAOptions aaOptions) {
        String aAChartViewText = copyString("AAChartView.html");
        //加载文件
        this.load(base64Html(aAChartViewText), "text/html", true);
        this.setWebAgent(new WebAgent() {
            @Override
            public void onError(WebView webView, ResourceRequest request, ResourceError error) {
                super.onError(webView, request, error);
                Log.i(TAG, "onError " + error.getInfo());
            }

            @Override
            public void onPageLoaded(WebView webView, String url) {
                Log.i(TAG, "onPageLoaded " + url);
                super.onPageLoaded(webView, url);

                executeRawJsFile();

                configureChartOptionsAndDrawChart(aaOptions);

                if (callBack != null) {
                    callBack.chartViewDidFinishLoad(AAChartView.this);
                }
            }
        });
    }

    //执行js 文件
    private void executeRawJsFile() {
        String s1 = copyString("AAHighchartsLib.js");
        String s2 = copyString("AAHighchartsMore.js");
        String s3 = copyString("AAFunnel.js");
        String s4 = copyString("AAEasing.js");
        String s5 = copyString("AAChartView.js");
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onReceive(String s) {
                Log.i(TAG, "executeJs 执行完成");
            }
        };
        this.executeJs(s1, callback);
        this.executeJs(s2, callback);
        this.executeJs(s3, callback);
        this.executeJs(s4, callback);
        this.executeJs(s5, callback);
    }

    private void configureChartOptionsAndDrawChart(AAOptions chartOptions) {
        if (isClearBackgroundColor) {
            chartOptions.chart.backgroundColor("rgba(0,0,0,0)");
        }
        Gson gson = new Gson();
        String aaOptionsJsonStr = gson.toJson(chartOptions);
        this.optionsJson = aaOptionsJsonStr;
        String javaScriptStr = "loadTheHighChartView('"
                + aaOptionsJsonStr + "','"
                + this.contentWidth + "','"
                + this.contentHeight + "')";
        this.safeEvaluateJavaScriptString(javaScriptStr);
    }

    private void showJavaScriptAlertView() {
        this.setBrowserAgent(new BrowserAgent(getContext()) {
            @Override
            public boolean onJsMessageShow(WebView webView, String url,
                                           String message, boolean isAlert, JsMessageResult result) {
                if (isAlert) {
                    String urlStr = "url --->" + url + "\n\n\n";
                    String messageStr = "message --->" + message + "\n\n\n";
                    String resultStr = "result --->" + result;
                    String alertMessageStr = urlStr + messageStr + resultStr;
                    showAlertDialog(alertMessageStr);
                    return true;
                }

                return super.onJsMessageShow(webView, url, message, isAlert, result);
            }
        });
    }

    private void showAlertDialog(String alertMessageStr) {
        CommonDialog commonDialog = new CommonDialog(getContext());
        commonDialog.setTitleText("JavaScript alert Information");
        Text text = new Text(getContext());
        text.setText(alertMessageStr);
        text.setTextColor(new Color(Color.rgb(0, 0, 0)));
        text.setTextSize(16, Text.TextSizeType.FP);
        text.setPadding(24, 0, 24, 12);
        text.setMultipleLine(true);
        ComponentContainer.LayoutConfig layoutConfig = new ComponentContainer.LayoutConfig
                (ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        text.setLayoutConfig(layoutConfig);
        commonDialog.setContentCustomComponent(text);
        commonDialog.setButton(0, "sure", (iDialog, i) -> commonDialog.destroy());
        commonDialog.setAutoClosable(true);
        int width = (int) (SystemUtils.getDisplayWidthInPx(getContext()) * 0.5);
        commonDialog.setSize(width, -2);
        commonDialog.setAlignment(LayoutAlignment.CENTER);
        commonDialog.show();
    }

    private AAMoveOverEventMessageModel getEventMessageModel(Map messageBody) {
        AAMoveOverEventMessageModel eventMessageModel = new AAMoveOverEventMessageModel();
        eventMessageModel.name = messageBody.get("name").toString();
        eventMessageModel.x = (Double) messageBody.get("x");
        eventMessageModel.y = (Double) messageBody.get("y");
        eventMessageModel.category = messageBody.get("category").toString();
        eventMessageModel.offset = (LinkedTreeMap) messageBody.get("offset");
        Double index = (Double) messageBody.get("index");
        eventMessageModel.index = index.intValue();
        return eventMessageModel;
    }

    private void safeEvaluateJavaScriptString(String javaScriptString) {
        // javaScriptString = "javascript:" + javaScriptString;
        this.executeJs(javaScriptString, new AsyncCallback<String>() {
            @Override
            public void onReceive(String s) {
                Log.i(TAG, "safeEvaluateJavaScriptString onReceive " + s);
            }
        });
    }
}
