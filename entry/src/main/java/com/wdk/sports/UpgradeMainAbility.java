package com.wdk.sports;

import com.wdk.sports.slice.UpgradeMainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class UpgradeMainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(UpgradeMainAbilitySlice.class.getName());
    }
}
