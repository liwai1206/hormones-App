package com.wdk.sports.callback;

import ohos.media.player.Player;

public class MyPlayerCallback implements Player.IPlayerCallback {
    @Override
    public void onPrepared() {
        System.out.println("准备播放");
    }

    @Override
    public void onMessage(int i, int i1) {

    }

    @Override
    public void onError(int i, int i1) {
        System.out.println("播放错误");
    }

    @Override
    public void onResolutionChanged(int i, int i1) {

    }

    @Override
    public void onPlayBackComplete() {
        System.out.println("播放完成");
    }

    @Override
    public void onRewindToComplete() {

    }

    @Override
    public void onBufferingChange(int i) {

    }

    @Override
    public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {

    }

    @Override
    public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {

    }
}
