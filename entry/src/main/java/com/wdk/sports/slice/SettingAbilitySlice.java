package com.wdk.sports.slice;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wdk.sports.ResourceTable;
import com.wdk.sports.domain.ResultVO;
import com.wdk.sports.util.Constans;
import com.wdk.sports.util.DataUtil;
import com.wdk.sports.util.StringUtils;
import com.zhy.http.library.OkHttpUtils;
import com.zhy.http.library.callback.StringCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.data.preferences.Preferences;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.utils.net.Uri;
import okhttp3.Call;
import okhttp3.MediaType;
import org.devio.hi.json.HiJson;
import org.devio.hi.json.JSONStringer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class SettingAbilitySlice extends AbilitySlice implements Component.ClickedListener, RadioContainer.CheckedStateChangedListener {
    private DirectionalLayout deviceInfoLayout = null ;

    // 设置界面
    private DirectionalLayout deviceNameLayout = null ;
    private Button settingBackBtn = null ;
    private Text deviceNameText = null ;

    // 设备名称相关
    private CommonDialog deviceNameCommonDialog = null;
    private Button cancelBtn = null ;
    private Button okBtn = null ;
    private TextField deviceNameTf = null;
    private Text tipInfoText = null ;

    // 所属空间相关
    private DirectionalLayout belongSpaceLayout = null;
    private CommonDialog belongSpaceCommonDialog = null;
    private Button cancelBelongBtn = null ;
    private Button addRoomBtn = null;
    private CommonDialog addRoomCommonDialog = null ;
    private Button addRoomCancelBtn = null ;
    private Button addRoomOkBtn = null ;
    private TextField addRoomNameTf = null;
    private Text addRoomTipInfoText = null;
    private RadioContainer radioContainer = null;
    private Text belongSpaceText = null;

    // 删除设备相关
    private DirectionalLayout deleteDeviceLayout = null;
    private CommonDialog deleteDeviceCommonDialog = null;
    private Button deleteCancelBtn = null;
    private Button deleteOkBtn = null;

    // 快捷方式相关
    private DirectionalLayout fastMethodLayout =null;
    private CommonDialog fastMethodCommonDialog = null;
    private Button fastCancelBtn = null;
    private Button fastOkBtn = null; // 前往设置

    // about
    private DirectionalLayout aboutLayout = null;

    DataAbilityHelper helper = DataAbilityHelper.creator(this);


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_page_setting);

        initComponent();
        initComponentListener();

    }

    private void initComponent() {
        deviceInfoLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_deviceInfo);

        deviceNameLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_device_name);
        settingBackBtn = (Button) this.findComponentById(ResourceTable.Id_btn_setting_back);
        deviceNameText = (Text) this.findComponentById( ResourceTable.Id_text_devicename);
        getDeviceName();

        belongSpaceLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_directionLayout_belong_space);
        belongSpaceText = (Text) this.findComponentById(ResourceTable.Id_text_belong_space);
        getBelongSpace();

        deleteDeviceLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_directionLayout_delete_device);
        fastMethodLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_directionLayout_fast_method);

        aboutLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_directionLayout_about);
    }


    /**
     * 获取所属空间
     */
    private void getBelongSpace() {
        //获取文件目录
        File dataDir = new File(this.getDataDir().toString());
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }

        FileInputStream  fis = null ;
        //目标文件
        File targetFile = new File(Paths.get(dataDir.toString(),"belongSpace.txt").toString());
        if ( !targetFile.exists()){
            belongSpaceText.setText( "客厅" );
            return ;
        }

        try {
            fis = new FileInputStream(targetFile);
            byte[] buffer = new byte[fis.available()];
            int count =0;
            while((count = fis.read(buffer)) >=0){
                belongSpaceText.setText( new String( buffer ));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 获取设备名称
     */
    private void getDeviceName() {

       // 读取文件
        //获取文件目录
        File dataDir = new File(this.getDataDir().toString());
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }

        FileInputStream  fis = null ;
        //目标文件
        File targetFile = new File(Paths.get(dataDir.toString(),"deviceName.txt").toString());
        if ( targetFile.exists()){
            // 如果文件存在，则读取内容，设置到界面
            try {
                fis = new FileInputStream(targetFile);
                byte[] buffer = new byte[fis.available()];
                int count =0;
                while((count = fis.read(buffer)) >=0){
                    deviceNameText.setText( new String( buffer ));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return ;
        }

        // 如果文件不存在，则读取设备名称数据
        MainAbilitySlice.bleManager.read(MainAbilitySlice.bleDevice, // 设备
                "00001800-0000-1000-8000-00805f9b34fb", // service的uuid
                "00002a00-0000-1000-8000-00805f9b34fb", // 这个service下的character的uuid
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] data) {
                        String s = new String(data);

                        getUITaskDispatcher().asyncDispatch( ()->{
                            deviceNameText.setText(  s.trim() );
                            try {
                                Thread.sleep( 100 );
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onReadFailure(BleException e) {
                        System.out.println( "==================读取设备名称失败");
                    }
                });


    }

    private void initComponentListener() {
        settingBackBtn.setClickedListener( this );
        deviceInfoLayout.setClickedListener( this );
        deviceNameLayout.setClickedListener( this );
        belongSpaceLayout.setClickedListener(this);
        deleteDeviceLayout.setClickedListener(this);
        fastMethodLayout.setClickedListener(this);
        aboutLayout.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        if ( deviceInfoLayout != null && component == deviceInfoLayout ){
            this.present( new DeviceInfoAbilitySlice(), new Intent());
        }else if ( settingBackBtn != null && component == settingBackBtn){
            this.onBackPressed();
        }else if ( deviceNameLayout != null && component == deviceNameLayout){
            setDeviceName(this);
        }else if ( cancelBtn != null && component == cancelBtn){
            deviceNameCommonDialog.hide();
        }else if ( okBtn != null && component == okBtn){
            changeDeviceName();
            deviceNameCommonDialog.hide();
        }else if ( belongSpaceLayout != null && component == belongSpaceLayout){
            try {
                setBelongSpace( this );
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotExistException e) {
                e.printStackTrace();
            }
        }else if ( cancelBelongBtn != null && component == cancelBelongBtn){
            belongSpaceCommonDialog.hide();
        }else if ( addRoomBtn != null && component == addRoomBtn){
            addRoom( this );
        }else if ( addRoomCancelBtn != null && component == addRoomCancelBtn){
            addRoomCommonDialog.hide();
        }else if ( addRoomOkBtn != null && component == addRoomOkBtn){
            addRoomClick(this);
        }else if ( deleteDeviceLayout != null && component == deleteDeviceLayout){
            deleteDevice(this);
        }else if ( deleteCancelBtn != null && component == deleteCancelBtn){
            deleteDeviceCommonDialog.hide();
        }else if ( deleteOkBtn != null && component == deleteOkBtn){
            deleteDeviceOk();
        }else if ( fastMethodLayout != null && component == fastMethodLayout){
            fastMethodDialog( this );
        }else if ( fastCancelBtn != null && component == fastCancelBtn){
            fastMethodCommonDialog.hide();
        }else if ( fastOkBtn != null && component == fastOkBtn){
            fastMethodOk();
        }else if ( aboutLayout != null && component == aboutLayout){
            this.present( new AboutAbilitySlice(), new Intent());
        }
    }


    /**
     * 设置快捷方式点击事件
     */
    private void fastMethodOk() {
        // todo:快捷方式
        fastMethodCommonDialog.hide();
    }


    /**
     * 快捷方式弹框
     */
    private void fastMethodDialog( Context context ) {
        //1、定义对话弹框
        fastMethodCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        fastMethodCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        fastMethodCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_fastmethod_common, null, false);

        fastCancelBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        fastOkBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok);

        // 设置点击事件
        fastCancelBtn.setClickedListener(this);
        fastOkBtn.setClickedListener(this);

        //将XML文件中的布局交给吐司弹框
        fastMethodCommonDialog.setContentCustomComponent(dl);
        fastMethodCommonDialog.setAutoClosable( false );
        //弹框显示时间
        fastMethodCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        fastMethodCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        fastMethodCommonDialog.show();
    }


    /**
     * 确定删除设备
     */
    private void deleteDeviceOk() {
        // todo: 删除蓝牙设备数据
        // 1. 删除后台数据
        appData_deleteHistory();

        // 2. 删除文件
        //获取文件目录
        File dataDir = new File(this.getDataDir().toString());
        if( !dataDir.exists()){
            return;
        }
        //目标文件
        File targetFile = new File(Paths.get(dataDir.toString(),"deviceName.txt").toString());
        File targetFile2 = new File(Paths.get(dataDir.toString(),"belongSpace.txt").toString());
        if( targetFile.exists()){
            targetFile.delete();
        }
        if( targetFile2.exists()){
            targetFile2.delete();
        }

        deleteDeviceCommonDialog.hide();
    }


    /**
     * 删除全部数据
     */
    private void appData_deleteHistory() {
        Map<String, String> map = new HashMap<>();
        map.put("token", Constans.TOKEN);
        map.put("device_id", MainAbilitySlice.deviceId);
        map.put("device_type", MainAbilitySlice.deviceType);

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .postString()
                    .content( new Gson().toJson( map ))
                    .url( Constans.HTTP_HEAD + "/appData/deleteHistory")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("删除设备数据失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            System.out.println("删除设备数据成功");
                            ResultVO resultVO = new Gson().fromJson(s, ResultVO.class);

                            if ( resultVO.getCode().equals("200")){
                                new ToastDialog(SettingAbilitySlice.this)
                                        .setText("删除数据成功")
                                        .setAutoClosable(true)
                                        .setDuration( 3000 )
                                        .show();
                            }
                        }
                    });
        });
    }


    /**
     * 删除这一天的数据
     * @param start
     */
    private void appData_deleteHistory( String start ) {

        if (StringUtils.isEmpty( start) ){
            appData_deleteHistory();
            return ;
        }

        Map<String, String> map = new HashMap<>();
        map.put("token", Constans.TOKEN);
        map.put("device_id", MainAbilitySlice.deviceId);
        map.put("device_type", MainAbilitySlice.deviceType);
        map.put("start", start);

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .postString()
                    .content( new Gson().toJson( map ))
                    .mediaType( MediaType.parse("application/json; charset=utf-8"))
                    .url( Constans.HTTP_HEAD + "/appData/deleteHistory")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            FancyToast.makeText( SettingAbilitySlice.this, "删除数据失败", 3000,FancyToast.ERROR, false).show();

                        }

                        @Override
                        public void onResponse(String s, int i) {
                            System.out.println("删除设备数据成功");
                            ResultVO resultVO = new Gson().fromJson(s, ResultVO.class);
                            System.out.println( resultVO );
                            if ( resultVO.getCode().equals("200")){

                                FancyToast.makeText( SettingAbilitySlice.this, "删除数据成功", 3000,FancyToast.SUCCESS, false).show();
                            }else {
                                FancyToast.makeText( SettingAbilitySlice.this, "删除数据失败", 3000,FancyToast.ERROR, false).show();

                            }
                        }
                    });
        });
    }


    /**
     * 删除这一段时间内的数据
     * @param start
     * @param end
     */
    private void appData_deleteHistory( String start, String end ) {

        if (StringUtils.isEmpty( start ) ){

            if ( StringUtils.isEmpty( end )){
                appData_deleteHistory();
            }else {
                appData_deleteHistory( end );
            }

            return ;
        }else {
            if ( StringUtils.isEmpty( end )){
                appData_deleteHistory( start );
                return ;
            }
        }

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                .get()
                .addParams("token", Constans.TOKEN )
                .addParams("device_id", MainAbilitySlice.deviceId)
                .addParams("device_type", MainAbilitySlice.deviceType)
                .addParams("start", start)
                .addParams("end", end)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        System.out.println("删除设备数据失败");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        System.out.println("删除设备数据成功");
                        ResultVO resultVO = new Gson().fromJson(s, ResultVO.class);
                        if ( resultVO.getCode().equals("200")){
                            FancyToast.makeText( SettingAbilitySlice.this, "删除设备数据成功",3000, FancyToast.INFO, true) ;
                        }
                    }
                });
        });


    }


    /**
     * 删除设备的点击事件
     * @param context
     */
    private void deleteDevice(Context context) {
        //1、定义对话弹框
        deleteDeviceCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        deleteDeviceCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        deleteDeviceCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_deletedevice_common, null, false);

        deleteCancelBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        deleteOkBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok);

        // 设置点击事件
        deleteCancelBtn.setClickedListener(this);
        deleteOkBtn.setClickedListener(this);

        //将XML文件中的布局交给吐司弹框
        deleteDeviceCommonDialog.setContentCustomComponent(dl);
        deleteDeviceCommonDialog.setAutoClosable( false );
        //弹框显示时间
        deleteDeviceCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        deleteDeviceCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        deleteDeviceCommonDialog.show();
    }


    /**
     * 添加房间的弹框的确定按钮的点击事件
     * @param context
     */
    private void addRoomClick(Context context) {
        String roomName = addRoomNameTf.getText().trim();

        // 如果输入为空，则给出提示信息
        if ( "".equals(roomName) ){
            addRoomTipInfoText.setVisibility( Component.VISIBLE );
            addRoomTipInfoText.setText("房间名称不能为空");
            return;
        }

        // todo: 判断添加的房间是否已经存在

        // todo: 添加 -> 需要做存储操作
        addRoomCommonDialog.hide();


    }


    /**
     * 添加房间按钮的点击事件
     */
    private void addRoom( Context context) {
        // 1. 弹框
        //1、定义对话弹框
        addRoomCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        addRoomCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        addRoomCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_devicename_common, null, false);

        Text title = (Text) dl.findComponentById(ResourceTable.Id_text_title);
        addRoomCancelBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        addRoomOkBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok);
        addRoomNameTf = (TextField) dl.findComponentById(ResourceTable.Id_tf_device_name);
        addRoomTipInfoText = (Text) dl.findComponentById(ResourceTable.Id_text_tip_info);

        title.setText("房间名称");
        addRoomNameTf.setHint("请输入房间类型");
        addRoomTipInfoText.setVisibility( Component.INVISIBLE );

        // 设置点击事件
        addRoomCancelBtn.setClickedListener(this);
        addRoomOkBtn.setClickedListener(this);

        //将XML文件中的布局交给吐司弹框
        addRoomCommonDialog.setContentCustomComponent(dl);
        addRoomCommonDialog.setAutoClosable( false );
        //弹框显示时间
        addRoomCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        addRoomCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        addRoomCommonDialog.show();

        // 2. 修改值

    }


    /**
     * 设置所属空间
     */
    private void setBelongSpace( Context context) throws IOException, NotExistException {
        //1、定义对话弹框
        belongSpaceCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        belongSpaceCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        belongSpaceCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_belongspace_common, null, false);

        cancelBelongBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        cancelBelongBtn.setClickedListener(this);

        // todo:添加房间
        addRoomBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_add_room);
        addRoomBtn.setClickedListener( this );

        radioContainer = (RadioContainer) dl.findComponentById(ResourceTable.Id_radioContainer);
        int count = radioContainer.getChildCount() ;
        for (int i = 0; i < count; i = i+2) {
            RadioButton radioButton = (RadioButton) radioContainer.getComponentAt(i);
            if ( radioButton.getText().equals(belongSpaceText.getText())){
                radioContainer.mark( i );
                PixelMapElement pixelMapElement = new PixelMapElement( this.getResourceManager().getResource( ResourceTable.Media_radio_seleceted));
                radioButton.setAroundElements(null,null, pixelMapElement,null);
            }else {
                PixelMapElement pixelMapElement = new PixelMapElement( this.getResourceManager().getResource( ResourceTable.Media_radio_unSelect));
                radioButton.setAroundElements(null,null, pixelMapElement,null);
            }
        }

        radioContainer.setMarkChangedListener( this );

        //将XML文件中的布局交给对话弹框
        belongSpaceCommonDialog.setContentCustomComponent(dl);
        belongSpaceCommonDialog.setAutoClosable( false );
        //弹框显示时间
        belongSpaceCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        belongSpaceCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        belongSpaceCommonDialog.show();
    }


    /**
     * 更改设备名称
     */
    private void changeDeviceName() {
        // 获取设置的设备名称
        String deviceName = deviceNameTf.getText().trim() ;

        // 如果输入为空，则给出提示信息
        if ( "".equals(deviceName) ){
            tipInfoText.setText("设备名称不能为空");
            return;
        }

        // 如果设备名称不发生改变,则直接返回
        if ( deviceNameText.getText().equals(deviceName) ){
            return ;
        }

        // 否则将修改后的设备名称保存到 deviceName.txt文件中
        deviceNameText.setText( deviceName );

        //获取文件目录
        File dataDir = new File(this.getDataDir().toString());
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }

        FileOutputStream  fos = null ;
        //目标文件
        File targetFile = new File(Paths.get(dataDir.toString(),"deviceName.txt").toString());

        try {
            fos = new FileOutputStream(targetFile);
            fos.write( deviceName.getBytes() );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置设备名称
     */
    private void setDeviceName(Context context) {
        //1、定义对话弹框
        deviceNameCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        deviceNameCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        deviceNameCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_devicename_common, null, false);

        cancelBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        okBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok);
        deviceNameTf = (TextField) dl.findComponentById(ResourceTable.Id_tf_device_name);
        tipInfoText = (Text) dl.findComponentById(ResourceTable.Id_text_tip_info);

        // 设置点击事件
        cancelBtn.setClickedListener(this);
        okBtn.setClickedListener(this);

        //将XML文件中的布局交给吐司弹框
        deviceNameCommonDialog.setContentCustomComponent(dl);
        deviceNameCommonDialog.setAutoClosable( false );
        //弹框显示时间
        deviceNameCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        deviceNameCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        deviceNameCommonDialog.show();
    }


    @Override
    public void onCheckedChanged(RadioContainer radioContainer, int index) {
        String roomName = ((RadioButton) radioContainer.getComponentAt(index)).getText();

        belongSpaceText.setText( roomName );
        //获取文件目录
        File dataDir = new File(this.getDataDir().toString());
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }

        FileOutputStream  fos = null ;
        //目标文件
        File targetFile = new File(Paths.get(dataDir.toString(),"belongSpace.txt").toString());
        System.out.println( Paths.get(dataDir.toString() ) );


        try {
            fos = new FileOutputStream(targetFile);
            fos.write( roomName.getBytes() );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        belongSpaceCommonDialog.hide();
    }
}
