package com.wdk.sports;

import com.wdk.sports.slice.VideoMainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class VideoMainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(VideoMainAbilitySlice.class.getName());
    }
}
