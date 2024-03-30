package com.wdk.sports.slice;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.wdk.sports.ResourceTable;
import com.wdk.sports.util.HexConverter;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.dispatcher.task.TaskPriority;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button deviceInfoBackBtn = null;

    public static Text manufacturerName;
    public static Text modelNumber;  // 品牌名称
    public static Text deviceType;  // 设备类型
    public static Text deviceNumber;  // 设备型号
    public static Text version;   // 版本号

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_page_deviceInfo);

        initComponent();
        initComponentListener();

    }

    private void initComponentListener() {
        deviceInfoBackBtn.setClickedListener(this);
    }

    private void initComponent() {
        deviceInfoBackBtn = (Button) findComponentById(ResourceTable.Id_btn_deviceInfo_back);

        manufacturerName = (Text) findComponentById(ResourceTable.Id_manufacturer_name);
        modelNumber = (Text) findComponentById(ResourceTable.Id_model_number);
        deviceType = (Text) findComponentById(ResourceTable.Id_device_type);
        deviceNumber = (Text) findComponentById(ResourceTable.Id_device_number);
        version = (Text) findComponentById(ResourceTable.Id_version);

        setDeviceInfo();
    }


    /**
     * 读取蓝牙信息，渲染到界面
     */
    private void setDeviceInfo() {

        BleManager.getInstance().write(MainAbilitySlice.bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes("aa55020400fb"),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println("写数据成功" + HexUtil.formatHexString( bytes ));
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println("写数据失败");
                    }
                });

    }

    @Override
    public void onClick(Component component) {
        if ( this.deviceInfoBackBtn != null && this.deviceInfoBackBtn == component){
            this.onBackPressed();
        }
    }
}
