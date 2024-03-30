package com.wdk.sports.util;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

import java.io.File;

public class DataUtil {

    private static DatabaseHelper databaseHelper;
    private static Preferences preferences;

    public static void initDatabaseHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static void initPreferences(Context context, String fileName){
        initDatabaseHelper( context );
        preferences = databaseHelper.getPreferences(fileName);
    }

    public static Preferences getPreferences( Context context,String fileName ){

        initPreferences( context, fileName );
        return preferences;

    }


}
