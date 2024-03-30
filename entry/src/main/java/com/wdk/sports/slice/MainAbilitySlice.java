package com.wdk.sports.slice;

import com.allenliu.badgeview.BadgeFactory;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.*;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.github.ybq.core.style.FadingCircle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParams;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.hms.accountsdk.support.account.service.AccountAuthService;
import com.huawei.hms.accountsdk.support.account.tasks.OnFailureListener;
import com.huawei.hms.accountsdk.support.account.tasks.OnSuccessListener;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wdk.sports.ResourceTable;
import com.wdk.sports.UpgradeMainAbility;
import com.wdk.sports.VideoMainAbility;
import com.wdk.sports.domain.*;
import com.wdk.sports.provider.InformationPageSlider;
import com.wdk.sports.util.AES;
import com.wdk.sports.util.Constans;
import com.wdk.sports.util.HexConverter;
import com.wdk.sports.util.StringUtils;
import com.zhy.http.library.OkHttpUtils;
import com.zhy.http.library.callback.FileCallBack;
import com.zhy.http.library.callback.StringCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.bluetooth.BluetoothRemoteDevice;
import ohos.bluetooth.ble.*;
import ohos.global.icu.lang.UCharacter;
import ohos.global.resource.RawFileDescriptor;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.common.Source;
import ohos.media.player.Player;
import okhttp3.Call;
import okhttp3.MediaType;
import org.devio.hi.json.HiJson;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener, Picker.ValueChangedListener {
    private BluetoothRemoteDevice device = null ;
    public static BleManager bleManager;
    public static BleDevice bleDevice;

    private Button upgradeBtn = null;
    private Button dataBtn = null;

    private CommonDialog timePickereCommonDialog;
    private Button okTimePickerBtn;
    private Button cancelTimePickerBtn ;
    private Picker pickerTime;
    private int pickerNewValue;
    private Button btn_mode_time;


    private Button reconnectBtn;
    private Button videoPlayBtn01;
    private Button videoPlayBtn02;
    private Button videoPlayBtn03;
    private Button musicPlayBtn01;
    private Button musicPlayBtn02;
    private Button musicPlayBtn03;

    private Player mPlayer;

    private Image musicPlayImg01;
    private Image musicPlayImg02;
    private Image musicPlayImg03;
    private Button historyBtn;

    private DirectionalLayout connectionState;
    private Component unconnectTemplate;
    private Component connectingTemplate;
    private Component finishedTemplate;
    private Component stopTemplate;
    private Component runningTemplate;
    private PageSlider pageSlider;
    private DirectionalLayout sliceRunning;
    private DirectionalLayout parse04; // 速度和档位
    public static Slider progressSlider; // 档位的进度条
    private Text textGear; // 档位的文本值
    private Text runningTimeText; // 运动时间
    private int historyGear;
    private Integer currentMode = 0; // 当前的模式： 1->自由模式    2->倒计时模式
    private Integer showHistoryGear; // 下次要继续显示的档位
    private CommonDialog endSportCommonDialog; // 结束运动的弹框
    private int sportMaxSpeed = 0; // 最大速度


    public static String deviceId = "11:11:11:22:01:EF";
    public static String deviceType = "";
    private Button btn_choose_time;

    private int max_rotation_counts = 0;
    private int current_rotation_counts = 0;
    private String interrupt;
    private CommonDialog connectOverTimeDialog;
    private Button waitTryDialogBtn;
    private Button reconnectDialogBtn;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String startSportTime = "";
    private int timeInteger; // 运动总时长


    private AtomicReference<Boolean> isSelected;
    private DirectionalLayout chooseMode;
    private DirectionalLayout chooseTime;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        initBlueTooth();

        initComponent();
        /*
            以下是设置运动状态为运动中时的界面
         */
        initView();

        // 初始化点击事件
        initComponentClickListener();
    }


    /**
     * 初始化蓝牙
     */
    private void initBlueTooth() {
        bleManager = BleManager.getInstance();
        bleManager.init(this);

        bleManager.enableLog( true )
                .setReConnectCount(1,5000) // 重连次数
                .setConnectOverTime(20000)
                .setOperateTimeout( 20000 );

        if ( bleManager.isSupportBle() ){
            // 开启蓝牙
            bleManager.openBluetooth();
        }

        AbilitySlice _this = this ;
        BlePeripheralDevice blePeripheralDevice = BlePeripheralDevice.createInstance(deviceId);
        this.bleDevice = new BleDevice(blePeripheralDevice);
    }

    private void initComponent() {
        runningTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_running, null, false);
        runningTimeText = (Text) runningTemplate.findComponentById(ResourceTable.Id_running_time);

        stopTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_stop, null, false);
        finishedTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_finished, null, false);
        connectingTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_connecting, null, false);
        unconnectTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_unconnect, null, false);
        sliceRunning = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_slice_running, null, false);

    }


    private void initComponentClickListener() {
        upgradeBtn.setClickedListener( this );
        dataBtn.setClickedListener( this );

        videoPlayBtn01.setClickedListener( this );
        videoPlayBtn02.setClickedListener( this );
        videoPlayBtn03.setClickedListener( this );

        musicPlayBtn01.setClickedListener(this);
        musicPlayBtn02.setClickedListener(this);
        musicPlayBtn03.setClickedListener(this);

    }


    /**
     * 初始化界面
     */
    private void initView() {

        // 设置运动状态到界面
        // 设置标题栏设置按钮的点击事件
        Button toSettingBtn = (Button) sliceRunning.findComponentById(ResourceTable.Id_toSetting);
        toSettingBtn.setClickedListener( component -> {
            if ( !StringUtils.isEmpty( Constans.TOKEN ) )
                this.present( new SettingAbilitySlice(), new Intent() );
            else
                FancyToast.makeText( MainAbilitySlice.this, "请先连接蓝牙", 3000, FancyToast.INFO, false );
        });

        connectionState = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_connection_state);


        reconnectBtn = (Button) unconnectTemplate.findComponentById(ResourceTable.Id_reconnect);
        reconnectBtn.setClickedListener( this );

        connectionState.removeAllComponents();
        connectionState.addComponent( unconnectTemplate );


