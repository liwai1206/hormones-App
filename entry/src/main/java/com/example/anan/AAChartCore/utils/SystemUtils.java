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

import ohos.agp.utils.Point;

import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;

import ohos.app.Context;

/**
 * Log
 *
 * @author author
 * @version 1.0
 */
public class SystemUtils {
    /**
     * getDisplayWidthInPx
     *
     * @param context
     * @return int
     */
    public static int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointX();
    }

    /**
     * getDisplayHightInPx
     *
     * @param context
     * @return int
     */
    public static int getDisplayHightInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointY();
    }


}
