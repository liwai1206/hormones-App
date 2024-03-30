package com.wdk.sports.util;

import java.io.FileInputStream;

public class Constans {



    public static int NOW_CHARGE; // 当前电量

    public static String HTTP_HEAD = "https://api.wandakang.cn/bodybuilding" ;

    public static boolean isEndSport = false ; // 运动中的关闭按钮，控制运动是否结束

    public static boolean isVersionCode = false ; // 是否有更新
    public static String versionAddress = "" ; // 更新版本地址
    public static String versionFileName = "" ; // 更新版本地址
    public static String versionCode = "" ; // 更新版本地址
    public static String newVersioSize = "" ; // 更新版本大小
    public static FileInputStream fis =null;

    public static String currentCode = ""; // 当前版本


    public static String TOKEN = ""; // 令牌
    public static String SECRETKEY = "kjgiamxqosxwbcsa"; // 密钥
    public static int UID;

    public static int INDEX; // OTA升级的索引
    public static int UPGRADE_STATE = 0; // 1 待更新  2 正在更新  0 无更新  3 更新完成


}
