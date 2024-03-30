package com.wdk.sports.slice;

import com.wdk.sports.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

public class AboutAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    private Button aboutBackBtn = null;
    private Button aboutUserPaper01 = null;
    private Button aboutUserPaper02 = null;

    private CommonDialog userProvicyCommonDialog = null;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_page_about);

        initComponent();
        initComponentListener();
    }

    private void initComponentListener() {
        aboutBackBtn.setClickedListener( this );
        aboutUserPaper01.setClickedListener( this );
        aboutUserPaper02.setClickedListener( this );
    }

    private void initComponent() {
        aboutBackBtn = (Button) this.findComponentById(ResourceTable.Id_btn_about_back);
        aboutUserPaper01 = (Button) this.findComponentById(ResourceTable.Id_btn_userPaper1);
        aboutUserPaper02 = (Button) this.findComponentById(ResourceTable.Id_btn_userPaper2);
    }

    @Override
    public void onClick(Component component) {
        if ( aboutBackBtn != null && component == aboutBackBtn ){
            this.onBackPressed();
        }else if ( (aboutUserPaper01!= null && component == aboutUserPaper01 ) ||
                (aboutUserPaper02!= null && component == aboutUserPaper02 ) ){
            alertUserProvicy( this );
        }
    }


    /**
     * 弹出用户隐私协议
     */
    private void alertUserProvicy(Context context ) {
        //1、定义对话弹框
        userProvicyCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        userProvicyCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        //设置对齐方式
        userProvicyCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_userprovicy_common, null, false);
        Button knownBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_known);
        Text tipInfoText = (Text) dl.findComponentById(ResourceTable.Id_text_tip_info);

        String tipInfo = "  华为技术有限公司及其全球的关联公司（下文简称“华为”、“我们”和“我们的”）深知隐私对您的重要性，" +
                "并会尊重您的隐私。请在向华为提交个人信息之前，阅读、了解本《隐私政策》（下文简称“本政策”）。" +
                "本政策适用于显示本隐私政策、或链接至本隐私政策的华为网站和产品、服务。特别的，本网站由华为技术有限公司" +
                "（注册地为深圳市龙岗区坂田华为总部办公楼）运营，华为技术有限公司是该网站所收集的个人信息的个人信息处理者。\n\n"+
                "  本政策阐述了华为如何处理您的个人信息，但本政策可能并不涉及所有可能的数据处理情境。" +
                "有关收集产品或服务特定数据的信息可能由华为在特定产品或服务发布的专门隐私通知或补充声明中阐述，或者在收集数据时提供的通知中发布。\n\n" +
                "  我们制定本政策的目的在于帮助您了解以下内容：\n" +
                "    1. 华为如何收集和使用您的个人信息\n" +
                "    2. 华为如何使用 Cookie 和同类技术\n" +
                "    3. 华为如何披露您的个人信息\n" +
                "    4. 您在个人信息处理活动中的权利\n" +
                "    5. 华为如何保护您的个人信息\n" +
                "    6. 华为如何处理儿童的个人信息\n" +
                "    7. 第三方提供商及其服务\n" +
                "    8. 您的个人信息如何在全球范围转移\n" +
                "    9. 本政策如何更新\n" +
                "    10. 如何联系华为\n" ;

        tipInfoText.setText( tipInfo );
        // 设置点击事件
        knownBtn.setClickedListener( component -> {
            userProvicyCommonDialog.hide();
        });

        //将XML文件中的布局交给吐司弹框
        userProvicyCommonDialog.setContentCustomComponent(dl);
        userProvicyCommonDialog.setAutoClosable( false );
        //弹框显示时间
        userProvicyCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        userProvicyCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        userProvicyCommonDialog.show();
    }
}
