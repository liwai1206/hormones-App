package com.wdk.sports.slice;

import com.allenliu.badgeview.BadgeFactory;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wdk.sports.MainAbility;
import com.wdk.sports.ResourceTable;
import com.wdk.sports.util.Constans;
import com.wdk.sports.util.HexConverter;
import com.zhy.http.library.OkHttpUtils;
import com.zhy.http.library.callback.FileCallBack;
import com.zhy.http.library.callback.StringCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.Revocable;
import ohos.app.dispatcher.task.TaskPriority;
import okhttp3.Call;
import org.devio.hi.json.HiJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UpgradeMainAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    private Button upgradeBackBtn = null; // 返回按钮
    private DirectionalLayout tipOrangeLayout = null; // 提示信息
    private Text foundNewVersionText = null; // 发现新版本文字
    private Button downloadUpgradeBtn = null; // 下载按钮
    private Text newVersionText = null; // 新版本文字，主要用来绑定小红点
    private Text newVersionNumberText; // 新版本版本号
    private Text newVersioSizeText; // 型版本大小
    private Text newVersionDetailText; // 新版本详情

    public static Text currentVersionNumberText; // 当前版本号

    public static MagicProgressCircle roundProgressBar;
    private DirectionalLayout newVersionDirectionalLayout;
    public static Text progressValueText;
    private Text progressCurrentVersionText;
    private Image upgradeImage;
    private DirectionalLayout propgress_value_layout;

    private FileInputStream fis;
    public static byte[] fileBytes; // 读出来的文件

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_page_upgrade_y);

        initComponent();
        initComponentListener();
        notifyFFC2();

        if ( Constans.isVersionCode ){
            // 绑定一个小红点
            BadgeFactory.createDot(this).setBadgeCount(1).bind(newVersionText);
            upgradeImage.setVisibility( Component.VISIBLE );
            propgress_value_layout.setVisibility( Component.HIDE );
        }
        if ( Constans.UPGRADE_STATE == 0 ){
            foundNewVersionText.setText("已是最新版本");
            downloadUpgradeBtn.setText("检查更新");
            downloadUpgradeBtn.setTextColor( Color.WHITE );
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(new RgbColor(10,89,247));
            shapeElement.setCornerRadius( 100f );
            downloadUpgradeBtn.setBackground(shapeElement);
            newVersionDirectionalLayout.setVisibility( Component.HIDE );
            currentVersionNumberText.setText( Constans.currentCode );
            progressCurrentVersionText.setText( Constans.currentCode );
            tipOrangeLayout.setVisibility( Component.HIDE );
        }else if ( Constans.UPGRADE_STATE == 2 ){
            // 正在更新
            upgradeImage.setVisibility( Component.HIDE );
            propgress_value_layout.setVisibility( Component.VISIBLE );

            // 后台更新
            tipOrangeLayout.setVisibility( Component.VISIBLE );
            foundNewVersionText.setText("正在更新...");

            ShapeElement shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_upgrading);
            downloadUpgradeBtn.setBackground(shapeElement);
            downloadUpgradeBtn.setTextColor( new Color( Color.getIntColor("#EAECEE")));
            downloadUpgradeBtn.setText("正在更新");

            newVersionDirectionalLayout.setVisibility( Component.VISIBLE );
            currentVersionNumberText.setText( Constans.currentCode );
            progressCurrentVersionText.setText( Constans.currentCode );
            tipOrangeLayout.setVisibility( Component.VISIBLE );
        }else if ( Constans.UPGRADE_STATE == 3 ){
            foundNewVersionText.setText("更新成功");
            downloadUpgradeBtn.setText("完成");
            downloadUpgradeBtn.setTextColor( Color.WHITE );
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(new RgbColor(10,89,247));
            shapeElement.setCornerRadius( 100f );
            downloadUpgradeBtn.setBackground(shapeElement);
            newVersionDirectionalLayout.setVisibility( Component.HIDE );
            currentVersionNumberText.setText( Constans.versionCode );
            progressCurrentVersionText.setText( Constans.versionCode );
            tipOrangeLayout.setVisibility( Component.HIDE );
        }else if ( Constans.UPGRADE_STATE == 1 ){
            // 待更新
            tipOrangeLayout.setVisibility( Component.HIDE );
        }
    }



    /**
     * 注册ffc2的通知
     */
    private void notifyFFC2() {
        // ota升级的订阅通知，ffc2
        BleManager.getInstance().notify(
                MainAbilitySlice.bleDevice,
                "f000ffc0-0451-4000-b000-000000000000",
                "f000ffc2-0451-4000-b000-000000000000",
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        System.out.println( "ffc2的订阅通知成功" );
                    }

                    @Override
                    public void onNotifyFailure(BleException e) {
                        System.out.println( "ffc2的订阅通知失败" );
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        String hex = HexConverter.byte2Hex(data);
                        System.out.println( "ffc2收到的通知消息:>>>>" + hex );

                        if ( "0000".equals( hex ) ){
                            // 设备确认可以开始升级
                            // 循环写
                            Constans.UPGRADE_STATE = 2;
                            startUpgradeOTA();

                        }else if ( "eeee".equalsIgnoreCase( hex )){
                            // 升级完成
                            Constans.INDEX = 3;
                            MainAbilitySlice.progressSlider.setProgressValue(0);

                            foundNewVersionText.setText("更新成功");
                            downloadUpgradeBtn.setText("完成");
                            downloadUpgradeBtn.setTextColor( Color.WHITE );
                            ShapeElement shapeElement = new ShapeElement();
                            shapeElement.setRgbColor(new RgbColor(10,89,247));
                            shapeElement.setCornerRadius( 100f );
                            downloadUpgradeBtn.setBackground(shapeElement);
                            newVersionDirectionalLayout.setVisibility( Component.HIDE );
                            currentVersionNumberText.setText( Constans.versionCode );
                            progressCurrentVersionText.setText( Constans.versionCode );
                            tipOrangeLayout.setVisibility( Component.HIDE );
                        }else{
                            // 升级出错
                            if ( hex.length() == 4 ){
                                String s = HexConverter.xiaoDuan(hex);
                                Constans.INDEX = HexConverter.hexStringToDecimal( s );
                                startUpgradeOTA();
                            }
                            System.out.println("升级出错::" + hex );
                        }
                    }
                }
        );

    }

    private void startUpgradeOTA() {
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(()->{
            for (int i = Constans.INDEX ; i < fileBytes.length / 16; i++) {
                String s = HexConverter.intToHex(Constans.INDEX, 4);
                // index转换为小端方式
                String xiaoDuanIndex = HexConverter.xiaoDuan(s);
                byte[] bytes = new byte[16];
                if ( Constans.INDEX * 16 + 16 > fileBytes.length ){
                    bytes = new byte[ fileBytes.length - Constans.INDEX ];
                    for (int j = 0; j < fileBytes.length - Constans.INDEX * 16; j++) {
                        bytes[j] = fileBytes[j + Constans.INDEX * 16];
                    }
                }else {
                    for (int j = 0; j < 16; j++) {
                        bytes[j] = fileBytes[j + Constans.INDEX * 16];
                    }
                }

                String binData = HexConverter.byte2Hex(bytes);

                BleManager.getInstance().write(
                        MainAbilitySlice.bleDevice,
                        "f000ffc0-0451-4000-b000-000000000000",
                        "f000ffc2-0451-4000-b000-000000000000",
                        HexUtil.hexStringToBytes(xiaoDuanIndex + binData),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int i, int i1, byte[] bytes) {
                                System.out.println("第" + Constans.INDEX + "个数据向ffc2发送成功:" + HexConverter.byte2Hex(bytes));
                                Constans.INDEX++;
                                float progress = (float) (Constans.INDEX * 16.0 / fileBytes.length);
                                roundProgressBar.setSmoothPercent(progress, 200);
                                progressValueText.setText(
                                        (progress * 100 + "").contains(".") ?
                                                (progress * 100 + "").substring(0, (progress * 100 + "").indexOf("."))
                                                : (progress * 100 + ""));
                            }

                            @Override
                            public void onWriteFailure(BleException e) {
                                System.out.println("第" + Constans.INDEX + "个数据向ffc2发送失败");
                            }
                        });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initComponentListener() {
        upgradeBackBtn.setClickedListener(this);
        downloadUpgradeBtn.setClickedListener(this);
    }

    private void initComponent() {
        upgradeBackBtn = (Button) this.findComponentById(ResourceTable.Id_btn_upgrade_back);
        tipOrangeLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_direction_tip_orange);
        foundNewVersionText = (Text) this.findComponentById(ResourceTable.Id_text_found_newversion);
        newVersionText = (Text) this.findComponentById( ResourceTable.Id_text_new_version );
        downloadUpgradeBtn = (Button) this.findComponentById(ResourceTable.Id_btn_download_upgrade);

        newVersionNumberText = (Text) this.findComponentById( ResourceTable.Id_text_newversion_number );
        newVersionNumberText.setText( Constans.versionCode );

        newVersioSizeText = (Text) this.findComponentById( ResourceTable.Id_text_newversion_size );
        newVersioSizeText.setText( Constans.newVersioSize );

        newVersionDetailText = (Text) this.findComponentById( ResourceTable.Id_text_newversion_detail );
        newVersionDetailText.setText( Constans.versionAddress );

        currentVersionNumberText = (Text) this.findComponentById( ResourceTable.Id_text_current_version );
        currentVersionNumberText.setText( Constans.currentCode );

        roundProgressBar = (MagicProgressCircle) this.findComponentById(ResourceTable.Id_roundProgressBar);
        progressValueText = (Text) this.findComponentById(ResourceTable.Id_text_progress_value);
        progressCurrentVersionText = (Text) this.findComponentById(ResourceTable.Id_text_progress_current_version);
        progressCurrentVersionText.setText( currentVersionNumberText.getText() );

        newVersionDirectionalLayout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_directionalLayout_new_version);

        upgradeImage = (Image) this.findComponentById(ResourceTable.Id_image_upgrade_progress);
        propgress_value_layout = (DirectionalLayout) this.findComponentById(ResourceTable.Id_propgress_value_layout);

        fis = Constans.fis;
    }

    @Override
    public void onClick(Component component) {
        if ( upgradeBackBtn != null && component == upgradeBackBtn ){

//            if ( Constans.UPGRADE_STATE == 2 ){
//                FancyToast.makeText( this, "OTA升级中,请稍后...", 3000, FancyToast.INFO, false).show();
//                return;
//            }

            this.onBackPressed();
        }else if ( downloadUpgradeBtn != null && component == downloadUpgradeBtn ){
            if ( "下载更新".equals(downloadUpgradeBtn.getText().trim() )){
                downloadUpgradeClick();
            }else if ( "完成".equals(downloadUpgradeBtn.getText().trim() )){
                Constans.UPGRADE_STATE = 0;// 更新完成
                this.onBackPressed();
            }else if ( "检查更新".equals(downloadUpgradeBtn.getText().trim() )){
                checkUpgrade();
            }
        }
    }


    /**
     * 检查更新
     */
    private void checkUpgrade() {
        foundNewVersionText.setText("正在检查...");

        ShapeElement shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_upgrading);
        downloadUpgradeBtn.setTextColor( new Color( Color.getIntColor("#EAECEE")));
        downloadUpgradeBtn.setBackground(shapeElement);

        roundProgressBar.setSmoothPercent( 0.2f, 200 );

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .get()
                    .addParams("product_id","2HGR")
                    .addParams("product_type","LYTSJ-004")
                    .url( Constans.HTTP_HEAD + "/appOTA/query")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {  }

                        @Override
                        public void onResponse(String s, int i) {
                            String version_code = new HiJson(s).get("data").value("version_code");
                            String substring = version_code.substring(version_code.indexOf(".") + 1);
                            Integer integer = HexConverter.hexStringToDecimal(substring);
                            version_code = version_code.substring(0, version_code.indexOf(".") + 1) + integer ;

                            roundProgressBar.setSmoothPercent( 0.4f, 200 );

                            if ( !Constans.currentCode.equals( version_code )){
                                Constans.UPGRADE_STATE = 1; // 待更新
                                Constans.versionCode = version_code;
                                Constans.isVersionCode = true ;
                                Constans.versionAddress =  new HiJson(s).get("data").value("upgrade_link");
                                Constans.versionFileName =  new HiJson(s).get("data").value("package_name");
                                roundProgressBar.setSmoothPercent( 1f, 200 );

                                foundNewVersionText.setText("发现新版本");
                                downloadUpgradeBtn.setText("下载更新");
                                downloadUpgradeBtn.setTextColor( Color.WHITE );
                                ShapeElement shapeElement = new ShapeElement();
                                shapeElement.setRgbColor(new RgbColor(10,89,247));
                                shapeElement.setCornerRadius( 100f );
                                downloadUpgradeBtn.setBackground(shapeElement);
                                newVersionDirectionalLayout.setVisibility( Component.VISIBLE );
                                currentVersionNumberText.setText( Constans.currentCode );
                                progressCurrentVersionText.setText( Constans.versionCode );
                                tipOrangeLayout.setVisibility( Component.HIDE );

                            }else {
                                Constans.UPGRADE_STATE = 0; // 无更新
                                roundProgressBar.setSmoothPercent( 1f, 200 );

                                foundNewVersionText.setText("已是最新版本");
                                downloadUpgradeBtn.setText("检查更新");
                                downloadUpgradeBtn.setTextColor( Color.WHITE );
                                ShapeElement shapeElement = new ShapeElement();
                                shapeElement.setRgbColor(new RgbColor(10,89,247));
                                shapeElement.setCornerRadius( 100f );
                                downloadUpgradeBtn.setBackground(shapeElement);
                                newVersionDirectionalLayout.setVisibility( Component.HIDE );
                                currentVersionNumberText.setText( Constans.currentCode );
                                progressCurrentVersionText.setText( Constans.currentCode );
                                tipOrangeLayout.setVisibility( Component.HIDE );
                            }
                        }
                    });
        });

    }


    /**
     * 下载更新按钮的点击事件
     */
    private void downloadUpgradeClick() {

        upgradeImage.setVisibility( Component.HIDE );
        propgress_value_layout.setVisibility( Component.VISIBLE );

        // 后台更新
        tipOrangeLayout.setVisibility( Component.VISIBLE );
        foundNewVersionText.setText("正在更新...");

        ShapeElement shapeElement = new ShapeElement( this, ResourceTable.Graphic_background_button_upgrading);
        downloadUpgradeBtn.setBackground(shapeElement);

        downloadUpgradeBtn.setTextColor( new Color( Color.getIntColor("#EAECEE")));
        downloadUpgradeBtn.setText("正在更新");

        try {
            fileBytes = new byte[ fis.available() ];
            int len = 0 ;
            if ( (len = fis.read( fileBytes )) != -1 ){

                byte[] bytes = new byte[16];
                for (int j = 0; j < 16; j++) {
                    bytes[ j ] = fileBytes[j] ;
                }

                BleManager.getInstance().write(
                        MainAbilitySlice.bleDevice,
                        "f000ffc0-0451-4000-b000-000000000000",
                        "f000ffc1-0451-4000-b000-000000000000",
                        bytes,
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int i, int i1, byte[] data) {
                                System.out.println("向ffc1发送前16子节成功:" + HexConverter.byte2Hex( bytes ));
                            }

                            @Override
                            public void onWriteFailure(BleException e) {
                                System.out.println("向ffc1发送前16子节失败");
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActive() {
        super.onActive();



    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}
