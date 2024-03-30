package com.wdk.sports.slice;

import com.wdk.sports.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.global.resource.RawFileDescriptor;
import ohos.media.common.Source;
import ohos.media.player.Player;

import java.io.IOException;

public class VideoMainAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    private SurfaceProvider surfaceProvider;
    private Player mPlayer;
    private Image stopImage;
    private Image backImage;
    private String root;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_video_main);

        root = (String) intent.getParams().getParam("root");

        initComponent();
        initComponentListener();
    }

    private void initComponentListener() {
        surfaceProvider.pinToZTop(false);
        WindowManager.getInstance().getTopWindow().get().setTransparent(true);
        surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceOps.Callback() {
            @Override
            public void surfaceCreated(SurfaceOps surfaceOps) {
                initPlayer();
            }

            @Override
            public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceOps surfaceOps) {

            }
        });
        surfaceProvider.setRotation( -180 );

        surfaceProvider.setClickedListener( this );
        backImage.setClickedListener( this );
    }


    /**
     * 初始化播放器
     */
    private void initPlayer() {
        mPlayer = new Player(this);
        mPlayer.setVolume(120f); // 音量
        mPlayer.setPlayerCallback(new Player.IPlayerCallback() {
            @Override
            public void onPrepared() {

            }

            @Override
            public void onMessage(int i, int i1) {

            }

            @Override
            public void onError(int i, int i1) {

            }

            @Override
            public void onResolutionChanged(int i, int i1) {

            }

            @Override
            public void onPlayBackComplete() {
                // 播放完成
                getUITaskDispatcher().asyncDispatch( ()->{
                    stopImage.setVisibility( Component.VISIBLE );
                });
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
        });

        try {
            RawFileDescriptor rawFileDescriptor = getResourceManager().
                    getRawFileEntry(root).
                    openRawFileDescriptor();

            Source source = new Source(rawFileDescriptor.getFileDescriptor(),
                    rawFileDescriptor.getStartPosition(),
                    rawFileDescriptor.getFileSize());

            mPlayer.setSource( source );
            // 准备播放
            mPlayer.prepare();
            // 设置surface配置
            mPlayer.setVideoSurface( surfaceProvider.getSurfaceOps().get().getSurface());
            mPlayer.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponent() {
        surfaceProvider = (SurfaceProvider) this.findComponentById(ResourceTable.Id_surfaceProvider);
        stopImage = (Image) this.findComponentById(ResourceTable.Id_img_stop);
        backImage = (Image) this.findComponentById(ResourceTable.Id_img_back);
    }

    @Override
    public void onClick(Component component) {
        if ( component.getId() == surfaceProvider.getId() ){
            if ( mPlayer.isNowPlaying() ){
                // 如果正在播放,则暂停，且显示图片
                mPlayer.pause();
                stopImage.setVisibility( Component.VISIBLE);
            }else {
                mPlayer.play();
                stopImage.setVisibility( Component.HIDE);
            }
        }else if ( component.getId() == backImage.getId() ){
            mPlayer.stop();
            mPlayer.release();
            surfaceProvider.removeFromWindow();
            this.terminateAbility();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.stop();
        mPlayer.release();
        surfaceProvider.removeFromWindow();
    }

    @Override
    protected void onBackground() {
        super.onBackground();
        mPlayer.stop();
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
