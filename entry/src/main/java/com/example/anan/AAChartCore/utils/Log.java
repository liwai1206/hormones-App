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

import java.util.logging.Logger;

/**
 * Log
 *
 * @author author
 * @version 1.0
 */
public class Log {
    /**
     * i
     *
     * @param tag  tag
     * @param info info
     */
    public static void i(String tag, String info) {
        Logger.getLogger(tag).info(info);
    }
}
