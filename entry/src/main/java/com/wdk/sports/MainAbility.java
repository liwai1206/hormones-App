package com.wdk.sports;

import com.wdk.sports.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.bundle.IBundleManager;

public class MainAbility extends Ability {
    private final String PERMISSION_LOCATION = "ohos.permission.LOCATION";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        // 隐藏状态栏、设置状态栏和导航栏透明
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_NAVIGATION);

        // 判断权限是否已授予
        if (verifySelfPermission(PERMISSION_LOCATION) != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授权
            if (canRequestPermission(PERMISSION_LOCATION)) {
                // 是否可以申请弹窗授权
                requestPermissionsFromUser(new String[]{PERMISSION_LOCATION}, 0);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
                new ToastDialog(getContext()).setText("请进入系统设置进行授权").show();
            }
        }
    }
}
