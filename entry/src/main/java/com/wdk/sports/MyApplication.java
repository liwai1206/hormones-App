package com.wdk.sports;

import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.tasks.OnFailureListener;
import com.huawei.hms.accountsdk.support.account.tasks.OnSuccessListener;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.wdk.sports.slice.MainAbilitySlice;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {


    @Override
    public void onInitialize() {
        super.onInitialize();
        // 示例：在应用初始化时就完成华为帐号SDK的初始化
        // 调用示例initHuaweiAccountSDK方法，在HarmonyOS应用初始化方法onInitialize中进行华为帐号SDK初始化
        initHuaweiAccountSDK();
    }

    // 示例：此方法中调用华为帐号SDK的初始化方法AccountAuthManager.init()进行初始化
    private void initHuaweiAccountSDK() {
        Task<Void> task;
        try {
            // 调用AccountAuthManager.init方法初始化
            task = AccountAuthManager.init(this);
        } catch (ApiException apiException) {
            apiException.getStatusCode();
            return;
        }
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                //初始化成功
                System.out.println( "SDK初始化成功");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // SDK初始化失败
                if (e instanceof ApiException) {

                    System.out.println( "SDK初始化失败");

                    ApiException apiException = (ApiException) e;
                    // SDK初始化失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
                    apiException.getStatusCode();
                }
            }
        });
    }
}