//        initReConnectButtonClick(connectionState,connectingTemplate,unconnectTemplate,runningTemplate);

        // runningclose按钮的点击事件
        initCloseButtonClick( connectionState,runningTemplate,finishedTemplate,"running");
        // runningstop按钮的点击事件
        initStopButtonClick( connectionState,runningTemplate,stopTemplate);

        // stop_close的点击事件
        initCloseButtonClick( connectionState,stopTemplate,finishedTemplate,"stop");

        // stop_start的点击事件
        initStartButtonClick( connectionState,stopTemplate,runningTemplate,"stop");

        // finished_start的点击事件
        initStartButtonClick( connectionState,finishedTemplate,runningTemplate,"finished");

        // 初始化pageSlider
        pageSlider = (PageSlider) sliceRunning.findComponentById(ResourceTable.Id_page_slider);
        PageSliderIndicator pageSliderIndicator = (PageSliderIndicator) sliceRunning.findComponentById(ResourceTable.Id_indicator);

        ShapeElement normalElement = new ShapeElement(this, ResourceTable.Graphic_unselected_page_bg_element);
        ShapeElement selectedElement = new ShapeElement(this, ResourceTable.Graphic_selected_page_bg_element);
        pageSliderIndicator.setItemElement(normalElement, selectedElement);

        pageSliderIndicator.setItemOffset(40);

        List<Integer> layoutFileIds = new ArrayList<>() ;
        layoutFileIds.add( ResourceTable.Layout_ability_pageslider01 );
        layoutFileIds.add( ResourceTable.Layout_ability_pageslider02);

        pageSlider.setProvider(new InformationPageSlider(this, layoutFileIds));
        pageSliderIndicator.setPageSlider(pageSlider);

        // 设置模式和定时到界面
        DirectionalLayout mode_time = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_mode_time);
        DirectionalLayout parse02 = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_mode_time, null, false);
        mode_time.addComponent( parse02 );

        // 给模式按钮设置点击事件
        isSelected = new AtomicReference<>(true);// mode被选中时为true，time被选中时为false
        chooseMode = (DirectionalLayout) parse02.findComponentById(ResourceTable.Id_choose_mode);
        chooseTime = (DirectionalLayout) parse02.findComponentById(ResourceTable.Id_choose_time);
        List<String> list = new ArrayList<>() ;
        list.add("自由模式");
        list.add("热身模式");
        list.add("中速模式");
        list.add("高速模式");
        chooseMode.setClickedListener(component->{
            // 设置当前选中按钮的背景色
            DirectionalLayout component1 = (DirectionalLayout) chooseMode.getComponentAt(0);
            ShapeElement backgroundElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_use);
            component1.setBackground( backgroundElement );
            // 设置当前未选中按钮的背景色
            DirectionalLayout component2 = (DirectionalLayout) chooseTime.getComponentAt(0);
            ShapeElement backgroundElement2 = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_nouse);
            component2.setBackground( backgroundElement2 );

            // 设置字体色
            Button btn_choose_mode = (Button) chooseMode.getComponentAt(1);
            Button btn_choose_time = (Button) chooseTime.getComponentAt(1);

            Element rightElement_blue = null;
            Element rightElement_black = null;
            if ( !isSelected.get()  ){
                rightElement_blue = btn_choose_time.getRightElement();
                rightElement_black = btn_choose_mode.getRightElement();
            }else {
                rightElement_black = btn_choose_time.getRightElement();
                rightElement_blue = btn_choose_mode.getRightElement();
            }
            isSelected.set(true);

            btn_choose_mode.setTextColor(new Color(Color.getIntColor("#0A59F7")));
            btn_choose_mode.setAroundElements(null,null, rightElement_blue,null);
            btn_choose_time.setTextColor(new Color(Color.getIntColor("#52594E")));
            btn_choose_time.setAroundElements(null,null, rightElement_black,null);

            createPopupDialog(getContext(), list,chooseMode);
        });

        // 给定时按钮设置点击事件
        List<String> list2 = new ArrayList<>() ;
        list2.add("1分钟");
        list2.add("5分钟");
        list2.add("10分钟");
        list2.add("自定义");
        chooseTime.setClickedListener(component->{
            // 设置当前选中按钮的背景色
            DirectionalLayout component1 = (DirectionalLayout) chooseTime.getComponentAt(0);
            ShapeElement backgroundElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_use);
            component1.setBackground( backgroundElement );
            // 设置当前未选中按钮的背景色
            DirectionalLayout component2 = (DirectionalLayout) chooseMode.getComponentAt(0);
            ShapeElement backgroundElement2 = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_nouse);
            component2.setBackground( backgroundElement2 );


            // 设置字体色
            btn_choose_time = (Button) chooseTime.getComponentAt(1);
            Button btn_choose_mode = (Button) chooseMode.getComponentAt(1);

            Element rightElement_blue = null;
            Element rightElement_black = null;
            if ( !isSelected.get()  ){
                rightElement_blue = btn_choose_time.getRightElement();
                rightElement_black = btn_choose_mode.getRightElement();
            }else {
                rightElement_black = btn_choose_time.getRightElement();
                rightElement_blue = btn_choose_mode.getRightElement();
            }
            isSelected.set(false);

            btn_choose_time.setTextColor(new Color(Color.getIntColor("#0A59F7")));
            btn_choose_time.setAroundElements(null,null, rightElement_blue,null);
            btn_choose_mode.setTextColor(new Color(Color.getIntColor("#52594E")));
            btn_choose_mode.setAroundElements(null,null, rightElement_black,null);

            createPopupDialog(getContext(), list2, chooseTime);
        });

        // 设置档位速度到界面
        DirectionalLayout gear_speed = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_speed_gear);
        parse04 = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_gear_speed, null, false);
        gear_speed.addComponent( parse04 );

        progressSlider = (Slider) parse04.findComponentById( ResourceTable.Id_progressSlider);
        textGear = (Text) ((DirectionalLayout) parse04.getComponentAt(0)).getComponentAt(1);
        progressSlider.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int i, boolean b) {   }

            @Override
            public void onTouchStart(Slider slider) {}

            @Override
            public void onTouchEnd(Slider slider) {
                // 设置档位
                int progress = slider.getProgress();
                int gearValue = progress / 10 + 1 ;

                if ( progress == 0 ) gearValue = 0;
                else if(progress == 120 ) gearValue = 12;
                System.out.println( progressSlider.getProgress() );

                if ( InformationPageSlider.currentSpeed.equals("0")){
                    // 如果还没开始运动，则不需要下发
                    return ;
                }

                if ( gearValue == historyGear ){
                    // 档位不发生变化，则不需要进行下发
                    return ;
                }

                if ( gearValue == 0 && historyGear != 0 ){
                    // 暂停
//                    String runningTime = ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
//                    connectionState.removeAllComponents();
//                    ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText( runningTime );
//                    connectionState.addComponent(stopTemplate);
                    showHistoryGear = historyGear ;
                }

                if ( historyGear == 0 && gearValue != 0 ){
                    // 继续
//                    String stopTime = ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).getText();
//                    connectionState.removeAllComponents();
//                    ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText( stopTime );
//                    connectionState.addComponent(runningTemplate);
                }
                historyGear = gearValue ;
                textGear.setText( gearValue + "档");

                // 获取命令的10进制,并转换为16进制
                requestDeviceGear( gearValue );
            }
        });

        // 设置固件升级、数据统计到界面
        DirectionalLayout upgrade_data = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_upgrade_data);
        DirectionalLayout parse05 = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_upgrade_data, null, false);
        upgrade_data.addComponent( parse05 );

        upgradeBtn = (Button) parse05.findComponentById(ResourceTable.Id_btn_upgrade);
        dataBtn = (Button) parse05.findComponentById(ResourceTable.Id_btn_data );

        // 设置背景音乐和使用视频
        DirectionalLayout vedio_music = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_video_music);
        DirectionalLayout videoTemplate = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_videos, null, false);
        videoPlayBtn01 = (Button) videoTemplate.findComponentById(ResourceTable.Id_btn_video_start01);
        videoPlayBtn02 = (Button) videoTemplate.findComponentById(ResourceTable.Id_btn_video_start02);
        videoPlayBtn03 = (Button) videoTemplate.findComponentById(ResourceTable.Id_btn_video_start03);


        DirectionalLayout musicTemplate = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_musics, null, false);
        musicPlayBtn01 = (Button) musicTemplate.findComponentById(ResourceTable.Id_btn_music01);
        musicPlayBtn02 = (Button) musicTemplate.findComponentById(ResourceTable.Id_btn_music02);
        musicPlayBtn03 = (Button) musicTemplate.findComponentById(ResourceTable.Id_btn_music03);

        musicPlayImg01 = (Image) musicTemplate.findComponentById(ResourceTable.Id_img_music01);
        musicPlayImg02 = (Image) musicTemplate.findComponentById(ResourceTable.Id_img_music02);
        musicPlayImg03 = (Image) musicTemplate.findComponentById(ResourceTable.Id_img_music03);



        vedio_music.addComponent( videoTemplate );

        Button useVedio = (Button) sliceRunning.findComponentById(ResourceTable.Id_btn_use_video);
        Button useVedioLine = (Button) sliceRunning.findComponentById(ResourceTable.Id_btn_use_video_line);
        Button useMusicLine = (Button) sliceRunning.findComponentById(ResourceTable.Id_btn_use_music_line);
        Button useMusic = (Button) sliceRunning.findComponentById(ResourceTable.Id_btn_use_music);
        useVedio.setClickedListener(component -> {
            vedioOrMusicClickFunction(useVedio,useMusic,useVedioLine,useMusicLine,1);
            vedio_music.removeAllComponents();
            vedio_music.addComponent( videoTemplate );
        });
        useMusic.setClickedListener(component -> {
            vedioOrMusicClickFunction( useVedio,useMusic,useVedioLine,useMusicLine,2);
            vedio_music.removeAllComponents();
            vedio_music.addComponent( musicTemplate );
        });

        // 最后将sliceRunning设置到mainAbilitySlice中
        DirectionalLayout mainBody = (DirectionalLayout) this.findComponentById(ResourceTable.Id_mainBody);

        mainBody.removeAllComponents();
        mainBody.addComponent( sliceRunning );
    }


    /**
     * 下发档位设置的命令
     * @param gearValue 档位值，10进制
     */
    private void requestDeviceGear(int gearValue) {
        String s = HexConverter.decimalToHexString( 261 + gearValue);
        String s1 = HexConverter.decimalToHexString(gearValue);
        s1 = s1.length() <= 1 ? "0"+s1 : s1 ;

        // 进行取反+1
        String backAndAddOne = HexConverter.backAndAddOne( "0" + s);
        String dataStr = "aa55030201" + s1 + backAndAddOne.substring( backAndAddOne.length()-2 ) ;
        // 下发档位设置
        BleManager.getInstance().write(bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes(dataStr ),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println("设置档位成功");
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println("设置档位失败");

                    }
                });
    }


    /**
     * 蓝牙未连接时的界面
     */
    private void unconnectView(){
        /*
            以下是设置运动状态为非运动时的界面
         */
        Component sliceRunning = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_slice_running, null, false);
        DirectionalLayout connectionState = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_connection_state);

        Component parse = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_connecting, null, false);
        connectionState.addComponent( parse );

        // 初始化pageSlider
        PageSlider pageSlider = (PageSlider) sliceRunning.findComponentById(ResourceTable.Id_page_slider);
        List<Integer> layoutFileIds = new ArrayList<>() ;
        layoutFileIds.add( ResourceTable.Layout_ability_pageslider01_norunning);
        layoutFileIds.add( ResourceTable.Layout_ability_pageslider02_norunning);

        pageSlider.setProvider(new InformationPageSlider(this, layoutFileIds));

        // 设置模式和定时到界面
        DirectionalLayout mode_time = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_mode_time);
        Component parse02 = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_mode_time_norunning, null, false);
        mode_time.addComponent( parse02 );

        // 设置档位速度到界面
        DirectionalLayout gear_speed = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_speed_gear);
        Component parse04 = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_gear_speed_norunning, null, false);
        gear_speed.addComponent( parse04 );

        // 设置固件升级、数据统计到界面
        DirectionalLayout upgrade_data = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_upgrade_data);
        Component parse05 = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_upgrade_data_norunning, null, false);
        upgrade_data.addComponent( parse05 );

        // 设置背景音乐和使用视频
        DirectionalLayout vedio_music = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_video_music);
        Component parse03 = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_ability_videos, null, false);
        vedio_music.addComponent( parse03 );

        // 最后将sliceRunning设置到mainAbilitySlice中
        DirectionalLayout mainBody = (DirectionalLayout) this.findComponentById(ResourceTable.Id_mainBody);
        mainBody.addComponent( sliceRunning );
    }

    /**
     * 点击重新连接
     * @param connectionState   父容器
     * @param connectingTemplate    正在连接模板
     */
    private void initReConnectButtonClick(DirectionalLayout connectionState, Component connectingTemplate,
             Component unconnectTemplate, Component runningTemplate) {
        Button reConnectBtn = (Button) unconnectTemplate.findComponentById(ResourceTable.Id_reconnect);
        reConnectBtn.setClickedListener( (component)->{
            this.connectBlueTooth( );
        });



    }




    /**
     * 连接蓝牙
     */
    private void connectBlueTooth() {

        bleManager.connect( this.bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                bleDevice.setConnectState("连接中…");

                connectionState.removeAllComponents();
                connectionState.addComponent( connectingTemplate );
                ProgressBar connectingImage = (ProgressBar) connectingTemplate.findComponentById(ResourceTable.Id_progressbar_connecting);

                FadingCircle fadingCircle = new FadingCircle();
                fadingCircle.setPaintColor(0X666666);
                fadingCircle.onBoundsChange(0, 0, connectingImage.getWidth(), connectingImage.getHeight());
                fadingCircle.setComponent(connectingImage);
                connectingImage.setProgressElement(fadingCircle);
                connectingImage.setIndeterminate(true);
                connectingImage.addDrawTask((component, canvas) -> fadingCircle.drawToCanvas(canvas));
            }

            @Override
            public void onConnectFail(BleDevice device, BleException e) {
                System.out.println("连接失败" + e);
                bleDevice.setConnectState("连接失败");

               connectionState.removeAllComponents();
               connectionState.addComponent( unconnectTemplate );

               showConnectOverTimeDialog( MainAbilitySlice.this );
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BlePeripheralDevice blePeripheralDevice, int i) {
                bleDevice.setConnectState("连接成功");

                connectionState.removeAllComponents();
                connectionState.addComponent( runningTemplate );

                Device device = new Device();
                device.setDevice_id("11:11:11:22:01:EF");
                device.setProduct_id("2HGR");
                device.setProduct_type("LYTSJ-004");
                // 订阅设备的通知
                BleManager.getInstance().notify(
                        bleDevice,
                        "0000fff0-0000-1000-8000-00805f9b34fb",
                        "0000fff1-0000-1000-8000-00805f9b34fb",
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                // 打开通知操作成功
                                System.out.println("打开通知操作成功");
                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {
                                // 打开通知操作失败
                                System.out.println("打开通知操作失败");

                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {

                                // 打开通知后，设备发过来的数据将在这里出现
                                String hex = HexConverter.byte2Hex( data );

                                if ( hex.length() <= 4 ){
                                    return ;
                                }

                                String commandLine = hex.substring(6, 8);
                                if ( "02".equals(commandLine)){
                                    // 上报电量
                                   showDeviceCharge( hex );

                                }else if ( "04".equals( commandLine  )){
                                    // 上报设备运动信息
                                    System.out.println("上报设备运动信息:::" + hex);

                                    // 如果不是结束状态
                                    showSportInfomation( hex );

                                }else if ( "06".equalsIgnoreCase( commandLine )){
                                    // 上报设备厂家信息
                                    String manufacturerName = hex.substring(8, hex.length()-2);
                                    byte[] bytes = HexConverter.hexTobytes(manufacturerName);
                                    if ( DeviceInfoAbilitySlice.manufacturerName != null )
                                        DeviceInfoAbilitySlice.manufacturerName.setText( new String( bytes ) );
                                }else if ( "07".equalsIgnoreCase( commandLine )){
                                    // 上报设备型号
                                    String deviceNumber = hex.substring(8, hex.length()-2);
                                    byte[] bytes = HexConverter.hexTobytes(deviceNumber);
                                    deviceType = new String( bytes ) + "-004" ;
                                    device.setDevice_type( deviceType );

                                    try {
                                        // 1.组成字符串
                                        String tokenStr = "deviceId=" + deviceId + "&&deviceType=" + deviceType;
                                        // 2. AES以及BASE64加密
                                        Constans.TOKEN = AES.aesEncryptToBytes(tokenStr, Constans.SECRETKEY);

                                        // 测试解码
//                                        String s = AES.aesDecryptByBytes(Constans.TOKEN, Constans.SECRETKEY);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if ( DeviceInfoAbilitySlice.deviceNumber != null )
                                        DeviceInfoAbilitySlice.deviceNumber.setText( new String( bytes ) );
                                }else if ( "08".equalsIgnoreCase( commandLine )){
                                    // 上报设备序列号
                                    String sequence = hex.substring(8, hex.length()-2);
                                    byte[] bytes = HexConverter.hexTobytes(sequence);
                                    device.setBatch_production( new String( bytes ));

                                }else if ( "09".equalsIgnoreCase( commandLine )){
                                    // 上报设备软件版本
                                    String version = hex.substring(8, hex.length()-2);
                                    byte[] bytes = HexConverter.hexTobytes(version);
                                    device.setSoftware_version( new String( bytes ));

                                    if ( DeviceInfoAbilitySlice.version != null )
                                        DeviceInfoAbilitySlice.version.setText(  new String( bytes )   );

                                }else if ( "0a".equalsIgnoreCase( commandLine )){
                                    // 上报设备硬件版本
                                    String version = hex.substring(8, hex.length()-2);
                                    byte[] bytes = HexConverter.hexTobytes(version);
                                    device.setHardware_version( new String( bytes ));

                                }else if ( "0b".equalsIgnoreCase( commandLine )){
                                    // 上报最大档位
                                    System.out.println(  "最大档位 : " + hex );
                                }else if ( "ff".equalsIgnoreCase( commandLine )){
                                    // 上报最大档位
                                    System.out.println(  "设备关机 : " + hex );
                                }

                            }
                        });


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // ota升级的订阅通知，ffc1
                BleManager.getInstance().notify(
                        bleDevice,
                        "f000ffc0-0451-4000-b000-000000000000",
                        "f000ffc1-0451-4000-b000-000000000000",
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                System.out.println( "ota升级的订阅通知成功" );
                            }

                            @Override
                            public void onNotifyFailure(BleException e) {
                                System.out.println( "ota升级的订阅通知失败" );
                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {
                                System.out.println( "ota升级的订阅通知消息:>>>>" + HexConverter.byte2Hex(data ));

                                String hex = HexConverter.byte2Hex(data);
                                String version = HexConverter.hexStringToDecimal( hex.substring( 2,4) )
                                        + "."
                                        + HexConverter.hexStringToDecimal( hex.substring( 0,2) );

                                Constans.currentCode = version;

                                appOTA_query( version );
                            }
                        }
                );

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 请求电量
                requestCharge();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 请求设备信息
                requestDeviceInfo();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 请求自由模式
                requestFreeMode();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 连接成功之后，开始运动
                requestDeviceGear( 3 );
                startSportTime = simpleDateFormat.format( new Date());

                // 获取ota信息1
                BleManager.getInstance().write(
                        bleDevice,
                        "f000ffc0-0451-4000-b000-000000000000",
                        "f000ffc1-0451-4000-b000-000000000000",
                        HexUtil.hexStringToBytes("00"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int i, int i1, byte[] bytes) {
                                System.out.println( "第一次发生00成功");
                            }

                            @Override
                            public void onWriteFailure(BleException e) {
                                System.out.println( "第一次发生00失败");

                            }
                        }
                );
                try {
                    Thread.sleep( 100 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 获取ota信息2
                BleManager.getInstance().write(
                        bleDevice,
                        "f000ffc0-0451-4000-b000-000000000000",
                        "f000ffc1-0451-4000-b000-000000000000",
                        HexUtil.hexStringToBytes("00"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int i, int i1, byte[] bytes) {
                                System.out.println( "第二次发生00成功");
                            }

                            @Override
                            public void onWriteFailure(BleException e) {
                                System.out.println( "第二次发生00失败");

                            }
                        }
                );

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BlePeripheralDevice blePeripheralDevice, int status) {
                bleDevice.setConnectState("连接断开");

                connectionState.removeAllComponents();
                connectionState.addComponent( unconnectTemplate );

                BleManager.getInstance().stopNotify(bleDevice,
                        "0000fff0-0000-1000-8000-00805f9b34fb",
                        "0000fff1-0000-1000-8000-00805f9b34fb");

                InformationPageSlider.deviceCharge.setText("0");
                InformationPageSlider.quanNumber.setText("0");
                InformationPageSlider.banShengNumber.setText("0");
                InformationPageSlider.currentSpeed.setText("0");

                currentMode = 0;
                sportMaxSpeed = 0;
                historyGear = 0;
                showHistoryGear = 0;
            }
        });

    }


    /**
     * 查询OTA
     * @param version
     */
    private void appOTA_query(String version) {
        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .get()
                    .addParams("product_id","2HGR")
                    .addParams("product_type","LYTSJ-004")
                    .url( Constans.HTTP_HEAD + "/appOTA/query")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {}

                        @Override
                        public void onResponse(String s, int i) {
                            String version_code = new HiJson(s).get("data").value("version_code");
                            String substring = version_code.substring(version_code.indexOf(".") + 1);
                            Integer integer = HexConverter.hexStringToDecimal(substring);
                            version_code = version_code.substring(0, version_code.indexOf(".") + 1) + integer ;

                            if ( !version.equals( version_code )){
                                Constans.UPGRADE_STATE =1 ;

                                Constans.isVersionCode = true ;
                                Constans.versionAddress =  new HiJson(s).get("data").value("upgrade_link");
                                Constans.versionFileName =  new HiJson(s).get("data").value("package_name");
                                Constans.versionCode =  version_code;
                                // 绑定一个小红点
                                BadgeFactory.createDot(MainAbilitySlice.this).setBadgeCount(1).bind(upgradeBtn);

                                //获取文件目录
                                File dataDir = new File(MainAbilitySlice.this.getDataDir().toString());
                                if(!dataDir.exists()){
                                    dataDir.mkdirs();
                                }

                                OkHttpUtils.get()
                                        .url( Constans.versionAddress )
                                        .build()
                                        .execute(new FileCallBack(dataDir.toString(), Constans.versionFileName ) {
                                            @Override
                                            public void onError(Call call, Exception e, int i) {
                                                System.out.println("下载文件失败");
                                            }

                                            @Override
                                            public void onResponse(File file, int i) {
                                                System.out.println( file.getAbsolutePath() + "-->" + i );
                                                String s = (file.length() / 1024.0) + "";
                                                Constans.newVersioSize =  "大小:" + s+ "M" ;

                                                try {
                                                    Constans.fis = new FileInputStream(file);

                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void inProgress(float progress, long l, int i) {

                                            }
                                        });

                            }else {
                                Constans.UPGRADE_STATE = 0; // 无更新
                            }
                        }
                    });
        });


    }


    /**
     * 上报用户信息
     * @param user
     */
    private void appUser_reportInfo(User user) {
        getGlobalTaskDispatcher( TaskPriority.DEFAULT ).asyncDispatch( ()->{
            OkHttpUtils
                    .postString()
                    .content( new Gson().toJson( user ))
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .addHeader("time", new Date().getTime() + "")
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appUser/reportInfo")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("上报用户信息失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            System.out.println("上报用户信息成功，返回的数据为:" + new Gson().fromJson(s, new ResultVO<User>().getClass() ) );
                        }
                    });
        });
    }


    /**
     * 上报设备信息
     */
    private void appDevice_reportInfo() {
        getGlobalTaskDispatcher( TaskPriority.DEFAULT ).asyncDispatch( ()->{
            while( Constans.TOKEN == null ){
                continue;
            }
            OkHttpUtils
                    .postString()
                    .content( new Gson().toJson( device ))
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .addHeader("time", new Date().getTime() + "")
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appDevice/reportInfo")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("上报设备信息失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            System.out.println( device );
                            System.out.println("上报设备信息成功，返回的数据为:" + new Gson().fromJson(s, ResultVO.class ));
                        }
                    });
        });
    }


    /**
     * 获取用户信息
     */
    private void appUser_getInfo() {
        getGlobalTaskDispatcher( TaskPriority.DEFAULT ).asyncDispatch( ()->{
            OkHttpUtils
                    .get()
                    .addHeader("time", new Date().getTime() + "")
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appUser/getInfo")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("获取用户信息失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            System.out.println("获取用户信息成功，返回的数据为:" + new Gson().fromJson(s, ResultVO.class ));
                        }
                    });
        });
    }


    /**
     * 连接失败后弹出重新连接的弹框
     */
    private void showConnectOverTimeDialog( Context context) {

        //1、定义对话弹框
        connectOverTimeDialog = new CommonDialog(context);
        //设置弹框的大小
        connectOverTimeDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        connectOverTimeDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_connect_overtime_common, null, false);
        waitTryDialogBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_wait_try);
        reconnectDialogBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_reconnect);

        // 设置点击事件
        waitTryDialogBtn.setClickedListener(this);
        reconnectDialogBtn.setClickedListener(this);


        //将XML文件中的布局交给吐司弹框
        connectOverTimeDialog.setContentCustomComponent(dl);
        connectOverTimeDialog.setAutoClosable( false );
        //弹框显示时间
        connectOverTimeDialog.setDuration(999999999);
        //设置对话框圆角的半径
        connectOverTimeDialog.setCornerRadius(50.0f);
        //显示弹框
        connectOverTimeDialog.show();
    }


    /**
     * 请求设备信息
     */
    private void requestDeviceInfo() {
        BleManager.getInstance().write(bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes("aa55020400fb"),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println("请求设备信息成功");
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println("请求设备信息失败");
                    }
                });
    }


    /**
     * 请求设备电量
     */
    private void requestCharge() {
        BleManager.getInstance().write(bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes("aa55020200fd"),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println("请求电量成功");
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println("请求电量失败");
                    }
                });

    }


    /**
     * 显示运动数据
     * @param hex
     */
    private void showSportInfomation(String hex) {

        // 档位, 如果档位为0，并且其他信息不为0，则显示暂停按钮，否则显示完成按钮
        String gearStr = hex.substring(8, 10);
        Integer gear = HexConverter.hexStringToDecimal(gearStr);
        progressSlider.setProgressValue( gear * 10 - 1 );
        textGear.setText( gear + "档");
        if ( gear != 0 ){
            showHistoryGear = gear ;
        }
        historyGear = gear ;


        if ( "00".equals( gearStr) && !"00:00:00".equals(runningTimeText.getText()) ){

            if ( "000000000000".equals( hex.substring(14,26))){
                String runningTime = ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
                connectionState.removeAllComponents();
                ((Text) finishedTemplate.findComponentById(ResourceTable.Id_finished_time)).setText( runningTime );
                connectionState.addComponent(finishedTemplate);

                String[] split = runningTime.split(":");
                int i = Integer.parseInt(split[0]);
                int i1 = Integer.parseInt(split[1]);
                int i2 = Integer.parseInt(split[2]);
                showEndDialog( this, i + i1 + i2 );
                return ;
            }else {
                String runningTime = ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
                connectionState.removeAllComponents();
                ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText( runningTime );
                connectionState.addComponent(stopTemplate);
            }

        }else if ( !"00".equals( gearStr) ){
            // !"00".equals( gearStr) && !"00:00:00".equals(runningTimeText.getText())
            connectionState.removeAllComponents();
            connectionState.addComponent(runningTemplate);
        }


        if ( !"000000000000".equals(hex.substring( 14,26))){

            if ( StringUtils.isEmpty( startSportTime ) ){
                startSportTime = simpleDateFormat.format( new Date() );
            }

            // 开始到此刻的总秒数
            String time = hex.substring(10, 14);
            timeInteger =  HexConverter.hexStringToDecimal( time.substring(0,2) )
                    + HexConverter.hexStringToDecimal( time.substring(2,4) + "00" ) ;


            String hour = ( timeInteger / 3600 ) + "";
            hour = hour.length() <=1 ? "0"+hour : hour ;

            String min = (timeInteger % 3600 / 60) + "";
            min = min.length() <=1 ? "0"+min : min ;

            String sen = (timeInteger % 60) + "";
            sen = sen.length() <=1 ? "0"+sen : sen ;

            String timeStr = hour + ":" + min + ":" + sen ;
            runningTimeText.setText( timeStr );

            // 总次数
            String totalSkip = hex.substring(14, 18);
            if ( !InformationPageSlider.quanNumber.getText()
                    .equals(( HexConverter.hexStringToDecimal( totalSkip.substring(0,2) )
                            + HexConverter.hexStringToDecimal( totalSkip.substring(2,4) + "00") ) +"")
                    && !InformationPageSlider.quanNumber.getText().equals("0")){
                current_rotation_counts++ ;
            }
            InformationPageSlider.quanNumber.setText( ( HexConverter.hexStringToDecimal( totalSkip.substring(0,2) )
                    + HexConverter.hexStringToDecimal( totalSkip.substring(2,4) + "00") ) +"" );


            // 实时速度
            String instantaneousSpeed = hex.substring(18, 22);
            Integer integer = HexConverter.hexStringToDecimal( instantaneousSpeed.substring(0,2) )
                    + HexConverter.hexStringToDecimal( instantaneousSpeed.substring(2,4) + "00")  ;
            InformationPageSlider.currentSpeed.setText(  integer+"");
            if ( integer > sportMaxSpeed ){
                sportMaxSpeed = integer ;
            }

            // 绊绳数
            interrupt = hex.substring(22, 26);
            int interruptInteger = HexConverter.hexStringToDecimal(interrupt.substring(0, 2))
                    + HexConverter.hexStringToDecimal(interrupt.substring(2, 4) + "00");
            if ( !InformationPageSlider.banShengNumber.getText().equals(interruptInteger+"")) {
                // 如果绊绳数发生变化，则更新最大连转个数
                if ( current_rotation_counts > max_rotation_counts )
                    max_rotation_counts = current_rotation_counts;
                current_rotation_counts = 0;
            }
            // 更新绊绳数
            InformationPageSlider.banShengNumber.setText(  interruptInteger+"" );



            // 获取当前的模式
            String mode = hex.substring(30, 32);
            currentMode = HexConverter.hexStringToDecimal(mode);
        }


        if (  currentMode == 2 &&  btn_choose_time != null && !"定时".equals( btn_choose_time.getText() )){

            String choosedTime = btn_choose_time.getText().substring(0, btn_mode_time.getText().length() - 2);

            if ( timeInteger == Integer.parseInt(choosedTime)*60 ){
                // 倒计时结束
                btn_choose_time.setText("定时");
                // 设置当前选中按钮的背景色
                DirectionalLayout component1 = (DirectionalLayout) chooseMode.getComponentAt(0);
                ShapeElement backgroundElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_use);
                component1.setBackground( backgroundElement );
                // 设置当前未选中按钮的背景色
                DirectionalLayout component2 = (DirectionalLayout) chooseTime.getComponentAt(0);
                ShapeElement backgroundElement2 = new ShapeElement(getContext(), ResourceTable.Graphic_background_mode_nouse);
                component2.setBackground( backgroundElement2 );

                // 设置字体色
                Button btn_choose_mode = (Button) chooseMode.getComponentAt(1);
                Button btn_choose_time = (Button) chooseTime.getComponentAt(1);

                Element rightElement_blue = null;
                Element rightElement_black = null;
                if ( !isSelected.get()  ){
                    rightElement_blue = btn_choose_time.getRightElement();
                    rightElement_black = btn_choose_mode.getRightElement();
                }else {
                    rightElement_black = btn_choose_time.getRightElement();
                    rightElement_blue = btn_choose_mode.getRightElement();
                }
                isSelected.set(true);
                btn_choose_mode.setTextColor(new Color(Color.getIntColor("#0A59F7")));
                btn_choose_mode.setAroundElements(null,null, rightElement_blue,null);
                btn_choose_time.setTextColor(new Color(Color.getIntColor("#52594E")));
                btn_choose_time.setAroundElements(null,null, rightElement_black,null);


                String runningTime = ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
                connectionState.removeAllComponents();
                ((Text) finishedTemplate.findComponentById(ResourceTable.Id_finished_time)).setText( runningTime );
                connectionState.addComponent(finishedTemplate);

                showEndDialog( this,timeInteger );

            }
        }

    }


    /**
     * 读取设备电量
     * @param hex
     */
    private void showDeviceCharge(String hex) {
        String charge = hex.substring(8, 10); // 电量

        Constans.NOW_CHARGE = HexConverter.hexStringToDecimal( charge ) ;

        InformationPageSlider.deviceCharge.setTextColor( new Color( Color.getIntColor("#E84026")));
        InformationPageSlider.deviceChargePer.setTextColor( new Color( Color.getIntColor("#E84026")));
        DirectionalLayout shutdown = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_tip_shutdown);
        DirectionalLayout lowTip = (DirectionalLayout) sliceRunning.findComponentById(ResourceTable.Id_tip_lowTen);

        if (  charge.equalsIgnoreCase("00") ){
            // 即将关机，显示提示
            shutdown.setVisibility( Component.VISIBLE );
            lowTip.setVisibility( Component.HIDE );
            InformationPageSlider.deviceChargePer.setVisibility( Component.VISIBLE );
        }else if (  Constans.NOW_CHARGE <= 10 ){
            // 电量低于10%，显示提示
            lowTip.setVisibility( Component.VISIBLE );
            shutdown.setVisibility( Component.HIDE );
            InformationPageSlider.deviceChargePer.setVisibility( Component.VISIBLE );

        }else if (  "ff".equals(charge) ){
            // 正在充电
            InformationPageSlider.deviceChargePer.setVisibility( Component.VISIBLE );
            lowTip.setVisibility( Component.HIDE );
            shutdown.setVisibility( Component.HIDE );
            InformationPageSlider.deviceCharge.setText( "充电中" );
            InformationPageSlider.deviceChargePer.setVisibility( Component.HIDE );
            return;
        }else if (  "fe".equals(charge) ){
            // 充电完成
            lowTip.setVisibility( Component.HIDE );
            shutdown.setVisibility( Component.HIDE );
            InformationPageSlider.deviceCharge.setText( "充电完成" );
            InformationPageSlider.deviceChargePer.setVisibility( Component.HIDE );
            return;
        }else {
            // 否则，正常显示
            InformationPageSlider.deviceChargePer.setVisibility( Component.VISIBLE );
            shutdown.setVisibility( Component.HIDE );
            lowTip.setVisibility( Component.HIDE );
            InformationPageSlider.deviceCharge.setTextColor( Color.BLACK );
            InformationPageSlider.deviceChargePer.setTextColor( new Color( Color.getIntColor("#52594E")));
        }

        System.out.println("当前电量为:::" +  HexConverter.hexStringToDecimal( charge ) );
        InformationPageSlider.deviceCharge.setText( Constans.NOW_CHARGE + "" );
    }


    /**
     * 点击开始运动按钮，跳转到运动中
     * @param connectionState
     * @param nowTemplate
     * @param runningTemplate
     * @param state
     */
    private void initStartButtonClick(DirectionalLayout connectionState, Component nowTemplate, Component runningTemplate, String state) {

        if ( "stop".equals( state )){
            nowTemplate.findComponentById( ResourceTable.Id_stop_start ).setClickedListener(component -> {
                String stopTime = ((Text) nowTemplate.findComponentById(ResourceTable.Id_stop_time)).getText();
                connectionState.removeAllComponents();
                ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText( stopTime );
                connectionState.addComponent(runningTemplate);

                requestDeviceGear( showHistoryGear == 0? 3:showHistoryGear );
            });
        }else if ( "finished".equals( state )){
            nowTemplate.findComponentById( ResourceTable.Id_finished_start ).setClickedListener(component -> {
                connectionState.removeAllComponents();
                ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText("00:00:00");
                connectionState.addComponent(runningTemplate);

                requestFreeMode();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestDeviceGear( 3 );
            });
        }
    }


    /**
     * 运动中跳转到暂停模板
     * @param connectionState
     * @param runningTemplate
     * @param stopTemplate
     */
    private void initStopButtonClick(DirectionalLayout connectionState, Component runningTemplate, Component stopTemplate) {
        runningTemplate.findComponentById( ResourceTable.Id_running_stop ).setClickedListener(component -> {
            String runningTime = ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
            connectionState.removeAllComponents();
            ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText( runningTime );
            connectionState.addComponent(stopTemplate);

             // 档位设置为0
             requestDeviceGear( 0 );
        });
    }


    /**
     * 点击close按钮，跳转到已完成模板
     * @param connectionState
     * @param nowTemplate
     * @param finishedTemplate
     */
    private void initCloseButtonClick(DirectionalLayout connectionState, Component nowTemplate, Component finishedTemplate, String state) {

        if ( "running".equals( state )){
            // 切换到关闭模板
            nowTemplate.findComponentById( ResourceTable.Id_running_close ).setClickedListener(component -> {

                String runningTime = ((Text) nowTemplate.findComponentById(ResourceTable.Id_running_time)).getText();
                connectionState.removeAllComponents();
                connectionState.addComponent(finishedTemplate);
//                ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText("00:00:00");
//                ((Text) finishedTemplate.findComponentById(ResourceTable.Id_finished_time)).setText("00:00:00");
//                ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText("00:00:00");

                // 设置档位为0
                requestDeviceGear( 0 );

                // 跳出结束运动弹框
                String[] split = runningTime.split(":");
                int timeInteger = Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1]) * 60 + Integer.parseInt(split[2]);
                showEndDialog( this, timeInteger);
            });

        }else if ( "stop".equals( state )){
            // 停止状态下结束运动
            nowTemplate.findComponentById( ResourceTable.Id_stop_close ).setClickedListener(component -> {
                String stopTime = ((Text) nowTemplate.findComponentById(ResourceTable.Id_stop_time)).getText();
                connectionState.removeAllComponents();
                connectionState.addComponent(finishedTemplate);
//                ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText("00:00:00");
//                ((Text) finishedTemplate.findComponentById(ResourceTable.Id_finished_time)).setText("00:00:00");
//                ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText("00:00:00");

                try {
                    Thread.sleep( 100 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 跳出结束运动弹框
                String[] split = stopTime.split(":");
                int timeInteger = Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1]) * 60 + Integer.parseInt(split[2]);
                showEndDialog( this, timeInteger);

            });
        }

    }


    /**
     * 结束运动的弹框
     */
    private void showEndDialog(Context context, Integer timeInteger) {
        //1、定义对话弹框
        endSportCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        endSportCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        endSportCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_endThisSport_common, null, false);
        Button knowBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_know);
        Text today = (Text) dl.findComponentById(ResourceTable.Id_text_today);
        Text quanshu = (Text) dl.findComponentById(ResourceTable.Id_text_quanshu);
        Text sportShiChang = (Text) dl.findComponentById(ResourceTable.Id_text_sportShiChang);
        Text banshengShu = (Text) dl.findComponentById(ResourceTable.Id_text_banshengShu);
        Text pingjunSpeed = (Text) dl.findComponentById(ResourceTable.Id_text_pingjunSpeed);
        Text maxSpeed = (Text) dl.findComponentById(ResourceTable.Id_text_maxSpeed);

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        today.setText( year+"年"+month+"月"+day+"日 "+(hour>12?"下午"+ (hour-12):"上午"+hour)+":" + minute);

        String quanNumberText = InformationPageSlider.quanNumber.getText();
        quanshu.setText(  quanNumberText );
        sportShiChang.setText( runningTimeText.getText() );
        banshengShu.setText( InformationPageSlider.banShengNumber.getText());
        double min = timeInteger / 60.0;
        float avarageSpeed = (float) (Integer.parseInt(quanNumberText) / min);
        pingjunSpeed.setText( (int)avarageSpeed + "");
        maxSpeed.setText( sportMaxSpeed + "" );

        knowBtn.setClickedListener( component -> {
            // todo:保存数据
            if ( Constans.UID == 0){
                SportData sportData = new SportData();
                sportData.setDevice_id( deviceId );
                sportData.setDevice_type(  deviceType );
                sportData.setMode( currentMode == 1? "计数模式":"倒计时模式");
                sportData.setCounts( Integer.parseInt( quanshu.getText() ) );
                sportData.setInterrupt_count(Integer.parseInt( banshengShu.getText() ) );
                sportData.setAverage_speed( avarageSpeed );
                sportData.setRotation_counts( current_rotation_counts > max_rotation_counts? current_rotation_counts: max_rotation_counts );
                sportData.setTime( startSportTime );
                sportData.setDuration( timeInteger );
                appData_store(sportData);
            }

            // 重置数据
            sportMaxSpeed = 0;
            historyGear = 0;
            showHistoryGear = 0;
            current_rotation_counts = 0;
            max_rotation_counts = 0;
            InformationPageSlider.quanNumber.setText("0");
            InformationPageSlider.banShengNumber.setText("0");
            InformationPageSlider.currentSpeed.setText("0");
            ((Text) runningTemplate.findComponentById(ResourceTable.Id_running_time)).setText("00:00:00");
            ((Text) finishedTemplate.findComponentById(ResourceTable.Id_finished_time)).setText("00:00:00");
            ((Text) stopTemplate.findComponentById(ResourceTable.Id_stop_time)).setText("00:00:00");
            connectionState.removeAllComponents();
            connectionState.addComponent( finishedTemplate );
            startSportTime = "" ;

            resetDeviceData();
            endSportCommonDialog.hide();
        });

        //将XML文件中的布局交给吐司弹框
        endSportCommonDialog.setContentCustomComponent(dl);
        endSportCommonDialog.setAutoClosable( false );
        //弹框显示时间
        endSportCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        endSportCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        endSportCommonDialog.show();
    }


    /**
     * 存储数据
     * @param sportData
     */
    private void appData_store(SportData sportData) {

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch( ()->{
            OkHttpUtils
                    .postString()
                    .content( new Gson().toJson( sportData ))
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .addHeader("time", new Date().getTime() + "")
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appData/store")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            FancyToast.makeText(MainAbilitySlice.this,"存储数据失败",3000, FancyToast.ERROR,false).show();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            ResultVO resultVO = new Gson().fromJson(s, new ResultVO<User>().getClass());
                            if ( resultVO.getCode().equals("200")){
                                FancyToast.makeText(MainAbilitySlice.this,"存储数据成功",3000, FancyToast.SUCCESS,false).show();
                            }else {
                                FancyToast.makeText(MainAbilitySlice.this,"存储数据失败",3000, FancyToast.ERROR,false).show();
                                System.out.println( resultVO.getMsg() );

                            }
                        }
                    });
        }) ;

    }


    /**
     * 重置设备数据
     */
    private void resetDeviceData() {
        BleManager.getInstance().write(bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes("aa55021101ed"),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println("重置数据成功");
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println("重置数据失败");
                    }
                });
    }

    /**
     * 使用视频和背景音乐的点击事件
     */
    private void vedioOrMusicClickFunction(Button useVedio, Button useMusic, Button useVedioLine, Button useMusicLine, int i) {
        if (1 == i){
            // 首先设置字体颜色
            useVedio.setTextColor( new Color( Color.getIntColor("#0A59F7")));
            ShapeElement shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_use_video_line);
            useVedioLine.setBackground( shapeElement );

            useMusic.setTextColor(Color.BLACK);
            shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_nouse_video_line);
            useMusicLine.setBackground( shapeElement );
        }
        else if (2 == i ){
            useMusic.setTextColor( new Color( Color.getIntColor("#0A59F7")));
            ShapeElement shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_use_video_line);
            useMusicLine.setBackground( shapeElement );

            useVedio.setTextColor(Color.BLACK);
            shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_nouse_video_line);
            useVedioLine.setBackground( shapeElement );
        }
    }

    /**
     * 创建一个弹出框
     * @param context
     * @param list
     * @param chooseModeOrTime
     * @return
     */
    private void createPopupDialog(Context context, List<String> list, DirectionalLayout chooseModeOrTime) {
        BasePopupView popupView = new XPopup.Builder(context)
                .atView(chooseModeOrTime)  // 依附于所点击的Component，内部会自动判断在上方或者下方显示
                .asAttachList(list.toArray(new String[list.size()]), null,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                btn_mode_time = (Button) chooseModeOrTime.getComponentAt(1);
                                if ("自定义".equals(text)) {
                                    showTimePicker( context );
                                } else {
                                    btn_mode_time.setText(text);
                                    switch ( text ){
                                        case "自由模式":
                                            // 自由模式
                                            requestFreeMode();
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            requestDeviceGear( 3 );
                                            break;
                                        case "热身模式":
                                            requestFreeMode();
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            requestDeviceGear( 6 );
                                            break;
                                        case "中速模式":
                                            requestFreeMode();
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            requestDeviceGear( 9 );
                                            break;
                                        case "高速模式":
                                            requestFreeMode();
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            requestDeviceGear( 12 );
                                            break;
                                        case "1分钟":
                                            // 倒计时模式
                                            requestDeviceTimeMode( 60 );
                                            break;
                                        case "5分钟":
                                            // 倒计时模式
                                            requestDeviceTimeMode( 300 );
                                            break;
                                        case "10分钟":
                                            // 倒计时模式
                                            requestDeviceTimeMode( 600 );
                                            break;
                                    }
                                }
                            }
                        });

        popupView.setWidth( 300 );
        popupView.show();

    }


    /**
     * 下发进入倒计时模式
     * @param time  计时时间
     */
    private void requestDeviceTimeMode(int time) {

        if ( currentMode ==2 ){
            return ;
        }

        // 设置当前的模式为倒计时模式
        currentMode = 2;
        sportMaxSpeed = 0;

        // 十进制时间转为16进制
        String s = HexConverter.decimalToHexString(time);
        switch ( s.length() ){
            case 1:
                s = "000" + s ;
                break;
            case 2:
                s = "00" + s ;
                break;
            case 3:
                s = "0" + s;
                break;
        }

        // 求和
        Integer i1 = HexConverter.hexStringToDecimal(s.substring(0, 2));
        Integer i2 = HexConverter.hexStringToDecimal(s.substring(2, 4));
        s =  s.substring(2, 4) + s.substring(0, 2) ;

        int sum = 264 + i1 + i2 ;
        String s1 = HexConverter.decimalToHexString(sum);
        s1 = s1.length() % 2 == 0 ? s1:"0" + s1 ;
        // 取反+1
        String backAndAddOne = HexConverter.backAndAddOne(s1);
//        System.out.println("aa55040302" + s + backAndAddOne.substring(backAndAddOne.length() - 2));
        BleManager.getInstance().write(bleDevice,
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                HexUtil.hexStringToBytes("aa55040302" + s +  backAndAddOne.substring( backAndAddOne.length()-2 )),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int i, int i1, byte[] bytes) {
                        System.out.println( "下发倒计时模式成功");
                    }

                    @Override
                    public void onWriteFailure(BleException e) {
                        System.out.println( "下发倒计时模式失败");
                    }
                });
    }


    /**
     * 下发进入自由模式
     */
    private void requestFreeMode() {

        if ( currentMode == 2 || currentMode == 0 ){
            currentMode = 1; // 设置当前模式为自由模式
            sportMaxSpeed= 0;

            if ( btn_choose_time != null )
                btn_choose_time.setText( "定时");

            // 如果现在是倒计时模式，则转为自由模式
            BleManager.getInstance().write(bleDevice,
                    "0000fff0-0000-1000-8000-00805f9b34fb",
                    "0000fff2-0000-1000-8000-00805f9b34fb",
                    HexUtil.hexStringToBytes("aa550403010000f9"),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int i, int i1, byte[] bytes) {
                            System.out.println( "下发自由模式成功");
                        }

                        @Override
                        public void onWriteFailure(BleException e) {
                            System.out.println( "下发自由模式失败");
                        }
                    });
        }


    }


    /**
     * 点击自定义时弹出时间选择器
     * @param context
     */
    private void showTimePicker(Context context ) {
        //1、定义对话弹框
        timePickereCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        timePickereCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        timePickereCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_timepicker_common, null, false);
        pickerTime = (Picker) dl.findComponentById(ResourceTable.Id_picker_time);

        cancelTimePickerBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel);
        okTimePickerBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok);

        // 设置点击事件
        cancelTimePickerBtn.setClickedListener(this);
        okTimePickerBtn.setClickedListener(this);

        pickerTime.setFormatter( i -> {
            String value = "" ;
            if( i == 1 ){
                value = i + "分钟";
            }else{
                value = i + "";
            }
            return value ;
        });

        pickerTime.setValueChangedListener( this );

        //将XML文件中的布局交给吐司弹框
        timePickereCommonDialog.setContentCustomComponent(dl);
        timePickereCommonDialog.setAutoClosable( false );
        //弹框显示时间
        timePickereCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        timePickereCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        timePickereCommonDialog.show();
    }


    @Override
    public void onClick(Component component) {
        if ( upgradeBtn != null && component == upgradeBtn ){
            // 跳转到固件升级界面

            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withDeviceId("")
                    .withBundleName( this.getBundleName() )
                    .withAbilityName(UpgradeMainAbility.class.getName())
                    .withAction("")
                    .build();

            intent.setOperation( operation );
            this.startAbility( intent );

        }else if ( dataBtn != null && component == dataBtn ){
            // 跳转到数据界面
            Intent intent = new Intent();
            intent.setParam("device_id", deviceId);
            intent.setParam("device_type", deviceType);
            this.present(new DataAbilitySlice(),intent);

        }else if ( cancelTimePickerBtn != null && component == cancelTimePickerBtn ){
            timePickereCommonDialog.hide();
        }else if ( okTimePickerBtn != null && component == okTimePickerBtn ){
            // 设置时间
            timePickereCommonDialog.hide();

            btn_mode_time.setText( pickerNewValue + "分钟");

            if ( pickerNewValue * 60 >= 65535 ){
                FancyToast.makeText( this, "所设时间太大啦，改小一点吧。",3000,FancyToast.WARNING,true);
                return;
            }

            requestDeviceTimeMode( pickerNewValue * 60 );

        }else if ( reconnectBtn != null && component == reconnectBtn ){
            connectBlueTooth( );
        }else if ( component.getId() == videoPlayBtn01.getId() ||
                component.getId() == videoPlayBtn02.getId() ||
                component.getId() == videoPlayBtn03.getId() ){
            try {

                if ( component.getId() == videoPlayBtn01.getId() ){
                    startVideo( videoPlayBtn01.getHint() );
                }else if ( component.getId() == videoPlayBtn02.getId() ){
                    startVideo( videoPlayBtn02.getHint() );
                }else if ( component.getId() == videoPlayBtn03.getId() ){
                    startVideo( videoPlayBtn03.getHint() );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if ( component.getId() == musicPlayBtn01.getId() ||
                component.getId() == musicPlayBtn02.getId() ||
                component.getId() == musicPlayBtn03.getId() ){

            if ( component.getId() == musicPlayBtn01.getId() ){
                if ( musicPlayBtn01.getText().equals("   播 放")){
                    startMusic( musicPlayBtn01 );
                    historyBtn = musicPlayBtn01;
                }else if ( musicPlayBtn01.getText().equals("   暂 停"))
                    stopMusic( musicPlayBtn01 );
            }else if ( component.getId() == musicPlayBtn02.getId() ){
                if ( musicPlayBtn02.getText().equals("   播 放")){
                    startMusic( musicPlayBtn02 );
                    historyBtn = musicPlayBtn02;
                }else if ( musicPlayBtn02.getText().equals("   暂 停"))
                    stopMusic(musicPlayBtn02);
            }else if ( component.getId() == musicPlayBtn03.getId() ){
                if ( musicPlayBtn03.getText().equals("   播 放")){
                    startMusic( musicPlayBtn03);
                    historyBtn = musicPlayBtn03;
                }else  if ( musicPlayBtn03.getText().equals("   暂 停"))
                    stopMusic(musicPlayBtn03);
            }

        }else if ( component.getId() == waitTryDialogBtn.getId() ){
            connectOverTimeDialog.hide();
        }else if ( component.getId() == reconnectDialogBtn.getId() ){
            connectBlueTooth( );
            connectOverTimeDialog.hide();
        }
    }


    /**
     * 暂停音乐
     * @param rootBtn
     */
    private void stopMusic(Button rootBtn) {
        if ( mPlayer !=null && mPlayer.isNowPlaying() ){
            getUITaskDispatcher().asyncDispatch( ()-> {
                rootBtn.setText("   播 放");
                ShapeElement shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_nouse );
                rootBtn.setBackground( shapeElement );

                String music = rootBtn.getHint().split("music")[1];
                String musicIndex = music.substring(0, 2);
                switch ( musicIndex ){
                    case "01":
                        musicPlayImg01.setVisibility(Component.VISIBLE);
                        break;
                    case "02":
                        musicPlayImg02.setVisibility(Component.VISIBLE);
                        break;
                    case "03":
                        musicPlayImg03.setVisibility(Component.VISIBLE);
                        break;
                }

            });
            mPlayer.pause();
        }
    }


    /**
     * 播放音乐
     * @param rootBtn
     */
    private void startMusic(Button rootBtn) {

        ShapeElement shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_use );
        rootBtn.setBackground( shapeElement );

        if ( historyBtn != null && historyBtn.getId() != rootBtn.getId() ){
            shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_nouse );
            historyBtn.setBackground( shapeElement );
        }


        /*
            是第一次播放
         */
        if ( mPlayer == null  ){
            getUITaskDispatcher().asyncDispatch( ()-> {
                rootBtn.setText("   暂 停");
            });
            try {

                mPlayer = new Player(this);
                mPlayer.setVolume(90f); // 音量
                historyBtn = rootBtn ;
                RawFileDescriptor rawFileDescriptor=getResourceManager()
                        .getRawFileEntry( rootBtn.getHint() )
                        .openRawFileDescriptor();
                Source source=new Source(rawFileDescriptor.getFileDescriptor(),
                        rawFileDescriptor.getStartPosition(),rawFileDescriptor.getFileSize());

                mPlayer.setSource(source);
                mPlayer.prepare();
                mPlayer.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return ;
        }

        /*
         不是第一次播放
           如果即将播放的音乐 == 正在播放的音乐
                1. 按钮文本改变
                2. 暂停Image隐藏
                3. 音乐继续
         */
        if ( rootBtn == historyBtn ){
            // 如果两次播放同一个音乐
            getUITaskDispatcher().asyncDispatch( ()-> {
                rootBtn.setText("   暂 停");
                //暂停Image隐藏与显示
                String music = rootBtn.getHint().split("music")[1];
                String musicIndex = music.substring(0, 2);
                switch ( musicIndex ){
                    case "01":
                        musicPlayImg01.setVisibility(Component.HIDE);
                        break;
                    case "02":
                        musicPlayImg02.setVisibility(Component.HIDE);
                        break;
                    case "03":
                        musicPlayImg03.setVisibility(Component.HIDE);
                        break;
                }
            });

            mPlayer.play();
            return ;
        }



        /*
        不是第一次播放
            如果两次音乐不同
                1）按钮文本改变（之前的按钮文本与当前的按钮文本都需要改变）
                2) 暂停Image隐藏与显示
                3）音乐播放
         */
        if ( rootBtn != historyBtn ){
            String music = historyBtn.getHint().split("music")[1];
            String musicIndex = music.substring(0, 2);

            historyBtn.setText("   播 放");
            rootBtn.setText("   暂 停");

            //按钮文本改变
            getUITaskDispatcher().asyncDispatch( ()-> {
                //暂停Image隐藏与显示
                switch ( musicIndex ){
                    case "01":
                        musicPlayImg01.setVisibility(Component.HIDE);
                        break;
                    case "02":
                        musicPlayImg02.setVisibility(Component.HIDE);
                        break;
                    case "03":
                        musicPlayImg03.setVisibility(Component.HIDE);
                        break;
                }
            });

            // 停止之前的音乐
            mPlayer.stop();
        }

        //播放当前的音乐
        try {

            RawFileDescriptor rawFileDescriptor=getResourceManager()
                    .getRawFileEntry( rootBtn.getHint() )
                    .openRawFileDescriptor();
            Source source=new Source(rawFileDescriptor.getFileDescriptor(),
                    rawFileDescriptor.getStartPosition(),rawFileDescriptor.getFileSize());

            mPlayer.setSource(source);
            mPlayer.prepare();
            mPlayer.play();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 播放视频
     * @param root
     */
    private void startVideo(String root) throws IOException {
        Intent mIntent = new Intent();
        mIntent.setParam("root",root ) ;

        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAction("")
                .withAbilityName(VideoMainAbility.class.getName())
                .withBundleName(getBundleName())
                .build();

        mIntent.setOperation( operation );

        this.startAbility( mIntent );

    }


    /**
     * picker的值改变事件
     * @param picker
     * @param oldValue
     * @param newValue
     */
    @Override
    public void onValueChanged(Picker picker, int oldValue, int  newValue) {
        pickerNewValue = newValue ;

        picker.setFormatter( i -> {
            String valueStr = i +"" ;
            if ( i == newValue){
                valueStr = i + "分钟" ;
            }
            return valueStr ;
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if ( mPlayer != null ) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        signOut();
        cancelAuthorization();
    }


    /**
     * 华为授权登录
     */
    private void huaweiIdSignIn() {
        AccountAuthService accountAuthService;
        // 1、配置登录请求参数AccountAuthParams，包括请求用户id(openid、unionid)、email、profile（昵称、头像）等。
        // 2、DEFAULT_AUTH_REQUEST_PARAM默认包含了id和profile（昵称、头像）的请求。
        // 3、如需要再获取用户邮箱，需要setEmail()。
        AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                .setEmail()
//                .setMobileNumber()
                .createParams();
        try {
            accountAuthService = AccountAuthManager.getService(accountAuthParams);
        } catch (ApiException e) {
            // 处理初始化登录授权服务失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
            e.getStatusCode();
            return;
        }
        // 调用静默登录接口。
        // 如果华为系统帐号已经登录，并且已经授权，会登录成功；
        // 否则静默登录失败，需要在失败监听中，显式调用前台登录授权接口，完成登录授权。
        Task<AuthAccount> task = accountAuthService.silentSignIn();
        // 添加静默登录成功处理监听
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // 静默登录成功后，根据结果中获取到的帐号基本信息更新UI
                System.out.println( "-------------------静默登录成功"  );
                connectBlueTooth( );
            }
        });
        // 添加静默登录失败监听
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    // 静默登录失败，显式调用前台登录授权接口，完成登录授权。
                    Task task = accountAuthService.signIn();
                    if (task == null) {
                        return;
                    }
                    task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
                        @Override
                        public void onSuccess(AuthAccount result) {
//                            updateUI(result);
                            System.out.println( "-------------------静默登录失败" );
                            connectBlueTooth( );
                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            if (e instanceof ApiException) {
                                ApiException apiException = (ApiException) e;
                                // 登录失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
                                apiException.getStatusCode();
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 退出登录
     */
    private void signOut() {
        AccountAuthService accountAuthService = null;
        AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setEmail().createParams();
        try {
            accountAuthService = AccountAuthManager.getService(accountAuthParams);
        } catch (ApiException e) {
            // 处理初始化登录授权服务失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
            e.getStatusCode();
            return;
        }
        Task<Void> task = accountAuthService.signOut();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                // 华为帐号登出成功，接入应用处理登出后逻辑
                System.out.println("-------------退出华为账号");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                // 华为帐号退出失败
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    // 华为帐号退出失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
                    apiException.getStatusCode();
                }
            }
        });
    }

    /**
     * 取消授权
     */
    private void cancelAuthorization() {
        AccountAuthService accountAuthService;
        AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setEmail().createParams();
        try {
            accountAuthService = AccountAuthManager.getService(accountAuthParams);
        } catch (ApiException e) {
            // 处理初始化登录授权服务失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
            e.getStatusCode();
            return;
        }
        // 调用取消授权接口
        Task<Void> task = accountAuthService.cancelAuthorization();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                // 取消授权成功
                System.out.println("------------取消授权成功");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 取消授权失败
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    // 华为帐号取消授权失败，status code标识了失败的原因，请参见API参考中的错误码了解详细错误原因
                    apiException.getStatusCode();
                }
            }
        });
    }
}
