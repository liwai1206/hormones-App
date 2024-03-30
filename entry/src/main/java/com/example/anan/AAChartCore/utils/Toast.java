/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.anan.AAChartCore.utils;

import ohos.agp.colors.RgbColor;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;

import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;

import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;

import ohos.app.Context;

/**
 * Log
 *
 * @author author
 * @version 1.0
 */
public class Toast {
    public static final int LENGTH_LONG = 4000;
    public static final int LENGTH_SHORT = 2000;

    /**
     * ToastLayout
     */
    public enum ToastLayout {
        DEFAULT,
        CENTER,
        TOP,
        BOTTOM,
    }

    /**
     * showShort
     *
     * @param mContext mContext
     * @param content  content
     */
    public static void showShort(Context mContext, String content) {
        createTost(mContext, content, LENGTH_SHORT, ToastLayout.DEFAULT);
    }

    /**
     * showLong
     *
     * @param mContext mContext
     * @param content  content
     */
    public static void showLong(Context mContext, String content) {
        createTost(mContext, content, LENGTH_LONG, ToastLayout.DEFAULT);
    }

    /**
     * showLong
     *
     * @param mContext mContext
     * @param content  content
     */
    public static void showLong(Context mContext, int content) {
        createTost(mContext, getString(mContext, content), LENGTH_LONG, ToastLayout.DEFAULT);
    }

    private static void createTost(Context mContext, String content, int duration, ToastLayout layout) {
        DirectionalLayout toastLayout = new DirectionalLayout(mContext);

        int width = (int) (SystemUtils.getDisplayWidthInPx(mContext) - AttrHelper.vp2px(50, mContext));


        DirectionalLayout.LayoutConfig textConfig = new DirectionalLayout.LayoutConfig(width, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        Text text = new Text(mContext);
        text.setMultipleLine(true);
        text.setText(content);
        text.setTextAlignment(TextAlignment.HORIZONTAL_CENTER);
        text.setTextColor(new Color(Color.getIntColor("#000000")));
        text.setPadding(vp2px(mContext, 16), vp2px(mContext, 16), vp2px(mContext, 16), vp2px(mContext, 16));
        text.setTextSize(vp2px(mContext, 14));
        text.setBackground(buildDrawableByColorRadius(Color.getIntColor("#eeeeee"), vp2px(mContext, 20)));
        text.setLayoutConfig(textConfig);
        toastLayout.addComponent(text);
        int mLayout = LayoutAlignment.CENTER;
        switch (layout) {
            case TOP:
                mLayout = LayoutAlignment.TOP;
                break;
            case BOTTOM:
                mLayout = LayoutAlignment.BOTTOM;
                break;
            case CENTER:
                mLayout = LayoutAlignment.CENTER;
                break;
        }
        ToastDialog toastDialog = new ToastDialog(mContext);
        toastDialog.setComponent(toastLayout);
        toastDialog.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        toastDialog.setAlignment(mLayout);
        toastDialog.setTransparent(true);
//        toastDialog.setDuration(duration);
        toastDialog.show();
    }

    private static ohos.agp.components.element.Element buildDrawableByColorRadius(int color, float radius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(0);
        drawable.setRgbColor(RgbColor.fromArgbInt(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private static String getString(Context mContent, int resId) {
        try {
            return mContent.getResourceManager().getElement(resId).getString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }
}
