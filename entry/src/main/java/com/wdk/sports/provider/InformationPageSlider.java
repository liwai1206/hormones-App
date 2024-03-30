package com.wdk.sports.provider;

import com.wdk.sports.ResourceTable;
import com.wdk.sports.util.Constans;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class InformationPageSlider extends PageSliderProvider {

    private AbilitySlice abilitySlice;
    private List<Integer> layoutFileIds ;
    public static Text quanNumber;
    public static Text deviceCharge;
    public static Text banShengNumber;
    public static Text currentSpeed;
    public static Text deviceChargePer;

    public InformationPageSlider(AbilitySlice abilitySlice, List<Integer> layoutFileIds) {
        this.abilitySlice = abilitySlice;
        this.layoutFileIds = layoutFileIds;
    }

    @Override
    public int getCount() {
        return this.layoutFileIds.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        // 获取布局文件的id
        Integer id = layoutFileIds.get( i ) ;
        // 解析id，获取布局文件的组件
        Component component = LayoutScatter.getInstance(abilitySlice).parse(id, null, false);

        if ( i == 0 ){
            quanNumber = (Text) component.findComponentById(ResourceTable.Id_text_number_quan);
            quanNumber.setText("0");

            banShengNumber = (Text) component.findComponentById(ResourceTable.Id_text_number_bansheng);
            banShengNumber.setText("0");

            deviceCharge = (Text) component.findComponentById(ResourceTable.Id_text_device_charge);
            deviceChargePer = (Text) component.findComponentById(ResourceTable.Id_text_device_charge_per);
            deviceCharge.setText("0");
        }else {
            currentSpeed = (Text) component.findComponentById(ResourceTable.Id_text_current_speed);
            currentSpeed.setText("0");
        }
        // 将组件添加到容器中
        componentContainer.addComponent( component );

        return component;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        // 销毁对象，直接从容器中删除这个对象即可
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}
